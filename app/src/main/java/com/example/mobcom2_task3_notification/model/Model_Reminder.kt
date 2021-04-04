package com.example.mobcom2_task3_notification.model

class Model_Reminder {
    var id_reminder: Int = 0
    var name_reminder: String? = null
    var date_reminder:String? = null
    var time_reminder:String? = null

    constructor()

    constructor(
            id_reminder: Int,
            name_reminder: String
    ){
        this.id_reminder = id_reminder
        this.name_reminder = name_reminder
    }

    constructor(
            id_reminder: Int,
            name_reminder: String,
            date_reminder: String,
            time_reminder: String
    ) {
        this.id_reminder = id_reminder
        this.name_reminder = name_reminder
        this.date_reminder = date_reminder
        this.time_reminder = time_reminder
    }
}