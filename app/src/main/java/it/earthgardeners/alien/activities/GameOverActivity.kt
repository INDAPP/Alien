package it.earthgardeners.alien.activities

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import it.earthgardeners.alien.R

class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        MediaPlayer.create(this, R.raw.game_over)?.start()

        Handler().postDelayed(this::close, 5000)
    }

    override fun onBackPressed() {
        close()
    }

    private fun close() {
        ActivityCompat.finishAffinity(this)
    }
}
