package com.ebukom.arch.ui.classdetail.school.schoolphoto

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.fragment_school_photo.*
import kotlinx.android.synthetic.main.fragment_school_photo.view.*

class SchoolPhotoFragment : Fragment() {
    var objectList = ArrayList<ClassDetailPhotoDao>()
    lateinit var schoolPhotoAdapter: SchoolPhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_school_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        schoolPhotoAdapter = SchoolPhotoAdapter(objectList, callback)
        rvSchoolPhoto.layoutManager = LinearLayoutManager(this.context)
        rvSchoolPhoto.adapter = schoolPhotoAdapter

        if (!objectList.isEmpty()) {
            view.ivSchoolPhotoEmpty.visibility = View.INVISIBLE
        } else {
            view.ivSchoolPhotoEmpty.visibility = View.VISIBLE
        }
    }

    private fun addData() {
        objectList.add(
            ClassDetailPhotoDao(
                "Study Tour\nBandung 2020",
                0
            )
        )
        objectList.add(
            ClassDetailPhotoDao(
                "Study Tour\nYogya 2020",
                1
            )
        )
        objectList.add(
            ClassDetailPhotoDao(
                "Study Tour\nBandung 2020",
                0
            )
        )
        objectList.add(
            ClassDetailPhotoDao(
                "Study Tour\nYogya 2020",
                1
            )
        )
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
