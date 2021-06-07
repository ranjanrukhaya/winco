package com.mmt.growth.cowin.certificates.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmt.R
import com.mmt.growth.cowin.certificates.model.Beneficiary
import com.mmt.growth.cowin.certificates.model.CowinCertificate

class UserCertificatesItemAdapter(val beneficiaries: List<Beneficiary>, val mobile: String) :
    RecyclerView.Adapter<UserCertificatesItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserCertificatesItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cowin_certificate_item, parent, false)
        return UserCertificatesItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserCertificatesItemViewHolder, position: Int) {
        holder.bind(beneficiaries[position], mobile)
    }

    override fun getItemCount(): Int {
        return beneficiaries.size
    }
}

