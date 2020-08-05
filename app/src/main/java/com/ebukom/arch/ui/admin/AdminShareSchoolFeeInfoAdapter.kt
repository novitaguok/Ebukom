package com.ebukom.arch.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.*
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.activity_admin_share_school_fee_info.view.*
import kotlinx.android.synthetic.main.item_admin_payment_detail_color.view.*
import kotlinx.android.synthetic.main.item_admin_payment_detail_white.view.*

private const val TYPE_COLOR = 0
private const val TYPE_WHITE = 1

class AdminShareSchoolFeeInfoAdapter(
    var data: List<AdminPaymentItemFormDao>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AdminShareSchoolFeeInfoViewHolderColor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: AdminPaymentItemFormDao) {
            itemView.tvItemAdminPaymentDetailName.text = dataModel.itemName
            itemView.tvItemAdminPaymentDetailFee.text = dataModel.itemFee
        }
    }

    class AdminShareSchoolFeeInfoViewHolderWhite(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: AdminPaymentItemFormDao) {
            itemView.tvItemAdminPaymentDetailWhiteName.text = dataModel.itemName
            itemView.tvItemAdminPaymentDetailWhiteFee.text = dataModel.itemFee
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_COLOR) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_admin_payment_detail_color, parent, false)
            return AdminShareSchoolFeeInfoViewHolderColor(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_admin_payment_detail_white, parent, false)
            return AdminShareSchoolFeeInfoViewHolderWhite(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_COLOR) {
            (holder as AdminShareSchoolFeeInfoViewHolderColor).bind(data[position])
//            (holder as AdminShareSchoolFeeInfoViewHolderColor).itemView.ivItemAdminPaymentDetailDelete.visibility = data[position].visibility
        } else {
            (holder as AdminShareSchoolFeeInfoViewHolderWhite).bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            TYPE_WHITE
        } else {
            TYPE_COLOR
        }
    }
}