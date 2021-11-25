package org.d3if2122.mobpro2.noorinotes.Model

import androidx.room.*

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
}