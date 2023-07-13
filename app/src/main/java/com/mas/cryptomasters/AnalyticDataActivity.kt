package com.mas.cryptomasters

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.text.style.QuoteSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.mas.BaseActivity
import com.mas.cryptomasters.data.response.AnalyticData
import com.mas.cryptomasters.databinding.ActivityAnalyticDataBinding
import com.mas.cryptomasters.utils.Navigate
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AnalyticDataActivity :
    BaseActivity<ActivityAnalyticDataBinding>(ActivityAnalyticDataBinding::inflate) {
    private lateinit var analyticData: AnalyticData


    override fun setView(phoneIsConnected: Boolean) {
        binding.ivBack.setOnClickListener(View.OnClickListener { finish() })
        binding.btnClose.setOnClickListener(View.OnClickListener { binding.linear.visibility=View.GONE })
        binding.linear.setOnClickListener(View.OnClickListener { binding.linear.visibility=View.GONE })

        if (intent?.extras?.containsKey(Navigate.DATA_POST) == true) {
            val recommendData = intent?.extras?.getParcelable<AnalyticData>(Navigate.DATA_POST)
            if (recommendData != null) {
                analyticData = recommendData
                binding.textView5.text=analyticData.title
                analyticData.content?.let {
                    displayHtml(it)
                }
            }
        }
    }


    private fun displayHtml(html: String) {
        // Creating object of ImageGetter class you just created
        val imageGetter = ImageGetter(resources, binding.textView6)

        // Using Html framework to parse html
        val styledText =
            HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)

        replaceQuoteSpans(styledText as Spannable)
        ImageClick(styledText as Spannable)

        // setting the text after formatting html and downloading and setting images
        binding.textView6.text = styledText

        // to enable image/link clicking
        binding.textView6.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun replaceQuoteSpans(spannable: Spannable) {
        val quoteSpans: Array<QuoteSpan> =
            spannable.getSpans(0, spannable.length - 1, QuoteSpan::class.java)

        for (quoteSpan in quoteSpans) {
            val start: Int = spannable.getSpanStart(quoteSpan)
            val end: Int = spannable.getSpanEnd(quoteSpan)
            val flags: Int = spannable.getSpanFlags(quoteSpan)
            spannable.removeSpan(quoteSpan)
            spannable.setSpan(
                QuoteSpanClass(
                    // background color
                    ContextCompat.getColor(this, R.color.them),
                    // strip color
                    ContextCompat.getColor(this, R.color.light_green),
                    // strip width
                    10F, 50F
                ),
                start, end, flags
            )
        }
    }

    // Function to parse image tags and enable click events
    fun ImageClick(html: Spannable) {
        for (span in html.getSpans(0, html.length, ImageSpan::class.java)) {
            val flags = html.getSpanFlags(span)
            val start = html.getSpanStart(span)
            val end = html.getSpanEnd(span)
            html.setSpan(object : URLSpan(span.source) {
                override fun onClick(v: View) {
//                    startActivity(
//                        Intent(this@AnalyticDataActivity, PlayerActivity::class.java)
//                            .putExtra("postData", span.source)
//                    )

                    showImg(span.source)
                    Log.e("TAG", "onClick: url is ${span.source}")
                }
            }, start, end, flags)
        }
    }

    private fun showImg(url: String?) {
        binding.linear.visibility=View.VISIBLE
        Picasso.get().load(url).into(binding.imageView2)

    }
//    private fun postImageDialog(url_list: List<String>) {
//        val dialog: Dialog = PopopDialogBuilder(context)
//            .setList(url_list, 0)
//            .setHeaderBackgroundColor(R.color.color_app)
//            .setDialogBackgroundColor(R.color.color_dialog_bg)
//            .setCloseDrawable(R.drawable.ic_close_white_24dp)
//            .showThumbSlider(true)
//            .setSliderImageScaleType(ImageView.ScaleType.FIT_XY)
//            .setIsZoomable(true)
//            .build()
//        dialog.show()
//    }
}