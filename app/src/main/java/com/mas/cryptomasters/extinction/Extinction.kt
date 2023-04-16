package com.mas.cryptomasters.extinction

import android.view.View
import android.view.animation.AnimationUtils
import com.mas.cryptomasters.R

class Extinction {

    companion object {
        fun View.animateView(type: Int = R.anim.fade_in) {
            val animas = AnimationUtils.loadAnimation(this.context, type)
            this.startAnimation(animas)
        }

    }
}