package com.mas.cryptomasters.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.databinding.LanguageDialogBinding
import com.mas.cryptomasters.utils.AlertAction

class LanguageDialog constructor(context: Context, val preferences: PreferenceHelper) :
    Dialog(context) {

    private lateinit var binding: LanguageDialogBinding
    private var alertAction: AlertAction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LanguageDialogBinding.inflate(layoutInflater)
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        setContentView(binding.root)

        if (preferences.getLanguage() == "en") {
            binding.tvEnglish.isChecked = true
            binding.tvArabic.isChecked = false
        } else {
            binding.tvEnglish.isChecked = false
            binding.tvArabic.isChecked = true
        }

        binding.tvArabic.setOnClickListener {
            setLanguage("ar")
            alertAction!!.onConfirm()
        }

        binding.tvEnglish.setOnClickListener {
            setLanguage("en")
            alertAction!!.onConfirm()
        }


    }

    fun setAction(alertAction: AlertAction): LanguageDialog {
        this.alertAction = alertAction
        return this
    }

    private fun setLanguage(language: String) {
        preferences.setLanguage(language)
        dismiss()
    }
}