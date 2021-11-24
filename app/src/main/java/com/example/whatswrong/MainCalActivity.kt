package com.example.whatswrong

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class MainCalActivity : AppCompatActivity() {
    val days = arrayOf("", "Mon", "Tue", "Wed", "Thu", "Fri")
    val times = Array(11) { i -> ((i + 9).toString()) }
    var calendarData = mutableMapOf(
        0 to SchdulerData(0, "응용통계학","김준호"),
        15 to SchdulerData(15, "인공지능","이재구")
    )
    var cells = mutableMapOf<Int, View>()
    lateinit var spinner: Spinner
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cal)

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
        for (i: Int in 1 until grid.rowCount) {
            for (j: Int in 1 until grid.columnCount) {
                val layout = createCell(175, 85, j, i, grid)
                val cell: View = layoutInflater.inflate(R.layout.scheduler_item, layout)
                val idx = ((i - 1) * (grid.columnCount - 1)) + (j - 1)
                cells[idx] = cell
                if (calendarData.containsKey(idx)){
                    val data = calendarData[idx]
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

        findViewById<ImageButton>(R.id.scheduler_button).setOnClickListener {
            val intent = Intent(this, MainCalActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.community_button).setOnClickListener {
            val intent = Intent(this,MyCommunity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.user_button).setOnClickListener {
            val intent = Intent(this,MyInfoActivity::class.java)
            startActivity(intent)
        }

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