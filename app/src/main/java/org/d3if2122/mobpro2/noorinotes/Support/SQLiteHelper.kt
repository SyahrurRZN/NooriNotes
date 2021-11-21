package org.d3if2122.mobpro2.noorinotes.Support

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import java.lang.Exception

class SQLiteHelper(context: Context?) : SQLiteOpenHelper(context,
    Constants.DATABASE_NAME,
    null,
    Constants.DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
//        val createTblNotes = ("CREATE TABLE "+ TBL_NOTES + " ("
//                + ID +" INTEGER PRIMARY KEY, "
//                + JUDUL +" TEXT, "
//                + ISI +" TEXT, "
//                + URLLINK + " TEXT, "
//                + GAMBAR + " TEXT, "
//                + TANGGAL + " TEXT);")
        db!!.execSQL(Constants.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS "+Constants.TBL_NOTES)
        onCreate(db)
    }

    fun insertNote(notes: Notes):Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constants.JUDUL,notes.judul)
        contentValues.put(Constants.ISI,notes.isi)
        contentValues.put(Constants.URLLINK,notes.urlLink)
        contentValues.put(Constants.GAMBAR,notes.gambar)
        contentValues.put(Constants.TANGGAL,notes.tanggal)

        val success = db.insert(Constants.TBL_NOTES, null, contentValues)
        db.close()
        return success
    }

    fun getAllNote():ArrayList<Notes>{
        val noteList: ArrayList<Notes> = ArrayList()
        val selectQuery = "SELECT * FROM ${Constants.TBL_NOTES}"
        val db = this.readableDatabase

        val cursor:Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id:String
        var judul:String
        var isi:String
        var urllink:String
        var gambar:String
        var tanggal:String

        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id")).toString()
                judul = cursor.getString(cursor.getColumnIndex("judul"))
                isi = cursor.getString(cursor.getColumnIndex("isi"))
                urllink = cursor.getString(cursor.getColumnIndex("urllink"))
                gambar = cursor.getString(cursor.getColumnIndex("gambar"))
                tanggal = cursor.getString(cursor.getColumnIndex("tanggal"))

                val note = Notes(id = id, judul = judul, isi = isi, urlLink =  urllink, gambar = gambar, tanggal = tanggal)
                noteList.add(note)
            }while (cursor.moveToNext())
        }
        return noteList
    }

    fun updateNote(notes: Notes): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,notes.id)
        contentValues.put(JUDUL,notes.judul)
        contentValues.put(ISI,notes.isi)
        contentValues.put(URLLINK,notes.urlLink)
        contentValues.put(GAMBAR,notes.gambar)
        contentValues.put(TANGGAL,notes.tanggal)

        val success = db.update(TBL_NOTES,contentValues,"id="+notes.id,null)
        db.close()
        return success
    }

    fun getNote(query:String):ArrayList<Notes>{
        val noteList: ArrayList<Notes> = ArrayList()
        val selectQuery = "SELECT * FROM ${Constants.TBL_NOTES} WHERE ${Constants.JUDUL} LIKE '% $query%'"
        val db = this.readableDatabase

        val cursor:Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id:String
        var judul:String
        var isi:String
        var urllink:String
        var gambar:String
        var tanggal:String

        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id")).toString()
                judul = cursor.getString(cursor.getColumnIndex("judul"))
                isi = cursor.getString(cursor.getColumnIndex("isi"))
                urllink = cursor.getString(cursor.getColumnIndex("urllink"))
                gambar = cursor.getString(cursor.getColumnIndex("gambar"))
                tanggal = cursor.getString(cursor.getColumnIndex("tanggal"))

                val note = Notes(id = id, judul = judul, isi = isi, urlLink =  urllink, gambar = gambar, tanggal = tanggal)
                noteList.add(note)
            }while (cursor.moveToNext())
        }
        return noteList
    }

    fun deleteNote(id: Int): Int{
        val db  = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,id)

        val success = db.delete(TBL_NOTES,"id=$id",null)
        db.close()
        return success
    }
}