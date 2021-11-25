package org.d3if2122.mobpro2.noorinotes.Support

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.Model.NotesDao

@Database(
    entities = [Notes::class],
    version = 1
)
abstract class NotesDB: RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object{
        @Volatile private var instance : NotesDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NotesDB::class.java,
            "table_note.db"
        ).build()
    }
}