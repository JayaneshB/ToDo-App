package com.project.to_do_app.database

import androidx.room.*

@Dao
interface DataDao {

    @Insert
    fun insert(data: Data)

    @Update
    fun update(data: Data)

    @Delete
    fun delete(data: Data)

    @Query("SELECT * FROM task_toDo")
    fun getAllNotes(): List<Data>

    @Query("DELETE FROM task_toDo")
    fun deleteAllNotes()

}