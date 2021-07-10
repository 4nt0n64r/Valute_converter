package com.a4nt0n64r.valuteconverter.screens.select

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a4nt0n64r.valuteconverter.R
import com.a4nt0n64r.valuteconverter.models.ValuteUI

class SelectAdapter : RecyclerView.Adapter<SelectAdapter.SelectViewHolder>() {

    private var listValutes = emptyList<ValuteUI>()
    private lateinit var changingType:ChangingType

    fun setData(list: List<ValuteUI>) {
        listValutes = list
        notifyDataSetChanged()
    }

    fun setChangingType(type: ChangingType){
        changingType = type
    }

    class SelectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val valuteName = itemView.findViewById<TextView>(R.id.valute_name)
        val valuteCharCode = itemView.findViewById<TextView>(R.id.valute_char_code)
        val image = itemView.findViewById<ImageView>(R.id.check_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.valute_item, parent, false)
        return SelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        holder.valuteName.text = listValutes[position].name
        holder.valuteCharCode.text = listValutes[position].charCode
        if (listValutes[position].isSelected) {
            holder.image.visibility = VISIBLE
        } else {
            holder.image.visibility = INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return listValutes.size
    }

    override fun onViewAttachedToWindow(holder: SelectViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            SelectValuteFragment.click(listValutes[holder.adapterPosition],changingType)
        }
    }

    override fun onViewDetachedFromWindow(holder: SelectViewHolder) {
        holder.itemView.setOnClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }
}
