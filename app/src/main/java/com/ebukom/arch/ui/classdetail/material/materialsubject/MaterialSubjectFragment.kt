package com.ebukom.arch.ui.classdetail.material.materialsubject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.material.MaterialPageAdapter
import com.ebukom.arch.ui.classdetail.school.SchoolPageAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_material_subject.view.*

class MaterialSubjectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_material_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val intent = Intent((context as MainClassDetailActivity), MaterialSubjectNewActivity::class.java)

        view.ivMaterialSubjectBlue.setOnClickListener {
            intent.putExtra("material", "0")
            (context as MainClassDetailActivity).startActivity(intent)
        }
        view.ivMaterialSubjectGreen.setOnClickListener {
            intent.putExtra("material", "1")
            (context as MainClassDetailActivity).startActivity(intent)
        }
        view.ivMaterialSubjectRed.setOnClickListener {
            intent.putExtra("material", "2")
            (context as MainClassDetailActivity).startActivity(intent)
        }
    }
}