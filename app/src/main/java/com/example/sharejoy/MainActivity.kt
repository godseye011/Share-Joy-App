package com.example.sharejoy

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.transition.Visibility
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

//import android.content.Intent as Intent

class MainActivity : AppCompatActivity() {
    var curURL: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()

        shareButton.setOnClickListener(){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Share it...\n $curURL")
            val chooser = Intent.createChooser(intent, "Share Using")
            startActivity(chooser)
        }

        nextButton.setOnClickListener(){
            loadMeme()
        }


    }

    private fun loadMeme(){
        loading.visibility = View.VISIBLE
        nextButton.visibility = View.GONE
        shareButton.visibility = View.GONE
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                curURL = response.getString("url")
                Glide.with(this).load(curURL).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loading.visibility = View.GONE
                        nextButton.visibility = View.VISIBLE
                        shareButton.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loading.visibility = View.GONE
                        nextButton.visibility = View.VISIBLE
                        shareButton.visibility = View.VISIBLE
                        return false
                    }
                }).into(memeImage)
            },
            {
                Toast.makeText(this, "SomeThing Went Wrong, TRY AGAIN", Toast.LENGTH_LONG).show()
            })
        Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


}