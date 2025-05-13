package com.example.apphoctap.view.document.flashcardmanagement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.apphoctap.database.entities.FlashcardEntity
import com.example.apphoctap.databinding.DialogFlashcardViewerBinding
import com.example.apphoctap.model.FlashCardResponse
import com.example.apphoctap.viewmodel.DeckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlashcardViewerFragment : Fragment() {

    private var _binding: DialogFlashcardViewerBinding? = null
    private val binding get() = _binding!!

    private val deckViewModel: DeckViewModel by viewModels()
    private val flashcardViewModel: CreateFlashcardViewModel by viewModels()
    private var deckId: Long = -1L
    private var flashcards: List<FlashCardResponse> = emptyList()
    private var currentIndex = 0
    private var showingFront = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deckId = it.getLong(ARG_DECK_ID, -1L)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFlashcardViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Thiết lập các sự kiện cho nút
        setupButtonListeners()

        if (deckId != -1L) {
            // Lấy thông tin bộ thẻ
            observeDeckInfo()

            // Theo dõi và hiển thị flashcards
            observeFlashcards()
        }
    }

    private fun observeDeckInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            val deckLiveData = deckViewModel.getDeckById(deckId)
            deckLiveData.observe(viewLifecycleOwner) { deck ->
                deck?.let {
                    binding.tvDialogTitle.text = it.name
                }
            }
        }
    }

    private fun observeFlashcards() {
        // Theo dõi trạng thái đồng bộ flashcards
        flashcardViewModel.syncStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess { flashcardList ->
                // Cập nhật danh sách flashcards
                flashcards = flashcardList

                // Cập nhật giao diện nếu có thẻ
                if (flashcards.isNotEmpty()) {
                    updateCardUI()
                } else {
                    // Xử lý trường hợp không có thẻ
                    binding.tvDialogCardContent.text = "Không có thẻ nào trong bộ"
                    binding.tvDialogCardType.text = ""
                    binding.tvDialogCardCounter.text = "0/0"
                }
            }.onFailure { exception ->
                // Xử lý lỗi khi tải thẻ
                Log.e("FlashcardViewerFragment", "Lỗi tải thẻ", exception)
                binding.tvDialogCardContent.text = "Lỗi: Không thể tải thẻ"
                binding.tvDialogCardType.text = ""
                binding.tvDialogCardCounter.text = "0/0"
            }
        }

        // Gọi hàm lấy flashcards từ server
        flashcardViewModel.getAllFlashcardsFromServer(deckId)
    }

    private fun setupButtonListeners() {
        binding.btnFlipCard.setOnClickListener {
            if (flashcards.isNotEmpty()) {
                showingFront = !showingFront
                updateCardUI()
            }
        }

        binding.btnNextCard.setOnClickListener {
            if (currentIndex < flashcards.size - 1) {
                currentIndex++
                showingFront = true
                updateCardUI()
            }
        }

        binding.btnPrevCard.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showingFront = true
                updateCardUI()
            }
        }

        binding.btnCloseDialog.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun updateCardUI() {
        if (flashcards.isEmpty()) {
            binding.tvDialogCardContent.text = "Không có thẻ nào"
            binding.tvDialogCardType.text = ""
            binding.tvDialogCardCounter.text = "0/0"
            return
        }

        val card = flashcards[currentIndex]
        binding.tvDialogCardType.text = if (showingFront) "Mặt trước" else "Mặt sau"
        binding.tvDialogCardContent.text = if (showingFront) card.frontText else card.backText
        binding.tvDialogCardCounter.text = "${currentIndex + 1}/${flashcards.size}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_DECK_ID = "deckId"

        fun newInstance(deckId: Long): FlashcardViewerFragment {
            return FlashcardViewerFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_DECK_ID, deckId)
                }
            }
        }
    }
}