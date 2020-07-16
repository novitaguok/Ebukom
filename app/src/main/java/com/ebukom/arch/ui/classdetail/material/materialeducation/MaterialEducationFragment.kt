package com.ebukom.arch.ui.classdetail.material.materialeducation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.personal.PersonalNoteAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter
import kotlinx.android.synthetic.main.fragment_material_education.*
import kotlinx.android.synthetic.main.fragment_personal_accepted_note.*
import kotlinx.android.synthetic.main.fragment_school_announcement.*
import kotlinx.android.synthetic.main.item_announcement.*
import kotlinx.android.synthetic.main.item_announcement.view.*
import java.lang.ClassCastException

class MaterialEducationFragment : Fragment() {
    var objectList = ArrayList<ClassDetailAnnouncementDao>()
    lateinit var schoolAnnouncementAdapter: SchoolAnnouncementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material_education, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        schoolAnnouncementAdapter = SchoolAnnouncementAdapter(objectList, callback)
//        schoolAnnouncementAdapter.announcements = objectList
        rvMaterialEducation.layoutManager = LinearLayoutManager(this.context)
        rvMaterialEducation.adapter = schoolAnnouncementAdapter

//        view.tvAnnouncementComment.setTextColor(Color.parseColor("#7A7A7A"))
//        view.tvAnnouncementComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_12))
    }

    private fun addData() {
        for (i in 0..10) {

            objectList.add(
                ClassDetailAnnouncementDao(
                    "Mendidik Anak Hyperaktif",
                    "Untuk mendidk anak yang hyperaktif, diperlukan suatu kemampuan aitu kesabaran yang luar biasa. Selain itu perlu diketahui juga cara yang...",
                    "Eni Trikuswanti",
                    "02/02/02"
                )
            )
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
}