package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddpaymentitem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemFormDao
import kotlinx.android.synthetic.main.item_school_fee_info_form.view.*

class AdminSchoolFeeInfoAddPaymentItemAdapter(
    var forms: List<AdminPaymentItemFormDao>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_school_fee_info_form, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return forms.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(forms[position])
    }

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(form: AdminPaymentItemFormDao) {
            itemView.etItemSchoolFeeInfoForm.setText(form?.itemName)
            itemView.etItemSchoolFeeInfoFormFee.setText(form?.itemFee)
        }
    }
}