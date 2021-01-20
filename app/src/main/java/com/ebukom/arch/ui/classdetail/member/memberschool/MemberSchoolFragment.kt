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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_member_school.*

class MemberSchoolFragment : Fragment() {
    private val mContactList: ArrayList<ClassDetailMemberContactDao> = arrayListOf()
    private lateinit var mContactAdapter: MemberContactAdapter
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_school, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db.collection("users").get().addOnSuccessListener {
            for (data in it.documents) {
                if ((data["level"] as Long).toInt() == 0) {
                    mContactList.add(
                        ClassDetailMemberContactDao(
                            data["name"] as String,
                            data["role.className"] as String,
                            data["profilePic"] as String,
                            data["phone"] as String
                        )
                    )
                }
            }
            mContactAdapter.notifyDataSetChanged()
        }
        addData()
        initRecycler()
    }

    private fun initRecycler() {
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
        mContactList.add(
            ClassDetailMemberContactDao(
                "Ratna Hendrawati",
                "Guru Kelas 1 Krypton",
                "https://pfpmaker.com/img/profile-3-1.3e702c5b.png"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Ahmad Juliansyah",
                "Guru Kelas 2 Helium",
                "https://images.unsplash.com/photo-1511367461989-f85a21fda167?ixid=MXwxMjA3fDB8MHxzZWFyY2h8M3x8cHJvZmlsZXxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Julia Isma",
                "Guru Kelas 3 Aurum",
                "https://qph.fs.quoracdn.net/main-qimg-217015358349186e0e382cb15c5d7c63"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Putri Eka",
                "Guru Kelas 2 Neon",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQc7Vjvt50mbNpWd6KbYcEPxCBNo8a6SXXjAw&usqp=CAU"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Sindi Putri",
                "Guru Kelas 1 Argon",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_okExtoR2sDSDSSlQ8pe8iw6euOoRuW1VZg&usqp=CAU"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Yulianti Puspita",
                "Guru Kelas 2 Titanium",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzeppCoxZPiVk60lL_9s7zbbzfwAKp0aLLKQ&usqp=CAU"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Dewi Putri",
                "Guru Kelas 3 Selenium",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSkkOmIoxwj2NTyX8Tod3xjtHyw4GADqPs-w&usqp=CAU"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Gigi Rahma",
                "Guru Kelas 3 Argentum",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNCiOBj3q6IRinqtqakJ1KbCFezayJzdVcJA&usqp=CAU"
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
