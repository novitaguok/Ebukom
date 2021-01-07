package com.ebukom.arch.ui.classdetail.material.materialeducation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailMaterialSubjectSectionDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.material.materialeducation.materialeducationnew.MaterialEducationNewActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd.MaterialSubjectAddActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectnew.MaterialSubjectSectionAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter
import com.ebukom.data.DataDummy
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_material_education.*
import kotlinx.android.synthetic.main.fragment_personal_sent_note.*
import timber.log.Timber
import java.lang.ClassCastException

class MaterialEducationFragment : Fragment() {
    private val mPersonalEducationList: ArrayList<ClassDetailMaterialSubjectSectionDao> = arrayListOf()
    lateinit var mPersonalEducationAdapter: MaterialSubjectSectionAdapter
    var classId: String? = null
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material_education, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Shared Preference
        val sharePref: SharedPreferences = activity!!.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getLong("level", 0) == 1L){
            btnMaterialEducationNew.visibility = View.GONE
        }

        classId = MainClassDetailActivity.CLASS_ID
        initRecycler()
        checkEducationEmpty()

        btnMaterialEducationNew.setOnClickListener {
            val intent = Intent((context as MainClassDetailActivity), MaterialSubjectAddActivity::class.java)
            intent.putExtra("layout", "education")
            (context as MainClassDetailActivity).startActivity(intent)
        }

        if (classId != null) {
            db.collection("material_education").addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    initRecycler()

                    if (value!!.documents != null) {
                        mPersonalEducationList.clear()
                        for (document in value.documents) {
                            mPersonalEducationList.add(
                                ClassDetailMaterialSubjectSectionDao(
                                    document["name"] as String,
                                    arrayListOf(),
                                    (document["date"] as Timestamp).toDate().toString(),
                                    document.id,
                                    "",
                                    classId!!,
                                    (document["date"] as Timestamp)
                                )
                            )
                        }
                        mPersonalEducationAdapter.notifyDataSetChanged()
                        checkEmpty()
                    }
                }
        }
    }

    private fun checkEducationEmpty() {
        if (mPersonalEducationList.isEmpty()) {
            ivMaterialEducationEmpty.visibility = View.VISIBLE
            tvMaterialEducationEmpty.visibility = View.VISIBLE
        } else {
            ivMaterialEducationEmpty.visibility = View.GONE
            tvMaterialEducationEmpty.visibility = View.GONE
        }
    }

    lateinit var callback: OnMoreCallback
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as OnMoreCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString()
                        + " must implement MyInterface "
            );
        }
    }

    private fun initRecycler() {
        /**
         * Section list
         */
//        mMaterialSubjectList.clear()
        mPersonalEducationAdapter = MaterialSubjectSectionAdapter(mPersonalEducationList, context as MainClassDetailActivity)
        rvMaterialEducation.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mPersonalEducationAdapter
        }

        checkEmpty()
    }

    private fun checkEmpty() {
        if (mPersonalEducationList.isEmpty()) {
            ivMaterialEducationEmpty.visibility = View.VISIBLE
            tvMaterialEducationEmpty.visibility = View.VISIBLE
        } else {
            ivMaterialEducationEmpty.visibility = View.GONE
            tvMaterialEducationEmpty.visibility = View.GONE
        }
    }
}