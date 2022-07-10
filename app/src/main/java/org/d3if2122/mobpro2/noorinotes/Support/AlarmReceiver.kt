package org.d3if2122.mobpro2.noorinotes.Support

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.d3if2122.mobpro2.noorinotes.AlarmOnActivity
import org.d3if2122.mobpro2.noorinotes.MainActivity
import org.d3if2122.mobpro2.noorinotes.Model.Notes
import org.d3if2122.mobpro2.noorinotes.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

//        val i = Intent(context, MainActivity::class.java)
//        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////        val noteID = intent.getIntExtra("note_id",0)
        val noteJudul = intent!!.getStringExtra("judul").toString()
        val noteIsi = intent.getStringExtra("isi").toString()
//
//        val pendingIntent = PendingIntent.getActivity(context,0,i,0)
//
//        val builder = NotificationCompat.Builder(context!!,"noorinotes")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle(noteJudul)
////            .setContentText(noteIsi)
//            .setAutoCancel(true)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//
//        val notificationManager = NotificationManagerCompat.from(context)
//        notificationManager.notify(123,builder.build())

        val i =Intent(context,AlarmOnActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.putExtra("judul",noteJudul)
        i.putExtra("isi",noteIsi)
        context?.startActivity(i)
    }
}