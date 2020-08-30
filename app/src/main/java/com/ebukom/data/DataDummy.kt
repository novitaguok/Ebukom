package com.ebukom.data

import android.content.Context
import android.provider.Settings
import com.ebukom.R
import com.ebukom.arch.dao.*

class DataDummy {

    companion object {
        // Class
        var chooseClassData = arrayListOf<ChooseClassDao>()
        var chooseClassDataMain = arrayListOf<ChooseClassDao>() // Empty

        // School Nav -- Announcement
        var announcementData = arrayListOf<ClassDetailAnnouncementDao>()
        var announcementAttachmentData = arrayListOf<ClassDetailAttachmentDao>()
        var announcementTemplateData = arrayListOf<ClassDetailTemplateTextDao>()

        // School Nav -- Schedule
        var scheduleData = arrayListOf<ClassDetailScheduleDao>()

        // School Nav -- Photo
        var photoData = arrayListOf<ClassDetailPhotoDao>()

        // Personal Nav
        var noteAcceptedData = arrayListOf<ClassDetailPersonalNoteDao>()
        var noteSentData = arrayListOf<ClassDetailPersonalNoteDao>()
        var noteAttachmentData = arrayListOf<ClassDetailAttachmentDao>()
        var noteTemplateData = arrayListOf<ClassDetailTemplateTextDao>()
        var parentNameData = arrayListOf<ClassDetailItemCheckDao>()

        // Material Nav -- Subject Tab
        var mathMaterial = arrayListOf<ClassDetailTemplateTextDao>()
        var scienceMaterial = arrayListOf<ClassDetailTemplateTextDao>()
        var englishMaterial = arrayListOf<ClassDetailTemplateTextDao>()

        // Material Nav -- Education Tab
        var educationData = arrayListOf<ClassDetailAnnouncementDao>()

        // Admin
        var paymentData = arrayListOf<AdminPaymentItemFormDao>()
        var adminNoteTemplateData = arrayListOf<ClassDetailTemplateTextDao>()
    }
}

fun ArrayList<ChooseClassDao>.buildClassDummy(): ArrayList<ChooseClassDao> {
    this.add(ChooseClassDao("Kelas 1", "Aurora", "Ratna Hendrawati", 0))
    this.add(ChooseClassDao("Kelas 2", "Spectra", "Eni Trikuswanti", 1))
    this.add(ChooseClassDao("Kelas 1", "Aurora", "Ratna Hendrawati", 0))
    this.add(ChooseClassDao("Kelas 2", "Spectra", "Eni Trikuswanti", 1))
    this.add(ChooseClassDao("Kelas 1", "Aurora", "Ratna Hendrawati", 0))
    this.add(ChooseClassDao("Kelas 2", "Spectra", "Eni Trikuswanti", 1))
    this.add(ChooseClassDao("Kelas 1", "Aurora", "Ratna Hendrawati", 0))
    this.add(ChooseClassDao("Kelas 2", "Spectra", "Eni Trikuswanti", 1))
    return this
}

fun ArrayList<ClassDetailPersonalNoteDao>.buildParentNoteDummy(context: Context): ArrayList<ClassDetailPersonalNoteDao> {
    val content: String = context.getString(R.string.announcement_content)
    for (i in 1..5) {
        this.add(
            ClassDetailPersonalNoteDao(
                R.drawable.bg_solid_gray,
                "Eni Trikuswanti",
                content,
                arrayListOf(),
                "18 aUG 2020 16:23",
                arrayListOf()
            )
        )
    }
    return this
}

fun ArrayList<ClassDetailItemCheckDao>.buildParentNameDummy(): ArrayList<ClassDetailItemCheckDao> {
    this.add(ClassDetailItemCheckDao("Jumaidah Estetika"))
    this.add(ClassDetailItemCheckDao("Siti Nur Mudhaya"))
    this.add(ClassDetailItemCheckDao("Rizki Azhar"))
    this.add(ClassDetailItemCheckDao("Putri Tryatna"))
    return this
}
