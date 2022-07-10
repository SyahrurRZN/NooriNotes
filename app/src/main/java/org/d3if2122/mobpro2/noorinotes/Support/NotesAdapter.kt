package org.d3if2122.mobpro2.noorinotes.Support

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.notes_item_list.view.*
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.R
import java.text.SimpleDateFormat

class NotesAdapter(private val notes:ArrayList<Notes>, private val listener:OnAdapterListener) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notes_item_list, parent, false))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        val formatter = SimpleDateFormat("HH:mm")
        holder.view.judulNotes.text = note.judul
        holder.view.tanggalNotes.text = formatter.format(note.tanggal)
        holder.view.tempatClick.setOnClickListener{
            listener.onRead(note)
        }
        holder.view.iEdit.setOnClickListener{
            listener.onUpdate(note)
        }
        holder.view.iDelete.setOnClickListener{
            listener.onDelete(note)
        }
    }

    override fun getItemCount() = notes.size

    class NotesViewHolder(val view: View): RecyclerView.ViewHolder(view)

    fun setData(list:List<Notes>){
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onRead(note:Notes)
        fun onUpdate(note:Notes)
        fun onDelete(note:Notes)
    }
}