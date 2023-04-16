package com.mas.cryptomasters.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.databinding.DeleteAdsDialogBinding
import com.mas.cryptomasters.utils.Constants

class DeleteAdsDialog constructor(context: Context, val preferences: PreferenceHelper) :
    Dialog(context) {
    private lateinit var binding: DeleteAdsDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeleteAdsDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))



        binding.ivTelgram.setOnClickListener {
            if (preferences.getAppSettings().tg != null && preferences.getAppSettings().tg == "null") {
                return@setOnClickListener
            }

            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.telegramLink + preferences.getAppSettings().tg)
                )
            )
        }
        binding.ivWhatsApp.setOnClickListener {
            if (preferences.getAppSettings().whts != null && preferences.getAppSettings().whts == "null") {
                return@setOnClickListener
            }
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.whatsappLink + preferences.getAppSettings().whts)
                )
            )
        }


        binding.ivClose.setOnClickListener {
            dismiss()
        }

    }
}