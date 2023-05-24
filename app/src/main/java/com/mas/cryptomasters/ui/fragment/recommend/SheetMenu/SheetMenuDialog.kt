package com.mas.cryptomasters.ui.fragment.recommend.SheetMenu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mas.cryptomasters.adapters.PlanAdapter
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.data.models.PlanResponse
import com.mas.cryptomasters.databinding.BottomSheetLayoutBinding


@AndroidEntryPoint
class SheetMenuDialog(private val menuViewModel: MenuViewModel) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetLayoutBinding
    private lateinit var planAdapter: PlanAdapter
    lateinit var preferences: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        preferences = PreferenceHelper(requireContext())
        binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        planAdapter = PlanAdapter(preferences, requireActivity())
        binding.rvSubscription.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = planAdapter
        }

        binding.whatup.setOnClickListener {
            val whatsappIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=${preferences.getAppSettings() .whts}")
            )
            startActivity(whatsappIntent)
        }
        binding.tel.setOnClickListener {
            val base = "http://telegram.me/"
            val tel = preferences.getAppSettings() .tg
            val telegramIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(base + tel))
            startActivity(telegramIntent)
        }
        // Observe changes to menu items
        menuViewModel.menuMutable.observe(viewLifecycleOwner) { response ->
            when {
                response.reLogin -> {
                    // Handle re-login
                }
                response.error.isNotEmpty() -> {
                    // Handle error
                }
                response.data != null && response.flag == 1 -> {
                    // Update menu items
                    planAdapter.updateAdapter((response.data as PlanResponse).data)
                }
            }
        }
    }
}
