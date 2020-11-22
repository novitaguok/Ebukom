package com.ebukom.data

import android.content.Context
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.google.firebase.firestore.FirebaseFirestore

class Databases {
    fun initClassesData(){
        val db = FirebaseFirestore.getInstance()

        buildClassesData().forEach {
            db.collection("classes").add(it)
        }
    }

    fun buildClassesData() : List<Map<String, Any>>{
        val arrayOfMap = arrayListOf<Map<String, Any>>()
        arrayOfMap.add(
            mapOf(
                Pair("class_bg", R.drawable.bg_class_krypton),
                Pair("class_grade", "Kelas 1"),
                Pair("class_name", "Krypton"),
                Pair("class_teacher", mapOf(
                    Pair("id","t1"),
                    Pair("name","Ratna Hendrawati")
                )),
                Pair("class_theme", "#005C39")
            )
        )
        arrayOfMap.add(
            mapOf(
                Pair("class_bg", R.drawable.bg_class_xenon),
                Pair("class_grade", "Kelas 1"),
                Pair("class_name", "Xenon"),
                Pair("class_teacher", mapOf(
                    Pair("id","t2"),
                    Pair("name","Eni Trikuswanti")
                )),
                Pair("class_theme", "#004A61")
            )
        )
        arrayOfMap.add(
            mapOf(
                Pair("class_bg", R.drawable.bg_class_argon),
                Pair("class_grade", "Kelas 1"),
                Pair("class_name", "Argon"),
                Pair("class_teacher", mapOf(
                    Pair("id","t3"),
                    Pair("name","Sindi Putri")
                )),
                Pair("class_theme", "#693535")
            )
        )
        arrayOfMap.add(
            mapOf(
                Pair("class_bg", R.drawable.bg_class_titanium),
                Pair("class_grade", "Kelas 2"),
                Pair("class_name", "Titanium"),
                Pair("class_teacher", mapOf(
                    Pair("id","t4"),
                    Pair("name","Yulianti Puspita")
                )),
                Pair("class_theme", "#229AE6")
            )
        )
        arrayOfMap.add(
            mapOf(
                Pair("class_bg", R.drawable.bg_class_neon),
                Pair("class_grade", "Kelas 2"),
                Pair("class_name", "Neon"),
                Pair("class_teacher", mapOf(
                    Pair("id","t5"),
                    Pair("name","Putri Eka")
                )),
                Pair("class_theme", "#FFADAD")
            )
        )
        arrayOfMap.add(
            mapOf(
                Pair("class_bg", R.drawable.bg_class_helium),
                Pair("class_grade", "Kelas 2"),
                Pair("class_name", "Helium"),
                Pair("class_teacher", mapOf(
                    Pair("id","t6"),
                    Pair("name","Ahmad Juliansyah")
                )),
                Pair("class_theme", "#645470")
            )
        )
        arrayOfMap.add(
            mapOf(
                Pair("class_bg", R.drawable.bg_class_argentum),
                Pair("class_grade", "Kelas 3"),
                Pair("class_name", "Argentum"),
                Pair("class_teacher", mapOf(
                    Pair("id","t7"),
                    Pair("name","Gigi Rahma")
                )),
                Pair("class_theme", "#313D2E")
            )
        )
        arrayOfMap.add(
            mapOf(
                Pair("class_bg", R.drawable.bg_class_aurum),
                Pair("class_grade", "Kelas 3"),
                Pair("class_name", "Aurum"),
                Pair("class_teacher", mapOf(
                    Pair("id","t8"),
                    Pair("name","Julia Isma")
                )),
                Pair("class_theme", "#828127")
            )
        )
        arrayOfMap.add(
            mapOf(
                Pair("class_bg", R.drawable.bg_class_selenium),
                Pair("class_grade", "Kelas 3"),
                Pair("class_name", "Selenium"),
                Pair("class_teacher", mapOf(
                    Pair("id","t9"),
                    Pair("name","Dewi Putri")
                )),
                Pair("class_theme", "#4F483B")
            )
        )

        return arrayOfMap
    }
}