package com.ebukom.arch.ui.classdetail

import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAdapter

interface OnMoreCallback {
    fun onMoreClicked(id: String, position: Int)
}