package com.example.apphoctap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.R
import com.example.apphoctap.database.entities.DeckEntity

class FlashcardAdapter(
    private var items: List<DeckEntity>,
    private var onClickItem: (DeckEntity)-> Unit,
    private val ondeleteClick: (DeckEntity) -> Unit
) : RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder>() {

    inner class FlashcardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFlashcardSet: TextView = itemView.findViewById(R.id.tvFlashcardSet)
        val tvFlashcardCount: TextView = itemView.findViewById(R.id.tvFlashcardCount)
        val btnOptions: ImageButton = itemView.findViewById(R.id.btnFlashcardOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flashcard, parent, false)
        return FlashcardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        val deck = items[position]
        holder.tvFlashcardSet.text = deck.name

        holder.itemView.setOnClickListener {
            onClickItem(deck)
        }

        holder.btnOptions.setOnClickListener {view ->
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.delete_popup_menu)
            popup.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.action_deletestudent) {
                    ondeleteClick(deck)
                    true
                } else false
            }
            popup.show()
        }
    }


    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<DeckEntity>) {
        items = newItems
        notifyDataSetChanged()
    }
}
