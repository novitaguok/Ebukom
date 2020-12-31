package com.ebukom.arch.ui.classdetail.personal.personalacceptednote

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.personal.PersonalNoteAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_personal_accepted_note.*
import kotlinx.android.synthetic.main.fragment_personal_accepted_note.view.*
import timber.log.Timber

class PersonalAcceptedNoteFragment : Fragment() {
    private val mPersonalNoteList: ArrayList<ClassDetailPersonalNoteDao> = arrayListOf()
    lateinit var mPersonalNoteAdapter: PersonalNoteAdapter
    val db = FirebaseFirestore.getInstance()
    var nm: String = ""
    var lev: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_accepted_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val sharePref: SharedPreferences =
            (context as MainClassDetailActivity).getSharedPreferences(
                "EBUKOM",
                Context.MODE_PRIVATE
            )
        val level = sharePref.getLong("level", 0)
        val uid = sharePref.getString("uid", "")
        val name = sharePref.getString("name", "")
        lev = level
        if (name != null) {
            nm = name
        }

        initRecycler()
        checkEmpty(view)

        if (level == 0L) {
            db.collection("notes").whereArrayContains("parent_ids", uid!!)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    initRecycler()
                    mPersonalNoteList.clear()
                    for (document in value!!.documents) {
                        val listUserIds = document["parent_ids"] as List<String>
                        if (listUserIds.size == 1) {
                            val data = ClassDetailPersonalNoteDao(
                                0,
                                "",
                                document["noteContent"] as String,
                                arrayListOf(),
                                document["time"] as String,
                                arrayListOf()
                            )

                            db.collection("users").document(listUserIds[0]).get()
                                .addOnSuccessListener { user ->
                                    Log.d("DEBUG", "onViewCreated: " + user["name"].toString())
                                    if (user != null) {
                                        data.noteTitle = user.get("name") as String
                                        data.profilePicture = 0
                                        mPersonalNoteList.add(data)

                                        mPersonalNoteAdapter.notifyDataSetChanged()
                                        checkEmpty(view)
                                    }
                                }
                        }
                    }
                }
        } else {
            db.collection("notes").whereArrayContains("teacher_ids", uid!!)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    for (document in value!!.documents) {
                        val listUserIds = document["teacher_ids"] as List<String>
                        if (listUserIds.size == 1) {
                            val data = ClassDetailPersonalNoteDao(
                                0,
                                "",
                                document["noteContent"] as String,
                                arrayListOf(),
                                document["time"] as String,
                                arrayListOf()
                            )

                            db.collection("users").document(listUserIds[0]).get()
                                .addOnSuccessListener { user ->
                                    Log.d("DEBUG", "onViewCreated: " + user["name"].toString())
                                    if (user != null) {
                                        data.noteTitle = user.get("name") as String
                                        data.profilePicture = 0
                                        mPersonalNoteList.add(data)

                                        mPersonalNoteAdapter.notifyDataSetChanged()
                                        checkEmpty(view)
                                    }
                                }
                        }
                    }
                }
        }

//        mPersonalNoteList.clear()
//        mPersonalNoteList.addAll(DataDummy.noteAcceptedData)
//        mPersonalNoteAdapter.notifyDataSetChanged()
    }

    private fun initRecycler() {
        mPersonalNoteAdapter =
            PersonalNoteAdapter(mPersonalNoteList, 0, callback, PersonalAcceptedNoteFragment())
        rvPersonalAcceptedNote.apply {
            layoutManager =
                LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = mPersonalNoteAdapter
        }
        mPersonalNoteAdapter.notifyDataSetChanged()
        checkEmpty(view!!)
    }

    private fun checkEmpty(view: View) {
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

