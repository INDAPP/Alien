package it.earthgardeners.alien.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.earthgardeners.alien.AlienRepository
import it.earthgardeners.alien.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        AlienRepository.sync(this::onCompletion)
    }

    private fun onCompletion() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
    }
}
