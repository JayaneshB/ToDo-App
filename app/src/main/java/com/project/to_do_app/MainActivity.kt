package com.project.to_do_app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.to_do_app.database.DataObject
import com.project.to_do_app.databinding.ActivityMainBinding
import com.project.to_do_app.fragments.CreateNew


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var recycler_view : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_toolbar_lefticon)

        /**
         *  Setting the list values to the dropDown data
         */

        val items = listOf("Default","Personal","Shopping","wishlist","work","Finished")
        val adapter = ArrayAdapter(this,R.layout.list_item,items)
        binding.dropdownList.setAdapter(adapter)


        /**
         *  Add button action
         */

        binding.btnAdd.setOnClickListener {

            replaceFragment(CreateNew())

        }

        recycler_view = binding.recyclerView

        recycler_view.adapter = com.project.to_do_app.adapter.Adapter(DataObject.getData())
        recycler_view.layoutManager = LinearLayoutManager(this)





    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options,menu)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_Container,fragment)
        fragmentTransaction.commit()
    }




}