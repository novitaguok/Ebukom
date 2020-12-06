//package com.ebukom.arch.ui.admin.adminschoolfeeinfo.schoolfeeinfosent
//
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.ebukom.R
//import com.ebukom.arch.dao.AdminPaymentItemDao
//import com.ebukom.arch.dao.AdminSchoolFeeInfoSentDao
//import com.ebukom.arch.ui.admin.MainAdminActivity
//import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfonext.AdminShareSchoolFeeInfoNextActivity
//import com.ebukom.arch.ui.classdetail.personal.personalparent.personalparentschoolfeeinfo.PersonalParentSchoolFeeInfoActivity
//import kotlinx.android.synthetic.main.item_admin_info_sent.view.*
//
//class AdminSchoolFeeInfoSentAdapter(
//    var data: List<AdminPaymentItemDao>,
//    val callback: onCheckListener?
//) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.item_admin_info_sent, parent, false)
//        return ViewHolder(
//            view, parent.context
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        (holder as ViewHolder).bind(data[position])
//    }
//
//    inner class ViewHolder(itemView: View, val context: Context) :
//        RecyclerView.ViewHolder(itemView) {
//        fun bind(info: AdminPaymentItemDao) {
//            itemView.tvItemAdminInfoSentTitle.text = "Info Biaya Pendidikan 14 Maret 2020"
//
//            if (context is PersonalParentSchoolFeeInfoActivity) itemView.tvItemAdminInfoSentDetail.text = "Bobbi Andrean • " + info?.eskuls + " • " + info?.classes
//            else itemView.tvItemAdminInfoSentDetail.text = info?.eskuls
//
//            itemView.tvItemAdminInfoSentDate.text = "Terakhir diupdate: 20.00 - 14 Maret 2020"
//
//            if (context is AdminShareSchoolFeeInfoNextActivity) {
//                itemView.cbItemAdminInfoSent.visibility = View.VISIBLE
//            }
//
//            itemView.cbItemAdminInfoSent.setOnCheckedChangeListener { _, isChecked ->
//                callback?.onCheckChange()
//            }
//
//            if (context is MainAdminActivity) {
//                itemView.clItemAdminInfoSent.setOnClickListener {
//                    val intent = Intent(context, PersonalParentSchoolFeeInfoActivity::class.java)
//                    intent.putExtra("role", "admin")
//                    intent.putExtra("pos", adapterPosition)
//                    intent.putExtra("data", info)
//                    context.startActivity(intent)
//                }
//            }
//        }
//    }
//
//    interface onCheckListener {
//        fun onCheckChange()
//    }
//}
