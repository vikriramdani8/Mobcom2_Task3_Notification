package com.example.mobcom2_task3_notification.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobcom2_task3_notification.Helper.DBHelper
import com.example.mobcom2_task3_notification.MainActivity
import com.example.mobcom2_task3_notification.R
import com.example.mobcom2_task3_notification.model.Model_Reminder
import kotlinx.android.synthetic.main.list_reminder.view.*

class ListReminderAdapter(
        internal var activity: MainActivity,
        internal var lstReminder: List<Model_Reminder>,
        val context: Context
):BaseAdapter(){

    internal lateinit var db: DBHelper
    internal lateinit var tempReminder: List<Model_Reminder>
    internal var inflanter:LayoutInflater

    init {
        inflanter = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as
                LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView:View
        db = DBHelper(context)
        rowView = inflanter.inflate(R.layout.list_reminder, null)
        rowView.txt_title.text = lstReminder[position].name_reminder.toString()
        rowView.txt_dateandtime.text = lstReminder[position].date_reminder + " " + lstReminder[position].time_reminder

        val trash = rowView.findViewById<ImageView>(R.id.trash)
        trash.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        val category = Model_Reminder(lstReminder[position].id_reminder, lstReminder[position].name_reminder.toString())
                        db.deleteReminder(category)
                        activity.loadData()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
            val alert = builder.create()
            alert.show()
        }

        return rowView
    }

    override fun getItem(position: Int): Any {
        return lstReminder[position]
    }

    override fun getItemId(position: Int): Long {
        return lstReminder[position].id_reminder.toLong()
    }

    override fun getCount(): Int {
        return lstReminder.size
    }

}