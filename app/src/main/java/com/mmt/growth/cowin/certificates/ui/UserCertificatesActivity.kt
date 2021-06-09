package com.mmt.growth.cowin.certificates.ui

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mmt.R
import com.mmt.databinding.ActivityUserCertificatesBinding
import com.mmt.growth.cowin.CowinConstants
import com.mmt.growth.cowin.certificates.model.CertificatesResponse
import com.mmt.growth.cowin.certificates.model.CowinCertificate
import com.mmt.growth.cowin.certificates.util.UserCertificatesUtil
import kotlinx.android.synthetic.main.bottom_sheet_refresh.view.*
import kotlinx.android.synthetic.main.bottom_sheet_refresh.view.update_certificates_text
import kotlinx.android.synthetic.main.bottom_sheet_refresh_item.view.*

class UserCertificatesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserCertificatesBinding

    private val viewModel: UserCertificatesViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                var mobile = intent.getStringExtra(CowinConstants.MOBILE_KEY)
                var token = intent.getStringExtra(CowinConstants.TOKEN_KEY)
                @Suppress("UNCHECKED_CAST")
                return application?.let {
                    UserCertificatesViewModel(
                        this@UserCertificatesActivity,
                        mobile,
                        token
                    )
                } as T
            }
        })[UserCertificatesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserCertificatesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.getCertificatesData()
            .observe(this, Observer { certificateResponse ->
                if (!certificateResponse.success) {
                    handleCertificateError()
                } else if (certificateResponse.success
                    && certificateResponse.result?.extendedUser?.cowinCertificates?.isNotEmpty() == true
                    && certificateResponse.result.extendedUser.cowinCertificates[0].beneficiaryList?.isEmpty() == true
                ) {
                    handleCertificateEmptyData(certificateResponse.result.extendedUser.cowinCertificates[0].mobileNumber)
                } else {
                    handleCertificateData(certificateResponse)
                }
            })

        viewModel.isLoading().observe(this, { loading ->
            handleLoading(loading)
        })
    }

    private fun handleCertificateError() {
        binding.bottomViewHeader.visibility = View.GONE
        binding.totalCertificatesLabel.visibility = View.GONE
        binding.tncLabel.visibility = View.GONE
        binding.bottomView.visibility = View.GONE
        binding.bottomLabel.visibility = View.GONE
        binding.refreshIcon.visibility = View.GONE
        binding.certificatesRefresh.visibility = View.GONE
        binding.noCertificatesLabel.visibility = View.VISIBLE
        binding.noCertificatesLabel.text = getString(R.string.certificates_fetch_error)
        binding.noCertificatesImg.visibility = View.VISIBLE
        binding.changeNoBtn.visibility = View.GONE
        binding.refreshIconError.visibility = View.VISIBLE
        binding.certificatesRefreshError.visibility = View.VISIBLE
    }

    private fun handleCertificateEmptyData(mobile: String) {
        binding.bottomViewHeader.visibility = View.GONE
        binding.totalCertificatesLabel.visibility = View.GONE
        binding.tncLabel.visibility = View.GONE
        binding.bottomView.visibility = View.GONE
        binding.bottomLabel.visibility = View.GONE
        binding.refreshIcon.visibility = View.GONE
        binding.certificatesRefresh.visibility = View.GONE
        binding.noCertificatesLabel.visibility = View.VISIBLE
        binding.noCertificatesLabel.text = getString(R.string.change_messge, mobile)
        binding.noCertificatesImg.visibility = View.VISIBLE
        binding.changeNoBtn.visibility = View.VISIBLE
        binding.changeNoBtn.text = getString(R.string.change_mobile_label)
        binding.refreshIconError.visibility = View.VISIBLE
        binding.certificatesRefreshError.visibility = View.VISIBLE
    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {
            binding.progressView.visibility = View.VISIBLE
            binding.fetchingLabel.visibility = View.VISIBLE
            binding.loaderView.visibility = View.VISIBLE
            binding.loaderView.startShimmer()
            binding.bottomViewHeader.visibility = View.GONE
            binding.totalCertificatesLabel.visibility = View.GONE
            binding.tncLabel.visibility = View.GONE
            binding.bottomView.visibility = View.GONE
            binding.bottomLabel.visibility = View.GONE
            binding.refreshIcon.visibility = View.GONE
            binding.certificatesRefresh.visibility = View.GONE
        } else {
            binding.progressView.visibility = View.GONE
            binding.fetchingLabel.visibility = View.GONE
            binding.loaderView.visibility = View.GONE
            binding.loaderView.stopShimmer()
        }
    }

    private fun handleCertificateData(certificateResponse: CertificatesResponse?) {
        binding.bottomViewHeader.visibility = View.VISIBLE
        binding.totalCertificatesLabel.visibility = View.VISIBLE
        binding.tncLabel.visibility = View.VISIBLE
        binding.bottomView.visibility = View.VISIBLE
        binding.bottomLabel.visibility = View.VISIBLE
        binding.refreshIcon.visibility = View.VISIBLE
        binding.certificatesRefresh.visibility = View.VISIBLE
        binding.noCertificatesLabel.visibility = View.GONE
        binding.changeNoBtn.visibility = View.GONE
        binding.refreshIconError.visibility = View.GONE
        binding.certificatesRefreshError.visibility = View.GONE
        binding.noCertificatesImg.visibility = View.GONE

        val groupAdapter =
            UserCertificatesGroupAdapter(certificateResponse?.result?.extendedUser?.cowinCertificates!!)
        binding.certificatesGroupRv.apply {
            layoutManager = LinearLayoutManager(
                this@UserCertificatesActivity,
                RecyclerView.VERTICAL, false
            )
            adapter = groupAdapter
        }

        val titleBuilder = SpannableStringBuilder()
        val beneficiaryLabelPrefix = "You've"
        val beneficiaryLabel =
            UserCertificatesUtil.getBeneficiaryCount(certificateResponse.result.extendedUser.cowinCertificates)
                .toString() + " beneficiaries "
        val beneficiaryLabelSuffix = "linked to your MakeMyTrip account"
        val title = titleBuilder.append(beneficiaryLabelPrefix).append(" ")
            .append(beneficiaryLabel).append(beneficiaryLabelSuffix)
        title.setSpan(
            StyleSpan(Typeface.BOLD),
            beneficiaryLabelPrefix.length,
            beneficiaryLabelPrefix.length + beneficiaryLabel.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        binding.totalCertificatesLabel.text = title
        binding.backIcon.setOnClickListener {
            onBackPressed()
        }
        binding.certificatesRefresh.setOnClickListener {
            handleRefresh(certificateResponse.result.extendedUser.cowinCertificates)
        }
        binding.refreshIcon.setOnClickListener {
            handleRefresh(certificateResponse.result.extendedUser.cowinCertificates)
        }
    }

    private fun handleRefresh(cowinCertificates: List<CowinCertificate>) {
        if (cowinCertificates.size == 1) {

            return
        }
        val refreshSheet = layoutInflater.inflate(R.layout.bottom_sheet_refresh, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(refreshSheet)
        refreshSheet.setOnClickListener {
            dialog.dismiss()
        }
        refreshSheet.back_icon_refresh.setOnClickListener {
            dialog.dismiss()
        }

        val titleBuilder = SpannableStringBuilder()
        val registerPrefix = resources.getString(R.string.refresh_text_prefix)
        val registerSuffix = resources.getString(R.string.refresh_text_suffix)
        val title = titleBuilder.append(registerPrefix).append(" ").append(registerSuffix)
        title.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            registerPrefix.length + 1,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        refreshSheet.update_certificates_text.text = title
        cowinCertificates.forEach { certificate ->
            val view = layoutInflater.inflate(R.layout.bottom_sheet_refresh_item, null)
            view.update_item_header.text = certificate.mobileNumber
            var titleLabel = ""
            certificate.beneficiaryList?.forEach { item -> titleLabel += item.name + ", " }
            view.update_item_text.text = titleLabel.dropLast(2)
            view.update_text.setOnClickListener {
                handleRefresh(certificate.mobileNumber)
            }
            refreshSheet.refresh_list_container.addView(view)
        }

        dialog.show()
    }

    private fun handleRefresh(mobile: String) {
        Toast.makeText(this, mobile, Toast.LENGTH_SHORT).show()
    }
}