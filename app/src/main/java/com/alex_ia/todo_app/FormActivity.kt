package com.alex_ia.todo_app

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.alex_ia.todo_app.MainActivity.Companion.NEW_TASK
import com.alex_ia.todo_app.MainActivity.Companion.NEW_TASK_KEY
import com.alex_ia.todo_app.MainActivity.Companion.UPDATE_TASK
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class FormActivity : AppCompatActivity() {

    private lateinit var edtTime: EditText
    private lateinit var edtTitle: EditText
    private lateinit var edtDate: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnAdd: Button

    private var isDetailTask = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        isDetailTask = intent.getBooleanExtra("isTaskDetail",false)

        initViews()
        if (isDetailTask) setTaskinfo(intent.getParcelableExtra("task")?: Task())
    }

    private fun setTaskinfo(task: Task) {

        edtTitle.setText(task.title)
        edtDescription.setText(task.description)
        edtDate.setText(task.dateTime?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        edtTime.setText(task.dateTime?.format(DateTimeFormatter.ofPattern("HH:mm")))

        btnAdd.text = "Update"

    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        edtDate = findViewById(R.id.edtDate)
        edtTime = findViewById(R.id.edtTime)
        btnAdd = findViewById(R.id.btnAdd)

        edtDate.setOnClickListener {
            val nowDate = LocalDate.now()


           val dataPicker =  DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    val month1 = month + 1
                    if(dayOfMonth < 10){
                        edtDate.setText("0$dayOfMonth/$month1/$year")
                        if(month1 < 10){
                            edtDate.setText("0$dayOfMonth/0$month1/$year")
                        }
                    }else{
                        edtDate.setText("$dayOfMonth/$month1/$year")
                        if(month < 10 ){
                            edtDate.setText("$dayOfMonth/0$month1/$year")
                        }
                    }
                },
                nowDate.year,
                nowDate.monthValue - 1,
                nowDate.dayOfMonth
            )
            dataPicker.datePicker.minDate = System.currentTimeMillis()
            dataPicker.show()

        }

        edtTime.setOnClickListener {
            val nowTime = LocalTime.now()

            TimePickerDialog(this,
                { _, hour, minute ->
                    if(hour < 10){
                        edtTime.setText("0$hour:$minute")
                        if(minute < 10){
                            edtTime.setText("0$hour:0$minute")
                        }
                    }else{
                        edtTime.setText("$hour:$minute")
                        if(minute < 10){
                            edtTime.setText("$hour:0$minute")
                        }
                    }
                },
            nowTime.hour,
            nowTime.minute,
            true
            ).show()
        }

        btnAdd.setOnClickListener {

            if (edtTitle.text.isNotEmpty() && edtDate.text.isNotEmpty() && edtDescription.text.isNotEmpty() && edtTime.text.isNotEmpty()){
                setResult(
                   if (isDetailTask) UPDATE_TASK else NEW_TASK,
                    Intent().putExtra(
                    NEW_TASK_KEY,
                    Task(
                        intent.getParcelableExtra<Task>("task")?.id ?: 0,
                        edtTitle.text.toString(),
                        edtDescription.text.toString(),
                        LocalDateTime.of(LocalDate.parse(edtDate.text, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            LocalTime.parse(edtTime.text, DateTimeFormatter.ofPattern("HH:mm")))
                    )
                ))
                finish()
            }else{
                showMessege("You have to fill in all the data")
            }
        }
    }

    fun showMessege(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}