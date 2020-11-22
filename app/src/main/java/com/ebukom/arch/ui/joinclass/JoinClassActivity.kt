package com.ebukom.arch.ui.joinclass

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.chooseclass.ChooseClassAdapter
import com.ebukom.data.DataDummy
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_choose_class.*
//import com.ebukom.data.buildClassDummy
import kotlinx.android.synthetic.main.activity_join_class.*
import timber.log.Timber

class JoinClassActivity : AppCompatActivity() {

    private val mList: ArrayList<ChooseClassDao> = arrayListOf()
    private val mAdapter = ChooseClassAdapter(mList)
    lateinit var sharePref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_class)

        sharePref = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)

//        loadDummy()
        loadData()

        rvJoinClassClasses.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@JoinClassActivity)
        }

        initToolbar()

//        mList.clear()
//        mList.addAll(DataDummy.chooseClassData)
//        mAdapter.addAll(mList)

    }

    /**
     * Load all classes data
     */
    private fun loadData() {

        val uid = sharePref.getString("uid", "") as String
        val db = FirebaseFirestore.getInstance()

        Log.d("JoinClassActivity", "loadData")
        db.collection("classes").get()
            .addOnSuccessListener {
                mList.clear()
                for (document in it.documents) {
                    var isContains = false
                    if (document["class_teacher_ids"] != null) {
                        val getArraysOfId = document["class_teacher_ids"] as List<String>
                        isContains = getArraysOfId.contains(uid)
                    }

                    if (!isContains) {
                        mList.add(
                            ChooseClassDao(
                                document["class_grade"] as String,
                                document["class_name"] as String,
                                document["class_teacher.name"] as String,
                                (document["class_bg"] as Long).toInt(),
                                Color.parseColor(document["class_theme"] as String),
                                document.id
                            )
                        )
                    }
                }

                mList.sortBy {
                    it.classNumber
                }

                rvJoinClassClasses.adapter?.notifyDataSetChanged()
//                checkEmptyList()
            }
            .addOnFailureListener {
                Timber.tag("ChooseClassActivity")
                Timber.e(it)
            }
    }

    private fun loadDummy() {
        mList.add(
            ChooseClassDao(
                "Kelas 1",
                "Krypton",
                "Ratna Hendrawati",
                R.drawable.bg_class_krypton,
                Color.parseColor("#005C39")
            )
        )
        mList.add(
            ChooseClassDao(
                "Kelas 1",
                "Xenon",
                "Eni Trikuswanti",
                R.drawable.bg_class_xenon,
                Color.parseColor("#004A61")
            )
        )
        mList.add(
            ChooseClassDao(
                "Kelas 1",
                "Argon",
                "Ratna Hendrawati",
                R.drawable.bg_class_argon,
                Color.parseColor("#693535")
            )
        )
        mList.add(
            ChooseClassDao(
                "Kelas 2",
                "Titanium",
                "Yulianti Puspita",
                R.drawable.bg_class_titanium,
                Color.parseColor("#229AE6")
            )
        )
        mList.add(
            ChooseClassDao(
                "Kelas 2",
                "Neon",
                "Putri Eka",
                R.drawable.bg_class_neon,
                Color.parseColor("#FFADAD")
            )
        )
        mList.add(
            ChooseClassDao(
                "Kelas 2",
                "Helium",
                "Ahmad Juliansyah",
                R.drawable.bg_class_helium,
                Color.parseColor("#645470")
            )
        )
        mList.add(
            ChooseClassDao(
                "Kelas 3",
                "Argentum",
                "Gigi Rahma",
                R.drawable.bg_class_argentum,
                Color.parseColor("#313D2E")
            )
        )
        mList.add(
            ChooseClassDao(
                "Kelas 3",
                "Aurum",
                "Julia Isma",
                R.drawable.bg_class_aurum,
                Color.parseColor("#828127")
            )
        )
        mList.add(
            ChooseClassDao(
                "Kelas 3",
                "Selenium",
                "Dewi Putri",
                R.drawable.bg_class_selenium,
                Color.parseColor("#4F483B")
            )
        )
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
