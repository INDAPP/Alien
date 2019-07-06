package it.earthgardeners.alien.fragments

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import it.earthgardeners.alien.AlienRepository

import it.earthgardeners.alien.R
import it.earthgardeners.alien.models.Creature
import kotlinx.android.synthetic.main.fragment_creature.*

private const val ARG_CREATURE_TYPE = "creature_type"
private const val ARG_TAG = "tag"

class CreatureFragment : Fragment() {
    private var creature: Creature? = null
    private var listener: OnFragmentInteractionListener? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = arguments?.getSerializable(ARG_CREATURE_TYPE) as Creature.Type? ?: return
        val tag = arguments?.getString(ARG_TAG) ?: return

        val creature: Creature = when (type) {
            Creature.Type.PLANT -> AlienRepository.plants.firstOrNull { it.tag == tag }
            Creature.Type.ANIMAL -> AlienRepository.animals.firstOrNull { it.tag == tag }
            else -> null
        } ?: return

        this.creature = creature
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creature, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val creature = this.creature ?: return
        textViewName.text = creature.name

        val folder = when (creature.type) {
            Creature.Type.PLANT -> "plant"
            Creature.Type.ANIMAL -> "animal"
        }
        val imageRef = FirebaseStorage.getInstance().getReference("$folder/${creature.tag}.jpg")
        Glide.with(this).load(imageRef).into(imageViewCreature)

        buttonPlay.setOnClickListener(this::playAudio)
        buttonInfo.setOnClickListener(this::showInfo)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnFragmentInteractionListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun playAudio(view: View) {
        val creature = this.creature ?: return
        val folder = when (creature.type) {
            Creature.Type.PLANT -> "plant_sound"
            Creature.Type.ANIMAL -> "animal_sound"
        }

        val soundRef = FirebaseStorage.getInstance().getReference("$folder/${creature.tag}.jpg")
        soundRef.downloadUrl
            .addOnSuccessListener(this::onAudioUrlSuccess)
            .addOnFailureListener(this::onAudioUrlFailure)
    }

    private fun onAudioUrlSuccess(uri: Uri) {
        mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(uri.toString())
            prepare() // might take long! (for buffering, etc)
            start()
        }
    }

    private fun onAudioUrlFailure(t: Throwable) {
        Log.e("ERRORE", "File Audio Url", t)
    }

    private fun showInfo(view: View) {
        AlertDialog.Builder(view.context)
            .setTitle(creature?.name)
            .setMessage(creature?.description)
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(creature: Creature) =
            CreatureFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CREATURE_TYPE, creature.type)
                    putString(ARG_TAG, creature.tag)
                }
            }
    }
}
