package com.ebukom.arch.ui.classdetail.personal.personalacceptednote

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
import com.ebukom.data.DataDummy
import com.ebukom.data.buildParentNoteDummy
import kotlinx.android.synthetic.main.fragment_personal_accepted_note.*
import kotlinx.android.synthetic.main.fragment_personal_accepted_note.view.*

class PersonalAcceptedNoteFragment : Fragment() {
    private val mPersonalNoteList: ArrayList<ClassDetailPersonalNoteDao> = arrayListOf()
    lateinit var mPersonalNoteAdapter: PersonalNoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_accepted_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPersonalNoteAdapter = PersonalNoteAdapter(mPersonalNoteList, 0, callback, PersonalAcceptedNoteFragment())
        rvPersonalAcceptedNote.apply {
            layoutManager =
                LinearLayoutManager(
                    this.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mPersonalNoteAdapter
        }

        mPersonalNoteList.clear()
        mPersonalNoteList.addAll(DataDummy.noteAcceptedData)
        mPersonalNoteAdapter.notifyDataSetChanged()

        checkEmptyNote(view)
    }

    private fun checkEmptyNote(view: View) {
        if (mPersonalNoteList.isNotEmpty()) {
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
}

