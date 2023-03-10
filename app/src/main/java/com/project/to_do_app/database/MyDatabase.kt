package com.project.to_do_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Data::class], version = 1, exportSchema = false)
abstract class MyDatabase() : RoomDatabase() {


    companion object {

        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase? {

            if (INSTANCE == null) {
                synchronized(MyDatabase::class.java) {

                    INSTANCE = Room.databaseBuilder(
                        context,
                        MyDatabase::class.java,
                        "MyDatabase"
                    ).build()

                    return INSTANCE
                }
            }
            return INSTANCE
        }
    }

    abstract fun dataDao(): DataDao
}