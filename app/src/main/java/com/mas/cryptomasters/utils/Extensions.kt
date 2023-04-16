package com.mas.cryptomasters.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mas.cryptomasters.BuildConfig
import com.mas.cryptomasters.R
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.ui.login.LoginActivity
import com.mas.gettime.GetTime
import es.dmoral.toasty.Toasty
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


object Extensions {


    private var progress: CryptoProgress? = null
    private val gson = Gson()

    fun Context.showProgress() {
        progress = CryptoProgress(this)
        progress?.setCancelable(false)
        progress?.show()
    }

    fun hideProgress() {
        if (progress != null)
            if (progress!!.isShowing)
                progress!!.dismiss()
    }

    fun Context.crToast(
        message: String = this.getString(R.string.general_error),
        type: ToastType = ToastType.FAIL
    ) {
        when (type) {
            ToastType.SUCCESS -> {
                Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
            }
            ToastType.FAIL -> {
                Toasty.error(this, message, Toast.LENGTH_SHORT, true).show()
            }
            else -> {
                Toasty.info(this, message, Toast.LENGTH_SHORT, true).show()
            }
        }
    }

    fun Response<*>.isRequestSuccess(bodyCode: Int? = null): RESPONSE {
        val listAuth = listOf(401)
        val listExist = listOf(404, 422, 500)
        return if (listAuth.contains(this.code())) {
            RESPONSE.AUT
        } else if (listExist.contains(this.code())) {
            RESPONSE.ERROR
        } else {
            if (bodyCode == null) {
                RESPONSE.ERROR
            } else {
                if (this.isSuccessful && this.body() != null && !listAuth.contains(bodyCode) && !listExist.contains(
                        bodyCode
                    )
                ) {
                    RESPONSE.SUCCESS
                } else {
                    RESPONSE.ERROR
                }
            }
        }
    }

    fun Context.permissionRequest(list: List<String>, action: PermissionsAction) {
        Dexter.withContext(this)
            .withPermissions(list).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        action.onGrant()
                    } else {
                        action.onDined()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
    }

    fun Context.registerToken(preferenceHelper: PreferenceHelper) {
        if (false) {
            FirebaseMessaging.getInstance()
                .token.addOnSuccessListener {
                    preferenceHelper.setFCMToken(it)
                }
        }
    }

    fun Context.setTextWithTowColor(
        view: TextView,
        string1: String = getString(R.string.not_member1),
        string2: String = getString(R.string.not_member2),
        color1: String = "#00235E",
        color2: String = "#FDBA21"
    ) {
        val text = "<font color=$color1> $string1 </font> <font color=$color2> $string2 </font>"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.text = Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            view.text = Html.fromHtml(text)
        }
    }

    fun Context.isInputsNotValid(inputs: List<String>): Boolean {
        var isValid = false
        for (string in inputs) {
            if (string.isEmpty())
                isValid = true
        }
        return isValid
    }

    fun Context.setInputErrorHint(inputs: List<EditText>): Boolean {
        var isValid = false
        for (input in inputs) {
            if (input.text.trim().isEmpty()) {
                isValid = true
                input.error = "*"
            } else {
                input.error = null
            }
        }
        return isValid
    }

    fun String.generateVideoLink(): String {
        return "https://crypto.cardproapp.com" + this
    }

    fun ImageView.loadWebImage(url: String, isProfile: Boolean = true, res: Boolean = false) {
        val placeholder: Int = if (isProfile)
            R.drawable.ic_profile
        else
            R.drawable.ic_image_fream

        val imagePath = if (res) url + "" else "https://crypto.cardproapp.com/" + url + ""
        try {
            Glide.with(this.context)
                .load(imagePath)
                .override(this.width, this.height)
                .placeholder(placeholder)
                .into(this)
Log.e("kkkkkkk",imagePath);
        } catch (e: FileNotFoundException) {

        }

    }


    fun String.setTimeFormat(context: Context): String {
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        return GetTime.getTimeAgo(context, this, formatter, 0)
    }

    fun Long.setLongTimeFormat(context: Context): String {
        return GetTime.getTimeAgo(context, this, TimeZone.getDefault().getOffset(this))
    }

    fun Activity.reLogin(preferenceHelper: PreferenceHelper) {
        preferenceHelper.clearUserData().let {
            finishAffinity()
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

    }

    fun String.percentageFormat(): String {
        val format = DecimalFormat("##", DecimalFormatSymbols(Locale.US))
        return try {
            format.format(this.toDouble()) + "%"
        } catch (e: NumberFormatException) {
            "0%"
        }
    }

    fun Bitmap.imageToBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun Context.getBase64Image(imageUri: String): String {
        val imageStream: InputStream = contentResolver.openInputStream(Uri.parse(imageUri))!!
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        return selectedImage.imageToBase64()
    }


    fun Context.showAlert(
        title: String = getString(R.string.alert),
        message: String = getString(R.string.alert_message),
        action: AlertAction
    ) {
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setMessage(message)
            .setTitle(title)
            .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                action.onConfirm()
                dialog.dismiss()
            }.setNegativeButton(getString(R.string.dismiss)) { dialog, _ ->
                dialog.dismiss()
            }

        alertDialog.create()
        alertDialog.show()
    }

    fun Activity.navigateToActivity(
        javaClass: Class<*>,
        finish: Boolean = false,
        bundle: Bundle? = null
    ) {
        val intent = Intent(this, javaClass)
        if (finish) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            this.finish()
        }
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun Activity.askLogin() {
        showAlert(
            title = getString(R.string.login),
            message = getString(R.string.login_first_2),
            action = object : AlertAction {
                override fun onConfirm() {
                    startActivity(
                        Intent(this@askLogin, LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    this@askLogin.finish()
                }
            }
        )
    }


    fun Context.isPhoneIsConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}

enum class RESPONSE {
    AUT, ERROR, SUCCESS
}

enum class ToastType {
    SUCCESS, FAIL, INFO
}
