package com.ebukom.arch.ui.joinclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.chooseclass.ChooseClassAdapter
import kotlinx.android.synthetic.main.activity_join_class.*

class JoinClassActivity : AppCompatActivity() {

    private val mList: ArrayList<ChooseClassDao> = arrayListOf()
    private val mAdapter = ChooseClassAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_class)

        rvJoinClassClasses.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@JoinClassActivity)
        }

        initToolbar()

        // Get intent from Choose Class
        val level = intent.getIntExtra("Level", -1)
        when (level) {
            // Teacher
            0 -> {
                mList.add(ChooseClassDao("Kelas 1", "Aurora", "Ratna Hendrawati", 0))
                mList.add(ChooseClassDao("Kelas 2", "Spectra", "Eni Trikuswanti", 1))
                mList.add(ChooseClassDao("Kelas 1", "Aurora", "Ratna Hendrawati", 0))
                mList.add(ChooseClassDao("Kelas 2", "Spectra", "Eni Trikuswanti", 1))
            }

            // Parent
            1 -> {
                mList.add(ChooseClassDao("", "", "", 1))
                mList.add(ChooseClassDao("", "", "", 0))
                mList.add(ChooseClassDao("", "", "", 1))
            }

            // Admin
            2 -> {
                mList.add(ChooseClassDao("", "", "", 0))
                mList.add(ChooseClassDao("", "", "", 1))
                mList.add(ChooseClassDao("", "", "", 0))
                mList.add(ChooseClassDao("", "", "", 1))
                mList.add(ChooseClassDao("", "", "", 0))
                mList.add(ChooseClassDao("", "", "", 1))
            }
            else -> {

            }
        }

        mAdapter.addAll(mList)
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
