package it.earthgardeners.alien.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import it.earthgardeners.alien.AlienRepository
import it.earthgardeners.alien.EXTRA_HABITAT_TAG
import it.earthgardeners.alien.R
import it.earthgardeners.alien.models.Habitat
import kotlinx.android.synthetic.main.activity_habitat.*

class HabitatActivity : AppCompatActivity() {

    private lateinit var habitat: Habitat
    private var habitatSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habitat)

        this.habitatSelected = intent.extras.getString(EXTRA_HABITAT_TAG)

        buttonContinue.setOnClickListener(this::onButtonClick)
        val tag = intent?.getStringExtra(EXTRA_HABITAT_TAG)?: return finish()
        this.habitat = AlienRepository.habitats.firstOrNull { it.tag == tag }?: return finish()

        textViewDescription.setText(habitat.description)
        textViewTitle.setText(habitat.name)

        val imageRef = FirebaseStorage.getInstance().getReference("habitat/${habitat.tag}.jpg")
        Glide.with(this).load(imageRef).into(imageView)
    }

    private fun onButtonClick(view: View) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(EXTRA_HABITAT_TAG, habitatSelected)
        }
        startActivity(intent)


    }

}
