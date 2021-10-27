package org.d3if2122.mobpro2.noorinotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var sqLiteHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = activityMainBinding.root
        setContentView(rootView)

        sqLiteHelper = SQLiteHelper(this)
    }
}