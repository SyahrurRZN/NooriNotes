package org.d3if2122.mobpro2.noorinotes.Support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.history_item_list.view.*
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.R
import java.text.SimpleDateFormat

class HistoryAdapter(private val notes:ArrayList<Notes>, private val listener: OnHistoryAdapterListener) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(val view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.history_item_list, parent,false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val note = notes[position]
        holder.view.judulNotes.text = note.judul
        val formatter = SimpleDateFormat(Constants.sdf)
        holder.view.tanggalNotes.text = formatter.format(note.tanggal)
        holder.view.tempatHistoryClick.setOnClickListener{
            listener.onRead(note)
        }
    }

    override fun getItemCount() = notes.size

    fun setData(list:List<Notes>){
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }
    interface OnHistoryAdapterListener{
        fun onRead(note:Notes)
    }
}