package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemFormDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminShareSchoolFeeInfoAdapter
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfonext.AdminShareSchoolFeeInfoNextActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddnote.AdminSchoolFeeInfoAddNoteActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddpaymentitem.AdminSchoolFeeInfoAddPaymentItemActivity
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info.*
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info.toolbar
import kotlinx.android.synthetic.main.alert_edit_payment.view.*

class AdminShareSchoolFeeInfoActivity : AppCompatActivity() {
    var mPaymentList: ArrayList<AdminPaymentItemFormDao> = arrayListOf()
    lateinit var mPaymentAdapter: AdminShareSchoolFeeInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_share_school_fee_info)

        initToolbar()

        btnAdminShareSchoolFeeInfoAdd.setOnClickListener {
            startActivity(Intent(this, AdminSchoolFeeInfoAddPaymentItemActivity::class.java))
        }

        btnAdminShareSchoolFeeInfoAddNote.setOnClickListener {
            startActivity(Intent(this, AdminSchoolFeeInfoAddNoteActivity::class.java))
        }

        mPaymentAdapter = AdminShareSchoolFeeInfoAdapter(mPaymentList)
        rvAdminShareSchoolFeeInfo.apply {
            layoutManager = LinearLayoutManager(
                this@AdminShareSchoolFeeInfoActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mPaymentAdapter
        }
        mPaymentList.addAll(DataDummy.paymentData)
        mPaymentAdapter.notifyDataSetChanged()

        checkPaymentEmpty()

        // Toggle button
        tbtnAdminShareSchoolFeeInfoDetailEdit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mPaymentList.forEach {
                    it.itemEdit = true
                }
                mPaymentAdapter.notifyDataSetChanged()
            } else {
                mPaymentList.forEach {
                    it.itemEdit = false
                }
                mPaymentAdapter.notifyDataSetChanged()
            }
        }

        // Next activity
        btnAdminShareSchoolFeeInfoNext.setOnClickListener {
            startActivity(Intent(this, AdminShareSchoolFeeInfoNextActivity::class.java))
        }

        // Get intent from PersonalParentSchoolFeeInfoActivity
        val layout = intent?.extras?.getString("layout", null)
        when (layout) {
            "edit" -> {
                tvToolbarTitle.text = "Edit Biaya Pendidikan"
                tvAdminShareSchoolFeeInfoTitle.visibility = View.VISIBLE
                tvAdminShareSchoolFeeInfoDate.visibility = View.VISIBLE
                btnAdminShareSchoolFeeInfoChangeRecipient.visibility = View.VISIBLE
                btnAdminShareSchoolFeeInfoNext.text = "SIMPAN PERUBAHAN"
                btnAdminShareSchoolFeeInfoAddNote.text = "UBAH CATATAN"
            }
        }

        // Change Recipient
        btnAdminShareSchoolFeeInfoChangeRecipient.setOnClickListener {
            startActivity(Intent(this, AdminShareSchoolFeeInfoNextActivity::class.java))
        }
    }

    private fun checkPaymentEmpty() {
        if (mPaymentList.isNotEmpty()) {
            ivAdminShareSchoolFeeInfoEmpty.visibility = View.GONE
            tvAdminShareSchoolFeeInfoEmpty.visibility = View.GONE
            tbtnAdminShareSchoolFeeInfoDetailEdit.visibility = View.VISIBLE
            btnAdminShareSchoolFeeInfoNext.isEnabled = true
            btnAdminShareSchoolFeeInfoNext.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        } else {
            ivAdminShareSchoolFeeInfoEmpty.visibility = View.VISIBLE
            tvAdminShareSchoolFeeInfoEmpty.visibility = View.VISIBLE
            tbtnAdminShareSchoolFeeInfoDetailEdit.visibility = View.GONE
            btnAdminShareSchoolFeeInfoNext.isEnabled = false
            btnAdminShareSchoolFeeInfoNext.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )
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

    fun deleteItem(item: AdminPaymentItemFormDao, pos: Int) {
        val builder = AlertDialog.Builder(this@AdminShareSchoolFeeInfoActivity)

        builder.setMessage("Apakah Anda yakin ingin menghapus item ini?")

        builder.setPositiveButton("HAPUS", null)
        builder.setNegativeButton("BATALKAN") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            dialog.dismiss()
            mPaymentList.remove(item)
            DataDummy.paymentData.removeAt(pos)
            mPaymentAdapter.notifyDataSetChanged()

            checkPaymentEmpty()
        }
        positiveButton.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorGray
            )
        )

        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorRed
            )
        )
    }

    fun editItem(item: AdminPaymentItemFormDao, pos: Int) {
        val builder = AlertDialog.Builder(this@AdminShareSchoolFeeInfoActivity)
        val view = layoutInflater.inflate(R.layout.alert_edit_payment, null)

        view.etAlertEditPaymentName.setText(DataDummy.paymentData[pos].itemName)
        view.etAlertEditPaymentFee.setText(DataDummy.paymentData[pos].itemFee)

        builder.setView(view)

        builder.setPositiveButton("SIMPAN", null)
        builder.setNegativeButton("BATALKAN") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (view.etAlertEditPaymentName.text.toString()
                    .isEmpty() && view.etAlertEditPaymentFee.text.toString().isEmpty()
            ) {
                view.tvAlertEditPaymentNameErrorMessage.visibility = View.VISIBLE
                view.tvAlertEditPaymentFeeErrorMessage.visibility = View.VISIBLE
            } else if (view.etAlertEditPaymentName.text.toString().isEmpty()) {
                view.tvAlertEditPaymentNameErrorMessage.visibility = View.VISIBLE
            } else if (view.etAlertEditPaymentFee.text.toString().isEmpty()) {
                view.tvAlertEditPaymentFeeErrorMessage.visibility = View.VISIBLE
            } else {
                dialog.dismiss()
                DataDummy.paymentData[pos].itemName = view.etAlertEditPaymentName.text.toString()
                DataDummy.paymentData[pos].itemFee = view.etAlertEditPaymentFee.text.toString()
                mPaymentList.clear()
                mPaymentList.addAll(DataDummy.paymentData)
                mPaymentAdapter.notifyDataSetChanged()
            }
        }
        positiveButton.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorSuperDarkBlue
            )
        )

        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorRed
            )
        )
    }

    override fun onResume() {
        super.onResume()

        mPaymentList.clear()
        mPaymentList.addAll(DataDummy.paymentData)
        mPaymentAdapter.notifyDataSetChanged()

        checkPaymentEmpty()
    }
}

