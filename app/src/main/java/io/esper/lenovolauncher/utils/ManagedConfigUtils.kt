package io.esper.lenovolauncher.utils

import android.content.*
import android.os.Bundle
import android.os.UserManager
import io.esper.lenovolauncher.activity.MainActivity
import io.esper.lenovolauncher.activity.SlideshowActivity
import io.esper.lenovolauncher.constants.Constants
import io.esper.lenovolauncher.constants.Constants.InternalRootFolder
import org.greenrobot.eventbus.EventBus

object ManagedConfigUtils {

    private var apiKey: String? = null
    private var enterpriseId: String? = null
    private var endpoint: String? = null
    private var patientId: String? = null
    private var launcherWallpaper: String? = null
    private var sampleData: Boolean? = null
    private var kioskSlideshowImageStrategy: Int = 1
    private var kioskSlideshowDelay: Int = 3
    private var kioskSlideshowPath: String = InternalRootFolder
    private var kioskSlideshow: Boolean = false

    private fun startManagedConfigValuesReceiver(
        activity: MainActivity,
        sharedPrefManaged: SharedPreferences,
    ) {
        val myRestrictionsMgr =
            activity.getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager
        val restrictionsFilter = IntentFilter(Intent.ACTION_APPLICATION_RESTRICTIONS_CHANGED)

        val restrictionsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                val appRestrictions = myRestrictionsMgr.applicationRestrictions

                getValueFromRestrictions(appRestrictions)
                savingInSharedPrefs(activity, sharedPrefManaged)
            }
        }
        activity.registerReceiver(restrictionsReceiver, restrictionsFilter)
    }

    @JvmStatic
    fun getManagedConfigValues(activity: MainActivity, sharedPrefManaged: SharedPreferences) {

        var restrictionsBundle: Bundle?
        val userManager =
            activity.getSystemService(Context.USER_SERVICE) as UserManager
        restrictionsBundle = userManager.getApplicationRestrictions(activity.packageName)
        if (restrictionsBundle == null) {
            restrictionsBundle = Bundle()
        }

        getValueFromRestrictions(restrictionsBundle)
        savingInSharedPrefs(activity, sharedPrefManaged)

        startManagedConfigValuesReceiver(activity, sharedPrefManaged)
    }

    private fun getValueFromRestrictions(appRestrictions: Bundle) {
        kioskSlideshow =
            if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW))
                appRestrictions.getBoolean(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW) else false

        kioskSlideshowPath =
            if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_PATH))
                appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_PATH)
                    .toString() else InternalRootFolder

        kioskSlideshowDelay =
            if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_DELAY))
                appRestrictions.getInt(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_DELAY) else 3

        kioskSlideshowImageStrategy =
            if (appRestrictions.containsKey(
                    Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_IMAGE_STRATEGY
                )
            )
                appRestrictions.getInt(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_IMAGE_STRATEGY) else 1

        patientId = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_PATIENT_ID))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ID)
                .toString() else null

        launcherWallpaper =
            if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_LAUNCHER_WALLPAPER))
                appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_LAUNCHER_WALLPAPER)
                    .toString() else null

        sampleData =
            if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_SAMPLE_DATA))
                appRestrictions.getBoolean(Constants.SHARED_MANAGED_CONFIG_SAMPLE_DATA) else false

        endpoint =
            if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_ENDPOINT))
                appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_ENDPOINT) else null

        enterpriseId =
            if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_ENTERPRISE_ID))
                appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_ENTERPRISE_ID) else null

        apiKey =
            if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_API_KEY))
                appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_API_KEY) else null
    }

    private fun savingInSharedPrefs(activity: MainActivity, sharedPrefManaged: SharedPreferences) {

        sharedPrefManaged.edit()
            .putBoolean(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW, kioskSlideshow)
            .apply()
        sharedPrefManaged.edit().putString(
            Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_PATH,
            kioskSlideshowPath
        ).apply()
        sharedPrefManaged.edit().putInt(
            Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_DELAY,
            kioskSlideshowDelay
        ).apply()
        sharedPrefManaged.edit().putInt(
            Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_IMAGE_STRATEGY,
            kioskSlideshowImageStrategy
        ).apply()
        if (sharedPrefManaged.getBoolean(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW, false))
            activity.startActivity(Intent(activity, SlideshowActivity::class.java))

        if (patientId != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                null)
        ) {
            sharedPrefManaged.edit()
                .putString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ID, patientId).apply()
            EventBus.getDefault().post(MainActivity.RefreshNeeded("ui"))
        }

        if (launcherWallpaper != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_LAUNCHER_WALLPAPER,
                null)
        ) {
            sharedPrefManaged.edit()
                .putString(Constants.SHARED_MANAGED_CONFIG_LAUNCHER_WALLPAPER, launcherWallpaper)
                .apply()
            EventBus.getDefault().post(MainActivity.RefreshNeeded("wallpaper"))
        }

        if (sampleData != sharedPrefManaged.getBoolean(Constants.SHARED_MANAGED_CONFIG_SAMPLE_DATA,
                false)
        ) {
            sampleData?.let {
                sharedPrefManaged.edit()
                    .putBoolean(Constants.SHARED_MANAGED_CONFIG_SAMPLE_DATA, it)
                    .apply()
            }
            EventBus.getDefault().post(MainActivity.RefreshNeeded("ui"))
        }

        if (!endpoint.isNullOrEmpty() && !enterpriseId.isNullOrEmpty() && !apiKey.isNullOrEmpty()) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_ENDPOINT, endpoint)
                .putString(Constants.SHARED_MANAGED_CONFIG_ENTERPRISE_ID, enterpriseId)
                .putString(Constants.SHARED_MANAGED_CONFIG_API_KEY, apiKey)
                .apply()
        }
    }
}