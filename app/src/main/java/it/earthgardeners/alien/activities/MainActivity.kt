package it.earthgardeners.alien.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import it.earthgardeners.alien.AlienRepository
import it.earthgardeners.alien.EXTRA_HABITAT_TAG
import it.earthgardeners.alien.R
import it.earthgardeners.alien.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart.setOnClickListener(this::onButtonClick)
        buttonGuide.setOnClickListener(this::showGuide)
        buttonLicence.setOnClickListener(this::showLicence)
        buttonInfo.setOnClickListener(this::showInfo)
        buttonWeb.setOnClickListener(this::showBrowser)
    }

    private fun onButtonClick(view: View) {
        val habitatSelected = AlienRepository.habitats.map { it.tag }.shuffled().firstOrNull()
            ?: return toast("Errore di sincronizzazione")
        val intent = Intent(this, HabitatActivity::class.java).apply {
            putExtra(EXTRA_HABITAT_TAG, habitatSelected)
        }
        startActivity(intent)
    }

    private fun showGuide(view: View) {
        AlertDialog.Builder(this)
            .setMessage(R.string.app_guide)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun showLicence(view: View) {
        AlertDialog.Builder(this)
            .setMessage(R.string.app_licence)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun showInfo(view: View) {
        AlertDialog.Builder(this)
            .setMessage(R.string.app_info)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun showBrowser(view: View) {
        val link = "http://earthgardeners.it/"
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        )
    }
}
