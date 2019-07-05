package it.earthgardeners.alien.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.earthgardeners.alien.R
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(toolbar)


    }
}
