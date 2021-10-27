package org.d3if2122.mobpro2.noorinotes.Model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if2122.mobpro2.noorinotes.R

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var noteList: ArrayList<Notes> = ArrayList()

    fun addItems(items : ArrayList<Notes>){
        this.noteList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= NotesViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.notes_item_list,parent,false)
    )

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        var note = noteList[position]
        holder.bindView(note)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class NotesViewHolder(var view: View): RecyclerView.ViewHolder(view){
        private var imageGambar = view.findViewById<ImageView>(R.id.fotoNotes)
        private var judul = view.findViewById<TextView>(R.id.judulNotes)
        private var tanggal = view.findViewById<TextView>(R.id.tanggalNotes)

        fun bindView(note: Notes){
            Glide.with(view).load(note.gambar).into(imageGambar)
            judul.text = note.judul
            tanggal.text =note.tanggal
        }
    }
}