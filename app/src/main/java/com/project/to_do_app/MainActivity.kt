package com.project.to_do_app

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var adapter: DataAdapter

    private lateinit var list: List<Data>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.recyclerView.setHasFixedSize(true)

        binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_toolbar_lefticon)

        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        setData()

        /**
         *  Setting the list values to the dropDown data
         */


        val items = listOf("All List","Personal","Shopping","wishlist","work","Finished")
        val adapter = ArrayAdapter(this,R.layout.list_item,items)
        binding.dropdownList.setAdapter(adapter)

        /**
         *  Add button action
         */

        binding.btnAdd.setOnClickListener {

            val intent = Intent(this@MainActivity,AddNew::class.java)
            startActivityForResult(intent, REQUEST_CODE)

        }

    }

    private fun setData() {

        val database = MyDatabase.getInstance(this@MainActivity)
        val dataDao = database!!.dataDao()

        CoroutineScope(Dispatchers.IO).launch {

            val list : List<Data> = dataDao.getAllNotes()
            this@MainActivity.list= list
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

    private fun deleteAllNotes() {

        val database = MyDatabase.getInstance(this@MainActivity)
        val noteDao = database?.dataDao()

        CoroutineScope(Dispatchers.IO).launch {

            noteDao?.deleteAllNotes()
            withContext(Dispatchers.Main) {

                Toast.makeText(this@MainActivity,"Deleted Successfully",Toast.LENGTH_SHORT).show()
                adapter.clearList()

            }

        }



//    private fun replaceFragment(fragment: Fragment) {
//
//        val fragmentManager : FragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.fragment_Container,fragment)
//        fragmentTransaction.commit()
//    }
//
//


}