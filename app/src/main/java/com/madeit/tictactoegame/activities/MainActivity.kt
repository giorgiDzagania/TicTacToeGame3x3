package com.madeit.tictactoegame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.madeit.tictactoegame.databinding.ActivityMainBinding
import com.madeit.tictactoegame.model.GameData
import com.madeit.tictactoegame.model.GameModel
import com.madeit.tictactoegame.model.GameStatus

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPlay.setOnClickListener {
            setUp()
        }
    }

    private fun setUp() {
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.JOINED
            )
        )
        startGame()
    }

    private fun startGame() {
        val intent = Intent(this@MainActivity, GameActivity::class.java)
        startActivity(intent)
    }
}