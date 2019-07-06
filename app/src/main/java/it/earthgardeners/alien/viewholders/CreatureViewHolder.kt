package it.earthgardeners.alien.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import it.earthgardeners.alien.models.Creature
import kotlinx.android.synthetic.main.viewholder_creature.view.*

class CreatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.imageView
//        val cardView = itemView.circleCardView

        fun bind(creature: Creature) {
            val folder = when (creature.type) {
                Creature.Type.PLANT -> "plant"
                Creature.Type.ANIMAL -> "animal"
            }
            val imageRef = FirebaseStorage.getInstance().getReference("$folder/${creature.tag}.jpg")
            Glide.with(itemView).load(imageRef).into(imageView)
        }
    }