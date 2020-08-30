package com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfoaddpaymentitem

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.AdminPaymentItemFormDao
import kotlinx.android.synthetic.main.item_school_fee_info_form.view.*

class AdminSchoolFeeInfoAddPaymentItemAdapter(
    var forms: List<AdminPaymentItemFormDao>,
    var callback : OnPaymentItemCallback
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

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.etItemSchoolFeeInfoForm.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    forms[adapterPosition].itemName = s.toString()
                    callback.onItemChange()
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
            itemView.etItemSchoolFeeInfoFormFee.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    forms[adapterPosition].itemFee = s.toString()
                    callback.onItemChange()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }

        fun bind(form: AdminPaymentItemFormDao) {
            itemView.etItemSchoolFeeInfoForm.setText(form?.itemName)
            itemView.etItemSchoolFeeInfoFormFee.setText(form?.itemFee)
        }
    }
}

interface OnPaymentItemCallback{
    fun onItemChange()
}


