package org.d3if2122.mobpro2.noorinotes.Model

import androidx.room.*
import java.util.*

@Dao
interface NotesDao {
    @Insert
    fun tambahNote(note:Notes)

    @Update
    fun ubahNote(note:Notes)

    @Delete
    fun hapusNote(note: Notes)

    @Query("SELECT * FROM notes")
    fun getAllNotes():List<Notes>

    @Query("SELECT * FROM notes WHERE id=:note_id")
    fun getNote(note_id:Int):List<Notes>

    @Query("SELECT * FROM notes WHERE tanggal BETWEEN :from AND :to")
    fun getAllNotesbyDay(from: Date, to: Date):List<Notes>

    @Query("DELETE FROM notes WHERE id=:note_id")
    fun deleteote(note_id:Int)
}