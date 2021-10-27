package org.d3if2122.mobpro2.noorinotes.Model

import java.util.*

data class Notes(
    val id: Int =getAutoId(),
    val judul: String="",
    val isi: String="",
    val urlLink: String="",
    val gambar: String="",
    val tanggal: String=""
){
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(1000)
        }
    }
}
