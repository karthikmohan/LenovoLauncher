package io.esper.lenovolauncher.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import im.delight.android.webview.AdvancedWebView
import io.esper.lenovolauncher.R


class RelaxationFragment : Fragment(), AdvancedWebView.Listener {
    private var mWebView: AdvancedWebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.fragment_relaxtion, container, false)
        mWebView = rootView.findViewById<View>(R.id.webview_relaxation) as AdvancedWebView
        mWebView!!.setDesktopMode(true)
        mWebView!!.setMixedContentAllowed(true)
        mWebView!!.webViewClient = WebViewClient()
        mWebView!!.webChromeClient = MyChrome(activity)
        val webSettings = mWebView!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled(true)
        mWebView!!.loadUrl("https://insighttimer.com/meditation-topics/relax")
        return rootView
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        mWebView!!.onResume()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        mWebView!!.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mWebView!!.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        mWebView!!.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onPageStarted(url: String, favicon: Bitmap) {}
    override fun onPageFinished(url: String) {}
    override fun onPageError(errorCode: Int, description: String, failingUrl: String) {}
    override fun onDownloadRequested(
        url: String,
        suggestedFilename: String,
        mimeType: String,
        contentLength: Long,
        contentDisposition: String,
        userAgent: String
    ) {
    }

    override fun onExternalPageRequest(url: String) {}

    private class MyChrome(private var activity: FragmentActivity?) : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        protected var mFullscreenContainer: FrameLayout? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0
        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(activity!!.applicationContext.resources, 2130837573)
        }

        override fun onHideCustomView() {
            (activity!!.window.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            activity!!.window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null
        }

        override fun onShowCustomView(paramView: View, paramCustomViewCallback: CustomViewCallback) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = paramView
            mOriginalSystemUiVisibility = activity!!.window.decorView.systemUiVisibility
            mCustomViewCallback = paramCustomViewCallback
            (activity!!.window.decorView as FrameLayout).addView(
                mCustomView,
                FrameLayout.LayoutParams(-1, -1)
            )
            activity!!.window.decorView.systemUiVisibility = 3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}