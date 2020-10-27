package com.ebukom.arch.ui.joinclass

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.chooseclass.ChooseClassAdapter
import com.ebukom.data.DataDummy
//import com.ebukom.data.buildClassDummy
import kotlinx.android.synthetic.main.activity_join_class.*

class JoinClassActivity : AppCompatActivity() {

    private val mList: ArrayList<ChooseClassDao> = arrayListOf()
    private val mAdapter = ChooseClassAdapter(mList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_class)

        mList.add(ChooseClassDao("Kelas 1", "Krypton", "Ratna Hendrawati", R.drawable.bg_class_krypton, Color.parseColor("#005C39")))
        mList.add(ChooseClassDao("Kelas 1", "Xenon", "Eni Trikuswanti", R.drawable.bg_class_xenon, Color.parseColor("#004A61")))
        mList.add(ChooseClassDao("Kelas 1", "Argon", "Ratna Hendrawati", R.drawable.bg_class_argon, Color.parseColor("#693535")))
        mList.add(ChooseClassDao("Kelas 2", "Titanium", "Yulianti Puspita", R.drawable.bg_class_titanium, Color.parseColor("#229AE6")))
        mList.add(ChooseClassDao("Kelas 2", "Neon", "Putri Eka", R.drawable.bg_class_neon, Color.parseColor("#FFADAD")))
        mList.add(ChooseClassDao("Kelas 2", "Helium", "Ahmad Juliansyah", R.drawable.bg_class_helium, Color.parseColor("#645470")))
        mList.add(ChooseClassDao("Kelas 3", "Argentum", "Gigi Rahma", R.drawable.bg_class_argentum, Color.parseColor("#313D2E")))
        mList.add(ChooseClassDao("Kelas 3", "Aurum", "Julia Isma", R.drawable.bg_class_aurum, Color.parseColor("#828127")))
        mList.add(ChooseClassDao("Kelas 3", "Selenium", "Dewi Putri", R.drawable.bg_class_selenium, Color.parseColor("#4F483B")))

        rvJoinClassClasses.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@JoinClassActivity)
        }

        initToolbar()

//        mList.clear()
//        mList.addAll(DataDummy.chooseClassData)
//        mAdapter.addAll(mList)

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

            val builder = AlertDialog.Builder(this@JoinClassActivity)

            builder.setMessage("Kelas berhasil ditambahkan")
            builder.setPositiveButton("OK", null)

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )

        }, 1000)
    }
}
