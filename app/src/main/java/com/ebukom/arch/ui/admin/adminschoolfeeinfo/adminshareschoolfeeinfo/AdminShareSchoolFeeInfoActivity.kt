//package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfo
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import androidx.appcompat.app.AlertDialog
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.ebukom.R
//import com.ebukom.arch.dao.AdminPaymentItemDao
//import com.ebukom.arch.dao.AdminPaymentItemFormDao
//import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
//import com.ebukom.arch.ui.admin.MainAdminActivity
//import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminShareSchoolFeeInfoAdapter
//import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfonext.AdminShareSchoolFeeInfoNextActivity
//import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddnote.AdminSchoolFeeInfoAddNoteActivity
//import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddpaymentitem.AdminSchoolFeeInfoAddPaymentItemActivity
//import com.ebukom.data.DataDummy
//import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info.*
//import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info.toolbar
//import kotlinx.android.synthetic.main.alert_edit_payment.view.*
//
//class AdminShareSchoolFeeInfoActivity : AppCompatActivity() {
//    private var pos: Int = -1
//    var mPaymentList: ArrayList<AdminPaymentItemFormDao> = arrayListOf()
//    lateinit var mPaymentAdapter: AdminShareSchoolFeeInfoAdapter
//    lateinit var layout : String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_admin_share_school_fee_info)
//
//        initToolbar()
//
//        btnAdminShareSchoolFeeInfoAdd.setOnClickListener {
//            startActivity(Intent(this, AdminSchoolFeeInfoAddPaymentItemActivity::class.java))
//        }
//
//        btnAdminShareSchoolFeeInfoAddNote.setOnClickListener {
//            startActivity(Intent(this, AdminSchoolFeeInfoAddNoteActivity::class.java))
//        }
//
//        btnAdminShareSchoolFeeInfoNext.setOnClickListener {
//            startActivity(Intent(this, AdminShareSchoolFeeInfoNextActivity::class.java))
//        }
//
//        mPaymentAdapter = AdminShareSchoolFeeInfoAdapter(mPaymentList)
//        rvAdminShareSchoolFeeInfo.apply {
//            layoutManager = LinearLayoutManager(
//                this@AdminShareSchoolFeeInfoActivity,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//            adapter = mPaymentAdapter
//        }
//        mPaymentList.addAll(DataDummy.paymentTemporaryData)
//        mPaymentAdapter.notifyDataSetChanged()
//
//        checkPaymentEmpty()
//
//        // Toggle button
//        tbtnAdminShareSchoolFeeInfoDetailEdit.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                mPaymentList.forEach {
//                    it.itemEdit = true
//                }
//                mPaymentAdapter.notifyDataSetChanged()
//            } else {
//                mPaymentList.forEach {
//                    it.itemEdit = false
//                }
//                mPaymentAdapter.notifyDataSetChanged()
//            }
//        }
//
//        // Get intent from PersonalParentSchoolFeeInfoActivity
//        layout = intent?.extras?.getString("layout")?: ""
//        when (layout) {
//            "edit" -> {
//                tvToolbarTitle.text = "Edit Biaya Pendidikan"
//                tvAdminShareSchoolFeeInfoTitle.visibility = View.VISIBLE
//                tvAdminShareSchoolFeeInfoDate.visibility = View.VISIBLE
//                btnAdminShareSchoolFeeInfoChangeRecipient.visibility = View.VISIBLE
//                btnAdminShareSchoolFeeInfoNext.text = "SIMPAN PERUBAHAN"
//                btnAdminShareSchoolFeeInfoAddNote.text = "UBAH CATATAN"
//
//                pos = intent?.extras?.getInt("pos", -1) ?: -1
//                val data = intent?.extras?.getSerializable("data") as AdminPaymentItemDao
//
//                mPaymentList.clear()
//                DataDummy.paymentTemporaryData.clear()
//                DataDummy.paymentTemporaryData.addAll(DataDummy.paymentData[pos].items)
//                mPaymentAdapter.notifyDataSetChanged()
//                rvAdminShareSchoolFeeInfo.adapter?.notifyDataSetChanged()
//                checkPaymentEmpty()
//
//                // Change Note
//                btnAdminShareSchoolFeeInfoAddNote.setOnClickListener {
//                    val intent = Intent(this, AdminSchoolFeeInfoAddNoteActivity::class.java)
//                    intent.putExtra("data", data)
//                    intent.putExtra("layout", "edit")
//                    intent.putExtra("pos", pos)
//                    startActivity(intent)
//                }
//
//                // Change Recipient
//                btnAdminShareSchoolFeeInfoChangeRecipient.setOnClickListener {
//                    val intent = Intent(this, AdminShareSchoolFeeInfoNextActivity::class.java)
//                    intent.putExtra("data", data)
////                    intent.putExtra("role", "admin")
//                    intent.putExtra("layout", "edit")
//                    intent.putExtra("pos", pos)
//                    startActivity(intent)
//                }
//
//                // Done
//                btnAdminShareSchoolFeeInfoNext.setOnClickListener {
//                    DataDummy.paymentData[pos].recipients = DataDummy.recipientTemporaryData.clone() as List<AdminSchoolFeeInfoSentDao>
//                    DataDummy.paymentData[pos].note = DataDummy.adminNoteTemporaryData
//                    DataDummy.paymentData[pos].items = DataDummy.paymentTemporaryData.clone() as List<AdminPaymentItemFormDao>
//
//                    startActivity(Intent(this, MainAdminActivity::class.java))
//                    finish()
//                }
//            }
//        }
//
//    }
//
//    private fun checkPaymentEmpty() {
//        if (mPaymentList.isNotEmpty()) {
//            ivAdminShareSchoolFeeInfoEmpty.visibility = View.GONE
//            tvAdminShareSchoolFeeInfoEmpty.visibility = View.GONE
//            tbtnAdminShareSchoolFeeInfoDetailEdit.visibility = View.VISIBLE
//            btnAdminShareSchoolFeeInfoNext.isEnabled = true
//            btnAdminShareSchoolFeeInfoNext.setBackgroundColor(
//                ContextCompat.getColor(
//                    applicationContext,
//                    R.color.colorSuperDarkBlue
//                )
//            )
//            rvAdminShareSchoolFeeInfo.visibility = View.VISIBLE
//        } else {
//            ivAdminShareSchoolFeeInfoEmpty.visibility = View.VISIBLE
//            tvAdminShareSchoolFeeInfoEmpty.visibility = View.VISIBLE
//            tbtnAdminShareSchoolFeeInfoDetailEdit.visibility = View.GONE
//            btnAdminShareSchoolFeeInfoNext.isEnabled = false
//            btnAdminShareSchoolFeeInfoNext.setBackgroundColor(
//                ContextCompat.getColor(
//                    applicationContext,
//                    R.color.colorGray
//                )
//            )
//            rvAdminShareSchoolFeeInfo.visibility = View.GONE
//        }
//    }
//
//    fun initToolbar() {
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.title = ""
//        toolbar.setNavigationOnClickListener {
//            onBackPressed()
//            DataDummy.recipientTemporaryData.clear()
//            DataDummy.adminNoteTemporaryData = ""
//            DataDummy.eskulTemporaryData.clear()
//            DataDummy.paymentTemporaryData.clear()
//        }
//    }
//
//    fun deleteItem(item: AdminPaymentItemFormDao, pos: Int) {
//        val builder = AlertDialog.Builder(this@AdminShareSchoolFeeInfoActivity)
//
//        builder.setMessage("Apakah Anda yakin ingin menghapus item ini?")
//
//        builder.setPositiveButton("HAPUS", null)
//        builder.setNegativeButton("BATALKAN") { dialog, which ->
//            dialog.dismiss()
//        }
//
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//
//        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//        positiveButton.setOnClickListener {
//            dialog.dismiss()
//            mPaymentList.remove(item)
//            DataDummy.paymentTemporaryData.removeAt(pos)
//            mPaymentAdapter.notifyDataSetChanged()
//
//            checkPaymentEmpty()
//        }
//        positiveButton.setTextColor(
//            ContextCompat.getColor(
//                applicationContext,
//                R.color.colorGray
//            )
//        )
//
//        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//        negativeButton.setTextColor(
//            ContextCompat.getColor(
//                applicationContext,
//                R.color.colorRed
//            )
//        )
//    }
//
//    fun editItem(item: AdminPaymentItemFormDao, pos: Int) {
//        val builder = AlertDialog.Builder(this@AdminShareSchoolFeeInfoActivity)
//        val view = layoutInflater.inflate(R.layout.alert_edit_payment, null)
//
//        view.etAlertEditPaymentName.setText(DataDummy.paymentTemporaryData[pos].itemName)
//        view.etAlertEditPaymentFee.setText(DataDummy.paymentTemporaryData[pos].itemFee)
//
//        builder.setView(view)
//
//        builder.setPositiveButton("SIMPAN", null)
//        builder.setNegativeButton("BATALKAN") { dialog, which ->
//            dialog.dismiss()
//        }
//
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//
//        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//        positiveButton.setOnClickListener {
//            if (view.etAlertEditPaymentName.text.toString()
//                    .isEmpty() && view.etAlertEditPaymentFee.text.toString().isEmpty()
//            ) {
//                view.tvAlertEditPaymentNameErrorMessage.visibility = View.VISIBLE
//                view.tvAlertEditPaymentFeeErrorMessage.visibility = View.VISIBLE
//            } else if (view.etAlertEditPaymentName.text.toString().isEmpty()) {
//                view.tvAlertEditPaymentNameErrorMessage.visibility = View.VISIBLE
//            } else if (view.etAlertEditPaymentFee.text.toString().isEmpty()) {
//                view.tvAlertEditPaymentFeeErrorMessage.visibility = View.VISIBLE
//            } else {
//                dialog.dismiss()
//                DataDummy.paymentTemporaryData[pos].itemName = view.etAlertEditPaymentName.text.toString()
//                DataDummy.paymentTemporaryData[pos].itemFee = view.etAlertEditPaymentFee.text.toString()
//                mPaymentList.clear()
//                mPaymentList.addAll(DataDummy.paymentTemporaryData)
//                mPaymentAdapter.notifyDataSetChanged()
//            }
//        }
//        positiveButton.setTextColor(
//            ContextCompat.getColor(
//                applicationContext,
//                R.color.colorSuperDarkBlue
//            )
//        )
//
//        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//        negativeButton.setTextColor(
//            ContextCompat.getColor(
//                applicationContext,
//                R.color.colorRed
//            )
//        )
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        mPaymentList.clear()
////        if(layout == "edit") mPaymentList.addAll(DataDummy.paymentData[pos].items)
////        else mPaymentList.addAll(DataDummy.paymentTemporaryData)
//        mPaymentList.addAll(DataDummy.paymentTemporaryData)
//        mPaymentAdapter.notifyDataSetChanged()
//
//        checkPaymentEmpty()
//    }
//}
//
