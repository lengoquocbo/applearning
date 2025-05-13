package com.example.apphoctap.view.document.flashcardmanagement

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.apphoctap.database.entities.DeckEntity
import com.example.apphoctap.database.entities.FlashcardEntity
import com.example.apphoctap.model.CreateFlashcardRequest
import com.example.apphoctap.model.FlashCardResponse
import com.example.apphoctap.repository.DeckRepository
import com.example.apphoctap.repository.FlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFlashcardViewModel @Inject constructor(
    private val deckRepository: DeckRepository,
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {

    // Các LiveData sẵn có
    private val _currentDeckId = MutableLiveData<Long>()
    val currentDeckId: LiveData<Long> = _currentDeckId

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _onlineCreateStatus = MutableLiveData<Result<FlashCardResponse>>()
    val onlineCreateStatus: LiveData<Result<FlashCardResponse>> = _onlineCreateStatus

    private val _onlineDeleteStatus = MutableLiveData<Result<Unit>>()
    val onlineDeleteStatus: LiveData<Result<Unit>> = _onlineDeleteStatus

    private val _syncStatus = MutableLiveData<Result<List<FlashCardResponse>>>()
    val syncStatus: LiveData<Result<List<FlashCardResponse>>> = _syncStatus

    // BỊ THIẾU (sửa lỗi bạn đang gặp)
    private val _createFlashcardResult = MutableLiveData<Result<Unit>>()
    val createFlashcardResult: LiveData<Result<Unit>> = _createFlashcardResult
    // LiveData để theo dõi trạng thái đồng bộ


    // LiveData để lưu trữ danh sách flashcard
    private val _flashcards = MutableLiveData<List<FlashCardResponse>>()
    val flashcards: LiveData<List<FlashCardResponse>> = _flashcards
    // Các biến trạng thái dữ liệu thẻ
    var frontText = MutableLiveData<String>("")
    var backText = MutableLiveData<String>("")
    var tags = MutableLiveData<String>("")
    val deckId = MutableLiveData<String>("")
    var frontColor = MutableLiveData<String>("#FFFFFF")
    var backColor = MutableLiveData<String>("#E8F5E9")

    fun setCurrentDeckId(deckId: Long) {
        _currentDeckId.value = deckId
    }

    fun createNewDeck(name: String, userId: String, onComplete: (Long) -> Unit) {
        viewModelScope.launch {
            val deckId = deckRepository.insertDeck(name, userId)
            _currentDeckId.value = deckId
            onComplete(deckId)
        }
    }

    fun createFlashcard(flashcardRequest: CreateFlashcardRequest) {
        viewModelScope.launch {
            val result = flashcardRepository.createFlashcardOnServer(flashcardRequest)
            if (result.isSuccess) {
                _createFlashcardResult.postValue(Result.success(Unit))
            } else {
                _createFlashcardResult.postValue(
                    Result.failure(result.exceptionOrNull() ?: Exception("Lỗi không xác định"))
                )
            }
        }
    }

    fun deleteFlashcardOnline(flashcardId: Int) {
        viewModelScope.launch {
            val result = flashcardRepository.deleteFlashcardOnServer(flashcardId)
            _onlineDeleteStatus.value = result
        }
    }

    fun syncFlashcardsFromServer(deckId: Long) {
        viewModelScope.launch {
            val result = flashcardRepository.getFlashcardsFromServer(deckId)
            _syncStatus.value = result
        }
    }

    fun getAllDecks(userId: String): LiveData<List<DeckEntity>> {
        return deckRepository.getAllDecks(userId)
    }

    // Hàm lấy flashcards từ server
    fun getAllFlashcardsFromServer(deckId: Long) {
        viewModelScope.launch {
            try {
                val result = flashcardRepository.getFlashcardsFromServer(deckId)

                // Cập nhật LiveData với kết quả
                result.onSuccess { flashcardList ->
                    _flashcards.value = flashcardList
                    _syncStatus.value = result
                }.onFailure { exception ->
                    _syncStatus.value = result
                    // Log lỗi nếu cần
                    Log.e("FlashcardViewModel", "Error fetching flashcards", exception)
                }
            } catch (e: Exception) {
                // Xử lý ngoại lệ nếu có
                _syncStatus.value = Result.failure(e)
                Log.e("FlashcardViewModel", "Unexpected error", e)
            }
        }
    }

    fun saveSyncedFlashcardsToLocal(flashcards: List<FlashCardResponse>) {
        viewModelScope.launch {
            flashcards.forEach { flash ->
                val entity = FlashcardEntity(
                    flashcardId = flash.flashcardID,
                    deckId = flash.deckID,
                    frontText = flash.frontText,
                    backText = flash.backText,
                    frontColor = "#FFFFFF",
                    backColor = "#FFFFFF"
                )
                flashcardRepository.insertFlashcard(entity)
            }
        }
    }

    fun resetSaveState() {
        _isSaved.value = false
    }
}

