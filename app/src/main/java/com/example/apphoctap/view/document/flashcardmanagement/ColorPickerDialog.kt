package com.example.apphoctap.view.document.flashcardmanagement

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apphoctap.R
import com.google.android.material.card.MaterialCardView

class ColorPickerDialog(
    context: Context,
    private val currentColor: String,
    private val onColorSelected: (String) -> Unit
) : Dialog(context) {

    private val colors = listOf(
        "#FFFFFF", // White
        "#F5F5F5", // Light Gray
        "#FFCDD2", // Light Red
        "#F8BBD0", // Light Pink
        "#E1BEE7", // Light Purple
        "#D1C4E9", // Light Deep Purple
        "#C5CAE9", // Light Indigo
        "#BBDEFB", // Light Blue
        "#B3E5FC", // Light Light Blue
        "#B2EBF2", // Light Cyan
        "#B2DFDB", // Light Teal
        "#C8E6C9", // Light Green
        "#DCEDC8", // Light Light Green
        "#F0F4C3", // Light Lime
        "#FFF9C4", // Light Yellow
        "#FFECB3", // Light Amber
        "#FFE0B2", // Light Orange
        "#FFCCBC", // Light Deep Orange
        "#D7CCC8", // Light Brown
        "#F5F5F5", // Light Grey
        "#CFD8DC"  // Light Blue Grey
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_color_picker)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewColors)
        val btnCancel = findViewById<Button>(R.id.btnCancelColor)

        recyclerView.layoutManager = GridLayoutManager(context, 4) // 4 columns
        recyclerView.adapter = ColorAdapter(colors) { selectedColor ->
            onColorSelected(selectedColor)
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        setTitle(context.getString(R.string.select_color))
    }

    inner class ColorAdapter(
        private val colorList: List<String>,
        private val onColorClick: (String) -> Unit
    ) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

        inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val colorCard: MaterialCardView = itemView.findViewById(R.id.colorItemCard)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_color, parent, false)
            return ColorViewHolder(view)
        }

        override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
            val color = colorList[position]
            holder.colorCard.setCardBackgroundColor(Color.parseColor(color))

            // Highlight currently selected color
            if (color == currentColor) {
                holder.colorCard.strokeWidth = 4
                holder.colorCard.strokeColor = ContextCompat.getColor(context, R.color.colorPrimary)
            } else {
                holder.colorCard.strokeWidth = 0
            }

            holder.itemView.setOnClickListener {
                onColorClick(color)
            }
        }

        override fun getItemCount() = colorList.size
    }
}