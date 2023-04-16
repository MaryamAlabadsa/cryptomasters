package com.mas.cryptomasters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.mas.cryptomasters.core.pref.PreferenceHelper
import com.mas.cryptomasters.databinding.LoadingLayoutBinding
import com.mas.cryptomasters.ui.othersActivity.NavigationActivity
import com.mas.cryptomasters.utils.Extensions.askLogin
import com.mas.cryptomasters.utils.Extensions.crToast
import com.mas.cryptomasters.utils.Extensions.isPhoneIsConnected
import com.mas.cryptomasters.utils.Extensions.navigateToActivity
import com.mas.cryptomasters.utils.Navigate
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    lateinit var preferences: PreferenceHelper
    lateinit var gson: Gson

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val type = javaClass.genericSuperclass
        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<*>
        val method = clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        _binding = method.invoke(null, layoutInflater, container, false) as VB
        gson = Gson()
        preferences = PreferenceHelper(requireContext())
        
        if (requireContext().isPhoneIsConnected()){
            init()
        }else{
            requireContext().crToast(getString(R.string.no_connection))
        }
        return _binding!!.root
    }


    abstract fun getViewBinding(): VB


    abstract fun init()

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun handleLoading(binding: LoadingLayoutBinding, isError: Boolean) {
        if (isError) {
            binding.clOnLoading.visibility = View.GONE

            binding.clLoading.visibility = View.VISIBLE
            binding.clOnError.visibility = View.VISIBLE
        } else {
            binding.clLoading.visibility = View.GONE
        }
    }


    fun navigateToNotifications(activity: Activity) {
        if (preferences.isGustUser()) {
            activity.askLogin()
        } else {
            val bundle = Bundle()
            bundle.putString(Navigate.TARGET, Navigate.NOTIFICATION)
            activity.navigateToActivity(NavigationActivity::class.java, bundle = bundle)
        }
    }


}