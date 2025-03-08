/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.unscramble.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscramble.Internet.RetrofitInstance
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.WordData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel containing the app data and methods to process the data
 */
class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _selectedLevel = MutableStateFlow("Easy")
    val selectedLevel: StateFlow<String> = _selectedLevel

    fun updateLevel(level: String) {
        _selectedLevel.value = level
        Log.d("level", "level : $level")
    }
    fun validateLevel(level: String): Boolean {
        val isValid = level.isNotEmpty()
        _isUsernameValid.value = isValid
        return isValid
    }


    private val _username = MutableStateFlow("")
    val username: StateFlow<String> get() = _username

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
        Log.d("username", "username : $newUsername")
    }

    var userGuess by mutableStateOf("")
        private set

    private val _words = MutableStateFlow<List<WordData>>(emptyList())
    val words: StateFlow<List<WordData>> = _words

    private val _loading = MutableStateFlow(false)
    val loading : StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error : StateFlow<String?> = _error

    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String
    private val _isUsernameValid = MutableStateFlow(true)
    val isUsernameValid: StateFlow<Boolean> get() = _isUsernameValid

    fun validateUsername(username: String): Boolean {
        val isValid = username.isNotEmpty()
        _isUsernameValid.value = isValid
        return isValid
    }


    init {
        fetchWords()
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        _username.value = ""
        _selectedLevel.value = ""
        if (_words.value.isNotEmpty() && _uiState.value.isGameOver == true) {
            _uiState.value = GameUiState(currentScrambledWord = shuffleCurrentWord(currentWord))
        }
    }

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }
    fun checkUserGuess() {
        val trimmedInput = if (userGuess.endsWith(" ")) {
            userGuess.trimEnd() // Remove the trailing space
        } else {
            userGuess
        }
        if (trimmedInput.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
            pickRandomWordAndShuffle()
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        updateUserGuess("")
        if(_uiState.value.isGameOver == false ) pickRandomWordAndShuffle()
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS){
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else{
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = currentWord,
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        Log.d("shuffleCurrentWord", "Current Word: $word, Shuffled Word: ${String(tempWord)}")
        return String(tempWord)
    }


    private fun pickRandomWordAndShuffle() {
        if (_words.value.isNotEmpty()) {
            val filteredWords = _words.value.filter { wordData ->
                wordData.level == selectedLevel.value
            }
            Log.d("pickRandomWord", "Filtered Words: $filteredWords")
            if (filteredWords.isNotEmpty()) {
                currentWord = filteredWords.random().word
                if (usedWords.contains(currentWord)) {
                    pickRandomWordAndShuffle() // Recurse if the word has been used
                } else {
                    usedWords.add(currentWord)
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentScrambledWord = shuffleCurrentWord(currentWord)
                        )
                    } // Shuffle the word
                }
            } else {
                Log.d("pickRandomWord", "No words found for selected level")
                currentWord = "defaultWord" // Or some other fallback word
            }
        } else {
            Log.d("pickRandomWord", "No words available")
            currentWord = "defaultWord" // Or some other fallback word
        }
    }




    fun fetchWords() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitInstance.api.getWords()
                _words.value = response
                Log.d("response", "$response")
                pickRandomWordAndShuffle()
            } catch (e: Exception) {
                _error.value = "Error Occurred"
                Log.d("error", "error is ${e.localizedMessage}")
            } finally {
                _loading.value = false
            }
        }
    }
    fun pickWordOnSubmit() {
        pickRandomWordAndShuffle()  // Call the method to pick a new word
    }

}
