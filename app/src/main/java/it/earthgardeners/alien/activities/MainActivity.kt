package it.earthgardeners.alien.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import it.earthgardeners.alien.AlienRepository
import it.earthgardeners.alien.EXTRA_HABITAT_TAG
import it.earthgardeners.alien.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart.setOnClickListener(this::onButtonClick)


    }

    private fun onButtonClick(view: View) {
        val habitatSelected = AlienRepository.habitats.map { it.tag }.shuffled().first()
        val intent = Intent(this, HabitatActivity::class.java).apply {
            putExtra(EXTRA_HABITAT_TAG, habitatSelected)
        }
        startActivity(intent)
    }
}
