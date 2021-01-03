package com.ebukom.arch.ui.classdetail.personal

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementCommentDao
import com.ebukom.arch.dao.ClassDetailPersonalNoteDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.personal.personalacceptednote.PersonalAcceptedNoteFragment
import com.ebukom.arch.ui.classdetail.personal.personalnotedetail.PersonalNoteDetailActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*
import kotlin.collections.ArrayList

class PersonalNoteAdapter(
    var data: List<ClassDetailPersonalNoteDao>,
    var tabItem: Int,
    var callback: OnMoreCallback,
    var fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var id: String
    private val mCommentList: ArrayList<ClassDetailAnnouncementCommentDao> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_note, parent, false)
        return ViewHolder(
            view,
            callback, parent.context
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    inner class ViewHolder(itemView: View, val callback: OnMoreCallback, val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(note: ClassDetailPersonalNoteDao) {

            itemView.tvItemNoteComment.text =
                note.comments.size.toString() + " KOMENTAR"
            itemView.ivItemNoteProfilePicture.setImageResource(note.profilePicture)
            if (tabItem == 1) {
                itemView.tvItemNoteTitle.text = "Untuk: " + note?.noteTitle
            } else {
                itemView.tvItemNoteTitle.text = note?.noteTitle
            }
            itemView.tvItemNoteContent.text = note?.noteContent

            // Time
            val uploadTime = note.uploadTime.toDate().toCalendar()
            val day = uploadTime.get(Calendar.DAY_OF_WEEK)
            val date = uploadTime.get(Calendar.DATE)
            val month = uploadTime.get(Calendar.MONTH)
            val year = uploadTime.get(Calendar.YEAR)
            var dayName = ""
            var monthName = ""

            when (day) {
                1 -> dayName = "Minggu"
                2 -> dayName = "Senin"
                3 -> dayName = "Selasa"
                4 -> dayName = "Rabu"
                5 -> dayName = "Kamis"
                6 -> dayName = "Jumat"
                7 -> dayName = "Sabtu"
            }

            when (month) {
                0 -> monthName = "Januari"
                1 -> monthName = "Febuari"
                2 -> monthName = "Maret"
                3 -> monthName = "April"
                4 -> monthName = "Mei"
                5 -> monthName = "Juni"
                6 -> monthName = "Juli"
                7 -> monthName = "Agustus"
                8 -> monthName = "September"
                9 -> monthName = "Oktober"
                10 -> monthName = "November"
                11 -> monthName = "Desember"
            }

            itemView.tvItemNoteTime.text = dayName + ", " + date!! + " " + monthName + " " + (year!!)

            if (fragment is PersonalAcceptedNoteFragment) {
                id = "5"
            } else {
                id = "3"
            }
            itemView.ivItemNoteMoreButton.setOnClickListener {
                callback.onMoreClicked(note.noteId, adapterPosition)

            }

            itemView.tvItemNoteMore.setOnClickListener {
                val intent = Intent(context, PersonalNoteDetailActivity::class.java)
                intent.putExtra("noteId", note.noteId)
                intent.putExtra("noteTitle", note.noteTitle)
                intent.putExtra("noteContent", note.noteContent)
                intent.putExtra("noteUploadTime", note.uploadTime)
                (context as MainClassDetailActivity).startActivity(intent)
            }
        }
    }

    fun Date.toCalendar() : Calendar {
        val cal = Calendar.getInstance()
        cal.time = this
        return cal
    }
}
