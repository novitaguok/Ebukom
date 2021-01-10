package com.ebukom.arch.ui.classdetail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.*
import com.ebukom.arch.ui.chooseclass.ChooseClassAdapter
import com.ebukom.arch.ui.classdetail.material.MaterialFragment
import com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationedit.MaterialEducationEditActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd.MaterialSubjectAddActivity
import com.ebukom.arch.ui.classdetail.member.MemberFragment
import com.ebukom.arch.ui.classdetail.notification.NotificationActivity
import com.ebukom.arch.ui.classdetail.personal.PersonalFragment
import com.ebukom.arch.ui.classdetail.personal.personalnoteedit.PersonalNoteEditActivity
import com.ebukom.arch.ui.classdetail.personal.PersonalNoteAdapter
import com.ebukom.arch.ui.classdetail.personal.personalparent.PersonalParentFragment
import com.ebukom.arch.ui.classdetail.school.SchoolFragment
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit.SchoolAnnouncementEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoAdapter
import com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotoedit.SchoolPhotoEditActivity
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleAdapter
import com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolscheduleedit.SchoolScheduleEditActivity
import com.ebukom.arch.ui.editprofile.EditProfileActivity
import com.ebukom.data.DataDummy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_class_detail.*
import kotlinx.android.synthetic.main.activity_personal_note_detail.*
import kotlinx.android.synthetic.main.bottom_sheet_choose_class.view.*
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_header.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.*
import kotlinx.android.synthetic.main.bottom_sheet_school_announcement.view.tvEditInfo
import timber.log.Timber

class MainClassDetailActivity : AppCompatActivity(), OnMoreCallback {
    companion object {
        var isAnnouncement = true
        var isMaterial = true
        var CLASS_ID = ""
    }

