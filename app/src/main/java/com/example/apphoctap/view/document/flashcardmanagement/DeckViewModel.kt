package com.example.apphoctap.viewmodel

import androidx.lifecycle.*
import com.example.apphoctap.database.entities.DeckEntity
import com.example.apphoctap.database.entities.FlashcardEntity
import com.example.apphoctap.model.DeckRequest
import com.example.apphoctap.model.DeckResponse
import com.example.apphoctap.repository.DeckRepository
import com.example.apphoctap.repository.FlashcardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val repository: DeckRepository,
    private val repositoryflashcard: FlashcardRepository
) : ViewModel() {



    // Lấy danh sách deck theo userId
    fun getAllDecks(userId: String): LiveData<List<DeckEntity>> {
        return repository.getAllDecks(userId)
    }

    // Lấy 1 deck theo ID
    suspend fun getDeckById(deckId: Long): LiveData<DeckEntity?> {
        return repository.getDeckById(deckId)
    }

    fun getAll():LiveData<List<FlashcardEntity>>{
        return  repositoryflashcard.getALL()
    }
    // Lấy tất cả flashcard theo deckId
//    fun getFlashcardsByDeckId(deckId: Long): LiveData<List<FlashcardEntity>> {
//        return repositoryflashcard.getFlashcardsByDeckId(deckId)
//    }


    // Thêm deck
    fun insertDeck(name: String, userId: String) {
        viewModelScope.launch {
            repository.insertDeck(name, userId)
        }
    }

    // Cập nhật deck
    fun updateDeck(deck: DeckEntity) {
        viewModelScope.launch {
            repository.updateDeck(deck)
        }
    }

    fun deleteDeck(deck: DeckEntity) {
        viewModelScope.launch {
            repository.deleteDeck(deck)
        }
    }

    // hàm API
    // Lấy danh sách Deck từ API
    fun getAllDecksFromApi(userId: String): LiveData<Result<List<DeckResponse>>> {
        val result = MutableLiveData<Result<List<DeckResponse>>>()
        viewModelScope.launch {
            result.value = repository.getAllDecksOnline(userId)
        }
        return result
    }

    // Tạo Deck qua API
    fun createDeckOnline(deckID: Long, name: String, userId: String): LiveData<Result<List<DeckResponse>>> {
        val result = MutableLiveData<Result<List<DeckResponse>>>()
        viewModelScope.launch {
            val request = DeckRequest(
                deckID = deckID,
                name = name,
                userID = userId,
                dateCreate = System.currentTimeMillis()
            )
            result.value = repository.createDeckOnline(request)
        }
        return result
    }

     fun getAllFlashcardByDeckID(deckId: Int){

   }


    // Xoá Deck qua API
    fun deleteDeckOnline(deckId: Int): LiveData<Result<String>> {
        val result = MutableLiveData<Result<String>>()
        viewModelScope.launch {
            result.value = repository.deleteDeckOnline(deckId)
        }
        return result
    }
    fun updateDecksInRoom(decks: List<DeckResponse>) {
        viewModelScope.launch {
            repository.saveDecks(decks)
        }
    }

}
