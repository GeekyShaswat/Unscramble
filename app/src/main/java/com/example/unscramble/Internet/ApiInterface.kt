package com.example.unscramble.Internet

import com.example.unscramble.data.WordData
import retrofit2.http.GET

interface ApiInterface {
    @GET("WordsData/main/words_with_levels.json")
    suspend fun getWords() : List<WordData>
}