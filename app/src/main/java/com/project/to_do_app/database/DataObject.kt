package com.project.to_do_app.database

import com.project.to_do_app.adapter.TaskInfo

object DataObject {

    var listData = mutableListOf<TaskInfo>()

    fun set(task:String,due:String) {
        listData.add(TaskInfo(task,due))

    }

    fun getData() : List<TaskInfo> {

        return listData
    }
}