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

    private var care1: String? = null
    private var care2: String? = null
    private var care3: String? = null
    private var care4: String? = null
    private var care5: String? = null
    private var meeting1: String? = null
    private var meeting2: String? = null
    private var meeting3: String? = null
    private var meeting4: String? = null
    private var meeting5: String? = null
    private var featureapp1: String? = null
    private var featureapp2: String? = null
    private var featureapp3: String? = null
    private var featureapp4: String? = null
    private var featureapp5: String? = null

    private var apiKey: String? = null
    private var enterpriseId: String? = null
    private var endpoint: String? = null
    private var patientPhone: String? = null
    private var patientRoom: String? = null
    private var patientName: String? = null
    private var patientId: String? = null
    private var kioskSlideshowImageStrategy: Int = 1
    private var kioskSlideshowDelay: Int = 3
    private var kioskSlideshowPath: String = InternalRootFolder
    private var kioskSlideshow: Boolean = false

    private var changeInValue: Boolean = false

    private fun startManagedConfigValuesReceiver(activity: MainActivity, sharedPrefManaged: SharedPreferences) {
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

        patientName = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_PATIENT_NAME))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_NAME)
                .toString() else null

        patientRoom = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_PATIENT_ROOM))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ROOM)
                .toString() else null

        patientPhone = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_PATIENT_PHONE))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_PHONE)
                .toString() else null

        care1 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_1))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_1)
                .toString() else null

        care2 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_2))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_2)
                .toString() else null

        care3 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_3))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_3)
                .toString() else null

        care4 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_4))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_4)
                .toString() else null

        care5 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_5))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_5)
                .toString() else null

        meeting1 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_1))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_1)
                .toString() else null

        meeting2 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_2))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_2)
                .toString() else null

        meeting3 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_3))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_3)
                .toString() else null

        meeting4 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_4))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_4)
                .toString() else null

        meeting5 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_5))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_UPCOMING_MEETINGS_5)
                .toString() else null

        featureapp1 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_1))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_1)
                .toString() else null

        featureapp2 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_2))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_2)
                .toString() else null

        featureapp3 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_3))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_3)
                .toString() else null

        featureapp4 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_4))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_4)
                .toString() else null

        featureapp5 = if (appRestrictions.containsKey(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_5))
            appRestrictions.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_5)
                .toString() else null

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

        sharedPrefManaged.edit().putBoolean(Constants.SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW, kioskSlideshow)
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

        if (patientId != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ID, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ID, patientId).apply()
            changeInValue = true
        }
        if (patientName != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_NAME, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_PATIENT_NAME, patientName).apply()
            changeInValue = true
        }
        if (patientRoom != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ROOM, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ROOM, patientRoom).apply()
            changeInValue = true
        }
        if (patientPhone != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_PHONE, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_PATIENT_PHONE, patientPhone).apply()
            changeInValue = true
        }

        if (care1 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_1, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_1, care1).apply()
            changeInValue = true
        }
        if (care2 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_2, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_2, care2).apply()
            changeInValue = true
        }
        if (care3 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_3, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_3, care3).apply()
            changeInValue = true
        }
        if (care4 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_4, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_4, care4).apply()
            changeInValue = true
        }
        if (care5 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_5, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_CARE_TEAM_5, care5).apply()
            changeInValue = true
        }

        if (featureapp1 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_1, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_1, featureapp1).apply()
            changeInValue = true
        }
        if (featureapp2 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_2, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_2, featureapp2).apply()
            changeInValue = true
        }
        if (featureapp3 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_3, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_3, featureapp3).apply()
            changeInValue = true
        }
        if (featureapp4 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_4, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_4, featureapp4).apply()
            changeInValue = true
        }
        if (featureapp5 != sharedPrefManaged.getString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_5, null)) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_FEATURED_APP_5, featureapp5).apply()
            changeInValue = true
        }

        if (!endpoint.isNullOrEmpty() && !enterpriseId.isNullOrEmpty() && !apiKey.isNullOrEmpty()) {
            sharedPrefManaged.edit().putString(Constants.SHARED_MANAGED_CONFIG_ENDPOINT, endpoint)
                .putString(Constants.SHARED_MANAGED_CONFIG_ENTERPRISE_ID, enterpriseId)
                .putString(Constants.SHARED_MANAGED_CONFIG_API_KEY, apiKey)
                .apply()
        }

        if (changeInValue) {
            changeInValue = false
            EventBus.getDefault().post(
                MainActivity.RefreshNeeded("ui")
            )
        }
    }
}