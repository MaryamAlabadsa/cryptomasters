package com.mas.cryptomasters.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels

import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.mas.BaseActivity
import com.mas.cryptomasters.R
import com.mas.cryptomasters.data.request.LoginByGoogleRequest
import com.mas.cryptomasters.data.request.LoginRequest
import com.mas.cryptomasters.data.response.ProfileResponse
import com.mas.cryptomasters.databinding.ActivityLoginBinding
import com.mas.cryptomasters.ui.forgetPassword.ForgetPasswordActivity
import com.mas.cryptomasters.ui.main.MainActivity
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.ui.signup.RegisterActivity
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.hideProgress
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Extensions.registerToken
import com.mas.cryptomasters.utils.Extensions.setTextWithTowColor
import com.mas.cryptomasters.utils.Extensions.showProgress
import com.mas.cryptomasters.utils.Navigate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()
    private val RC_SIGN_IN = 123
    private lateinit var mGoogleSignInClient: GoogleSignInClient
//    private lateinit var callbackManager: CallbackManager

    var context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView(isPhoneConnected())
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set Facebook App ID
//        FacebookSdk.setApplicationId("YOUR_FACEBOOK_APP_ID")
//
//        // Initialize Facebook SDK
//        FacebookSdk.sdkInitialize(applicationContext)
//        callbackManager = CallbackManager.Factory.create()
    }

    private fun isPhoneConnected(): Boolean {
        // Implement your logic here to check if the phone is connected
        return true
    }

    override fun setView(phoneIsConnected: Boolean) {
        if (!phoneIsConnected) {
            crToast(getString(R.string.no_connection))
            binding.tvLogin.isEnabled = false
            return
        }

        setTextWithTowColor(binding.tvSingUp)

        registerToken(preferenceHelper)

        binding.tvLogin.setOnClickListener {
            binding.txtPassword.error = null
            binding.txtPhone.error = null
            val password = binding.txtPassword.text.toString()
            binding.cpCode.registerCarrierNumberEditText(binding.txtPhone)
            val phone = binding.cpCode.fullNumberWithPlus

            if (binding.cpCode.isValidFullNumber && password.length >= 6) {
                showProgress()
                viewModel.loginRequest = LoginRequest(
                    deviceToken = preferenceHelper.getFCMToken(),
                    password = password,
                    phone = phone
                )
                viewModel.startLogin()
            } else {
                if (password.length < 6) {
                    binding.txtPassword.error = "*"
                    crToast(getString(R.string.password_validation))
                }

                if (!binding.cpCode.isValidFullNumber) {
                    binding.txtPhone.error = "*"
                }
            }
        }

        viewModel.profile.observe(this) { result ->
            when {
                result.data != null -> {
                    val profileResponse = result.data as ProfileResponse
                    preferenceHelper.setUserProfile(profileResponse.profileObject!!)
                        .apply {
                            preferenceHelper.setGuestStatus(false)
                            hideProgress()
                            navigateToActivity(MainActivity::class.java, true)
                        }
                }
                result.reLogin -> {
                    crToast(getString(R.string.check_data))
                    hideProgress()
                }
                result.error.isNotEmpty() -> {
                    crToast()
                    hideProgress()
                }
            }
        }

        binding.tvSingUp.setOnClickListener {
            navigateToActivity(RegisterActivity::class.java)
        }

        binding.tvForgetPassword.setOnClickListener {
            navigateToActivity(ForgetPasswordActivity::class.java)
        }

        binding.signInButton.setOnClickListener {
            signIn()
        }
//        binding.faceBookLoginButton.setOnClickListener {
//            signInWithFacebook()
//        }

        binding.tvTerms.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Navigate.TARGET, Navigate.TERMS)
            navigateToActivity(NavigationActivity::class.java, bundle = bundle)
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        handleSignInResult(task)
//        callbackManager.onActivityResult(requestCode, resultCode, data)


    }

//    private fun signInWithFacebook() {
//        // Set the required permissions
//        val loginManager = LoginManager.getInstance()
//        loginManager.logInWithReadPermissions(this, listOf("email"))
//
//        // Register a callback for the Facebook sign-in result
//        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onCancel() {
//                // Handle canceled sign-in
//            }
//
//
//            override fun onError(error: FacebookException) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSuccess(result: LoginResult) {
//                val accessToken = result.accessToken
//            }
//        })
//    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct = completedTask.getResult(ApiException::class.java)
            updateUI(acct)
        } catch (e: ApiException) {
            val statusCode = e.statusCode
            val errorMessage = GoogleSignInStatusCodes.getStatusCodeString(statusCode)
            Log.e("TAGMaryam", "Error message: $errorMessage")

            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            viewModel.loginByGoogleRequest =
                account.displayName?.let {
                    account.email?.let { it1 ->
                        LoginByGoogleRequest(
                            deviceToken = preferenceHelper.getFCMToken(),
                            name = it,
                            email = it1,
                            avatar = "",
                            registerBy = "google"
                        )
                    }
                }!!
            showProgress()
            viewModel.startLoginByGoogle()
        }

    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }
}
