package com.mmt.growth.cowin.certificates.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mmt.R
import com.mmt.growth.cowin.certificates.model.CowinCertificate

class UserCertificatesGroupAdapter(val cowinCertificates: List<CowinCertificate>) :
    RecyclerView.Adapter<UserCertificatesGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCertificatesGroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cowin_certificate_group, parent, false)
        return UserCertificatesGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserCertificatesGroupViewHolder, position: Int) {
        holder.bind(cowinCertificates[position], position, cowinCertificates.size)
    }

    override fun getItemCount(): Int {
        return cowinCertificates.size
    }
}

