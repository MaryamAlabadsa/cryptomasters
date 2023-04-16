package com.mas.cryptomasters.ui.fragment.rules

import android.view.View
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.response.home.Coin
import com.mas.cryptomasters.databinding.FragmentCoinBinding
import com.mas.cryptomasters.databinding.FragmentRecommendBinding
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Navigate

class CoinFragment : BaseFragment<FragmentCoinBinding>() {

    override fun getViewBinding() = FragmentCoinBinding.inflate(layoutInflater)


    override fun init() {

        if (arguments?.containsKey(Navigate.COIN_DATA) == true &&
            requireArguments().getParcelable<Coin>(Navigate.COIN_DATA) != null
        ) {
            requireArguments().getParcelable<Coin>(Navigate.COIN_DATA)?.let {
                setView(it)
            }
        } else {
            requireActivity().crToast()
        }
    }


    fun setView(it: Coin) {
        binding.tvName.text = it.name
        binding.tvStatus.text = it.halal

        binding.tvProject.text = it.project
        binding.tvDetails.text = it.body

        if (it.isHalal == "0") {
            binding.tvStatus.setTextColor(requireContext().getColor(R.color.green))
        } else {
            binding.tvStatus.setTextColor(requireContext().getColor(R.color.red))
        }

        if (it.image != null) {
            binding.clImage.visibility = View.VISIBLE
            binding.ivImage.loadWebImage(it.image + "", isProfile = false)
        } else {
            binding.clImage.visibility = View.GONE
        }

        if (it.icon != null) {
            binding.cvLogo.loadWebImage(it.icon + "", isProfile = false)
        }
    }

}