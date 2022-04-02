@file:Suppress("DEPRECATION")

package io.esper.lenovolauncher.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Build.VERSION
import android.os.StrictMode
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.esper.lenovolauncher.constants.Constants
import io.esper.lenovolauncher.constants.Constants.SHARED_MANAGED_CONFIG_VALUES
import io.esper.lenovolauncher.constants.Constants.sharedPrefManaged
import io.esper.lenovolauncher.activity.MainActivity
import io.esper.lenovolauncher.utils.ManagedConfigUtils.getManagedConfigValues
import java.io.File

object GeneralUtils {
    fun createDir(mCurrentPath: String) {
        val fileDirectory = File(mCurrentPath)
        if (!fileDirectory.exists())
            fileDirectory.mkdir()
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null)
            view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 300
        view.startAnimation(anim)
    }

    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    fun requestPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
            ), Constants.storagePermission
        )
    }

    @JvmStatic
    fun initSharedPrefs(context: Context) {
        sharedPrefManaged = context.getSharedPreferences(SHARED_MANAGED_CONFIG_VALUES, Context.MODE_PRIVATE)
    }

    @JvmStatic
    fun initNetworkConfigs() {
        //Used for Glide Image Loader
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    @JvmStatic
    fun initPremissions(context: Context) {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkPermission(context)) requestPermission(context)
    }

    private fun checkPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    @JvmStatic
    fun initManagedConfig(mainActivity: MainActivity) {
        getManagedConfigValues(mainActivity, sharedPrefManaged!!)
    }
}