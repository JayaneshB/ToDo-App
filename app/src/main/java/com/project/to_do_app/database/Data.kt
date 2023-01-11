package com.project.to_do_app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task_toDo")
class Data(
    @ColumnInfo(name = "Id")
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "Title")
    var title: String,
    @ColumnInfo(name = "Description")
    var desc: String,
    @ColumnInfo(name = "Date")
    var date: String,
    @ColumnInfo(name="Time")
    var time: String
) {

}