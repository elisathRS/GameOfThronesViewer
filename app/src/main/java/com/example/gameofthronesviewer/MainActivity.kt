package com.example.gameofthronesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random
import org.json.JSONArray



class MainActivity : AppCompatActivity() {
 private val characterList = mutableListOf<Pair<String, Triple<String, String, String>>>()
 private var currentIndex = 0

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContentView(R.layout.activity_main)

  val button = findViewById<Button>(R.id.btnNextCharacter)
  val imageView = findViewById<ImageView>(R.id.imageViewCharacter)
  val tvFullName = findViewById<TextView>(R.id.tvFullNameCharacter)
  val tvTitle = findViewById<TextView>(R.id.tvTitleCharacter)
  val tvFamily = findViewById<TextView>(R.id.tvFamilyCharacter)

  fetchCharacterList()

  button.setOnClickListener {
   requestRandomCharacter()
   val (imageUrl, characterInfo) = characterList[currentIndex]
   displayCharacter(imageUrl, imageView)
   tvFullName.text = characterInfo.first
   tvTitle.text = characterInfo.second
   tvFamily.text = characterInfo.third
  }
 }

 private fun fetchCharacterList() {
  val client = AsyncHttpClient()
  client["https://thronesapi.com/api/v2/Characters", object : JsonHttpResponseHandler() {
   override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
    val jsonArray = json.jsonArray
    for (i in 0 until jsonArray.length()) {
     val character = jsonArray.getJSONObject(i)
     val imageUrl = character.getString("imageUrl")
     val fullName = character.getString("fullName")
     val title = character.getString("title")
     val family = character.getString("family")
     characterList.add(Pair(imageUrl, Triple(fullName, title, family)))
    }
    Log.d("Character List", "Successfully fetched character list.")
    // Set initial character data after fetching the list
    if (characterList.isNotEmpty()) {
     val (imageUrl, characterInfo) = characterList[currentIndex]
     val imageView = findViewById<ImageView>(R.id.imageViewCharacter)
     val tvFullName = findViewById<TextView>(R.id.tvFullNameCharacter)
     val tvTitle = findViewById<TextView>(R.id.tvTitleCharacter)
     val tvFamily = findViewById<TextView>(R.id.tvFamilyCharacter)
     displayCharacter(imageUrl, imageView)
     tvFullName.text = characterInfo.first
     tvTitle.text = characterInfo.second
     tvFamily.text = characterInfo.third
    }
   }

   override fun onFailure(
    statusCode: Int,
    headers: Headers?,
    errorResponse: String,
    throwable: Throwable?
   ) {
    Log.e("Character List Error", "Failed to fetch character list: $errorResponse")
   }
  }]
 }

 private fun requestRandomCharacter() {
  currentIndex = (0 until characterList.size).random()
 }

 private fun displayCharacter(imageUrl: String, imageView: ImageView) {
  Glide.with(this)
   .load(imageUrl)
   .centerCrop()
   .into(imageView)
 }
}