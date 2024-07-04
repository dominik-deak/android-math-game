/* ATTENTION: This app was tested on a Pixel 5 with API 30
* Other screen sizes may position the popup windows differently,
* resulting in a less enjoyable user experience */

package com.example.coursework1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import java.util.Random

/**
 * The game screen of the application,
 * contains a timer, two arithmetic expressions
 * and three buttons: greater, equals, less.
 * Also includes an "end game" screen displaying the user's scores.
 * @author Dominik Deak - w1778659
 */
class GameScreen : AppCompatActivity() {
    private lateinit var leftExpression: TextView
    private lateinit var rightExpression: TextView
    private lateinit var timerView: TextView
    private lateinit var endView: TextView
    private lateinit var greaterButton: Button
    private lateinit var equalsButton: Button
    private lateinit var lessButton: Button
    private lateinit var timer: CountDownTimer
    /* I used a data class to store the game data
    * I implemented it using this guide: https://kotlinlang.org/docs/data-classes.html */
    data class GameData(var correctAnswers: Int, var incorrectAnswers: Int, var currentMillisUntilFinished: Long) {
        fun incrementCorrectAnswers() {
            correctAnswers++
        }
        fun incrementIncorrectAnswers() {
            incorrectAnswers++
        }
    }
    private val userGameData: GameData = GameData(0, 0, 50000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)

        timerView = findViewById(R.id.timerView)
        greaterButton = findViewById(R.id.greaterButton)
        equalsButton = findViewById(R.id.equalsButton)
        lessButton = findViewById(R.id.lessButton)
        endView = findViewById(R.id.endView)
        endView.visibility = View.INVISIBLE

        // Giving functionality to the greater button
        greaterButton.setOnClickListener {
            val isCorrect: Boolean = displayCorrectness("greater")
            if (isCorrect && userGameData.correctAnswers % 5 == 0) {
                userGameData.currentMillisUntilFinished += 10000
                updateTimer()
            }
        }
        // Giving functionality to the equals button
        equalsButton.setOnClickListener {
            val isCorrect: Boolean = displayCorrectness("equals")
            if (isCorrect && userGameData.correctAnswers % 5 == 0) {
                userGameData.currentMillisUntilFinished += 10000
                updateTimer()
            }
        }
        // Giving functionality to the less button
        lessButton.setOnClickListener {
            val isCorrect: Boolean = displayCorrectness("less")
            if (isCorrect && userGameData.correctAnswers % 5 == 0) {
                userGameData.currentMillisUntilFinished += 10000
                updateTimer()
            }
        }

        // Generating the initial expressions upon starting the game
        generateExpressions()

        // Loading the state of the previous screen orientation
        if (savedInstanceState != null) {
            /* I used the .text property accessor because Android Studio
            * was complaining about the usage of the .setText() method */
            leftExpression.text = savedInstanceState.getString("leftExpression", "")
            rightExpression.text = savedInstanceState.getString("rightExpression", "")
            userGameData.correctAnswers = savedInstanceState.getInt("correctAnswers", 0)
            userGameData.incorrectAnswers = savedInstanceState.getInt("incorrectAnswers", 0)
            userGameData.currentMillisUntilFinished = savedInstanceState.getLong("currentMillisUntilFinished", 50000)
        }

