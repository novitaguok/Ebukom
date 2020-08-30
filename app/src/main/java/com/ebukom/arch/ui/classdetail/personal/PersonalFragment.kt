package com.ebukom.arch.ui.classdetail.personal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.personal.personalnotenew.PersonalNoteNewActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_personal.*
import kotlinx.android.synthetic.main.fragment_personal.view.*

class PersonalFragment : Fragment() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    lateinit var callback: OnMoreCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_personal, container, false)

        tabLayout = view.findViewById(R.id.mainClassPersonalTabLayout) as TabLayout
        viewPager = view.findViewById(R.id.mainClassPersonalViewPager) as ViewPager
        viewPager?.adapter = PersonalPageAdapter(childFragmentManager)
        tabLayout?.setupWithViewPager(viewPager)
        viewPager?.currentItem = 0

//        mainClassPersonalViewPager?.adapter = PersonalPageAdapter(childFragmentManager)
//        mainClassPersonalTabLayout?.setupWithViewPager(mainClassPersonalViewPager)
//        mainClassPersonalViewPager?.currentItem = 0

        view.btnPersonalNew.setOnClickListener {
            activity?.startActivity(Intent(activity, PersonalNoteNewActivity::class.java))
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharePref: SharedPreferences = activity!!.getSharedPreferences("EBUKOM", Context.MODE_PRIVATE)
        if(sharePref.getInt("level", 0) == 1){
            btnPersonalNew.text = "Buat Catatan untuk Guru"
        }
    }

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
