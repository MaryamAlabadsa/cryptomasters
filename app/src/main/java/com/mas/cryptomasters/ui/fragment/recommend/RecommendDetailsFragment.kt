package com.mas.cryptomasters.ui.fragment.recommend

import androidx.fragment.app.viewModels
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.request.VoteRequest
import com.mas.cryptomasters.data.response.home.User
import com.mas.cryptomasters.data.response.recommendations.RecommendData
import com.mas.cryptomasters.data.response.recommendations.Vote
import com.mas.cryptomasters.databinding.FragmentRecommendDetailsBinding
import com.mas.cryptomasters.databinding.FragmentYourPostsBinding
import com.mas.cryptomasters.utils.Extensions.askLogin
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.loadWebImage
import com.mas.cryptomasters.utils.Extensions.percentageFormat
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Extensions.setTimeFormat
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.Navigate
import com.mas.cryptomasters.utils.ToastType
import com.mas.cryptomasters.utils.UpdateData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendDetailsFragment : BaseFragment<FragmentRecommendDetailsBinding>() {
    val viewModel: RecommendViewModel by viewModels()
    private lateinit var recommendData: RecommendData
    private var voteValue = 0

    override fun getViewBinding() = FragmentRecommendDetailsBinding.inflate(layoutInflater)

    override fun init() {

        if (arguments?.containsKey(Navigate.DATA_POST) == true) {
            if (requireArguments().getParcelable<RecommendData>(Navigate.DATA_POST) != null) {
                recommendData = requireArguments().getParcelable(Navigate.DATA_POST)!!
                recommendData.let {
                    binding.tvTitle.text = it.title
                    binding.tvText.text = it.content
                    binding.tvDate.text = it.createdAt.setTimeFormat(binding.tvDate.context)

                        binding.ivPostImage.loadWebImage(it.image + "")
                    "${getString(R.string.up)} (${
                        it.upCount.toString().percentageFormat()
                    } ${getString(R.string.user)})".also { text ->
                        binding.tvUpValue.text = text
                    }
                    "${getString(R.string.down)} (${
                        it.downCount.toString().percentageFormat()
                    } ${getString(R.string.user)})".also { text ->
                        binding.tvDownValue.text = text
                    }
                    checkVoteState()
                }
            } else {
                requireContext().crToast()
            }
        }
        // add vote
        binding.tvUpValue.setOnClickListener {
            if (preferences.isGustUser()) {
                requireActivity().askLogin()
                return@setOnClickListener
            }
            recommendData.type = "up"
            sendVote()
        }
        binding.tvDownValue.setOnClickListener {
            if (preferences.isGustUser()) {
                requireActivity().askLogin()
                return@setOnClickListener
            }

            recommendData.type = "down"
            sendVote(false)
        }


        viewModel.mutableRecommend.observe(this) {
            if (this.isVisible) {
                hideProgress()
                when {
                    it.reLogin -> requireActivity().reLogin(preferences)
                    it.error.isNotEmpty() -> requireActivity().crToast()
                    it.data == null -> handleSuccess()
                }
            }
        }
    }

    private fun handleSuccess() {
        recommendData.isVoted = 1
        preferences.updateSomeData(UpdateData(isThereRecommendUpdate = true))
        requireActivity().crToast(getString(R.string.vote_success), ToastType.SUCCESS)
        checkVoteState()
    }

    private fun checkVoteState() {
        //0 for up , 1 for down
        binding.tvDownValue.background.setTint(requireContext().getColor(R.color.light_gray))
        binding.tvUpValue.background.setTint(requireContext().getColor(R.color.light_gray))

        if (recommendData.isVoted == 1) {
            if ((recommendData.type + "") == "up") {
                binding.tvUpValue.background.setTint(requireContext().getColor(R.color.green))
            } else {
                binding.tvDownValue.background.setTint(requireContext().getColor(R.color.green))
            }
        }


    }

    private fun sendVote(isUpVote: Boolean = true) {
        if (recommendData.isVoted == 0) {
            if (!isUpVote) {
                voteValue = 1
            }

            viewModel.voteRequest = VoteRequest(
                recommendation_id = recommendData.id,
                vote = voteValue
            )

            requireContext().showProgress()
            viewModel.setVote()
        }
    }
}