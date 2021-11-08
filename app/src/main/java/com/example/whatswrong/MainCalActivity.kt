package com.example.whatswrong

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop

class MainCalActivity : AppCompatActivity() {
    val days = arrayOf("", "Mon", "Tue", "Wed", "Thu", "Fri")
    val times = Array(11, { i -> ((i+9).toString()) })
    var calendarData = mutableMapOf(
        0 to SchdulerData(0, "응용통계학", "김도엽", "미래관 2층 32호실"),
        15 to SchdulerData(15, "인공지능", "김도3", "미래관 6층 23호실")
    )
    var cells = mutableMapOf<Int, View>()
    lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_cal)

        val grid: GridLayout = findViewById(R.id.recyclerGrid)
        grid.columnCount = 6
        grid.rowCount = 12
        createCell(100, 45, 0, 0, grid)

        for (i: Int in 1 until grid.columnCount) {
            val layout = createCell(170, 60, i, 0, grid)
            val text = TextView(this)
            val cell: View = layoutInflater.inflate(R.layout.scheduler_item, layout)
            text.gravity= Gravity.CENTER
            val data = days[i]
            cell?.findViewById<TextView>(R.id.subject)?.text = data
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
                val layout = createCell(170, 85, j, i, grid)
                val cell: View = layoutInflater.inflate(R.layout.scheduler_item, layout)
                val idx = ((i - 1) * (grid.columnCount - 1)) + (j - 1)
                cells[idx] = cell

            }
        }
        val grid1: GridLayout = findViewById(R.id.gridSubject)
        grid1.rowCount=3
        grid1.columnCount=2
        for (i: Int in 0 until grid1.rowCount) {
            for (j: Int in 0 until grid1.columnCount) {
                val layout = createCell(600, 85, j, i, grid1)
                val cell: View = layoutInflater.inflate(R.layout.community_by_class, layout)
                val idx = ((i) * (grid.columnCount - 1)) + (j)
                cells[idx] = cell

            }
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
}