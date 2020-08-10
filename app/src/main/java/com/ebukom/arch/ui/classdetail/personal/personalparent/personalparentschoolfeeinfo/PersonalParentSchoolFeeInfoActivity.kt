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
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfo.AdminShareSchoolFeeInfoActivity
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent.AdminSchoolFeeInfoSentAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_personal_parent_school_fee_info.*
import kotlinx.android.synthetic.main.activity_personal_parent_school_fee_info.toolbar
import kotlinx.android.synthetic.main.activity_personal_parent_school_fee_info.tvToolbarTitle
import kotlinx.android.synthetic.main.activity_school_announcement_add_template.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement_comment.view.*

class PersonalParentSchoolFeeInfoActivity : AppCompatActivity() {

    var objectList = ArrayList<AdminSchoolFeeInfoSentDao>()
    lateinit var adminSchoolFeeInfoSentAdapter: AdminSchoolFeeInfoSentAdapter

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

                // Recycler View
                rvPersonalParentSchoolFeeInfoRecipient.visibility = View.VISIBLE
                addData()
                adminSchoolFeeInfoSentAdapter =
                    AdminSchoolFeeInfoSentAdapter(
                        objectList, null
                    )
                rvPersonalParentSchoolFeeInfoRecipient.layoutManager = LinearLayoutManager(this)
                rvPersonalParentSchoolFeeInfoRecipient.adapter = adminSchoolFeeInfoSentAdapter

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
        when (item.itemId) {
            R.id.editInfo -> {
                val bottomSheetDialog = BottomSheetDialog(this)
                val view =
                    layoutInflater.inflate(R.layout.bottom_sheet_school_announcement_comment, null)
                bottomSheetDialog.setContentView(view)

                view.tvEditComment.text = "Edit Biaya Pendidikan"
                view.tvEditComment.setOnClickListener {
//                    bottomSheetDialog.dismiss()
                    val intent = Intent(this, AdminShareSchoolFeeInfoActivity::class.java)
                    intent.putExtra("layout", "edit")
                    startActivity(intent)
                }

                view.tvDeleteComment.text = "Hapus Biaya Pendidikan"
                view.tvDeleteComment.setOnClickListener {
                    bottomSheetDialog.dismiss()

                    val builder = AlertDialog.Builder(this)

                    bottomSheetDialog.dismiss()

                    builder.setMessage("Apakah Anda yakin ingin menghapus informasi biaya pendidikan ini?")

                    builder.setNegativeButton("BATALKAN") { dialog, which ->
                        Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
                    }
                    builder.setPositiveButton("HAPUS") { dialog, which ->
                        Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
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

    private fun addData() {
        for (i in 0..10) {
            objectList.add(
                AdminSchoolFeeInfoSentDao(
                    "Info Biaya Pendidikan 14 Maret 2020",
                    "Pramuka, Basket",
                    "Dikirim pada 20.00 - 14 Maret 2020"
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
}