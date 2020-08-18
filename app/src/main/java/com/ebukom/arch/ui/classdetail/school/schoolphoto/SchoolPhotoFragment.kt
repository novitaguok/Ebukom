package com.ebukom.arch.ui.classdetail.school.schoolphoto

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
import com.ebukom.arch.dao.ClassDetailPhotoDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.schoolphoto.schoolphotonew.SchoolPhotoNewActivity
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.fragment_school_photo.*
import kotlinx.android.synthetic.main.fragment_school_photo.view.*
import java.util.*
import kotlin.collections.ArrayList

class SchoolPhotoFragment : Fragment() {
    private val mPhotoList: ArrayList<ClassDetailPhotoDao> = arrayListOf()
    lateinit var mPhotoAdapter: SchoolPhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_school_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharePref: SharedPreferences = activity!!.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getInt("level", 0) == 1){
            btnSchoolPhotoNew.visibility = View.GONE
        }

        mPhotoAdapter = SchoolPhotoAdapter(mPhotoList, callback)
        mPhotoList.clear()
        mPhotoList.addAll(DataDummy.photoData)
        mPhotoAdapter.notifyDataSetChanged()

        // Photo List
        rvSchoolPhoto.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mPhotoAdapter
        }

        checkPhotoEmpty()

        // "Bagikan Foto Kegiatan" button
        btnSchoolPhotoNew.setOnClickListener {
            (context as MainClassDetailActivity).startActivity(
                Intent(
                    (context as MainClassDetailActivity),
                    SchoolPhotoNewActivity::class.java
                )
            )
        }
    }

    private fun checkPhotoEmpty() {
        if (mPhotoList.isNotEmpty()) {
            ivSchoolPhotoEmpty.visibility = View.GONE
            tvSchoolPhotoEmpty.visibility = View.GONE
        } else {
            ivSchoolPhotoEmpty.visibility = View.VISIBLE
            tvSchoolPhotoEmpty.visibility = View.VISIBLE
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

    override fun onResume() {
        super.onResume()

        // Photo List
        mPhotoList.clear()
        mPhotoList.addAll(DataDummy.photoData)
        mPhotoAdapter.notifyDataSetChanged()

        checkPhotoEmpty()
    }
}
