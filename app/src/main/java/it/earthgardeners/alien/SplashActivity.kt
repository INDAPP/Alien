package it.earthgardeners.alien

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
