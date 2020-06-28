package com.ebukom.arch.ui.joinclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.chooseclass.ChooseClassActivity
import com.ebukom.arch.ui.chooseclass.ChooseClassAdapter
import kotlinx.android.synthetic.main.activity_choose_class.*
import kotlinx.android.synthetic.main.activity_join_class.*
import kotlinx.android.synthetic.main.item_class_blue.*
import kotlinx.android.synthetic.main.item_class_green.*

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

        mList.add(ChooseClassDao("", "", "", 0))
        mList.add(ChooseClassDao("", "", "", 1))
        mList.add(ChooseClassDao("", "", "", 0))
        mList.add(ChooseClassDao("", "", "", 1))
        mList.add(ChooseClassDao("", "", "", 0))
        mList.add(ChooseClassDao("", "", "", 1))
        mList.add(ChooseClassDao("", "", "", 0))
        mList.add(ChooseClassDao("", "", "", 1))
        mList.add(ChooseClassDao("", "", "", 0))
        mList.add(ChooseClassDao("", "", "", 1))

        mAdapter.addAll(mList)


    }
}
