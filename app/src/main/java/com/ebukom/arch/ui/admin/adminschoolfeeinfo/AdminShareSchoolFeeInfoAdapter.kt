package com.ebukom.arch.ui.admin.adminschoolfeeinfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.*
import com.ebukom.arch.ui.admin.adminschoolfeeinfo.adminshareschoolfeeinfo.AdminShareSchoolFeeInfoActivity
import kotlinx.android.synthetic.main.item_admin_payment_detail_color.view.*
import kotlinx.android.synthetic.main.item_admin_payment_detail_white.view.*

private const val TYPE_COLOR = 0
private const val TYPE_WHITE = 1

class AdminShareSchoolFeeInfoAdapter(
    var data: List<AdminPaymentItemFormDao>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AdminShareSchoolFeeInfoViewHolderColor(
        itemView: View,
        var context: Context
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: AdminPaymentItemFormDao) {
            itemView.tvItemAdminPaymentDetailName.text = dataModel.itemName
            itemView.tvItemAdminPaymentDetailFee.text = dataModel.itemFee
            itemView.ivItemAdminPaymentDetailDelete.visibility = if (dataModel.itemEdit) {
                View.VISIBLE
            } else {
                View.GONE
            }
            itemView.ivItemAdminPaymentDetailDelete.setOnClickListener {
                (context as AdminShareSchoolFeeInfoActivity).deleteItem(dataModel)
            }
            itemView.trItemAdminPaymentDetail.setOnClickListener {
                (context as AdminShareSchoolFeeInfoActivity).editItem(dataModel)
            }
        }
    }

    class AdminShareSchoolFeeInfoViewHolderWhite(
        itemView: View,
        var context: Context
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(dataModel: AdminPaymentItemFormDao) {
            itemView.tvItemAdminPaymentDetailWhiteName.text = dataModel.itemName
            itemView.tvItemAdminPaymentDetailWhiteFee.text = dataModel.itemFee
            itemView.ivItemAdminPaymentDetailWhiteDelete.visibility = if (dataModel.itemEdit) {
                View.VISIBLE
            } else {
                View.GONE
            }
            itemView.ivItemAdminPaymentDetailWhiteDelete.setOnClickListener {
                (context as AdminShareSchoolFeeInfoActivity).deleteItem(dataModel)
            }
            itemView.trItemAdminPaymentDetailWhite.setOnClickListener {
                (context as AdminShareSchoolFeeInfoActivity).editItem(dataModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_COLOR) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_admin_payment_detail_color, parent, false)
            return AdminShareSchoolFeeInfoViewHolderColor(
                view,
                parent.context
            )
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_admin_payment_detail_white, parent, false)
            return AdminShareSchoolFeeInfoViewHolderWhite(
                view,
                parent.context
            )
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