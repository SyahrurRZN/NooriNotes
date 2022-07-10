package org.d3if2122.mobpro2.noorinotes

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityAlarmOnBinding

class AlarmOnActivity : AppCompatActivity() {
    private lateinit var alarmOnBinding: ActivityAlarmOnBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmOnBinding = ActivityAlarmOnBinding.inflate(layoutInflater)
        val rootview = alarmOnBinding.root
        setContentView(rootview)

        val noteJudul = intent.getStringExtra("judul").toString()
        val noteIsi = intent.getStringExtra("isi").toString()

        alarmOnBinding.njudul.text = noteJudul
        alarmOnBinding.nisi.text = noteIsi

        val mediaPlayer = MediaPlayer.create(this,R.raw.alarmtone1)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        alarmOnBinding.stopButton.setOnClickListener{
            mediaPlayer.stop()
            finish()
        }
    }
}