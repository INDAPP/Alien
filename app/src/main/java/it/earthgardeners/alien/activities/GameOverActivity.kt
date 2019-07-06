package it.earthgardeners.alien.activities

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import it.earthgardeners.alien.EXTRA_ALIEN
import it.earthgardeners.alien.R
import it.earthgardeners.alien.toast

class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        Handler().postDelayed(this::close, 5000)

        val alien = intent.getBooleanExtra(EXTRA_ALIEN, false)

        if (alien) {
            MediaPlayer.create(this, R.raw.alien)?.start()
            toast("Hai inserito una specie Aliena!!")
        } else {
            MediaPlayer.create(this, R.raw.game_over)?.start()
            toast("Hai commesso troppi errori!!")
        }
    }

    override fun onBackPressed() {
        close()
    }

    private fun close() {
        ActivityCompat.finishAffinity(this)
    }
}
