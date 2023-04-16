package com.mas.cryptomasters.ui.fragment.others

import androidx.core.text.HtmlCompat
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.databinding.FragmentTermsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TermsFragment : BaseFragment<FragmentTermsBinding>() {
    override fun getViewBinding() = FragmentTermsBinding.inflate(layoutInflater)

    override fun init() {
        binding.tvTerms.text = HtmlCompat.fromHtml(
            preferences.getAppSettings().appPrivacy.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
        )


    }

}