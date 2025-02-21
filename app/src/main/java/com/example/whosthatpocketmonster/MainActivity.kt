package com.example.whosthatpocketmonster

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var monsterImage: ImageView
    private lateinit var answerGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var nextButton: Button
    private lateinit var scoreText: TextView

    private val monsters = listOf(
        "carvanha", "miraidon", "persian", "sneasler"
    )

    private var currentMonster: String = ""
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI Elements
        monsterImage = findViewById(R.id.monsterImage)
        answerGroup = findViewById(R.id.answerGroup)
        submitButton = findViewById(R.id.submitButton)
        nextButton = findViewById(R.id.nextButton)
        scoreText = findViewById(R.id.scoreText)

        loadNewMonster()

        submitButton.setOnClickListener {
            checkAnswer()
        }

        nextButton.setOnClickListener {
            loadNewMonster() // Load next question
            submitButton.visibility = View.VISIBLE
            nextButton.visibility = View.GONE
        }
    }

    private fun loadNewMonster() {
        // Pick a random monster
        currentMonster = monsters.random()
        val silhouetteResId = resources.getIdentifier("${currentMonster}_silhouette", "drawable", packageName)
        val actualMonsterResId = resources.getIdentifier(currentMonster, "drawable", packageName)

        // Set the silhouette image by default
        if (silhouetteResId != 0) {
            monsterImage.setImageResource(silhouetteResId)
        } else {
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
        }

        // Shuffle answer options
        val options = monsters.shuffled()
        for (i in 0 until answerGroup.childCount) {
            (answerGroup.getChildAt(i) as RadioButton).apply {
                text = options[i]
                isChecked = false
            }
        }
    }

    private fun checkAnswer() {
        val selectedId = answerGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedButton = findViewById<RadioButton>(selectedId)
        val correctImageResId = resources.getIdentifier(currentMonster, "drawable", packageName)

        if (selectedButton.text == currentMonster) {
            // Correct answer: Show the actual monster image
            if (correctImageResId != 0) {
                monsterImage.setImageResource(correctImageResId)
            }
            score++
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            // Wrong answer: Keep showing the silhouette
            val silhouetteResId = resources.getIdentifier("${currentMonster}_silhouette", "drawable", packageName)
            if (silhouetteResId != 0) {
                monsterImage.setImageResource(silhouetteResId)
            }
            Toast.makeText(this, "Wrong! It was $currentMonster", Toast.LENGTH_SHORT).show()
        }

        // Hide submit and show next button
        submitButton.visibility = View.GONE
        nextButton.visibility = View.VISIBLE

        // Update score
        scoreText.text = "Score: $score"
    }
}
