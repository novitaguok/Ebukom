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

        mList.add(ChooseClassDao("Kelas 1", "Aurora", "Ratna Hendrawati", 0))
        mList.add(ChooseClassDao("Kelas 2", "Spectra", "Eni Trikuswanti", 1))
        mList.add(ChooseClassDao("Kelas 1", "Aurora", "Ratna Hendrawati", 0))
        mList.add(ChooseClassDao("Kelas 2", "Spectra", "Eni Trikuswanti", 1))

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

    fun addClass() {}
}
