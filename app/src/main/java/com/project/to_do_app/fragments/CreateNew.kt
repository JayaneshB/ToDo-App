package com.project.to_do_app.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import com.project.to_do_app.R
import com.project.to_do_app.databinding.FragmentCreateNewBinding
import java.text.SimpleDateFormat
import java.util.*


class CreateNew : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    /**
     *  Using view Binding in fragment
     *  Using the Calendar class for accessing the date
     *  Using the SimpleDateFormat class for displaying the data in the correct format
     *  Using the SimpleTimeFormat class for displaying the time in the correct format
     */

    private lateinit var binding : FragmentCreateNewBinding

    private lateinit var calendar: Calendar

    private lateinit var formatter: SimpleDateFormat

    private lateinit var time_formatter : SimpleDateFormat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCreateNewBinding.inflate(inflater, container, false)

        /**
         * Usuage the above class inherited class
         */

        calendar = Calendar.getInstance()
        formatter = SimpleDateFormat("dd/mm/yyyy", Locale.UK)
        time_formatter = SimpleDateFormat("hh:mm:ss a", Locale.UK)

        /**
         *  Onclick event on calendar
         *  for selecting the day/month/year
         */

        binding.btnCalendar.setOnClickListener {

            DatePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        /**
         *  Onclick event on time
         *  for selecting the time
         */

        binding.btnTime.setOnClickListener {

            TimePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        /**
         *  Initialising the array for the dropDown list menu
         */

        val items = listOf("No repeat","Once a Day","Once a Day (Mon-Fri)","Once a Week","Once a Month","Once a Year")
        val adapter = ArrayAdapter(requireContext(),R.layout.list_item,items)
        binding.fragmentDropdownList.setAdapter(adapter)


        return binding.root
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

    private fun displayFormattedDate(data: Long) {

        binding.dueDateBox.setText(formatter.format(data))

    }

    private fun displayFormatTime(data: Long) {

        binding.timeBoxData.setText((time_formatter.format(data)))

    }


}