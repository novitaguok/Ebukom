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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_personal_accepted_note.*
import kotlinx.android.synthetic.main.fragment_personal_accepted_note.view.*

class PersonalAcceptedNoteFragment : Fragment() {
    var objectList = ArrayList<ClassDetailPersonalNoteDao>()
    lateinit var personalNoteAdapter: PersonalNoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_accepted_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        personalNoteAdapter =
            PersonalNoteAdapter(
                objectList,
                callback
            )
        rvPersonalAcceptedNote.layoutManager = LinearLayoutManager(this.context)
        rvPersonalAcceptedNote.adapter = personalNoteAdapter

        if (objectList.isNotEmpty()) {
            view.ivPersonalEmpty.visibility = View.INVISIBLE
            view.tvPersonalEmpty.visibility = View.INVISIBLE
        } else {
            view.ivPersonalEmpty.visibility = View.VISIBLE
            view.tvPersonalEmpty.visibility = View.VISIBLE
        }
    }

    private fun addData() {
        for (i in 0..10) {
            objectList.add(
                ClassDetailPersonalNoteDao(
                    R.drawable.bg_solid_gray,
                    "Eni Trikuswanti",
                    "Besok akan dilaksanakan kegiatan pentas seni. Orang tua dimohon untuk mempersiapkan peralatan di bawah ini. Tolong diperhatikan ya Ibu...",
                    "1 KOMENTAR",
                    "12.00 - Senin, 12 Maret 2020"
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

