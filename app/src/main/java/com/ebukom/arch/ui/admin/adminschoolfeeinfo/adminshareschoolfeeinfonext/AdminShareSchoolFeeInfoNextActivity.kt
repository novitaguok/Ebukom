package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfonext

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.*
import com.ebukom.arch.ui.admin.MainAdminActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinforecheck.AdminShareSchoolFeeInfoRecheckActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent.ParentSchoolFeeInfoAdapter
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info_next.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_header.view.*
import kotlinx.android.synthetic.main.bottom_sheet_register_parent.view.*

class AdminShareSchoolFeeInfoNextActivity : AppCompatActivity(),
    ParentSchoolFeeInfoAdapter.onCheckListener {

    private var pos: Int = -1
    private val mParentList: ArrayList<AdminSchoolFeeInfoSentDao> = arrayListOf()
    lateinit var mParentAdapter: ParentSchoolFeeInfoAdapter
    private val mEskulList: ArrayList<ClassDetailItemCheckDao> = arrayListOf()
    lateinit var mEskulAdapter: ClassDetailItemCheckDao
    private var mClass: String = "Kelas 1 Aurora"
    private var mEskul: ArrayList<String> = arrayListOf()
    private var allEskul: String? = ""
    private var isChosen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_share_school_fee_info_next)

        initToolbar()

        btnAdminSchoolFeeInfoNextClass.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_class_detail_header, null)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            view.rbBottomSheetClassDetailHeaderKelas1.isChecked = mClass.contains("Kelas 1 Aurora")
            view.rbBottomSheetClassDetailHeaderKelas2.isChecked =
                mClass.contains("Kelas 2 Fatamorgana")

            view.rbGroup.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == R.id.rbBottomSheetClassDetailHeaderKelas1) {
                    bottomSheetDialog.dismiss()
                    mClass = "Kelas 1 Aurora"
                    btnAdminSchoolFeeInfoNextClass.text = mClass
                } else if (checkedId == R.id.rbBottomSheetClassDetailHeaderKelas2) {
                    bottomSheetDialog.dismiss()
                    mClass = "Kelas 2 Fatamorgana"
                    btnAdminSchoolFeeInfoNextClass.text = mClass
                } else {
                    mClass = "Kelas 1 Aurora, Kelas 2 Fatamorgana"
                    btnAdminSchoolFeeInfoNextClass.text = "Semua Kelas"
                }
            }
        }

        btnAdminSchoolFeeInfoNextEskul.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_register_parent, null)
            view.btnRegisterParentBottomSheetDone.setOnClickListener {
                bottomSheetDialog.dismiss()
                if (view.cbRegisterParentBottomSheetPramuka.isChecked) {
                    mEskul.add("Pramuka")
                }
                if (view.cbRegisterParentBottomSheetFutsal.isChecked) {
                    mEskul.add("Futsal")
                }
                if (view.cbRegisterParentBottomSheetBasket.isChecked) {
                    mEskul.add("Basket")
                }
                if (view.cbRegisterParentBottomSheetPMR.isChecked) {
                    mEskul.add("PMR")
                }

                allEskul = mEskul.distinct().toString()
                allEskul = allEskul?.substring(1, allEskul!!.length - 1)
                btnAdminSchoolFeeInfoNextEskul.text = allEskul

                if (mEskul.isNotEmpty()) {
                    isChosen = true
                    mParentList.clear()
                    addData(allEskul!!, mClass)
                    mParentAdapter.notifyDataSetChanged()
                }
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        mParentAdapter =
            ParentSchoolFeeInfoAdapter(mParentList, this@AdminShareSchoolFeeInfoNextActivity)
        rvAdminSchoolFeeInfoNext.apply {
            layoutManager = LinearLayoutManager(
                this@AdminShareSchoolFeeInfoNextActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mParentAdapter
        }

        // Check all
        cbAdminSchoolFeeInfoNextAllParent.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mParentList.forEach {
                    it.isChecked = true
                }
                mParentAdapter.notifyDataSetChanged()
            } else {
                mParentList.forEach {
                    it.isChecked = false
                }
                mParentAdapter.notifyDataSetChanged()
            }
        }

        // Done
        btnAdminSchoolFeeInfoNextDone.setOnClickListener {
            val intent = Intent(this, AdminShareSchoolFeeInfoRecheckActivity::class.java)
            intent.putExtra("role", "admin")
            intent.putExtra("eskulData", allEskul)
            intent.putExtra("classData", mClass)
            recipientSelected()

            loading.visibility = View.VISIBLE
            Handler().postDelayed({
                loading.visibility = View.GONE
                startActivity(intent)
            }, 1000)
        }

        // Get intent from PersonalParentSchoolFeeInfoActivity
        val layout = intent?.extras?.getString("layout", null)
        when (layout) {
            "edit" -> {
                btnAdminSchoolFeeInfoNextDone.text = "SIMPAN PERUBAHAN"

                pos = intent?.extras?.getInt("pos", -1) ?: -1
                val data = intent?.extras?.getSerializable("data") as AdminPaymentItemDao

                DataDummy.recipientTemporaryData = DataDummy.paymentData[pos].recipients as ArrayList<AdminSchoolFeeInfoSentDao>

                btnAdminSchoolFeeInfoNextDone.setOnClickListener {
                    recipientSelected()
                    loading.visibility = View.VISIBLE
                    Handler().postDelayed({
                        loading.visibility = View.GONE
                        finish()
                    }, 1000)
                }
            }
        }
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun addData(allEskul: String, mClass: String) {
        mParentList.add(
            AdminSchoolFeeInfoSentDao(
                "Jumaidah Estetika",
                "Bobbi Andrean • " + allEskul + " • " + mClass,
                "Terakhir diupdate: 20.00 - 14 Maret 2020"
            )
        )
        mParentList.add(
            AdminSchoolFeeInfoSentDao(
                "Siti Nur Mudhaya",
                "Bobbi Andrean • " + allEskul + " • " + mClass,
                "Terakhir diupdate: 20.00 - 14 Maret 2020"
            )
        )
        mParentList.add(
            AdminSchoolFeeInfoSentDao(
                "Rizki Azhar",
                "Bobbi Andrean • " + allEskul + " • " + mClass,
                "Terakhir diupdate: 20.00 - 14 Maret 2020"
            )
        )
        mParentList.add(
            AdminSchoolFeeInfoSentDao(
                "Putri Tryatna",
                "Bobbi Andrean • " + allEskul + " • " + mClass,
                "Terakhir diupdate: 20.00 - 14 Maret 2020"
            )
        )
    }

    override fun onCheckChange() {
        var isCheckedItem = false
        mParentList.forEach {
            if (it.isChecked) isCheckedItem = true
        }

        if (isCheckedItem) {
            btnAdminSchoolFeeInfoNextDone.isEnabled = true
            btnAdminSchoolFeeInfoNextDone.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        } else {
            btnAdminSchoolFeeInfoNextDone.isEnabled = false
            btnAdminSchoolFeeInfoNextDone.setBackgroundColor(
                Color.parseColor("#BDBDBD")
            )
        }
    }

    private fun recipientSelected() {
        mParentList.forEach {
            if (it.isChecked) {
                DataDummy.recipientTemporaryData.add(
                    AdminSchoolFeeInfoSentDao(
                        it.title,
                        "Bobbi Andrean • " + allEskul + " • " + mClass,
                        "Terakhir diupdate: 20.00 - 14 Maret 2020"
                    )
                )
                it.isChecked = false
            }
        }
    }
}