package com.example.apphoctap.view.document.flashcardmanagement

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apphoctap.R
import com.example.apphoctap.adapter.FlashcardAdapter
import com.example.apphoctap.databinding.FragmentFlashcardsBinding
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.viewmodel.DeckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlashcardManageFragment : Fragment() {

    private var _binding: FragmentFlashcardsBinding? = null
    private val binding get() = _binding!!

    private lateinit var flashcardAdapter: FlashcardAdapter
    private val viewModel: DeckViewModel by viewModels()

    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlashcardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lấy userID từ token
        val token = SessionManager(requireContext()).getAccessToken()
        userId = token?.let { JwtUtils.getUserIdFromToken(it) }

        setupRecyclerView()
        observeDecks()

        binding.cardCreateFlashcard.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container_teacher, CreateFlashcardFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerView() {
        flashcardAdapter = FlashcardAdapter(
            emptyList(),
            onClickItem = { deck ->
                val viewerFragment = FlashcardViewerFragment.newInstance(deck.deckId.toLong())
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_container_teacher, viewerFragment)
                    .addToBackStack(null)
                    .commit()
            },
            ondeleteClick = { deck ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá bộ thẻ '${deck.name}' không?")
                    .setPositiveButton("Xoá") { _, _ ->
                        // trên API trước để kiểm mạng

                        viewModel.deleteDeckOnline(deck.deckId)

                        viewModel.deleteDeck(deck)
                    }
                    .setNegativeButton("Huỷ", null)
                    .show()
            }
        )

        binding.rvFlashcards.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = flashcardAdapter
        }
    }

    private fun observeDecks() {
        userId?.let { uid ->
            // Lấy dữ liệu cũ từ Room (hiển thị nhanh)
            viewModel.getAllDecks(uid).observe(viewLifecycleOwner) { decks ->
                flashcardAdapter.updateData(decks)
                binding.tvEmptyFlashcards.visibility =
                    if (decks.isEmpty()) View.VISIBLE else View.GONE
            }

            // Trong Fragment/Activity
            viewModel.getAllDecksFromApi(userId!!).observe(viewLifecycleOwner) { result ->
                result.fold(
                    onSuccess = { decks ->
                        lifecycleScope.launch {
                            viewModel.updateDecksInRoom(decks)
                        }
                        println(decks)
                        Log.e("DeckFragment", "thang cong")

                    },
                    onFailure = { error ->
                        // Xử lý lỗi
                        Log.e("DeckFragment", "Error loading decks: ${error.message}")
                    }
                )
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
