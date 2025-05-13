package com.example.apphoctap.view.document.flashcardmanagement

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.apphoctap.R
import com.example.apphoctap.database.entities.DeckEntity
import com.example.apphoctap.databinding.FragmentCreateFlashcardBinding
import com.example.apphoctap.model.CreateFlashcardRequest
import com.example.apphoctap.utils.JwtUtils
import com.example.apphoctap.utils.SessionManager
import com.example.apphoctap.viewmodel.DeckViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFlashcardFragment : Fragment() {

    private var _binding: FragmentCreateFlashcardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateFlashcardViewModel by viewModels()
    private val viewModelDeck: DeckViewModel by viewModels()

    private var decksList = listOf<DeckEntity>()
    private var isFlipped = false
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateFlashcardBinding.inflate(inflater, container, false)

        // Lấy userID từ token
        val token = SessionManager(requireContext()).getAccessToken()
        userId = token?.let { JwtUtils.getUserIdFromToken(it) }

        setupListeners()
        setupObservers()

        // Màu mặc định khi khởi tạo
        updateCardPreviewColors(
            frontColor = viewModel.frontColor.value ?: "#FFFFFF",
            backColor = viewModel.backColor.value ?: "#E8F5E9"
        )

        return binding.root
    }

    private fun setupListeners() {
        binding.btnFlipCard.setOnClickListener {
            flipCard()
        }

        binding.btnAddImage.setOnClickListener { showColorPicker(isFront = true) }
        binding.btnBackAddImage.setOnClickListener { showColorPicker(isFront = false) }

        binding.btnSave.setOnClickListener {
            val front = binding.etFront.text.toString()
            val back = binding.etBack.text.toString()
            val deckId = viewModel.currentDeckId.value?.toInt() ?: return@setOnClickListener

            val flashcardRequest = CreateFlashcardRequest(
                flashcardID = null,
                deckID = deckId,
                frontText = front,
                backText = back,
                frontColor = viewModel.frontColor.value ?: "#FFFFFF",
                backColor = viewModel.backColor.value ?: "#E8F5E9"
            )

            viewModel.createFlashcard(flashcardRequest)
        }

        binding.btnAddMore.setOnClickListener {
            val front = binding.etFront.text.toString()
            val back = binding.etBack.text.toString()
            val deckId = viewModel.currentDeckId.value?.toInt() ?: return@setOnClickListener

            val flashcardRequest = CreateFlashcardRequest(
                flashcardID = null,
                deckID = deckId,
                frontText = front,
                backText = back,
                frontColor = viewModel.frontColor.value ?: "#FFFFFF",
                backColor = viewModel.backColor.value ?: "#E8F5E9"
            )

            viewModel.createFlashcard(flashcardRequest)

            // Reset nội dung thẻ
            binding.etFront.setText("")
            binding.etBack.setText("")
        }

        binding.btnNewDeck.setOnClickListener { showCreateDeckDialog() }

        binding.etBack.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && !isFlipped) flipCard()
        }

        binding.etFront.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && isFlipped) flipCard()
        }

        binding.etFront.addTextChangedListener {
            binding.tvCardFrontPreview.text = it.toString().ifEmpty { getString(R.string.front_preview) }
        }

        binding.etBack.addTextChangedListener {
            binding.tvCardBackPreview.text = it.toString().ifEmpty { getString(R.string.back_preview) }
        }
    }

    private fun setupObservers() {
        userId?.let { uid ->
            viewModel.getAllDecks(uid).observe(viewLifecycleOwner) { decks ->
                if (decks.isEmpty()) {
                    showCreateDeckDialog()
                } else {
                    decksList = decks
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        decks
                    ).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }

                    binding.spinnerDeck.adapter = adapter

                    binding.spinnerDeck.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            val selectedDeck = decks[position]
                            viewModel.setCurrentDeckId(selectedDeck.deckId.toLong())
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }

                    if (viewModel.currentDeckId.value == null) {
                        viewModel.setCurrentDeckId(decks[0].deckId.toLong())
                    }
                }
            }
        } ?: run {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }

        viewModel.isSaved.observe(viewLifecycleOwner) { isSaved ->
            if (isSaved) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.frontColor.observe(viewLifecycleOwner) { frontColor ->
            updateCardPreviewColors(frontColor, viewModel.backColor.value ?: "#E8F5E9")
        }

        viewModel.backColor.observe(viewLifecycleOwner) { backColor ->
            updateCardPreviewColors(viewModel.frontColor.value ?: "#FFFFFF", backColor)
        }
    }

    private fun showColorPicker(isFront: Boolean) {
        val currentColor = if (isFront) viewModel.frontColor.value ?: "#FFFFFF"
        else viewModel.backColor.value ?: "#E8F5E9"

        ColorPickerDialog(requireContext(), currentColor) { selectedColor ->
            if (isFront) viewModel.frontColor.value = selectedColor
            else viewModel.backColor.value = selectedColor
        }.show()
    }

    private fun updateCardPreviewColors(frontColor: String, backColor: String) {
        binding.cardFlipper.getChildAt(0).setBackgroundColor(Color.parseColor(frontColor))
        binding.cardFlipper.getChildAt(1).setBackgroundColor(Color.parseColor(backColor))
    }

    private fun flipCard() {
        binding.cardFlipper.apply {
            val nextChild = if (displayedChild == 0) 1 else 0
            setInAnimation(requireContext(), if (nextChild == 1)
                R.anim.slide_in_right else R.anim.slide_in_left)
            setOutAnimation(requireContext(), if (nextChild == 1)
                R.anim.slide_out_left else R.anim.slide_out_right)
            displayedChild = nextChild
        }
        isFlipped = !isFlipped
    }

    private fun showCreateDeckDialog() {
        val editText = EditText(requireContext()).apply {
            hint = getString(R.string.deck_name_hint)
            setPadding(32, 32, 32, 32)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.create_new_deck))
            .setView(editText)
            .setPositiveButton(getString(R.string.create)) { _, _ ->
                val deckName = editText.text.toString().trim()
                val currentUserId = userId

                if (deckName.isNotEmpty() && currentUserId != null) {
                    viewModel.createNewDeck(deckName, currentUserId) { newDeckId ->
                        viewModel.setCurrentDeckId(newDeckId.toLong())
                        Toast.makeText(requireContext(), getString(R.string.deck_created), Toast.LENGTH_SHORT).show()
                        Log.d("CreateDeck", "Created deck: $deckName for user: $currentUserId")
                        viewModelDeck.createDeckOnline(newDeckId, deckName, currentUserId)
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.deck_name_required), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