    private val mAnnouncementList: ArrayList<ClassDetailAnnouncementDao> =
        DataDummy.announcementData
    private lateinit var mAnnouncementAdapter: SchoolAnnouncementAdapter
    private val mScheduleList: ArrayList<ClassDetailScheduleDao> = DataDummy.scheduleData
    private lateinit var mScheduleAdapter: SchoolScheduleAdapter
    private val mPhotoList: ArrayList<ClassDetailPhotoDao> = DataDummy.photoData
    lateinit var mPhotoAdapter: SchoolPhotoAdapter
    private val mNoteList: ArrayList<ClassDetailPersonalNoteDao> = DataDummy.noteSentData
    lateinit var mNoteAdapter: PersonalNoteAdapter
    private val mEducationList: ArrayList<ClassDetailAnnouncementDao> = DataDummy.educationData
    lateinit var mEducationAdapter: SchoolAnnouncementAdapter
    private val mClassList: ArrayList<ChooseClassDao> = arrayListOf()
    lateinit var mClassAdapter: MainClassDetailAdapter
    val db = FirebaseFirestore.getInstance()
    var classId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_class_detail)

        ivClassHeaderNotificationBell.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        ivClassHeaderProfilePicture.setOnClickListener {
//            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        val sharePref: SharedPreferences = getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        val schoolFragment = SchoolFragment()
        val personalFragment = if (sharePref.getLong("level", 0) == 1L) {
            PersonalParentFragment()
        } else {
            PersonalFragment()
        }
        val materialFragment = MaterialFragment()
        val memberFragment = MemberFragment()

        classId = intent?.extras?.getString("classId")
        val className = intent?.extras?.getString("className") ?: ""
        val classNumber = intent?.extras?.getString("classNumber") ?: ""
        tvClassHeaderClassName.text = className
        tvClassHeaderClassNumber.text = classNumber

        if (classId != null) {
            CLASS_ID = classId!!
            val bundle = Bundle().apply {
                putString("classId", classId)
            }
            schoolFragment.arguments = bundle
            materialFragment.arguments = bundle
        }

        makeCurrentFragment(schoolFragment)
        bnClassDetail.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.schoolInfo -> makeCurrentFragment(schoolFragment)
                R.id.personalInfo -> makeCurrentFragment(personalFragment)
                R.id.studyMaterial -> makeCurrentFragment(materialFragment)
                R.id.classMember -> makeCurrentFragment(memberFragment)
            }
            true
        }

        // Class Header Dropdown
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_class_detail_header, null)

        mClassAdapter = MainClassDetailAdapter(mClassList)
        view.rvBottomSheetClassDetailHeader.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mClassAdapter
        }
        mClassAdapter.notifyDataSetChanged()

        val uid = sharePref.getString("uid", "") as String
        db.collection("classes").whereArrayContains("class_teacher_ids", uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.e(error)
                    return@addSnapshotListener
                }

                mClassList.clear()
                for (document in value!!.documents) {

                    if (classId != document.id) {
                        mClassList.add(
                            ChooseClassDao(
                                document["class_grade"] as String,
                                document["class_name"] as String,
                                document["class_teacher.name"] as String,
                                (document["class_bg"] as Long).toInt(),
                                Color.parseColor(document["class_theme"] as String),
                                document.id
                            )
                        )
                    } else {
                        mClassList.add(
                            ChooseClassDao(
                                document["class_grade"] as String,
                                document["class_name"] as String,
                                document["class_teacher.name"] as String,
                                (document["class_bg"] as Long).toInt(),
                                Color.parseColor(document["class_theme"] as String),
                                document.id,
                                true
                            )
                        )
                    }
                    view.rvBottomSheetClassDetailHeader.adapter?.notifyDataSetChanged()
                }
            }

        bottomSheetDialog.setContentView(view)

        tvClassHeaderClassName.setOnClickListener {
            bottomSheetDialog.show()
        }
    }

    // Move fragment
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flClassDetail, fragment)
            commit()
        }

    fun popupMenuInfo(id: String, pos: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_school_announcement, null)
        bottomSheetDialog.setContentView(view)

        if (id == "1") { // School Schedule
            view.tvEditInfo.text = "Edit Jadwal"
            view.tvDeleteInfo.text = "Hapus Jadwal"
        } else if (id == "2") { // School Photo
            view.tvEditInfo.text = "Edit Informasi Foto"
            view.tvDeleteInfo.text = "Hapus Foto"
        } else if (id == "3") { // Personal Note
            view.tvEditInfo.text = "Edit Catatan"
            view.tvDeleteInfo.text = "Hapus Catatan"
        } else if (id == "4") { // Material Subject FRAGMENT
            view.tvEditInfo.text = "Edit Materi"
            view.tvDeleteInfo.text = "Hapus Materi"
        } else if (!isAnnouncement) { // Personal Note FRAGMENT
            view.tvEditInfo.text = "Edit Catatan"
            view.tvDeleteInfo.text = "Hapus Catatan"
        }

        view.tvEditInfo.setOnClickListener {
            bottomSheetDialog.dismiss()

            when (id) {
                "0" -> {
                    intent = Intent(this, SchoolAnnouncementEditActivity::class.java)
                    intent.putExtra("pos", pos)
                }
                "1" -> {
                    intent = Intent(this, SchoolScheduleEditActivity::class.java)
                    intent.putExtra("pos", pos)
                }
                "2" -> {
                    intent = Intent(this, SchoolPhotoEditActivity::class.java)
                    intent.putExtra("pos", pos)
                }
                "3" -> {
                    intent = Intent(this, PersonalNoteEditActivity::class.java)
                    intent.putExtra("pos", pos)
                }
                "4" -> {
                    intent = Intent(this, MaterialEducationEditActivity::class.java)
                    intent.putExtra("pos", pos)
                }
                else -> {
                    if (bnClassDetail.selectedItemId == R.id.studyMaterial) {
                        intent = Intent(this, MaterialSubjectAddActivity::class.java)
                        intent.putExtra("sectionId", id)
                        intent.putExtra("layout", "educationEdit")
                    } else {
                        intent = Intent(this, PersonalNoteEditActivity::class.java)
                        intent.putExtra("noteId", id)
                    }
                }
            }

            startActivity(intent)
        }

        view.tvDeleteInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainClassDetailActivity)
            var alert = "pengumuman"

            if (id == "1") {
                alert = "jadwal"
            } else if (id == "3") {
                alert = "catatan"
            } else if (!isAnnouncement) {
                alert = "catatan"
            }

            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus $alert ini?")

            builder.setNegativeButton("BATALKAN") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { _, _ ->
                when (id) {
                    "0" -> { // School announcement
                        DataDummy.announcementData.removeAt(pos)

//                        mAnnouncementAdapter = SchoolAnnouncementAdapter(mAnnouncementList, this)
                        mAnnouncementAdapter.notifyDataSetChanged()

                        loading_main.visibility = View.VISIBLE
                        Handler().postDelayed({
                            loading_main.visibility = View.GONE
                        }, 1000)
                    }
                    "1" -> {
                        DataDummy.scheduleData.removeAt(pos)

                        mScheduleAdapter = SchoolScheduleAdapter(mScheduleList, this)
                        mScheduleAdapter.notifyDataSetChanged()

                        loading_main.visibility = View.VISIBLE
                        Handler().postDelayed({
                            loading_main.visibility = View.GONE
                        }, 1000)
                    }
                    "2" -> {
                        DataDummy.photoData.removeAt(pos)

                        mPhotoAdapter = SchoolPhotoAdapter(mPhotoList, this)
                        mPhotoAdapter.notifyDataSetChanged()

                        loading_main.visibility = View.VISIBLE
                        Handler().postDelayed({
                            loading_main.visibility = View.GONE
                        }, 1000)
                    }
                    "3" -> { // Note
//                        DataDummy.noteSentData.removeAt(pos)
//
////                        mNoteAdapter =
////                            PersonalNoteAdapter(mNoteList, 1, this, PersonalSentNoteFragment())
////                        mNoteAdapter.notifyDataSetChanged()
//
//                        loading_main.visibility = View.VISIBLE
//                        Handler().postDelayed({
//                            loading_main.visibility = View.GONE
//                        }, 1000)
                    }
                    "4" -> { // Note
                        DataDummy.educationData.removeAt(pos)

//                        mEducationAdapter = SchoolAnnouncementAdapter(
//                            mEducationList,
//                            this,
//                            MaterialEducationFragment()
//                        )
                        mEducationAdapter.notifyDataSetChanged()

                        loading_main.visibility = View.VISIBLE
                        Handler().postDelayed({
                            loading_main.visibility = View.GONE
                        }, 1000)
                    }
                    else -> {
                        if (bnClassDetail.selectedItemId == R.id.studyMaterial) {
                            // Delete files collection
                            val collectionFiles =
                                db.collection("material_education").document(id)
                                    .collection("files")
                            deleteCollection(collectionFiles, 5) {}

                            // Delete the material
                            db.collection("material_education").document(id).delete()
                                .addOnSuccessListener {
                                    Log.d("MaterialEducation", "deleted successfully")
                                }.addOnFailureListener {
                                Log.d("MaterialEducation", "fail to delete")
                            }
                        } else {
                            // Delete comments collection
                            val collectionComments =
                                db.collection("notes").document(id).collection("comments")
                            deleteCollection(collectionComments, 5) {}

                            // Delete the note
                            db.collection("notes").document(id).delete().addOnSuccessListener {
                                Log.d("PersonalNote", "deleted successfully")
                            }.addOnFailureListener {
                                Log.d("PersonalNote", "fail to delete")
                            }
                        }
                    }
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

    fun popupMenuDelete(id: String, pos: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_choose_class, null)
        bottomSheetDialog.setContentView(view)

        view.tvDeleteClass.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainClassDetailActivity)
            bottomSheetDialog.dismiss()

            builder.setMessage("Apakah Anda yakin ingin menghapus catatan ini?")

            builder.setNegativeButton("BATALKAN") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setPositiveButton("HAPUS") { _, _ ->
                DataDummy.noteAcceptedData.removeAt(pos)

//                mNoteAdapter =
//                    PersonalNoteAdapter(mNoteList, 0, this, PersonalAcceptedNoteFragment())
//                mNoteAdapter.notifyDataSetChanged()

                loading_main.visibility = View.VISIBLE
                Handler().postDelayed({
                    loading_main.visibility = View.GONE
                }, 1000)
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

        view.tvCancelClass.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    fun deleteCollection(collection: CollectionReference, batchSize: Int, nextAction: () -> Unit) {
        try {
            // Retrieve a small batch of documents to avoid out-of-memory errors/
            var deleted = 0
            collection
                .limit(batchSize.toLong())
                .get()
                .addOnCompleteListener {
                    for (document in it.result!!.documents) {
                        document.getReference().delete()
                        ++deleted
                    }
                    if (deleted >= batchSize) {
                        // retrieve and delete another batch
                        deleteCollection(collection, batchSize, nextAction)
                    } else {
                        nextAction()
                    }
                }
        } catch (e: Exception) {
            System.err.println("Error deleting collection : " + e.message)
        }
    }

    override fun onMoreClicked(id: String, pos: Int) {
        if (id == "5") popupMenuDelete(id, pos)
        else popupMenuInfo(id, pos)
    }
}
