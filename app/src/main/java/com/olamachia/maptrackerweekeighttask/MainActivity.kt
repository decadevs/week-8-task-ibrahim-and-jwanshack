package com.olamachia.maptrackerweekeighttask

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.mapButton).setOnClickListener {
            val intent = Intent(this, TrackerActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.pokeButton).setOnClickListener {
            val intent = Intent(this, PokemonActivity::class.java)
            startActivity(intent)
        }
    }
}
