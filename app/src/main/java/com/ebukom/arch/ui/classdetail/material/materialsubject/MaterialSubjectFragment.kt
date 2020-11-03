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
import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.AdminShareSchoolFeeInfoAdapter
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent.ParentSchoolFeeInfoAdapter
import com.ebukom.arch.ui.chooseclass.ChooseClassAdapter
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import kotlinx.android.synthetic.main.fragment_material_subject.view.*

class MaterialSubjectFragment : Fragment() {

    private val mSubjectList: ArrayList<ChooseClassDao> = arrayListOf()
    lateinit var mSubjectAdapter: MaterialSubjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val intent = Intent((context as MainClassDetailActivity), MaterialSubjectNewActivity::class.java)

//        view.ivMaterialSubjectBlue.setOnClickListener {
//            intent.putExtra("material", "0")
//            (context as MainClassDetailActivity).startActivity(intent)
//        }
//        view.ivMaterialSubjectGreen.setOnClickListener {
//            intent.putExtra("material", "1")
//            (context as MainClassDetailActivity).startActivity(intent)
//        }
//        view.ivMaterialSubjectRed.setOnClickListener {
//            intent.putExtra("material", "2")
//            (context as MainClassDetailActivity).startActivity(intent)
//        }

        mSubjectAdapter = MaterialSubjectAdapter(mSubjectList)
        view.rvMaterialSubject.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mSubjectAdapter
        }

        mSubjectList.add(ChooseClassDao(null, "Rekap Pembelajaran\nOnline", null, R.drawable.bg_subject_recap, null))
        mSubjectList.add(ChooseClassDao(null, "Matematika", null, R.drawable.bg_subject_math, null))
        mSubjectList.add(ChooseClassDao(null, "Bahasa Indonesia", null, R.drawable.bg_subject_indo, null))
        mSubjectList.add(ChooseClassDao(null, "Art", null, R.drawable.bg_subject_art, null))
        mSubjectList.add(ChooseClassDao(null, "Literasi", null, R.drawable.bg_subject_literature, null))
        mSubjectList.add(ChooseClassDao(null, "Agama", null, R.drawable.bg_subject_religion, null))
        mSubjectList.add(ChooseClassDao(null, "PPKn", null, R.drawable.bg_subject_pkn, null))
        mSubjectList.add(ChooseClassDao(null, "Musik", null, R.drawable.bg_subject_music, null))
        mSubjectList.add(ChooseClassDao(null, "PLH", null, R.drawable.bg_subject_plh, null))
    }
}