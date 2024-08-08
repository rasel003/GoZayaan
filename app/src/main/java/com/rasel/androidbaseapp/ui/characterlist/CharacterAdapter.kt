package com.rasel.androidbaseapp.ui.characterlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rasel.androidbaseapp.base.BaseAdapter
import com.bumptech.glide.RequestManager
import com.rasel.androidbaseapp.R
import com.rasel.androidbaseapp.domain.models.Character
import com.rasel.androidbaseapp.databinding.ItemCharacterListBinding
import javax.inject.Inject

class CharacterAdapter @Inject constructor(
    private val glide: RequestManager
) : BaseAdapter<Character>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    override val differ = AsyncListDiffer(this, diffCallback)
    private var clickListener: ClickListener? = null

    fun setUpListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }


    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemCharacterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    inner class CharacterViewHolder(private val binding: ItemCharacterListBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<Character> {
        override fun bind(item: Character) {
            binding.apply {
                textViewCharacterName.text = "$bindingAdapterPosition ${item.name}"
                glide.load(item.image).into(imageViewCharacter)
                root.setOnClickListener {
                    onItemClickListener?.let { itemClick ->
                        itemClick(item)

                        ViewCompat.setTransitionName(binding.imageViewCharacter, itemView.context.getString(R.string.transition_image))

                        clickListener?.onItemSelected(
                            item,
                            binding.imageViewCharacter,
                            binding.imageViewCharacter.context.getString(
                                R.string.transition_image
                            )
                        )
                    }
                }
                textViewStatus.text = "${item.status} - ${item.species}"
                textViewKnownLocation.text = item.characterLocation.name
            }
        }
    }

    interface ClickListener {
        fun onItemSelected(character: Character, transitionView: View, transitionName: String)
    }
}
