package com.ebukom.data

import com.ebukom.arch.dao.*

class DataDummy {
    companion object{
        var chooseClassData = arrayListOf<ChooseClassDao>()
        var chooseClassDataMain = arrayListOf<ChooseClassDao>() // Empty
        var announcementData = arrayListOf<ClassDetailAnnouncementDao>()
        var attachmentData = arrayListOf<ClassDetailAttachmentDao>()
        var textTemplateData = arrayListOf<ClassDetailTemplateTextDao>()
        var scheduleData = arrayListOf<ClassDetailScheduleDao>()
        var photoData = arrayListOf<ClassDetailPhotoDao>()
    }
}

fun ArrayList<ChooseClassDao>.buildClassDummy() : ArrayList<ChooseClassDao>{
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

