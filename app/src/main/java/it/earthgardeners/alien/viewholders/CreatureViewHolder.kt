package it.earthgardeners.alien.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import it.earthgardeners.alien.models.Creature
import kotlinx.android.synthetic.main.viewholder_creature.view.*

class CreatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.imageView
        val cardView = itemView.circleCardView

        fun bind(creature: Creature) {
            //TODO: impostare immagine da firebase
        }
    }