package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info.*
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info.toolbar
import kotlinx.android.synthetic.main.alert_edit_payment.view.*

class AdminShareSchoolFeeInfoActivity : AppCompatActivity() {
    var objectList = ArrayList<AdminPaymentItemFormDao>()
    lateinit var adminShareSchoolFeeInfoAdapter: AdminShareSchoolFeeInfoAdapter

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

        addData()
        adminShareSchoolFeeInfoAdapter =
            AdminShareSchoolFeeInfoAdapter(
                objectList
            )
        rvAdminShareSchoolFeeInfo.layoutManager = LinearLayoutManager(this)
        rvAdminShareSchoolFeeInfo.adapter = adminShareSchoolFeeInfoAdapter

        if (objectList.isNotEmpty()) {
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
        }

        if (objectList.isEmpty()) {
            ivAdminShareSchoolFeeInfoEmpty.visibility = View.VISIBLE
            tvAdminShareSchoolFeeInfoEmpty.visibility = View.VISIBLE
            tbtnAdminShareSchoolFeeInfoDetailEdit.visibility = View.GONE
            btnAdminShareSchoolFeeInfoNext.isEnabled = false
            btnAdminShareSchoolFeeInfoNext.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorSuperDarkBlue
                )
            )
        }

        // Toggle button
        tbtnAdminShareSchoolFeeInfoDetailEdit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                objectList.forEach {
                    it.itemEdit = true
                }
                adminShareSchoolFeeInfoAdapter.notifyDataSetChanged()
            } else {
                objectList.forEach {
                    it.itemEdit = false
                }
                adminShareSchoolFeeInfoAdapter.notifyDataSetChanged()
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

        // Ubah penerima
        btnAdminShareSchoolFeeInfoChangeRecipient.setOnClickListener {
            startActivity(Intent(this, AdminShareSchoolFeeInfoNextActivity::class.java))
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

    private fun addData() {
        for (i in 0..1) {
            objectList.add(
                AdminPaymentItemFormDao(
                    "Pengembangan I/II",
                    "880.000"
                )
            )
        }
    }

    fun deleteItem(item: AdminPaymentItemFormDao) {
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
            objectList.remove(item)
            adminShareSchoolFeeInfoAdapter.notifyDataSetChanged()

            if (objectList.isEmpty()) {
                ivAdminShareSchoolFeeInfoEmpty.visibility = View.VISIBLE
                tvAdminShareSchoolFeeInfoEmpty.visibility = View.VISIBLE
                tbtnAdminShareSchoolFeeInfoDetailEdit.visibility = View.GONE
                btnAdminShareSchoolFeeInfoNext.isEnabled = false
                btnAdminShareSchoolFeeInfoNext.setBackgroundColor(Color.parseColor("#BDBDBD"))
            }
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

    fun editItem(item: AdminPaymentItemFormDao) {
        val builder = AlertDialog.Builder(this@AdminShareSchoolFeeInfoActivity)
        val view = layoutInflater.inflate(R.layout.alert_edit_payment, null)

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
}

