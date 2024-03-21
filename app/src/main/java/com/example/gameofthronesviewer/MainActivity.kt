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
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
 private val characterList = mutableListOf<Character>()
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
   val character = characterList[currentIndex]
   displayCharacter(character.imageUrl, imageView)
   tvFullName.text = character.fullName
   tvTitle.text = character.title
   tvFamily.text = character.family
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
     characterList.add(Character(imageUrl, fullName, title, family))
    }
    Log.d("Character List", "Successfully fetched character list.")
    // Set initial character data after fetching the list
    if (characterList.isNotEmpty()) {
     val character = characterList[currentIndex]
     val imageView = findViewById<ImageView>(R.id.imageViewCharacter)
     val tvFullName = findViewById<TextView>(R.id.tvFullNameCharacter)
     val tvTitle = findViewById<TextView>(R.id.tvTitleCharacter)
     val tvFamily = findViewById<TextView>(R.id.tvFamilyCharacter)
     displayCharacter(character.imageUrl, imageView)
     tvFullName.text = character.fullName
     tvTitle.text = character.title
     tvFamily.text = character.family
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
