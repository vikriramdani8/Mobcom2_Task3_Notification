package com.example.mobcom2_task3_notification.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mobcom2_task3_notification.model.Model_Reminder

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object{
        private val DATABASE_VER = 1;
        private val DATABASE_NAME = "Reminder.db"

        private val TABLE_REMINDER = "reminder"
        private val COL_ID_REMINDER = "id_reminder"
        private val COL_NAME = "name_reminder"
        private val COL_DATE = "date_reminder"
        private val COL_TIME = "time_reminder"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY1 = ("CREATE TABLE $TABLE_REMINDER($COL_ID_REMINDER INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, $COL_NAME TEXT, $COL_DATE TEXT, $COL_TIME TEXT)")
        db!!.execSQL(CREATE_TABLE_QUERY1)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_REMINDER")
        onCreate(db!!)
    }

    val getAllReminder:List<Model_Reminder> get(){
        val listReminder = ArrayList<Model_Reminder>()
        val selectQuery = "SELECT * FROM $TABLE_REMINDER"
        val db = this.writableDatabase
        val crusor = db.rawQuery(selectQuery, null)
        if (crusor.moveToFirst()){
            do {
                val reminder = Model_Reminder()
                reminder.id_reminder = crusor.getInt(crusor.getColumnIndex(COL_ID_REMINDER))
                reminder.name_reminder = crusor.getString(crusor.getColumnIndex(COL_NAME))
                reminder.date_reminder = crusor.getString(crusor.getColumnIndex(COL_DATE))
                reminder.time_reminder = crusor.getString(crusor.getColumnIndex(COL_TIME))

                listReminder.add(reminder)
            } while (crusor.moveToNext())
        }

        return listReminder
    }

    fun getReminder(id_reminder: Int):List<Model_Reminder> {
        val listReminder = ArrayList<Model_Reminder>()
        val selectQuery = "SELECT * FROM $TABLE_REMINDER WHCOL_$COL_ID_REMINDER"+id_reminder
        val db = this.writableDatabase
        val crusor = db.rawQuery(selectQuery, null)
        if (crusor.moveToFirst()){
            do {
                val reminder = Model_Reminder()
                reminder.id_reminder = crusor.getInt(crusor.getColumnIndex(COL_ID_REMINDER))
                reminder.name_reminder = crusor.getString(crusor.getColumnIndex(COL_NAME))
                reminder.date_reminder = crusor.getString(crusor.getColumnIndex(COL_DATE))
                reminder.time_reminder = crusor.getString(crusor.getColumnIndex(COL_TIME))

                listReminder.add(reminder)
            } while (crusor.moveToNext())
        }

        return listReminder
    }

    fun addReminder(reminder: Model_Reminder){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, reminder.name_reminder)
        values.put(COL_DATE, reminder.date_reminder)
        values.put(COL_TIME, reminder.time_reminder)
        db.insert(TABLE_REMINDER,null, values)
        db.close()
    }

    fun deleteReminder(category: Model_Reminder){
        val db = this.writableDatabase
        db.delete(TABLE_REMINDER,"$COL_ID_REMINDER=?", arrayOf(category.id_reminder.toString()))
        db.close()
    }
}