package com.ebukom.arch.ui.classdetail.school.schoolphoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotoedit.SchoolPhotoEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotonew.SchoolPhotoNewActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_school_photo.*
import kotlinx.android.synthetic.main.activity_school_photo.toolbar
import kotlinx.android.synthetic.main.activity_school_photo_new.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import timber.log.Timber
import java.util.ArrayList

class SchoolPhotoActivity : AppCompatActivity() {
    private var mPhotoList: ArrayList<ClassDetailPhotoDao> = arrayListOf()
    lateinit var mPhotoAdapater: SchoolPhotoAdapter
    val db = FirebaseFirestore.getInstance()
    var classId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_photo)

        initToolbar()
        checkEmpty()

        /**
         * Get intent from SchoolFragment
         */
        classId = intent?.extras?.getString("classId")

        btnSchoolPhotoNew.setOnClickListener {
            val intent = Intent(this, SchoolPhotoNewActivity::class.java)
            intent.putExtra("classId", classId)
            startActivity(intent)
        }

        db.collection("classes").document(classId!!).collection("photos")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.e(error)
                    return@addSnapshotListener
                }

                initRecycler()

                for (document in value!!.documents) {
                    mPhotoList.add(
                        ClassDetailPhotoDao(
                            document["title"] as String,
                            document["link"] as String,
                            document.id
                        )
                    )
                    mPhotoAdapater.notifyDataSetChanged()
                    checkEmpty()
                }
            }

        checkEmpty()

    }

    private fun initRecycler() {
        /**
         * Load classes joined data
         */
        mPhotoList.clear()
        mPhotoAdapater = SchoolPhotoAdapter(mPhotoList, this)
        rvSchoolPhoto.apply {
            layoutManager = LinearLayoutManager(
                this@SchoolPhotoActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mPhotoAdapater
        }
    }

    private fun checkEmpty() {
        if (mPhotoList.isEmpty()) {
            ivSchoolPhotoEmpty.visibility = View.VISIBLE
            tvSchoolPhotoEmpty.visibility = View.VISIBLE
        } else {
            ivSchoolPhotoEmpty.visibility = View.GONE
            tvSchoolPhotoEmpty.visibility = View.GONE
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

    fun popUpMenu(photoId: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)

        view.tvEditInfo.text = "Edit informasi foto"
        view.tvDeleteInfo.text = "Hapus foto"

        bottomSheetDialog.setContentView(view)

        /**
         * Edit photo information
         */
        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            val intent = Intent(this, SchoolPhotoEditActivity::class.java)
            intent.putExtra("classId", classId)
            intent.putExtra("photoId", photoId)
            startActivity(intent)
        }

        /**
         * Delete photo
         */
        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolPhotoActivity)

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus pengumuman ini?")
            builder.setNegativeButton("BATALKAN") { dialog, which ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
                db.collection("classes").document(classId!!).collection("photos").document(photoId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("TAG", "Photo deleted successfully")
                    }.addOnFailureListener {
                        Log.d("TAG", "Photo failed to be deleted")
                    }
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

        view.tvCancelInfo.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
}