package it.earthgardeners.alien.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import it.earthgardeners.alien.*
import kotlinx.android.synthetic.main.activity_score.*

class ScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        buttonContinue.setOnClickListener(this::onButtonClick)

        val time = intent?.getFloatExtra(EXTRA_TIME, 1f)?: return finish()
        val wrong = intent?.getFloatExtra(EXTRA_ERROR_COUNT, 0f)?: return finish()
        val total = intent?.getFloatExtra(EXTRA_TOTAL_COUNT, 0f)?: return finish()

        textViewWrong.text = "${wrong.toInt()}"
        textViewTime.text =time.timer

        val result = (total - wrong)/time*10000
        textViewPoints.text = "${result.toInt()}"

        MediaPlayer.create(this, R.raw.win)?.start()
    }

    override fun onBackPressed() {
        close()
    }

    private fun onButtonClick(view: View) {
        close()
    }

    private fun close() {
        ActivityCompat.finishAffinity(this)
    }

}
