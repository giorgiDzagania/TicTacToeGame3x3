package com.madeit.tictactoegame.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.madeit.tictactoegame.databinding.ActivityGameBinding
import com.madeit.tictactoegame.model.GameData
import com.madeit.tictactoegame.model.GameModel
import com.madeit.tictactoegame.model.GameStatus

class GameActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGameBinding
    private var gameModel: GameModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnBack()
        setOnclick()
    }

    private fun btnBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onClick(v: View?) {
        gameModel?.apply {
            if (gameStatus != GameStatus.INPROGRESS) {
                Toast.makeText(this@GameActivity, "Game not started", Toast.LENGTH_SHORT).show()
                return
            }
            val clickPos = (v?.tag as String).toInt()
            if (filledPos[clickPos].isEmpty()) {
                filledPos[clickPos] = currentPlayer
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                checkForWinner()
                updateGameData(this)
            }
        }
    }

    private fun setOnclick() {
        with(binding) {
            btn0.setOnClickListener(this@GameActivity)
            btn1.setOnClickListener(this@GameActivity)
            btn2.setOnClickListener(this@GameActivity)
            btn3.setOnClickListener(this@GameActivity)
            btn4.setOnClickListener(this@GameActivity)
            btn5.setOnClickListener(this@GameActivity)
            btn6.setOnClickListener(this@GameActivity)
            btn7.setOnClickListener(this@GameActivity)
            btn8.setOnClickListener(this@GameActivity)

            btnStartGame.setOnClickListener {
                startGame()
            }

            GameData.gameModel.observe(this@GameActivity) {
                gameModel = it
                setUI()
            }

        }
    }

    private fun startGame() {
        gameModel?.apply {
            updateGameData(
                GameModel(
                    gameId = gameId,
                    gameStatus = GameStatus.INPROGRESS
                )
            )
        }
    }

    private fun updateGameData(model: GameModel) {
        GameData.saveGameModel(model)
    }

    private fun setUI() {
        gameModel?.apply {
            with(binding) {
                btn0.text = filledPos[0]
                btn1.text = filledPos[1]
                btn2.text = filledPos[2]
                btn3.text = filledPos[3]
                btn4.text = filledPos[4]
                btn5.text = filledPos[5]
                btn6.text = filledPos[6]
                btn7.text = filledPos[7]
                btn8.text = filledPos[8]

                btnStartGame.visibility = View.VISIBLE

                gameStatusText.text =
                    when (gameStatus) {
                        GameStatus.CREATED -> {
                            binding.btnStartGame.visibility = View.INVISIBLE
                            "GameId: $gameId"
                        }

                        GameStatus.JOINED -> {
                            "Click on Start Game"
                        }

                        GameStatus.INPROGRESS -> {
                            btnStartGame.visibility = View.INVISIBLE
                            "$currentPlayer Turn"
                        }

                        GameStatus.FINISHED -> {
                            if (winner.isNotEmpty()) {
                                "$winner won"
                            } else
                                "Draw"
                        }
                    }
            }
        }
    }

    private fun checkForWinner() {
        val winnerPos = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),

            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),

            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6),
        )
        gameModel?.apply {
            for (i in winnerPos) {
                if (
                    filledPos[i[0]] == filledPos[i[1]] &&
                    filledPos[i[1]] == filledPos[i[2]] &&
                    filledPos[i[0]].isNotEmpty()
                ) {
                    gameStatus = GameStatus.FINISHED
                    winner = filledPos[i[0]]
                }
                if (filledPos.none() { it.isEmpty() }) {
                    gameStatus = GameStatus.FINISHED
                }
                updateGameData(this)
            }
        }

    }

}