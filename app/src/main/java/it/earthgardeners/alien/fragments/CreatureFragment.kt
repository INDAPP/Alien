package it.earthgardeners.alien.fragments

import android.content.Context
import android.graphics.Typeface
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
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

    override fun onPause() {
        super.onPause()
        mediaPlayer?.stop()
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
        view.isEnabled = false
        val folder = when (creature.type) {
            Creature.Type.PLANT -> "plant_sound"
            Creature.Type.ANIMAL -> "animal_sound"
        }

        val soundRef = FirebaseStorage.getInstance().getReference("$folder/${creature.tag}.m4a")
        soundRef.downloadUrl
            .addOnSuccessListener(this::onAudioUrlSuccess)
            .addOnFailureListener(this::onAudioUrlFailure)
    }

    private fun onAudioUrlSuccess(uri: Uri) {
        mediaPlayer = MediaPlayer.create(context, uri).apply {
            //setAudioStreamType(AudioManager.STREAM_MUSIC)
            setOnPreparedListener(MediaPlayer::start)
            //prepareAsync() // might take long! (for buffering, etc)
        }
    }

    private fun onAudioUrlFailure(t: Throwable) {
        Log.e("ERRORE", "File Audio Url", t)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!isVisibleToUser) {
            mediaPlayer?.stop()
        }
    }

    private fun showInfo(view: View) {
        val latinCount = creature?.latin?.length
        val latin = creature?.latin?.let { "$it\n\n" } ?: ""
        val description = creature?.description ?: ""
        val message = SpannableString("$latin$description").apply {
            latinCount?.let { count ->
                setSpan(StyleSpan(Typeface.ITALIC), 0, count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        AlertDialog.Builder(view.context)
            .setTitle(creature?.name)
            .setMessage(message)
            .setNegativeButton(android.R.string.ok, null)
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
