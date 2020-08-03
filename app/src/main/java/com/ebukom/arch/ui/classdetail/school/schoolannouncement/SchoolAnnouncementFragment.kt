package com.ebukom.arch.ui.classdetail.school.schoolannouncement

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew.SchoolAnnouncementNewActivity
import kotlinx.android.synthetic.main.activity_school_announcement_edit.*
import kotlinx.android.synthetic.main.fragment_school_announcement.*
import java.lang.ClassCastException

class SchoolAnnouncementFragment : androidx.fragment.app.Fragment() {

    var objectList = ArrayList<ClassDetailAnnouncementDao>()
    lateinit var schoolAnnouncementAdapter : SchoolAnnouncementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_school_announcement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharePref: SharedPreferences = activity!!.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getInt("level", 0) == 1){
            btnSchoolAnnouncementNew.visibility = View.GONE
        }

        addData()
        schoolAnnouncementAdapter = SchoolAnnouncementAdapter(objectList, callback)
        rvSchoolAnnouncement.layoutManager = LinearLayoutManager(this.context)
        rvSchoolAnnouncement.adapter = schoolAnnouncementAdapter

        btnSchoolAnnouncementNew.setOnClickListener {
            (context as MainClassDetailActivity).startActivity(Intent((context as MainClassDetailActivity), SchoolAnnouncementNewActivity::class.java))
        }
    }

    private fun addData() {
        for (i in 0..10) {
            objectList.add(
                ClassDetailAnnouncementDao(
                    "Kegiatan Pentas Seni",
                    "Besok akan dilaksanakan kegiatan pentas seni. Orang tua dimohon untuk mempersiapkan peralatan di bawah ini. Tolong diperhatikan ya Ibu.",
                    "9 KOMENTAR",
                    "02/02/02"
                )
            )
        }
    }

    lateinit var callback : OnMoreCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as OnMoreCallback
        } catch (e : ClassCastException){
            throw ClassCastException(activity.toString()
                    + " must implement MyInterface ");
        }
    }

    override fun onResume() {
        super.onResume()
        MainClassDetailActivity.isAnnouncement = true
    }
}
