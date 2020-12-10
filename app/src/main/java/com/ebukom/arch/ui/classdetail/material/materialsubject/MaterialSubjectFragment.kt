package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMaterialSubjectDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectadd.MaterialSubjectRecapActivity
import com.ebukom.arch.ui.classdetail.material.materialsubject.materialsubjectnew.MaterialSubjectNewActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_material_subject.*
import kotlinx.android.synthetic.main.fragment_material_subject.view.*
import timber.log.Timber

class MaterialSubjectFragment : Fragment() {

    private val mSubjectList: ArrayList<ClassDetailMaterialSubjectDao> = arrayListOf()
    lateinit var mSubjectAdapter: MaterialSubjectAdapter
    val db = FirebaseFirestore.getInstance()
    var classId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        classId = MainClassDetailActivity.CLASS_ID

        /**
         * Open rekap pembelajaran online card
         */
//        cvMaterialSubjectRecap.setOnClickListener {
//            val intent = Intent(context, MaterialSubjectNewActivity::class.java)
//            intent.putExtra("subject", "recap")
//            startActivity(intent)
//        }

        /**
         * Get all subjects
         */
        db.collection("material_subjects").addSnapshotListener { value, error ->
            if (error != null) {
                Timber.e(error)
                return@addSnapshotListener
            }

            initRecycler(view)

            for (document in value!!.documents) {

                mSubjectList.add(
                    ClassDetailMaterialSubjectDao(
                        document["material_name"] as String,
                        (document["material_bg"] as Long).toInt(),
                        (document["order_number"] as Long).toInt(),
                        document.id,
                        classId
                    )
                )
            }

            mSubjectList.sortBy {
                it.order
            }
        }
        
    }

    private fun initRecycler(view: View) {
        mSubjectAdapter = MaterialSubjectAdapter(mSubjectList)
        mSubjectList.clear()
        view.rvMaterialSubject.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mSubjectAdapter
        }
    }
}