        // Creating the timer
        timer = object: CountDownTimer(userGameData.currentMillisUntilFinished, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                userGameData.currentMillisUntilFinished = millisUntilFinished
                timerView.text = (millisUntilFinished / 1000).toString()
            }
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                userGameData.currentMillisUntilFinished = 0
                timerView.visibility = View.INVISIBLE
                leftExpression.visibility = View.INVISIBLE
                rightExpression.visibility = View.INVISIBLE
                greaterButton.visibility = View.INVISIBLE
                equalsButton.visibility = View.INVISIBLE
                lessButton.visibility = View.INVISIBLE
                endView.text = "Correct answers: ${userGameData.correctAnswers}" +
                        "\nIncorrect answers: ${userGameData.incorrectAnswers}"
                endView.visibility = View.VISIBLE
            }
        }

        // Starting the timer
        timer.start()
    }

    /**
     * Generates two random arithmetic expressions.
     * The expressions can have 1, 2, 3 or 4 terms, the number of terms are determined randomly.
     * The operations between terms can be the following: +, -, *, /, the operations are determined randomly.
     */
    @SuppressLint("SetTextI18n")
    private fun generateExpressions() {
        leftExpression = findViewById(R.id.leftExpression)
        rightExpression = findViewById(R.id.rightExpression)

        // The available operations
        val operations = listOf('+', '-', '*', '/')
        // Creating a random number generator
        val randomNumberGenerator = Random()
        // Generating the number of terms in the left expression
        val leftLength = randomNumberGenerator.nextInt(4) + 1
        // Generating the number of terms in the second expression
        val rightLength = randomNumberGenerator.nextInt(4) + 1

        // Generating the left expression
        when (leftLength) {
            1 -> leftExpression.text = (randomNumberGenerator.nextInt(20) + 1).toString() + ' '
            2 -> {
                do {
                    /* I chose a random item from the operations list using this method:
                    * https://www.baeldung.com/kotlin/list-get-random-item */
                    val randomOperation: Char = operations.random()
                    leftExpression.text = (randomNumberGenerator.nextInt(20) + 1).toString() + ' ' +
                            randomOperation + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ' '
                } while (!validSubExpression(leftExpression.text.toString()))
            }
            3 -> {
                do {
                    val randomOperation1: Char = operations.random()
                    val randomOperation2: Char = operations.random()
                    leftExpression.text = '(' + (randomNumberGenerator.nextInt(20) + 1).toString() + ' ' +
                            randomOperation1 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ')' + ' ' +
                            randomOperation2 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ' '
                } while (!validSubExpression(leftExpression.text.toString()))
            }
            4 -> {
                do {
                    val randomOperation1: Char = operations.random()
                    val randomOperation2: Char = operations.random()
                    val randomOperation3: Char = operations.random()
                    leftExpression.text = "((" + (randomNumberGenerator.nextInt(20) + 1).toString() + ' ' +
                            randomOperation1 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ')' + ' ' +
                            randomOperation2 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ')' + ' ' +
                            randomOperation3 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ' '
                } while (!validSubExpression(leftExpression.text.toString()))
            }
        }

        // Generating the right expression
        when (rightLength) {
            1 -> rightExpression.text = (randomNumberGenerator.nextInt(20) + 1).toString() + ' '
            2 -> {
                do {
                    val randomOperation: Char = operations.random()
                    rightExpression.text = (randomNumberGenerator.nextInt(20) + 1).toString() + ' ' +
                            randomOperation + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ' '
                } while (!validSubExpression(rightExpression.text.toString()))
            }
            3 -> {
                do {
                    val randomOperation1: Char = operations.random()
                    val randomOperation2: Char = operations.random()
                    rightExpression.text = '(' + (randomNumberGenerator.nextInt(20) + 1).toString() + ' ' +
                            randomOperation1 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ')' +
                            randomOperation2 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ' '
                } while (!validSubExpression(rightExpression.text.toString()))
            }
            4 -> {
                do {
                    val randomOperation1: Char = operations.random()
                    val randomOperation2: Char = operations.random()
                    val randomOperation3: Char = operations.random()
                    rightExpression.text = "((" + (randomNumberGenerator.nextInt(20) + 1).toString() + ' ' +
                            randomOperation1 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ')' + ' ' +
                            randomOperation2 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ')' + ' ' +
                            randomOperation3 + ' ' + (randomNumberGenerator.nextInt(20) + 1).toString() + ' '
                } while (!validSubExpression(rightExpression.text.toString()))
            }
        }
    }

    /**
     * Checks which button the user has pressed,
     * determines if they're correct, displays the appropriate message.
     * @param button the name of the button the user has pressed
     * @return true if the user pressed the correct button, false otherwise
     */
    private fun displayCorrectness(button: String): Boolean {
        val leftExpressionString: String = leftExpression.text.toString()
        val rightExpressionString: String = rightExpression.text.toString()
        val leftSolution = solveExpression(leftExpressionString)
        val rightSolution = solveExpression(rightExpressionString)
        when (button) {
            "greater" -> {
                if (leftSolution > rightSolution) {
                    userGameData.incrementCorrectAnswers()
                    displayCorrectMessage()
                    return true
                } else {
                    userGameData.incrementIncorrectAnswers()
                    displayIncorrectMessage()
                }
            }
            "equals" -> {
                if (leftSolution == rightSolution) {
                    userGameData.incrementCorrectAnswers()
                    displayCorrectMessage()
                    return true
                } else {
                    userGameData.incrementIncorrectAnswers()
                    displayIncorrectMessage()
                }
            }
            "less" -> {
                if (leftSolution < rightSolution) {
                    userGameData.incrementCorrectAnswers()
                    displayCorrectMessage()
                    return true
                } else {
                    userGameData.incrementIncorrectAnswers()
                    displayIncorrectMessage()
                }
            }
        }
        return false
    }

    /**
     * Solves String expressions and returns the solution as an Integer.
     * @param expression the arithmetic expression to be solved
     * @return the result of the expression
     */
    private fun solveExpression(expression: String): Int {
        val numbers = mutableListOf<Int>()
        val operations = mutableListOf<Char>()
        var tempNumber = ""
        for (i in expression) {
            if (i in "()") {
                continue
            } else if (i in '0'..'9') {
                tempNumber += i
            } else if (i == ' ' && tempNumber.isNotEmpty()) {
                numbers.add(tempNumber.toInt())
                tempNumber = ""
            } else if (i in "+-*/") {
                operations.add(i)
            }
        }
        var result: Int = numbers[0]
        for (i in 0 until operations.size) {
            when (operations[i]) {
                '+' -> result += numbers[i + 1]
                '-' -> result -= numbers[i + 1]
                '*' -> result *= numbers[i + 1]
                '/' -> result /= numbers[i + 1]
            }
        }
        return result
    }

    /**
     * Displays a green box that says the user was correct.
     */
    @SuppressLint("InflateParams")
    private fun displayCorrectMessage() {
        val popupView = layoutInflater.inflate(R.layout.correct_popup, null)
        val popupWindow = PopupWindow(this)
        popupWindow.contentView = popupView
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 400)
        popupView.setOnClickListener {
            popupWindow.dismiss()
            generateExpressions()
        }
    }

    /**
     * Displays a red box that says the user was incorrect.
     */
    @SuppressLint("InflateParams")
    private fun displayIncorrectMessage() {
        val popupView = layoutInflater.inflate(R.layout.incorrect_popup, null)
        val popupWindow = PopupWindow(this)
        popupWindow.contentView = popupView
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 400)
        popupView.setOnClickListener {
            popupWindow.dismiss()
            generateExpressions()
        }
    }

    /**
     * Validates sub-expressions.
     * @param expression the arithmetic expression to be validated
     * @return false if any sub-expression doesn't evaluate to whole number or evaluates to a number over 100, true otherwise
     */
    private fun validSubExpression(expression: String): Boolean {
        val numbers = mutableListOf<Int>()
        val operations = mutableListOf<Char>()
        var tempNumber = ""
        for (i in expression) {
            if (i in "()") {
                continue
            } else if (i in '0'..'9') {
                tempNumber += i
            } else if (i == ' ' && tempNumber.isNotEmpty()) {
                numbers.add(tempNumber.toInt())
                tempNumber = ""
            } else if (i in "+-*/") {
                operations.add(i)
            }
        }
        var result: Int = numbers[0]
        for (i in 0 until operations.size) {
            when (operations[i]) {
                '+' -> {
                    if (result + numbers[i + 1] > 100) {
                        return false
                    }
                    result += numbers[i + 1]
                }
                '-' -> result -= numbers[i + 1]
                '*' -> {
                    if (result * numbers[i + 1] > 100) {
                        return false
                    }
                    result *= numbers[i + 1]
                }
                '/' -> {
                    if (result % numbers[i + 1] != 0) {
                        return false
                    }
                    result /= numbers[i + 1]
                }
            }
        }
        return true
    }

    /**
     * Adds 10 seconds to the timer by cancelling the current one and creating a new one with the extra time added.
     */
    private fun updateTimer() {
        timer.cancel()
        timer = object: CountDownTimer(userGameData.currentMillisUntilFinished, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                userGameData.currentMillisUntilFinished = millisUntilFinished
                timerView.text = (millisUntilFinished / 1000).toString()
            }
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                timerView.visibility = View.INVISIBLE
                leftExpression.visibility = View.INVISIBLE
                rightExpression.visibility = View.INVISIBLE
                greaterButton.visibility = View.INVISIBLE
                equalsButton.visibility = View.INVISIBLE
                lessButton.visibility = View.INVISIBLE
                endView.text = "Correct answers: ${userGameData.correctAnswers}" +
                        "\nIncorrect answers: ${userGameData.incorrectAnswers}"
                endView.visibility = View.VISIBLE
            }
        }
        timer.start()
    }

    /**
     * Saves the state of the application.
     * Saves the following data: the left expression, the right expression, the timer
     * and the number of correct and incorrect answers.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("leftExpression", leftExpression.text.toString())
        outState.putString("rightExpression", rightExpression.text.toString())
        outState.putInt("correctAnswers", userGameData.correctAnswers)
        outState.putInt("incorrectAnswers", userGameData.incorrectAnswers)
        outState.putLong("currentMillisUntilFinished", userGameData.currentMillisUntilFinished)
    }
}
