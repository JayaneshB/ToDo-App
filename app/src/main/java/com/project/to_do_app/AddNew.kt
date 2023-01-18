package com.project.to_do_app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.project.to_do_app.database.Data
import com.project.to_do_app.database.MyDatabase
import com.project.to_do_app.databinding.ActivityAddNewBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AddNew : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddNewBinding

    private lateinit var calendar: Calendar

    private lateinit var formatter: SimpleDateFormat

    private lateinit var time_formatter: SimpleDateFormat

    private lateinit var title: String

    private lateinit var desc: String

    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendar = Calendar.getInstance()

        /**
         *  Added a calendar image button
         *  to select the date for the application
         */

        formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        time_formatter = SimpleDateFormat("hh:mm:ss a", Locale.UK)


        binding.btnCalendar.setOnClickListener {

            DatePickerDialog(
                this@AddNew,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.btnTime.setOnClickListener {

            TimePickerDialog(
                this@AddNew,
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        /**
         *  Initialising the array for the dropDown list menu
         */

        val items = listOf("All Lists","Personal","Work")
        val adapter = ArrayAdapter(this@AddNew,R.layout.list_item,items)
        binding.activityDropdownList.setAdapter(adapter)


        binding.btnSave.setOnClickListener {

            if (validate()) {
                if (intent.hasExtra(NOTE_ID)) {

                    updateNote()

                } else {

                    saveNote()

                }
            }
        }


    }
    private fun updateNote() {

        val id = intent.getIntExtra(NOTE_ID, -1)


        val database = MyDatabase.getInstance(this)
        val noteDao = database?.dataDao()

        CoroutineScope(Dispatchers.IO).launch {

            val note = Data(
                id,
                binding.titleEdit.text.toString(),
                binding.descEdit.text.toString(),
                binding.dateEdit.text.toString(),
                binding.timeEdit.text.toString()
            )
            noteDao?.update(note)
            withContext(Dispatchers.Main) {
                val intent = Intent(this@AddNew, MainActivity::class.java)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun saveNote() {

        val database = MyDatabase.getInstance(this)
        val noteDao = database?.dataDao()
        val note = Data(
            0,
            binding.titleEdit.text.toString(),
            binding.descEdit.text.toString(),
            binding.dateEdit.text.toString(),
            binding.timeEdit.text.toString()
        )
        GlobalScope.launch {
            noteDao?.insert(note)
            withContext(Dispatchers.Main) {
                val intent = Intent(this@AddNew, MainActivity::class.java)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun validate(): Boolean {

        if (binding.titleEdit.text.toString().isEmpty() || binding.descEdit.text.toString()
                .isEmpty() || binding.dateEdit.text.toString().isEmpty() || binding.timeEdit.text.toString().isEmpty()
        ) {

            Toast.makeText(this@AddNew, "Please fill the requirements", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }



    override fun onDateSet(view: DatePicker?, year: Int, month: Int, date: Int) {

        calendar.set(year, month, date)
        displayFormattedDate(calendar.timeInMillis)

    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {

        calendar.apply {

            set(Calendar.HOUR_OF_DAY,hour)
            set(Calendar.MINUTE,minute)
        }

        displayFormatTime(calendar.timeInMillis)
    }

    private fun displayFormatTime(timeInMillis: Long) {

        binding.timeEdit.setText((time_formatter.format(timeInMillis)))

    }

    private fun displayFormattedDate(timeInMillis: Long) {

        binding.dateEdit.setText(formatter.format(timeInMillis))

    }

}