package com.example.mobcom2_task3_notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mobcom2_task3_notification.Adapter.ListReminderAdapter
import com.example.mobcom2_task3_notification.Helper.DBHelper
import com.example.mobcom2_task3_notification.Helper.PreferencesHelper
import com.example.mobcom2_task3_notification.model.Model_Reminder
import com.example.mobcom2_task3_notification.receiver.MyAlarmReceiver
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "CHANNEL_ID_01"
    internal lateinit var db: DBHelper
    internal var lstReminder:List<Model_Reminder> = ArrayList<Model_Reminder>()
    internal lateinit var sharedpref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedpref = PreferencesHelper(this)
        db = DBHelper(this)

        reminder_list.setDivider(null)

        db = DBHelper(this)
        loadData();

        createNoticficationChannel()
        edit_date.setFocusable(false)
        edit_date.setClickable(true)
        edit_time.setFocusable(false)
        edit_time.setClickable(true)

        val c = Calendar.getInstance()
        val dy = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        var mL=  arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        var mS = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec")
        edit_date.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                edit_date.setText(""+ dy[c.get(Calendar.DAY_OF_WEEK)-1] +", " + mL[month] + " " + dayOfMonth + ", " + year)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))

            dpd.show()
        }

        edit_time.setOnClickListener {
            val tpd = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                c.set(Calendar.MINUTE, minute)
                c.set(Calendar.SECOND, 0)
                edit_time.setText(SimpleDateFormat("HH:mm").format(c.time))
            }
            TimePickerDialog(this, tpd, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        btn_save.setOnClickListener {
            if (edit_name.text.toString() != null && edit_name.text.toString() != "" &&
                edit_date.text.toString() != null && edit_date.text.toString() != "" &&
                edit_time.text.toString() != null && edit_time.text.toString() != "") {

                val reminder = Model_Reminder(
                        0,
                        edit_name.text.toString(),
                        edit_date.text.toString(),
                        edit_time.text.toString()
                )

                db.addReminder(reminder)
                loadData()
                setAlarm(c, "${edit_date.text} - ${edit_time.text} Waktunya ${edit_name.text} ")
                clear()
            } else {
                Toast.makeText( this, "Field tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clear(){
        edit_name.text = null
        edit_date.text = null
        edit_time.text = null
    }

    fun loadData(){
        lstReminder = db.getAllReminder
        val adapter = ListReminderAdapter(this@MainActivity, lstReminder, this)
        reminder_list.adapter = adapter
    }

    private fun setAlarm(now: Calendar, message: String){
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")

        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendarList = ArrayList<Calendar>()

        calendarList.add(now)
        val text_timer = StringBuilder()
        for (calendar in calendarList){
            calendar.add(Calendar.SECOND, 0)
            val requestCode = Random().nextInt()
            val intent = Intent(this, MyAlarmReceiver::class.java)
            intent.putExtra("message", message)

            val pi = PendingIntent.getBroadcast(this, requestCode, intent, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            else
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)

            text_timer.append(simpleDateFormat.format(calendar.timeInMillis)).append("\n")
        }

        Toast.makeText(this, "Alarm has been set", Toast.LENGTH_SHORT).show()
    }

    private fun createNoticficationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notif Title"
            val descriptionText = "Notif Desc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}