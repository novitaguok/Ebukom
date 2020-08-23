package com.ebukom.arch.ui.joinclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.chooseclass.ChooseClassAdapter
import com.ebukom.data.DataDummy
import com.ebukom.data.buildClassDummy
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

        mList.clear()
        mList.addAll(DataDummy.chooseClassData)
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

    fun addClass(item: ChooseClassDao) {
        DataDummy.chooseClassDataMain.add(item)

        loading.visibility = View.VISIBLE
        Handler().postDelayed({
            loading.visibility = View.GONE
            finish()
        }, 1000)
    }
}
