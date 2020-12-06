package com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentschoolfeeinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemDao
import com.ebukom.arch.dao.AdminPaymentItemFormDao
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminShareSchoolFeeInfoAdapter
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent.ParentSchoolFeeInfoAdapter
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_personal_parent_school_fee_info.*
import kotlinx.android.synthetic.main.activity_personal_parent_school_fee_info.toolbar
import kotlinx.android.synthetic.main.activity_personal_parent_school_fee_info.tvToolbarTitle
import kotlinx.android.synthetic.main.activity_personal_parent_school_fee_info.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*

class PersonalParentSchoolFeeInfoActivity : AppCompatActivity() {

    private var pos: Int = -1

    private val mPaymentList: ArrayList<AdminPaymentItemFormDao> = arrayListOf()
    lateinit var mPaymentAdapter: AdminShareSchoolFeeInfoAdapter
    private val mParentList: ArrayList<AdminSchoolFeeInfoSentDao> = arrayListOf()
    lateinit var mParentAdapter: ParentSchoolFeeInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_parent_school_fee_info)

        initToolbar()

        val role = intent.extras?.getString("role", null)
        when (role) {
            "admin" -> {
                tvToolbarTitle.text = "Info Biaya Pendidikan"
                cvPersonalParentSchoolFeeInfoProcedure.visibility = View.GONE
                vPersonalParentSchoolFeeInfo.visibility = View.VISIBLE
                tvPersonalParentSchoolFeeInfoRecipientTitle.visibility = View.VISIBLE
                tbAdminSchoolFeeInfo.visibility = View.VISIBLE
                tbPersonalParentSchoolFeeInfo.visibility = View.GONE

                pos = intent?.extras?.getInt("pos", -1) ?: -1
                val data = intent?.extras?.getSerializable("data") as AdminPaymentItemDao

                // Payment Item
                mPaymentAdapter = AdminShareSchoolFeeInfoAdapter(mPaymentList)
                rvPersonalParentSchoolFeeInfo.apply {
                    layoutManager = LinearLayoutManager(
                        this@PersonalParentSchoolFeeInfoActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = mPaymentAdapter
                }
                mPaymentList.addAll(data.items)
                mPaymentAdapter.notifyDataSetChanged()

                // Note
                if (data.note == "") llPersonalParentSchoolFeeInfoNote.visibility = View.GONE
                else {
                    llPersonalParentSchoolFeeInfoNote.visibility = View.VISIBLE
                    tvPersonalParentSchoolFeeInfoNoteContent.text = data.note.toString()
                }

                // Recipient List
                mParentAdapter = ParentSchoolFeeInfoAdapter(mParentList, null)
                rvPersonalParentSchoolFeeInfoRecipient.apply {
                    layoutManager = LinearLayoutManager(
                        this@PersonalParentSchoolFeeInfoActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = mParentAdapter
                }
                mParentList.addAll(data.recipients)
                mParentAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val role = intent.extras?.getString("role", "parent")
        when (role) {
            "admin" -> {
                menuInflater.inflate(R.menu.admin_school_fee_info_menu, menu)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        pos = intent?.extras?.getInt("pos", -1) ?: -1
//        val data = intent?.extras?.getSerializable("data") as AdminPaymentItemDao
        val data = AdminPaymentItemDao(
            DataDummy.paymentData[pos].parentName,
            "Bobbi Andrean",
            DataDummy.paymentData[pos].eskuls,
            DataDummy.paymentData[pos].classes,
            DataDummy.paymentData[pos].time,
            DataDummy.paymentData[pos].note,
            DataDummy.paymentData[pos].items,
            DataDummy.paymentData[pos].recipients
        )

        when (item.itemId) {
            R.id.editInfo -> {
                val bottomSheetDialog = BottomSheetDialog(this)
                val view =
                    layoutInflater.inflate(R.layout.bottom_sheet_school_announcement_comment, null)
                bottomSheetDialog.setContentView(view)

                // Edit
                view.tvEditComment.text = "Edit Biaya Pendidikan"
                view.tvEditComment.setOnClickListener {
                    bottomSheetDialog.dismiss()
//                    val intent = Intent(this, AdminShareSchoolFeeInfoActivity::class.java)
                    intent.putExtra("layout", "edit")
                    intent.putExtra("pos", pos)
                    intent.putExtra("data", data)
                    startActivity(intent)
                }

                // Delete
                view.tvDeleteComment.text = "Hapus Biaya Pendidikan"
                view.tvDeleteComment.setOnClickListener {
                    bottomSheetDialog.dismiss()

                    val builder = AlertDialog.Builder(this)

                    bottomSheetDialog.dismiss()

                    builder.setMessage("Apakah Anda yakin ingin menghapus informasi biaya pendidikan ini?")

                    builder.setNegativeButton("BATALKAN") { _, _ ->
                        bottomSheetDialog.dismiss()
                    }
                    builder.setPositiveButton("HAPUS") { _, _ ->
                        bottomSheetDialog.dismiss()
                        DataDummy.paymentData.removeAt(pos)
                        finish()
                    }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()

                    val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
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
                view.tvCancelComment.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }

                bottomSheetDialog.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
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
}