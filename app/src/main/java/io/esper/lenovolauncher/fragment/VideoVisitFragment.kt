@file:Suppress("DEPRECATION")

package io.esper.lenovolauncher.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import im.delight.android.webview.AdvancedWebView
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.constants.Constants
import io.esper.lenovolauncher.constants.Constants.sharedPrefManaged
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class VideoVisitFragment : Fragment(), AdvancedWebView.Listener {
    private lateinit var ctx: Context
    private lateinit var mWebView: AdvancedWebView
    private lateinit var featureNotAvailable: TextView
    private lateinit var loading: LinearLayout

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_video_visit, container, false)
        mWebView = rootView.findViewById<View>(R.id.webview_video_visit) as AdvancedWebView
        featureNotAvailable =
            rootView.findViewById<View>(R.id.feature_not_available_video_visit) as TextView
        loading =
            rootView.findViewById<View>(R.id.feature_loading_video_visit) as LinearLayout

        mWebView.setDesktopMode(true)
        mWebView.settings.builtInZoomControls = true
        mWebView.settings.displayZoomControls = false
        mWebView.settings.setAppCacheMaxSize(5 * 1024 * 1024)
        mWebView.settings.setAppCachePath(requireContext().applicationContext.cacheDir.absolutePath)
        mWebView.settings.allowFileAccess = true
        mWebView.settings.setAppCacheEnabled(true)
        mWebView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        mWebView.setMixedContentAllowed(true)
        mWebView.setMixedContentAllowed(true)
        mWebView.webViewClient = WebViewClient()
        mWebView.webChromeClient = MyChrome(activity)
        val webSettings = mWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled(true)

        checkForFeatureAvailability()
        return rootView
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        mWebView.onResume()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        mWebView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mWebView.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        mWebView.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onPageStarted(url: String, favicon: Bitmap) {
        mWebView.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }
    override fun onPageFinished(url: String) {
        mWebView.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }
    override fun onPageError(errorCode: Int, description: String, failingUrl: String) {}
    override fun onDownloadRequested(
        url: String,
        suggestedFilename: String,
        mimeType: String,
        contentLength: Long,
        contentDisposition: String,
        userAgent: String,
    ) {
    }

    override fun onExternalPageRequest(url: String) {}

    private class MyChrome(private var activity: FragmentActivity?) : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null

        //        protected var mFullscreenContainer: FrameLayout? = null
//        private var mOriginalOrientation = 0
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

        override fun onShowCustomView(
            paramView: View,
            paramCustomViewCallback: CustomViewCallback,
        ) {
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
            activity!!.window.decorView.systemUiVisibility =
                3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        override fun onPermissionRequest(request: PermissionRequest) {
            val resources = request.resources
            when (resources[0]) {
                PermissionRequest.RESOURCE_AUDIO_CAPTURE -> {
//                    Toast.makeText(activity, "Audio Permission", Toast.LENGTH_SHORT).show()
                    request.grant(arrayOf(PermissionRequest.RESOURCE_AUDIO_CAPTURE))
                }
                PermissionRequest.RESOURCE_MIDI_SYSEX -> {
//                    Toast.makeText(activity, "MIDI Permission", Toast.LENGTH_SHORT).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        request.grant(arrayOf(PermissionRequest.RESOURCE_MIDI_SYSEX))
                    }
                }
                PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID -> {
//                    Toast.makeText(activity, "Encrypted media permission", Toast.LENGTH_SHORT).show()
                    request.grant(arrayOf(PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID))
                }
                PermissionRequest.RESOURCE_VIDEO_CAPTURE -> {
//                    Toast.makeText(activity, "Video Permission", Toast.LENGTH_SHORT).show()
                    request.grant(arrayOf(PermissionRequest.RESOURCE_VIDEO_CAPTURE))
                }
            }
        }
    }

    class RefreshNeededInFragment

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RefreshNeededInFragment) {
        checkForFeatureAvailability()
    }

    private fun checkForFeatureAvailability() {
        if (sharedPrefManaged == null) {
            sharedPrefManaged = ctx.getSharedPreferences(Constants.SHARED_MANAGED_CONFIG_VALUES,
                Context.MODE_PRIVATE)
        }
        if (sharedPrefManaged!!.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                null) != null && sharedPrefManaged!!.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_NAME,
                null) != null
        ) {
            featureNotAvailable.visibility = View.GONE
            mWebView.loadUrl(
                "https://meet.jit.si/" + sharedPrefManaged!!.getString(
                    Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                    null
                )
            )
        } else {
            mWebView.visibility = View.GONE
            featureNotAvailable.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}