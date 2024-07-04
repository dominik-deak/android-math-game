/* ATTENTION: This app was tested on a Pixel 5 with API 30
* Other screen sizes may position the popup windows differently,
* resulting in a less enjoyable user experience */

package com.example.coursework1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity

/**
 * The welcome screen of the application,
 * contains an about button and a new game button.
 * @author Dominik Deak - w1778659
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newGameButton: Button = findViewById(R.id.newGameButton)
        val aboutButton: Button = findViewById(R.id.aboutButton)
        // Display popup message when the about button is pressed
        aboutButton.setOnClickListener {
            displayPopupMessage()
        }
        // Display the game screen when the new game button is pressed
        newGameButton.setOnClickListener {
            val gameScreenIntent = Intent(this, GameScreen::class.java)
            startActivity(gameScreenIntent)
        }
    }

    /**
     * Displays a popup window with the about message.
     * Dismisses the popup window when pressed.
     * I added the SuppressLint annotation to remove warnings with the layoutInflater.
     */
    @SuppressLint("InflateParams")
    private fun displayPopupMessage() {
        val popupView = layoutInflater.inflate(R.layout.popup_window, null)
        val popupWindow = PopupWindow(this)
        popupWindow.contentView = popupView
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 400)
        popupView.setOnClickListener {
            popupWindow.dismiss()
        }
    }
}
