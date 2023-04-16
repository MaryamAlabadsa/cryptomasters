package com.mas.cryptomasters.ui.forgetPassword

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.mas.BaseActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.databinding.ActivityForgetPasswordBinding
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.Navigate
import java.util.concurrent.TimeUnit

class ForgetPasswordActivity :
    BaseActivity<ActivityForgetPasswordBinding>(ActivityForgetPasswordBinding::inflate) {

    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var mResendingToken: ForceResendingToken
    private lateinit var mCallbacks: OnVerificationStateChangedCallbacks
    private lateinit var mVerificationId: String
    private lateinit var userInputPhone: String

    override fun setView(phoneIsConnected: Boolean) {
        if (!phoneIsConnected){
            crToast(getString(R.string.no_connection))
            binding.tvLogin.isEnabled = false
            return
        }

        // when send code id clicked
        binding.tvLogin.setOnClickListener {
            binding.cpCode.registerCarrierNumberEditText(binding.txtPhone)
            userInputPhone = binding.cpCode.fullNumberWithPlus
            if (binding.cpCode.isValidFullNumber) {
                showProgress()
                startPhoneNumberVerification(userInputPhone)
            } else {
                binding.txtPhone.error = "!"
            }
        }
        // when verify is clicked
        binding.tvVerify.setOnClickListener {
            val code = binding.otpView.otp.toString()
            if (code.length == 6) {
                showProgress()
                verifyPhoneNumberWithCode(mVerificationId, code)
            } else {
                binding.otpView.showError()
            }
        }
        // when resend is clicked
        binding.tvResendCode.setOnClickListener {
            binding.otpView.otp = ""
            phoneArea()
        }

        binding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {
                if (otp.length == 6) {
                    showProgress()
                    verifyPhoneNumberWithCode(mVerificationId, otp)
                }
            }
        }

        // phone callback
        //*********************************************************//
        // Initialize phone auth callbacks
        mCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                hideProgress()
                if (e is FirebaseAuthInvalidCredentialsException) {
                    binding.otpView.showError()
                } else if (e is FirebaseTooManyRequestsException) {
                    crToast()
                }
            }

            override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
                mVerificationId = verificationId
                mResendingToken = token

                hideProgress()
                codeArea()
            }

            override fun onCodeAutoRetrievalTimeOut(s: String) {
                super.onCodeAutoRetrievalTimeOut(s)
                hideProgress()
                if (s.isNotEmpty()) {
                    crToast(s)
                }
            }
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                hideProgress()
                if (task.isSuccessful) {
                    startActivity(
                        Intent(this, ResetPasswordActivity::class.java)
                            .putExtra(Navigate.USER_PHONE, userInputPhone)
                    )
                } else {
                    // the code is not correct
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        crToast(getString(R.string.code_error))
                        binding.otpView.showError()
                    } else {
                        crToast()
                    }
                }
            }
    }

    private fun codeArea() {
        // enable code area
        startCounter()
        binding.tvW1.text = getString(R.string.enter_code, userInputPhone)
        binding.clCodeArea.visibility = View.VISIBLE
        binding.otpView.isEnabled = true
        binding.tvVerify.isClickable = true

        // disable phone area
        binding.clPhoneArea.visibility = View.GONE
        binding.tvLogin.isClickable = false
        binding.txtPhone.isEnabled = false
    }

    private fun phoneArea() {
        // enable phone area
        binding.clPhoneArea.visibility = View.VISIBLE
        binding.tvLogin.isClickable = true
        binding.txtPhone.isEnabled = true

        // disable code area
        binding.clCodeArea.visibility = View.GONE
        binding.otpView.isEnabled = false
        binding.tvResendCode.isClickable = false
        binding.tvResendCode.setTextColor(getColor(R.color.them))
        binding.tvVerify.isClickable = false
    }

    private fun startCounter() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(l: Long) {
                binding.tvResendCode.text =
                    getString(R.string.wait_s_before_resend, (l / 1000).toString())
            }

            override fun onFinish() {
                binding.tvResendCode.text = getString(R.string.resend_now)
                binding.tvResendCode.setTextColor(getColor(R.color.yellow))
                binding.tvResendCode.isEnabled = true
                binding.tvResendCode.isClickable = true
            }
        }.start()
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {
            hideProgress()
            crToast(getString(R.string.code_error))
        }
    }

}