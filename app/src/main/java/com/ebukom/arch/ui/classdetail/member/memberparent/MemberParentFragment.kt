package com.ebukom.arch.ui.classdetail.member.memberparent

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailMemberContactDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.member.MemberContactAdapter
import com.ebukom.arch.ui.classdetail.personal.personalnotenew.PersonalNoteAdapter
import kotlinx.android.synthetic.main.fragment_member_parent.*
import kotlinx.android.synthetic.main.fragment_personal_accepted_note.*

class MemberParentFragment : Fragment() {
    var objectList = ArrayList<ClassDetailMemberContactDao>()
    lateinit var memberContactAdapter: MemberContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_parent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        memberContactAdapter =
            MemberContactAdapter(
                objectList,
                callback
            )
//        schoolAnnouncementAdapter.announcements = objectList
        rvMemberParent.layoutManager = LinearLayoutManager(this.context)
        rvMemberParent.adapter = memberContactAdapter

//        if (objectList.isNotEmpty()) {
//            view.ivPersonalEmpty.visibility = View.INVISIBLE
//            view.tvPersonalEmpty.visibility = View.INVISIBLE
//        } else {
//            view.ivPersonalEmpty.visibility = View.VISIBLE
//            view.tvPersonalEmpty.visibility = View.VISIBLE
//        }
    }

    private fun addData() {
        for (i in 0..10) {
            objectList.add(
                ClassDetailMemberContactDao(
                    "Eni Trikuswanti",
                    "Julian Akbar",
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
