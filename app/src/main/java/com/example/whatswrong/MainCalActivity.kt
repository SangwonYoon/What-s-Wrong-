package com.example.whatswrong

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cal_plus_dialog.view.*
import kotlinx.android.synthetic.main.time_picker.view.*
import java.util.*
import kotlin.collections.set


class MainCalActivity : AppCompatActivity() {


    val days = arrayOf("", "Mon", "Tue", "Wed", "Thu", "Fri")
    val times = Array(11) { i -> ((i + 9).toString()) }
//    var calendarData = mutableMapOf(
//        0 to SchdulerData(0, "응용통계학"),
//        15 to SchdulerData(15, "인공지능"),
//        20 to SchdulerData(20, "컴퓨터구조"),
//        21 to SchdulerData(21, "이산수학"),
//        26 to SchdulerData(26, "객지프"),
//        4 to SchdulerData(4, "응용통계학"),
//        8 to SchdulerData(8, "객체지향프로그래밍"),
//        33 to SchdulerData(33, "모바일프로그래밍"),
//        40 to SchdulerData(40, "인공지능"),
//    )
    var subjectData = mutableListOf<SubjectData>(
        SubjectData("컴구",1234),
        SubjectData("이산수학",1234),
        SubjectData("응통",1234),
        SubjectData("생활속스포츠",1234),
        SubjectData("모바일프로그래밍",1234),
        SubjectData("모바일프로그래밍",1234),
        SubjectData("컴구",1234),
        SubjectData("이산수학",1234),
        SubjectData("응통",1234),
        SubjectData("생활속스포츠",1234),
        SubjectData("모바일프로그래밍",1234),
        SubjectData("모바일프로그래밍",1234),
    )
    var cells = mutableMapOf<Int, View>()
    //FireBase & RealTimeBase connect
    private lateinit var mFirebaseAuth : FirebaseAuth // 파이어베이스 인증처리
    private lateinit var mDatabaseRef : DatabaseReference
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val gridLayoutManager:GridLayoutManager
//
//        gridLayoutManager.spanSizeLookup= object : GridLayoutManager.SpanSizeLookup {
//                                    override fun getSpanSize(position: Int): Int {
//                            if (idx>3) return 2
//                            else return 1
//                        }
//                    }
        setContentView(R.layout.activity_main_cal)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Whatswrong")
        val test1 = SubjectData("컴구",1)
        val testIndex = mDatabaseRef.child("UserAccount").child("3SPXSEcQB3e6bD0X8bKM4LbDktF3").child("index").get()
        var strIndex : String = ""
        var strSubject : String =""
        var arrIndex = listOf<String>(" ")
        var arrSubject = listOf<String>(" ")
        testIndex.addOnSuccessListener {
            strIndex=it.value.toString()
            arrIndex= strIndex.split(",")
            Log.i("firebase", "Got value ${arrIndex}")

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        val testSubject = mDatabaseRef.child("UserAccount").child("3SPXSEcQB3e6bD0X8bKM4LbDktF3").child("subject").get()


        var calendarData = mutableMapOf(
            0 to SchdulerData(null, ""),
        )


        testSubject.addOnSuccessListener {
            strSubject=it.value.toString()
            arrSubject=strSubject.split(",")
            Log.i("firebase", "Got value ${arrSubject}")
            for(i in 1..arrIndex.size-1){
                calendarData[arrIndex[i].toInt()] = SchdulerData(arrIndex[i].toInt(),arrSubject[i])
            }

            refreshCell(calendarData)
            var setSubject = arrSubject.distinct()
            Log.i("firebase", "Got value $setSubject}")
            val grid1: GridLayout = findViewById(R.id.gridSubject)
            grid1.rowCount=setSubject.size/2
            grid1.columnCount=2

            var idx = 0
            for (i: Int in 0 until grid1.rowCount) {
                for (j: Int in 0 until grid1.columnCount) {
                    if (idx<setSubject.size-1){
                        val layout = createCell(550, 85, j, i, grid1)
                        val cell1: View = layoutInflater.inflate(R.layout.community_by_class, layout)

                        idx=(i*2) + (j)+1
                        var data1 = setSubject[idx]
                        cell1.findViewById<Button>(R.id.btSubjectCode).text="${data1}"
                        cell1.findViewById<Button>(R.id.btSubjectCode).textSize=10f

                    }

                }
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
        val gridLayoutManager:GridLayoutManager

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
                if (calendarData.containsKey(idx)) {
                    val data = calendarData[idx]
                    cell.setBackgroundColor(BackgroundColors[i % 4])
                    cell.findViewById<TextView>(R.id.scheduler_item_subject).text = data?.subject

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
            }
        }


        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
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
        val gridLayoutManager:GridLayoutManager

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
//                else{
//                    layout.setOnClickListener {
//                        val view = layoutInflater.inflate(R.layout.dialog_scheculer,null)
//                        val builder : AlertDialog.Builder = AlertDialog.Builder(this@MainCalActivity)
//                        builder
//                            .setView(view)
//                            .setPositiveButton(
//                                "등록",
//                                DialogInterface.OnClickListener { dialog, index ->
//                                    calendarData[idx] = SchdulerData(
//                                        idx,
//                                        view.findViewById<EditText>(R.id.dialog_scheduler_subject).text.toString(),
//                                    )
//                                    layout.setBackgroundColor(BackgroundColors[i%4])
//                                    refreshCell(calendarData)
//                                })
//                            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, _ ->
//                                dialog.cancel()
//                            })
//                            .create().show()
//                    }
//                }
                val btCalPlus:ImageButton=findViewById(R.id.btCalPlus)
                val popup = PopupWindow(this)
                btCalPlus.setOnClickListener {
                    var stHour:Int = 0
                    var stMinute:Int = 0
                    var endHour:Int = 0
                    var endMinute:Int = 0
                    var index :Int = 0
                    val view = layoutInflater.inflate(R.layout.activity_cal_plus_dialog,null)
                    popup.contentView=view

                    popup.showAtLocation(view,Gravity.CENTER,0,0)
                    val cancel = view.bt_dialog_cancel.setOnClickListener{
                        popup.dismiss()
                    }
                    val add = view.findViewById<Button>(R.id.bt_dialog_add).setOnClickListener{
                        var textSubject :String = view.findViewById<Spinner>(R.id.spinner_subjects).selectedItem.toString()
                        var textDays : String = ""
                        stHour=view.findViewById<Spinner>(R.id.spinner_start_hour).selectedItem.toString().toInt()
                        endHour=view.findViewById<Spinner>(R.id.spinner_end_hour).selectedItem.toString().toInt()
                        textDays=view.findViewById<Spinner>(R.id.spinner_days).selectedItem.toString()
                        when(textDays){
                            "Mon"->{
                                if ((endHour.toInt()-stHour.toInt())==1){
                                    when(stHour.toInt()){
                                        9 -> index=0
                                        10 -> index=5
                                        11 -> index=10
                                        12 -> index=15
                                        13 -> index=20
                                        14 -> index=25
                                        15 -> index=30
                                        16 -> index=35
                                        17 -> index=40
                                        18 -> index=45
                                        19 -> index=50
                                    }
                                }
                                if ((endHour.toInt()-stHour.toInt())==2){

                                    when(stHour.toInt()){
                                        9 -> index=0
                                        10 -> index=5
                                        11 -> index=10
                                        12 -> index=15
                                        13 -> index=20
                                        14 -> index=25
                                        15 -> index=30
                                        16 -> index=35
                                        17 -> index=40
                                        18 -> index=45
                                        19 -> index=50
                                    }
                                }
                            }
                            "Tue"->{
                                if ((endHour.toInt()-stHour.toInt())==1){
                                    when(stHour.toInt()){
                                        9 -> index=1
                                        10 -> index=6
                                        11 -> index=11
                                        12 -> index=16
                                        13 -> index=21
                                        14 -> index=26
                                        15 -> index=31
                                        16 -> index=36
                                        17 -> index=41
                                        18 -> index=46
                                        19 -> index=51
                                    }
                                }
                            }
                            "Wed"->{
                                if ((endHour.toInt()-stHour.toInt())==1){
                                    when(stHour.toInt()){
                                        9 -> index=2
                                        10 -> index=7
                                        11 -> index=12
                                        12 -> index=17
                                        13 -> index=22
                                        14 -> index=27
                                        15 -> index=32
                                        16 -> index=37
                                        17 -> index=42
                                        18 -> index=47
                                        19 -> index=52
                                    }
                                }
                            }
                            "Thu"->{
                                if ((endHour.toInt()-stHour.toInt())==1){
                                    when(stHour.toInt()){
                                        9 -> index=3
                                        10 -> index=8
                                        11 -> index=13
                                        12 -> index=18
                                        13 -> index=23
                                        14 -> index=28
                                        15 -> index=33
                                        16 -> index=38
                                        17 -> index=43
                                        18 -> index=48
                                        19 -> index=53
                                    }
                                }
                            }
                            "Fri"->{
                                if ((endHour.toInt()-stHour.toInt())==1){
                                    when(stHour.toInt()){
                                        9 -> index=4
                                        10 -> index=9
                                        11 -> index=14
                                        12 -> index=19
                                        13 -> index=24
                                        14 -> index=29
                                        15 -> index=34
                                        16 -> index=39
                                        17 -> index=44
                                        18 -> index=49
                                        19 -> index=54
                                    }
                                }
                            }
                        }
                        calendarData[index] = SchdulerData(
                            index,
                            textSubject
                        )
                        var tmpIndex : String=""
                        var tmpSubject : String=""
                        for (i :Int in 0..54) if(calendarData[i]?.index!=null){tmpIndex ="${tmpIndex},${calendarData[i]?.index}"}
                        mDatabaseRef.child("UserAccount").child("3SPXSEcQB3e6bD0X8bKM4LbDktF3").child("index").setValue(tmpIndex)
                        for (i :Int in 0..54) if(calendarData[i]?.subject!=null){tmpSubject ="${tmpSubject},${calendarData[i]?.subject}"}
                        mDatabaseRef.child("UserAccount").child("3SPXSEcQB3e6bD0X8bKM4LbDktF3").child("subject").setValue(tmpSubject)
                        refreshCell(calendarData)
                        popup.dismiss()
                    }
                    popup.showAsDropDown(btCalPlus)
                }
            }
        }
//        val grid1: GridLayout = findViewById(R.id.gridSubject)
//        grid1.rowCount=3
//        grid1.columnCount=2
//        for (i: Int in 0 until grid1.rowCount) {
//            for (j: Int in 0 until grid1.columnCount) {
//                val layout = createCell(550, 85, j, i, grid1)
//                val cell1: View = layoutInflater.inflate(R.layout.community_by_class, layout)
//                val idx = ((i) * (grid1.columnCount - 1)) + (j)
//
//                var data1 = calendarData[j]?.subject
//                cell1.findViewById<Button>(R.id.btSubjectCode).text="${data1}"
//                cell1.findViewById<Button>(R.id.btSubjectCode).textSize=10f
//
//            }
//        }



        val btHor1 :Button = findViewById(R.id.btSchedulerHor1)
        btHor1.setOnClickListener {
        }
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
        val BackgroundColors = arrayOf(Color.rgb(223, 250, 180),
            Color.rgb(234, 249, 209),
            Color.rgb(213, 255, 146),
            Color.rgb(207, 225, 177))

        for (i: Int in 1 until grid.rowCount) {
            for (j: Int in 1 until grid.columnCount) {
                val idx = ((i - 1) * (grid.columnCount - 1)) + (j - 1)
                val cell: View? = cells[idx]
                if (datas.containsKey(idx)) {
                    val data = datas[idx]
                    cell?.findViewById<TextView>(R.id.scheduler_item_subject)?.text = data?.subject
                    cell?.setBackgroundColor(BackgroundColors[(i*j*i+i+j)%4])
                } else {
                    cell?.findViewById<TextView>(R.id.scheduler_item_subject)?.text = ""

                }
            }

        }
    }
}