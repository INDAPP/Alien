package it.earthgardeners.alien.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.earthgardeners.alien.AlienRepository

import it.earthgardeners.alien.R
import it.earthgardeners.alien.models.Creature

private const val ARG_CREATURE_TYPE = "creature_type"
private const val ARG_TAG = "tag"

class CreatureFragment : Fragment() {
    private var creature: Creature? = null
    private var listener: OnFragmentInteractionListener? = null

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnFragmentInteractionListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
