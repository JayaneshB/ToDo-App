package com.project.to_do_app

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.to_do_app.adapter.DataAdapter
import com.project.to_do_app.database.Data
import com.project.to_do_app.database.MyDatabase
import com.project.to_do_app.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val REQUEST_CODE = 1
const val EDIT_REQUEST_CODE = 2
const val NOTE_ID: String = "NOTE_ID"
const val TITLE = "TITLE"
const val DESC = "DESC"
const val DATE = "DATE"
const val TIME = "TIME"


class MainActivity : AppCompatActivity(), DataAdapter.noteClickListener {

    private lateinit var binding : ActivityMainBinding

    private lateinit var adapter: DataAdapter

    private lateinit var list: MutableList<Data>

    private var lastClickTime : Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        binding.recyclerView.setHasFixedSize(true)


        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        setData()


        /**
         *  Displaying the array list in the
         *  spinner in the toolbar
         */

        val items= listOf("All Lists","Personal","Work")
        val displayList = ArrayAdapter(this@MainActivity,R.layout.list_item,items)
        binding.DropDownMain.setAdapter(displayList)

        binding.DropDownMain.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val database = MyDatabase.getInstance(this@MainActivity)
                val dataDao = database!!.dataDao()

                CoroutineScope(Dispatchers.IO).launch {

                    list = dataDao.search()

                    withContext(Dispatchers.Main) {
                        adapter=DataAdapter(list,this@MainActivity)
                        binding.recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("Not yet implemented")
            }


        }

        /**
         *  Add button action
         */

        binding.btnAdd.setOnClickListener {

            if(SystemClock.elapsedRealtime() - lastClickTime < 1000)
            {
                return@setOnClickListener
            }

            lastClickTime = SystemClock.elapsedRealtime()

            val intent = Intent(this@MainActivity,AddNew::class.java)
            startActivityForResult(intent, REQUEST_CODE)

        }

    }



    /**
     *  Creating a new data
     *
     */

    private fun setData() {

        val database = MyDatabase.getInstance(this@MainActivity)
        val dataDao = database!!.dataDao()

        CoroutineScope(Dispatchers.IO).launch {

           list=dataDao.getAllNotes()

            withContext(Dispatchers.Main){
                adapter = DataAdapter(list,this@MainActivity)
                binding.recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onItemClick(position: Int) {

        val note: Data = list[position]
        val intent = Intent(this@MainActivity,AddNew::class.java)
        intent.putExtra(NOTE_ID, note.id)
        intent.putExtra(TITLE, note.title)
        intent.putExtra(DESC, note.desc)
        intent.putExtra(DATE, note.date)
        intent.putExtra(TIME,note.time)
        startActivityForResult(intent, EDIT_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show()
            setData()
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
            setData()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_options,menu)
        return super.onCreateOptionsMenu(menu)

    }

    /**
     *  Functions performed when an item is selected
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.deleteAll -> {

                showDialog()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    /**
     *  Displaying a alert pop box to confirm the selected details
     */


    private fun showDialog() {

        val dialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("Delete")
            .setMessage("Are you sure to delete this ?")
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    deleteAllNotes()
                }
            })
            .setNegativeButton("NO", null).show()

    }

    /**
     *  Deleting all the list in the
     *  recycler view
     */

    private fun deleteAllNotes() {

        val database = MyDatabase.getInstance(this@MainActivity)
        val noteDao = database?.dataDao()

        CoroutineScope(Dispatchers.IO).launch {

            noteDao?.deleteAllNotes()
            withContext(Dispatchers.Main) {

                Toast.makeText(this@MainActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                adapter.clearList()

            }

        }
    }

    override fun onLongClick(position: Int){

        val note: Data = list[position]
        val dialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->

                val database = MyDatabase.getInstance(this@MainActivity)
                val data = database!!.dataDao()
                CoroutineScope(Dispatchers.IO).launch {
                    data.delete(note)
                    list.removeAt(position)
                    adapter.notifyDataSetChanged()
//                adapter.notifyItemRemoved(position)
                }
            }
        dialog.setNegativeButton("No", null)
        val alert = dialog.create()
        alert.show()
    }
}