package com.mmt.growth.cowin.certificates.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mmt.R
import com.mmt.growth.cowin.CowinConstants
import com.mmt.growth.cowin.certificates.model.Beneficiary
import com.mmt.growth.cowin.certificates.model.NextAppointment
import com.mmt.growth.cowin.pdfviewer.ui.CertificateViewerActivity
import kotlinx.android.synthetic.main.cowin_certificate_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class UserCertificatesItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(beneficiary: Beneficiary, mobile: String) {
        itemView.cowin_user.text = beneficiary.name
        itemView.cowin_user_dob.text =
            beneficiary.gender + ", " + beneficiary.birthYear + ", " + beneficiary.vaccine

        val titleBuilder = SpannableStringBuilder()
        val registerLabel = "Registered with"

        val title = titleBuilder.append(registerLabel).append(" ").append(mobile)

        title.setSpan(
            StyleSpan(Typeface.BOLD),
            registerLabel.length,
            registerLabel.length + mobile.length + 1,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        itemView.cowin_user_no.text = title

        when (beneficiary.vaccinationStatus) {
            "FULLY_VACCINATED" -> {
                itemView.dose2_label.visibility = View.GONE
                itemView.dose2_view.visibility = View.GONE
                itemView.dose1_view.visibility = View.VISIBLE
                itemView.dose1_label.visibility = View.VISIBLE
                itemView.dose1_text.visibility = View.GONE
                itemView.dose1_label.text = "Final Certificate"
                val params = itemView.dose1_view.layoutParams
                // TODO Ranjan Convert to dp for 256 value
                params.width = 650
                itemView.dose1_view.layoutParams = params
                itemView.dose1_view.setImageResource(R.drawable.img_no_certificate_big)
            }
            "PARTIAL_VACCINATED" -> {
                itemView.dose1_view.visibility = View.VISIBLE
                itemView.dose1_label.visibility = View.VISIBLE
                itemView.dose1_text.visibility = View.GONE
                itemView.dose1_view.setImageResource(R.drawable.img_no_certificate_small)
                itemView.dose2_label.visibility = View.VISIBLE
                itemView.dose2_view.visibility = View.VISIBLE
                itemView.dose2_text.text =
                    getDoseText(beneficiary.doseRequired, beneficiary.nextAppointment)
                itemView.dose2_text.setTextColor(
                    getDoseTextColor(
                        beneficiary.doseRequired,
                        beneficiary.nextAppointment,
                        itemView.context
                    )
                )
                itemView.dose2_view.background = getDoseBackground(
                    beneficiary.doseRequired,
                    beneficiary.nextAppointment,
                    itemView.context
                )
            }
            "NOT_VACCINATED" -> {
                if (beneficiary.doseRequired == 1) {
                    itemView.dose1_view.visibility = View.VISIBLE
                    itemView.dose1_label.visibility = View.VISIBLE
                    itemView.dose1_text.visibility = View.VISIBLE
                    itemView.dose1_text.text =
                        getDoseText(beneficiary.doseRequired, beneficiary.nextAppointment)
                    itemView.dose1_text.setTextColor(
                        getDoseTextColor(
                            beneficiary.doseRequired,
                            beneficiary.nextAppointment,
                            itemView.context
                        )
                    )
                    itemView.dose1_view.background = getDoseBackground(
                        beneficiary.doseRequired,
                        beneficiary.nextAppointment,
                        itemView.context
                    )

                    itemView.dose2_label.visibility = View.GONE
                    itemView.dose2_view.visibility = View.GONE
                    itemView.dose2_text.visibility = View.GONE
                } else {
                    itemView.dose1_view.visibility = View.VISIBLE
                    itemView.dose1_label.visibility = View.VISIBLE
                    itemView.dose1_text.visibility = View.VISIBLE
                    itemView.dose1_text.text =
                        getDoseText(beneficiary.doseRequired, beneficiary.nextAppointment)
                    itemView.dose1_text.setTextColor(
                        getDoseTextColor(
                            beneficiary.doseRequired,
                            beneficiary.nextAppointment,
                            itemView.context
                        )
                    )
                    itemView.dose1_view.background = getDoseBackground(
                        beneficiary.doseRequired,
                        beneficiary.nextAppointment,
                        itemView.context
                    )

                    itemView.dose2_label.visibility = View.VISIBLE
                    itemView.dose2_view.visibility = View.VISIBLE
                    itemView.dose2_text.text =
                        getDoseText(beneficiary.doseRequired, beneficiary.nextAppointment)
                    itemView.dose2_text.setTextColor(
                        getDoseTextColor(
                            beneficiary.doseRequired,
                            beneficiary.nextAppointment,
                            itemView.context
                        )
                    )
                    itemView.dose2_view.background = getDoseBackground(
                        beneficiary.doseRequired,
                        beneficiary.nextAppointment,
                        itemView.context
                    )
                }

            }
        }

        itemView.rootView.setOnClickListener {
            val intent = Intent(itemView.context, CertificateViewerActivity::class.java)
            intent.putExtra(CowinConstants.BENEFICIARY_KEY, beneficiary)
            itemView.context.startActivity(intent)
        }
    }

    // TODO Ranjan remove context
    private fun getDoseTextColor(
        doseNumber: Int,
        nextAppointment: NextAppointment?,
        context: Context
    ): Int {
        return if (nextAppointment?.dose != doseNumber)
            context.resources.getColor(R.color.dose_not_scheduled_color)
        else if (nextAppointment.dose == doseNumber && nextAppointment.appointmentDate > System.currentTimeMillis())
            context.resources.getColor(R.color.dose_scheduled_color)
        else if (nextAppointment.dose == doseNumber && nextAppointment.appointmentDate < System.currentTimeMillis())
            context.resources.getColor(R.color.dose_scheduled_color)
        else
            context.resources.getColor(R.color.dose_not_scheduled_color)
    }

    private fun getDoseBackground(
        doseNumber: Int,
        nextAppointment: NextAppointment?,
        context: Context
    ): Drawable {
        return if (nextAppointment?.dose != doseNumber)
            context.getDrawable(R.drawable.vaccine_not_scheduled_view)!!
        else if (nextAppointment.dose == doseNumber && nextAppointment.appointmentDate > System.currentTimeMillis())
            context.getDrawable(R.drawable.vaccine_scheduled_view)!!
        else if (nextAppointment.dose == doseNumber && nextAppointment.appointmentDate < System.currentTimeMillis())
            context.getDrawable(R.drawable.vaccine_scheduled_view)!!
        else
            context.getDrawable(R.drawable.vaccine_not_scheduled_view)!!
    }

    private fun getDoseText(doseNumber: Int, nextAppointment: NextAppointment?): String {
        return if (nextAppointment?.dose != doseNumber)
            "Not Scheduled"
        else if (nextAppointment.dose == doseNumber && nextAppointment.appointmentDate > System.currentTimeMillis())
            "Scheduled For ${getVaccineDate(nextAppointment.appointmentDate)}"
        else if (nextAppointment.dose == doseNumber && nextAppointment.appointmentDate < System.currentTimeMillis())
            "Scheduled For ${getVaccineDate(nextAppointment.appointmentDate)}"
        else
            "Not Scheduled"
    }

    private fun getVaccineDate(value: Long): String =
        SimpleDateFormat("dd-MM-yyyy", Locale.ROOT).format(value)

}