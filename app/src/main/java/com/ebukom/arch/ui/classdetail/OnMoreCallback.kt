package com.ebukom.arch.ui.classdetail

import androidx.fragment.app.Fragment
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter

interface OnMoreCallback {
    fun onMoreClicked(id: String, position: Int)
}