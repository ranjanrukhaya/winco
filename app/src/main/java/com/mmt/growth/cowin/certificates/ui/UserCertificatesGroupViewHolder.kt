package com.mmt.growth.cowin.certificates.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mmt.growth.cowin.certificates.model.CowinCertificate
import kotlinx.android.synthetic.main.cowin_certificate_group.view.*

class UserCertificatesGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //private var resourceProvider = ResourceProvider.instance

    fun bind(cowinCertificate: CowinCertificate, position: Int, size: Int) {
        itemView.group_header.text =
            cowinCertificate.beneficiaryList?.size.toString() + " Certificates registered under " + cowinCertificate.mobileNumber
        // TODO Ranjan
        /*resourceProvider.getString(
                R.string.certificate_label_single,
                cowinCertificate.beneficiaryList.size.toString(),
                cowinCertificate.mobileNumber
            )*/

        itemView.certificates_item_rv.apply {
            adapter = UserCertificatesItemAdapter(
                cowinCertificate.beneficiaryList ?: emptyList(),
                cowinCertificate.mobileNumber
            )
        }

        if (position == size -1) {
            itemView.group_sep_view.visibility = View.VISIBLE
        } else {
            itemView.group_sep_view.visibility = View.GONE
        }
    }
}