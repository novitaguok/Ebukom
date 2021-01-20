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
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.member.MemberContactAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_member_parent.*

class MemberParentFragment : Fragment() {
    private val mContactList: ArrayList<ClassDetailMemberContactDao> = arrayListOf()
    private lateinit var mContactAdapter: MemberContactAdapter
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_parent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initRecycler()
        db.collection("users").get().addOnSuccessListener {
            for (data in it.documents) {
                if ((data["level"] as Long).toInt() == 1) {
                    mContactList.add(
                        ClassDetailMemberContactDao(
                            data["name"] as String,
                            data["child"] as String,
                            data["profilePic"] as String,
                            data["phone"] as String
                        )
                    )
                }
            }
            mContactAdapter.notifyDataSetChanged()
        }
        addData()
    }

    private fun initRecycler() {
        mContactAdapter = MemberContactAdapter(mContactList, callback, activity!!)
        rvMemberParent.apply {
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
                "Ade Andreansyah",
                "Julian Akbar",
                "https://cutewallpaper.org/21/anime-profile-pictures-boy/cartoon-and-anime-profile-pics-toon.pfps-Instagram-Profile-.jpg"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Aqiel Hilman Maulandany",
                "Hermawan",
                "https://upload.wikimedia.org/wikipedia/commons/5/5f/Alberto_conversi_profile_pic.jpg"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Julita Pramani",
                "Prita",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXMMdfGVo0DUO60HH2rPeiBySnx-93spUlmw&usqp=CAU"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Aditya Anhar",
                "Gita Putri",
                "https://p.favim.com/orig/2018/10/01/cartoon-profile-picture-cute-Favim.com-6346120.jpg"
            )
        )
        mContactList.add(
            ClassDetailMemberContactDao(
                "Jordi Pangestu",
                "Jihan Maulana",
                "https://qph.fs.quoracdn.net/main-qimg-217015358349186e0e382cb15c5d7c63"
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
