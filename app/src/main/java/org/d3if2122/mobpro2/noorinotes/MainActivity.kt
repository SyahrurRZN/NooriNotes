package org.d3if2122.mobpro2.noorinotes

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if2122.mobpro2.noorinotes.Model.NotesAdapter
import org.d3if2122.mobpro2.noorinotes.Support.SQLiteHelper
import org.d3if2122.mobpro2.noorinotes.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var sqLiteHelper: SQLiteHelper
    private var adapter: NotesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = activityMainBinding.root
        setContentView(rootView)

        sqLiteHelper = SQLiteHelper(this)

        val addNoteBaru = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode  == Activity.RESULT_OK){
                getView()
            }
        }


        activityMainBinding.recycleview.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter()
        activityMainBinding.recycleview.adapter = adapter

        adapter?.setOnClickItem {
            val intent = Intent(this, DetailNoteActivity::class.java)
            intent.putExtra("data",it as Serializable)
            startActivity(intent)
        }

        activityMainBinding.fab.setOnClickListener{
            addNoteBaru.launch(Intent(this,AddNotesActivity::class.java))
        }
        getView()
    }

    private fun getView() {
        val noteList = sqLiteHelper.getAllNote()

        adapter?.addItems(noteList)
    }
}