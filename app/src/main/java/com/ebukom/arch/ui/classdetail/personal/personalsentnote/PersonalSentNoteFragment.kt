package com.ebukom.arch.ui.classdetail.personal.personalsentnote

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.personal.personalnotenew.PersonalNoteAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter
import com.ebukom.data.DataDummy
import kotlinx.android.synthetic.main.fragment_personal_sent_note.*
import kotlinx.android.synthetic.main.fragment_personal_sent_note.view.*
import kotlinx.android.synthetic.main.fragment_school_announcement.*

class PersonalSentNoteFragment : Fragment() {
    private val mNoteList: ArrayList<ClassDetailPersonalNoteDao> = arrayListOf()
    lateinit var mNoteAdapter: PersonalNoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_sent_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Note List
        mNoteAdapter = PersonalNoteAdapter(mNoteList, 1, callback, PersonalSentNoteFragment())
        rvPersonalSentNote.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mNoteAdapter
        }
//        mNoteList.addAll(DataDummy.noteSentData)
//        mNoteAdapter.notifyDataSetChanged()

        checkNoteEmpty(view)
    }

    private fun checkNoteEmpty(view: View) {
        if (mNoteList.isNotEmpty()) {
            view.ivPersonalEmpty.visibility = View.GONE
            view.tvPersonalEmpty.visibility = View.GONE
        } else {
            view.ivPersonalEmpty.visibility = View.VISIBLE
            view.tvPersonalEmpty.visibility = View.VISIBLE
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

        // Announcement List
        mNoteList.clear()
        mNoteList.addAll(DataDummy.noteSentData)
        mNoteAdapter.notifyDataSetChanged()

        checkNoteEmpty(view!!)
    }
}
