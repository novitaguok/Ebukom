package com.ebukom.data

import com.ebukom.arch.dao.ChooseClassDao

class DataDummy {

    companion object{
        var chooseClassData = arrayListOf<ChooseClassDao>()
        var chooseClassDataMain = arrayListOf<ChooseClassDao>() // Empty
    }
}

fun ArrayList<ChooseClassDao>.buildDummy() : ArrayList<ChooseClassDao>{
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