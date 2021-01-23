package com.ebukom.arch.ui.classdetail.personal.personalsentnote

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
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.personal.PersonalNoteAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_personal_note_new.*
import kotlinx.android.synthetic.main.fragment_personal_sent_note.*
import kotlinx.android.synthetic.main.fragment_personal_sent_note.view.*
import timber.log.Timber

class PersonalSentNoteFragment : Fragment() {
    private val mNoteList: ArrayList<ClassDetailPersonalNoteDao> = arrayListOf()
    lateinit var mNoteAdapter: PersonalNoteAdapter
    val db = FirebaseFirestore.getInstance()
    var nm: String = ""
    var lev: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_sent_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler(view)

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

        initRecycler(view)
        checkEmpty(view)

        if (level == 0L) {
            db.collection("notes").whereArrayContains("teacher_ids", uid!!)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    mNoteList.clear()
                    for (document in value!!.documents) {
                        val listUserIds = document["parent_ids"] as List<String>
                        listUserIds.forEach {
                            val data = ClassDetailPersonalNoteDao(
                                0,
                                "",
                                document["content"] as String,
                                arrayListOf(),
                                document["time"] as String,
                                arrayListOf(),
                                document.id
                            )

                            db.collection("notes").document(document.id).collection("comments")
                                .addSnapshotListener { value, error ->
                                    data.comments.clear()
                                    for (document in value!!.documents) {
                                        data.comments.add(
                                            ClassDetailAnnouncementCommentDao(
                                                document["user.name"] as String,
                                                document["comment"] as String,
                                                R.drawable.bg_square_blue_4dp,
                                                (document["upload_time"] as Timestamp).toDate()
                                                    .toString(),
                                                document.id
                                            )
                                        )
                                    }
                                    mNoteAdapter.notifyDataSetChanged()
                                }
//                                .addOnSuccessListener {
//                                }

                            db.collection("users").document(it).get()
                                .addOnSuccessListener { user ->
                                    Log.d("DEBUG", "onViewCreated: " + user["name"].toString())
                                    if (user != null) {
                                        data.noteTitle = user.get("name") as String
                                        data.profilePicture = 0
                                        mNoteList.add(data)
                                        mNoteAdapter.notifyDataSetChanged()
                                        checkEmpty(view!!)
                                    }
                                }
                        }
                    }
                }
        } else {
            db.collection("notes").whereArrayContains("parent_ids", uid!!)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Timber.e(error)
                        return@addSnapshotListener
                    }

                    mNoteList.clear()
                    for (document in value!!.documents) {
                        val listUserIds = document["parent_ids"] as List<String>
                        if (listUserIds.size == 1) {
                            val data = ClassDetailPersonalNoteDao(
                                0,
                                "",
                                document["content"] as String,
                                arrayListOf(),
                                document["time"] as String,
                                arrayListOf(),
                                document.id
                            )

                            db.collection("users").document(listUserIds[0]).get()
                                .addOnSuccessListener { user ->
                                    Log.d("DEBUG", "onViewCreated: " + user["name"].toString())
                                    if (user != null) {
                                        data.noteTitle = user.get("name") as String
                                        data.profilePicture = 0
                                        mNoteList.add(data)
                                        mNoteAdapter.notifyDataSetChanged()
                                        checkEmpty(view)
                                    }
                                }
                        }
                    }
                }
        }
    }

    private fun initRecycler(view: View) {
        /**
         * Note list
         */
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
        mNoteAdapter.notifyDataSetChanged()
        checkEmpty(view)
    }

    private fun checkEmpty(view: View) {
        /**
         * Check if sent note is empty
         */
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
}
