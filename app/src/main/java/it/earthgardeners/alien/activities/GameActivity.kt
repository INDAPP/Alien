package it.earthgardeners.alien.activities

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import it.earthgardeners.alien.*
import it.earthgardeners.alien.fragments.CreatureFragment
import it.earthgardeners.alien.listeners.CircularViewPagerHandler
import it.earthgardeners.alien.models.Animal
import it.earthgardeners.alien.models.Creature
import it.earthgardeners.alien.models.Habitat
import it.earthgardeners.alien.models.Plant
import it.earthgardeners.alien.viewholders.CreatureViewHolder
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    private lateinit var habitat: Habitat

    private lateinit var animals: MutableList<Animal>
    private lateinit var plants: MutableList<Plant>

    private var creatures: MutableList<Creature> = mutableListOf()

    private var creaturesType: Creature.Type = Creature.Type.PLANT
    private var currentCreatureIndex = 0
    private var creaturesTypeCount = 0

    private var targetPlantsCount = 0
    private var targetAnimalsCount = 0

    private lateinit var viewPagerAdapter: CreaturesPagerAdapter
    private lateinit var recyclerViewAdapter: CreaturesRecyclerViewAdapter

    private var mediaPlayer: MediaPlayer? = null
        set(value) {
            field?.stop()
            field = value
            value?.start()
        }

    private val currentCreature: Creature
        get() = when (creaturesType) {
            Creature.Type.PLANT -> plants[currentCreatureIndex]
            Creature.Type.ANIMAL -> animals[currentCreatureIndex]
        }

    private var errorCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(toolbar)

        val tag = intent?.getStringExtra(EXTRA_HABITAT_TAG) ?: return finish()
        this.habitat = AlienRepository.habitats.firstOrNull { it.tag == tag } ?: return finish()
        this.animals = AlienRepository.animals.toMutableList()
        this.plants = AlienRepository.plants.toMutableList()

        setup()
    }

    private fun setup() {
        title = this.habitat.name
        animals.shuffle()
        plants.shuffle()
        creaturesTypeCount = this.plants.size

        targetPlantsCount = (this.plants.count { it.habitat.contains(habitat.tag) } + 1) / 2
        targetAnimalsCount = (this.animals.count { it.habitat.contains(habitat.tag) } + 1) / 2

        progressBar.max = targetPlantsCount + targetAnimalsCount
        progressBar.progress = 0

        this.viewPagerAdapter = CreaturesPagerAdapter(supportFragmentManager)
        this.recyclerViewAdapter = CreaturesRecyclerViewAdapter()

//        viewPager.addOnPageChangeListener(CircularViewPagerHandler(viewPager))
        viewPager.adapter = this.viewPagerAdapter

        recyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = this.recyclerViewAdapter

        val imageRef = FirebaseStorage.getInstance().getReference("habitat/${habitat.tag}.jpg")

        Glide.with(this).load(imageRef).into(imageView)

        //TODO: impostare i suoni di sottofondo

        buttonInsert.setOnClickListener(this::insertCreature)
        buttonDiscard.setOnClickListener(this::discardCreature)
    }

    private fun insertCreature(view: View) {
        checkInsertedCreature(currentCreature)
        nextCreature()
        mediaPlayer = MediaPlayer.create(this, R.raw.choose)
//        when (creaturesType) {
//            Creature.Type.PLANT -> plants.removeAt(currentCreatureIndex)
//            Creature.Type.ANIMAL -> animals.removeAt(currentCreatureIndex)
//        }
//        viewPagerAdapter.notifyDataSetChanged()
    }

    private fun discardCreature(view: View) {
        nextCreature()
        mediaPlayer = MediaPlayer.create(this, R.raw.choose)
    }

    private fun nextCreature() {
        currentCreatureIndex++
        viewPager.setCurrentItem(currentCreatureIndex % creaturesTypeCount, true)
        if (creatures.contains(currentCreature)) nextCreature()
    }

    private fun checkInsertedCreature(creature: Creature) {
        when {
            creature.alien.contains(habitat.tag) -> {
                toast("Hai inserito una specie Aliena!!")
                return gameOver()
            }
            creature.habitat.contains(habitat.tag) -> {
                this.creatures.add(currentCreature)
                recyclerViewAdapter.notifyDataSetChanged()
                calculateProgress()
            }
            else -> {
                errorCounter++
                //TODO: animazione alieno??
                checkGameOver()
            }
        }

    }

    private fun calculateProgress() {
        progressBar.progress = creatures.size
        if (creatures.size == targetPlantsCount) {
            creaturesType = Creature.Type.ANIMAL
            viewPagerAdapter.notifyDataSetChanged()
            //TODO: mostrare un popup/suono intermedio?
        } else if (creatures.size == targetPlantsCount + targetAnimalsCount) {
            startActivity(
                Intent(this, ScoreActivity::class.java).apply {
                    putExtra(EXTRA_TOTAL_COUNT, (targetPlantsCount+targetAnimalsCount).toFloat())
                    putExtra(EXTRA_ERROR_COUNT, errorCounter.toFloat())
                    putExtra(EXTRA_TIME, 0) //timer contatore
                }
            )
        }
    }

    private fun checkGameOver() {
        if (errorCounter >= targetPlantsCount + targetAnimalsCount) {
            toast("Hai commesso troppi errori!!")
            gameOver()
        }
    }

    private fun gameOver() {
        startActivity(
            Intent(this, GameOverActivity::class.java)
        )
    }

    inner class CreaturesPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment = when (creaturesType) {
            Creature.Type.PLANT -> CreatureFragment.newInstance(this@GameActivity.plants[position])
            Creature.Type.ANIMAL -> CreatureFragment.newInstance(this@GameActivity.animals[position])
        }

        override fun getCount(): Int = when (creaturesType) {
            Creature.Type.PLANT -> this@GameActivity.plants.size
            Creature.Type.ANIMAL -> this@GameActivity.animals.size
        }

    }

    inner class CreaturesRecyclerViewAdapter: RecyclerView.Adapter<CreatureViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatureViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_creature, parent, false)
            return CreatureViewHolder(view)
        }

        override fun getItemCount(): Int = this@GameActivity.creatures.size

        override fun onBindViewHolder(holder: CreatureViewHolder, position: Int) {
            holder.bind(this@GameActivity.creatures[position])
        }

    }


}
