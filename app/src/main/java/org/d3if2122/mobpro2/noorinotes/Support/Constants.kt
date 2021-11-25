package org.d3if2122.mobpro2.noorinotes.Support

class Constants {
//    const val DATABASE_NAME="notes.db"
//    const val DATABASE_VERSION = 1
//    const val TBL_NOTES = "tbl_note"
//    const val ID = "id"
//    const val JUDUL = "judul"
//    const val ISI = "isi"
//    const val URLLINK = "urllink"
//    const val GAMBAR = "gambar"
//    const val TANGGAL = "tanggal"
//
//    const val CREATE_TABLE = ("CREATE TABLE "+ TBL_NOTES + " ("
//            + ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + JUDUL +" TEXT, "
//            + ISI +" TEXT, "
//            + URLLINK + " TEXT, "
//            + GAMBAR + " TEXT, "
//            + TANGGAL + " TEXT,); ")\
    companion object{
        const val TYPE_READ=0
        const val TYPE_CREATE=1
        const val TYPE_UPDATE=2
    }
}