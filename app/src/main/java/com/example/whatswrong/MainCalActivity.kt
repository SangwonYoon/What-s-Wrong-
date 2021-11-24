package com.example.whatswrong

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.GridLayoutManager
import java.util.concurrent.ScheduledExecutorService

//import kotlinx.android.synthetic.main.activity_main_cal.*

class MainCalActivity : AppCompatActivity() {
    val days = arrayOf("", "Mon", "Tue", "Wed", "Thu", "Fri")
    val times = Array(11) { i -> ((i + 9).toString()) }
    var calendarData = mutableMapOf(
        0 to SchdulerData(0, "응용통계학","김준호"),
        15 to SchdulerData(15, "인공지능","이재구"),
        20 to SchdulerData(20, "컴퓨터구조","임은진"),
        21 to SchdulerData(21, "이산수학","우종우"),
        26 to SchdulerData(26, "객지프","김영만"),
        4 to SchdulerData(4, "응용통계학","김준호"),
        8 to SchdulerData(8, "객지프","김영만"),
        33 to SchdulerData(33, "모바일프로그래밍","이창우"),
        40 to SchdulerData(40, "인공지능","이재구"),
    )
    var cells = mutableMapOf<Int, View>()
    lateinit var spinner: Spinner
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cal)

        var userSchedulerData = mutableListOf<SchdulerData>()
        for(i:Int in 0 until calendarData.size){
             userSchedulerData[calendarData[i]?.idx!!]= calendarData[i]!!
        }

        val grid: GridLayout = findViewById(R.id.recyclerGrid)
        grid.columnCount = 6
        grid.rowCount = 12
        createCell(100, 45, 0, 0, grid)

        for (i: Int in 1 until grid.columnCount) {
            val layout = createCell(175, 70, i, 0, grid)
            val cell: View = layoutInflater.inflate(R.layout.scheduler_item, layout)
            val data = days[i]
            cell?.findViewById<TextView>(R.id.scheduler_item_subject)?.text = data
            cells[i] = cell
        }
        for (i: Int in 1 until grid.rowCount) {
            val layout = createCell(100, 50, 0, i, grid)
            val text = TextView(this)
            text.text = times[(i - 1)].toString()
            text.textSize = 12f
            text.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.colortime))
            text.gravity = Gravity.BOTTOM or Gravity.RIGHT
            text.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            layout.addView(text)
        }
        val BackgroundColors = arrayOf(Color.rgb(223, 250, 180),
            Color.rgb(234, 249, 209),
            Color.rgb(213, 255, 146),
            Color.rgb(207, 225, 177))
        for (i: Int in 1 until grid.rowCount) {
            for (j: Int in 1 until grid.columnCount) {
                val layout = createCell(175, 85, j, i, grid)
                val cell: View = layoutInflater.inflate(R.layout.scheduler_item, layout)
                val idx = ((i - 1) * (grid.columnCount - 1)) + (j - 1)
                cells[idx] = cell
                if (calendarData.containsKey(idx)){
                    val data = calendarData[idx]
                    cell.setBackgroundColor(BackgroundColors[i%4])
                    cell.findViewById<TextView>(R.id.scheduler_item_subject).text= data?.subject
                    cell.findViewById<TextView>(R.id.scheduler_item_professor).text=data?.professor

                    cell.setOnClickListener{
                        val view = layoutInflater.inflate(R.layout.dialog_scheculer,null)
                        view.findViewById<EditText>(R.id.dialog_scheduler_subject)
                            .setText(calendarData[idx]?.subject)
                        view.findViewById<EditText>(R.id.dialog_scheduler_professor)
                            .setText(calendarData[idx]?.professor)
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainCalActivity)
                        builder
                            .setView(view)
                            .setPositiveButton(
                                "수정",
                                DialogInterface.OnClickListener { dialog, index ->
                                    calendarData[idx]?.subject =
                                        view.findViewById<EditText>(R.id.dialog_scheduler_subject).text.toString()
                                    calendarData[idx]?.professor =
                                        view.findViewById<EditText>(R.id.dialog_scheduler_professor).text.toString()
                                    refreshCell(calendarData)
                                })
                            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, _ ->
                                dialog.cancel()
                            })
                            .create().show()
                    }
                    cell.setOnLongClickListener {
                        val builder = AlertDialog.Builder(this@MainCalActivity)
                        builder.setMessage("삭제하시겠습니까?")
                            .setPositiveButton("삭제",
                                DialogInterface.OnClickListener { dialog, id ->
                                    calendarData.remove(idx)
                                    cell.setBackgroundColor(Color.WHITE)
                                    refreshCell(calendarData)
                                })
                            .setNegativeButton("취소",
                                DialogInterface.OnClickListener { dialog, id ->
                                    dialog.cancel()
                                })
                            .create().show()
                        return@setOnLongClickListener true
                    }
                }
                else{
                    layout.setOnClickListener {
                        val view = layoutInflater.inflate(R.layout.dialog_scheculer,null)
                        val builder : AlertDialog.Builder = AlertDialog.Builder(this@MainCalActivity)
                        builder
                            .setView(view)
                            .setPositiveButton(
                                "등록",
                                DialogInterface.OnClickListener { dialog, index ->
                                    calendarData[idx] = SchdulerData(
                                        idx,
                                        view.findViewById<EditText>(R.id.dialog_scheduler_subject).text.toString(),
                                        view.findViewById<EditText>(R.id.dialog_scheduler_professor).text.toString(),
                                    )
                                    refreshCell(calendarData)
                                })
                            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, _ ->
                                dialog.cancel()
                            })
                            .create().show()
                    }
                }
            }
        }
        val grid1: GridLayout = findViewById(R.id.gridSubject)
        grid1.rowCount=3
        grid1.columnCount=2
        for (i: Int in 0 until grid1.rowCount) {
            for (j: Int in 0 until grid1.columnCount) {
                val layout = createCell(550, 85, j, i, grid1)
                val cell: View = layoutInflater.inflate(R.layout.community_by_class, layout)
                val idx = ((i) * (grid.columnCount - 1)) + (j)
                cells[idx] = cell

            }
        }
        val btHor1 :Button = findViewById(R.id.btSchedulerHor1)
        val btHor2 :Button = findViewById(R.id.btSchedulerHor2)
        val btHor3 :Button = findViewById(R.id.btSchedulerHor3)

    }

    private fun createCell(w: Int, h: Int, c: Int, r: Int, grid: GridLayout): ConstraintLayout {
        val layout = ConstraintLayout(this)
        val param: GridLayout.LayoutParams = GridLayout.LayoutParams()
        param.setGravity(Gravity.CENTER)
        param.columnSpec = GridLayout.spec(c)
        param.rowSpec = GridLayout.spec(r)
        param.width = w
        param.height = h
        layout.layoutParams = param
        grid.addView(layout)
        return layout
    }
    private fun refreshCell(datas: MutableMap<Int, SchdulerData>) {
        val grid: GridLayout = findViewById(R.id.recyclerGrid)

        for (i: Int in 1 until grid.rowCount) {
            for (j: Int in 1 until grid.columnCount) {
                val idx = ((i - 1) * (grid.columnCount - 1)) + (j - 1)
                val cell: View? = cells[idx]
                if (datas.containsKey(idx)) {
                    val data = datas[idx]
                    cell?.findViewById<TextView>(R.id.scheduler_item_subject)?.text = data?.subject
                    cell?.findViewById<TextView>(R.id.scheduler_item_professor)?.text = data?.professor
                } else {
                    cell?.findViewById<TextView>(R.id.scheduler_item_subject)?.text = ""
                    cell?.findViewById<TextView>(R.id.scheduler_item_professor)?.text = ""
                }
            }
        }
    }
}