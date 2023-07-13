package com.mas.cryptomasters.ui.fragment.recommend

import android.content.Intent
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mas.cryptomasters.BaseFragment
import com.mas.cryptomasters.R
import com.mas.cryptomasters.adapters.RecommendAdapter
import com.mas.cryptomasters.data.response.recommendations.Recommendations
import com.mas.cryptomasters.databinding.FragmentRecommendBinding
import com.mas.cryptomasters.databinding.LoadingLayoutBinding
import com.mas.cryptomasters.ui.fragment.recommend.SheetMenu.MenuViewModel
import com.mas.cryptomasters.ui.fragment.recommend.SheetMenu.SheetMenuDialog
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.reLogin
import com.mas.cryptomasters.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecommendFragment : BaseFragment<FragmentRecommendBinding>() {

    private val viewModel: RecommendViewModel by viewModels()
    private lateinit var recommendAdapter: RecommendAdapter
    var page = 1
    var limit: Int = 10

    var isLoading = false
    var isLastPage = true
    var lastVisibleItem = 0
    var totalItemCount = 0

    var selceted = 1
    var isSend = false

    //    private var mInterstitialAd: InterstitialAd? = null
//    private val adRequest: AdRequest = AdRequest.Builder().build()
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button

    private lateinit var shape1: Drawable
    private lateinit var shape2: Drawable

    override fun getViewBinding() = FragmentRecommendBinding.inflate(layoutInflater)
    private val menuViewModel: MenuViewModel by viewModels()
    private lateinit var sheetMenuDialog: SheetMenuDialog

    override fun init() {

        // Load the shape drawables
        shape1 = resources.getDrawable(R.drawable.btn_bg1)
        shape2 = resources.getDrawable(R.drawable.btn_bg2)

        // Set click listeners for the buttons
        btn1 = binding.dailyBtn;
        btn2 = binding.WeaklyBtn;
        btn3 = binding.MonthlyBtn;
        binding.dailyBtn.setOnClickListener { onButtonClicked(btn1) }
        binding.WeaklyBtn.setOnClickListener { onButtonClicked(btn2) }
        binding.MonthlyBtn.setOnClickListener { onButtonClicked(btn3) }



        if (!preferences.getUserProfile().isPaid.equals("1")) {

            handleLoading(binding.loading, false)

            binding.linear2.visibility = View.GONE
            binding.linear.visibility = View.VISIBLE

            sheetMenuDialog = SheetMenuDialog(menuViewModel)
            binding.linear.setOnClickListener {
                sheetMenuDialog.show(parentFragmentManager, "SheetMenuDialog")
            }


        } else {
            if (arguments?.containsKey(Navigate.TARGET) == true) {
                if (requireArguments().getString(Navigate.TARGET) == Navigate.RECOMMEND_FRAGMENT) {
                    binding.clTop.visibility = View.GONE
                }
            }
            handelRV()
//            val recommendData = preferences.getRecommend()
//            if (recommendData.isEmpty()) {
//            viewModel.getDailyRecommendList()
//            } else {
//                handleLoading(binding.loading, false)
//                recommendAdapter.updateAdapter(recommendData)
//            }

            viewModel.mutableRecommend.observe(viewLifecycleOwner) { result ->
                if (isVisible) {
                    when {
                        result.reLogin -> requireActivity().reLogin(preferences)
                        result.error.isNotEmpty() -> {
                            handleLoading(binding.loading, true)
                            handleLoading(binding.loading2, true)
                            requireContext().crToast()
                        }
                        result.data != null -> {
                            handleLoading(binding.loading, false)
                            handleLoading(binding.loading2, false)
                            handleResponse(result.data as Recommendations)
                        }
                    }
                }
            }
            viewModel.getDailyRecommendList(page)


            recommendAdapter.showAdsObservable.observe(this) {
//                if (preferences.getUserProfile().isPaid != "1") {
                intentToRecommendData()
            }

            binding.top.ivNotifications.setOnClickListener {
                navigateToNotifications(requireActivity())
            }
        }

    }

    private fun intentToRecommendData() {
        requireActivity().startActivity(
            Intent(context, NavigationActivity::class.java)
                .putExtra(Navigate.TARGET, Navigate.RECOMMEND_DATA)
                .putExtra(Navigate.DATA_POST, recommendAdapter.currentData!!)
        )

    }

    private fun handelRV() {
        recommendAdapter = RecommendAdapter()
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = recommendAdapter

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                totalItemCount = layoutManager.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItem == totalItemCount - 1 && !isLoading && totalItemCount != 0 && !isLastPage && !isSend) {
                    binding.progressBar.visibility = View.VISIBLE
                    isSend = true
                    Log.e("pagemaryam22", page.toString())
                    if (selceted == 1)
                        viewModel.getDailyRecommendList(page)
                    else if (selceted == 2)
                        viewModel.getWeaklyRecommendList(page)
                    else if (selceted == 3)
                        viewModel.getMonthlyRecommendList(page)
                }
            }
        })
    }

    private fun handleResponse(recommendations: Recommendations) {
//        preferences.setRecommend(recommendations.recommendData)
        recommendations.data?.let {
            binding.progressBar.visibility = View.GONE
            if (page == 1) {
                recommendAdapter.setList(it.recommendData)
            } else recommendAdapter.addToList(it.recommendData)
            Log.e("pagemaryam", page.toString())
            if (it.lastPage == page) {
                isLastPage = true
                Log.e("lastPage", isLastPage.toString() + "")
            } else {
                isLastPage = false
            }
            page++
            isSend = false
        }
    }

    private fun onButtonClicked(clickedButton: Button) {
        // Set background shape for clicked button
        clickedButton.background = shape2
        clickedButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        handleLoading(binding.loading2, false, "new")

        // Set background shape and text color for the other buttons
        if (clickedButton != btn1) {
            btn1.background = shape1
            btn1.setTextColor(ContextCompat.getColor(requireContext(), R.color.them))
        } else {
            page = 1
            viewModel.getDailyRecommendList(page)
            selceted = 1
        }

        if (clickedButton != btn2) {
            btn2.background = shape1
            btn2.setTextColor(ContextCompat.getColor(requireContext(), R.color.them))
        } else {
            page = 1

            viewModel.getWeaklyRecommendList(page)
            selceted = 2
        }

        if (clickedButton != btn3) {
            btn3.background = shape1
            btn3.setTextColor(ContextCompat.getColor(requireContext(), R.color.them))
        } else {
            page = 1
            viewModel.getMonthlyRecommendList(page)
            selceted = 3
        }
    }

    fun handleLoading(binding: LoadingLayoutBinding, isError: Boolean, x: String) {
        if (isError) {
            binding.clOnLoading.visibility = View.GONE

            binding.clLoading.visibility = View.VISIBLE
            binding.clOnError.visibility = View.VISIBLE
        } else if (x.equals("new"))
            binding.clLoading.visibility = View.VISIBLE
        else {
            binding.clLoading.visibility = View.GONE
        }
    }
}