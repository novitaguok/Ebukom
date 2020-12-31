package com.ebukom.arch.ui.classdetail.member.memberschool

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMemberContactDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.member.MemberContactAdapter
import kotlinx.android.synthetic.main.fragment_member_school.*

class MemberSchoolFragment : Fragment() {
    private val mContactList: ArrayList<ClassDetailMemberContactDao> = arrayListOf()
    private lateinit var mContactAdapter: MemberContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_school, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        mContactAdapter = MemberContactAdapter(mContactList, callback, activity!!)
        rvMemberSchool.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mContactAdapter
        }
    }

    private fun addData() {
        for (i in 0..10) {
            mContactList.add(
                ClassDetailMemberContactDao(
                    "Eni Trikuswanti",
                    "Guru Kelas 1 Aurora",
                    R.drawable.bg_solid_gray
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
