package com.divallion.diginote.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.divallion.diginote.R
import com.divallion.diginote.model.Notes
import com.divallion.diginote.model.NotesList
import kotlinx.android.synthetic.main.list_item_delete.view.*

open class RViewDeleteAdapter : RecyclerView.Adapter<RViewDeleteAdapter.VHolder>() {

    open var mList: List<Notes> = NotesList.list

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VHolder =
            VHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_view_delete, parent, false))

    override fun onBindViewHolder(holder: VHolder?, position: Int) {
        holder?.bindViews(mList[position])
    }

    override fun getItemCount(): Int = mList.size

    inner class VHolder( view: View) : RecyclerView.ViewHolder(view) {

        fun bindViews(notes: Notes) {
            with(notes){
                itemView.noteTitle.text = notes.title
                itemView.noteDescription.text = notes.desc
                itemView.deleteCheck.isChecked = notes.isSelected
                itemView.deleteCheck.tag = notes
                itemView.deleteCheck.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(button: CompoundButton?, isChecked: Boolean) {
                        val noteTag = button?.tag as Notes
                        noteTag.isSelected = isChecked
                        NotesList.list = this@RViewDeleteAdapter.mList
                    }
                })
            }
        }

    }

}