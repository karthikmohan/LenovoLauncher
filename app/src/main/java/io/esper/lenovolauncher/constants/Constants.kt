@file:Suppress("DEPRECATION")

package io.esper.lenovolauncher.constants

import android.content.SharedPreferences
import android.os.Environment
import io.esper.lenovolauncher.model.HospitalDbItem
import java.io.File

object Constants {

    const val storagePermission = 100
    var InternalRootFolder: String = Environment.getExternalStorageDirectory()
        .path + File.separator + "esperfiles" + File.separator
    var InternalCheckerString: String = "/storage/emulated/0/"

    var ExternalRootFolder: String = "esperfiles" + File.separator

    var InternalScreenshotFolderDCIM: String = Environment.getExternalStorageDirectory()
        .path + File.separator + "DCIM" + File.separator + "Screenshots" + File.separator
    var InternalScreenshotFolderPictures: String = Environment.getExternalStorageDirectory()
        .path + File.separator + "Pictures" + File.separator + "Screenshots" + File.separator

    @Suppress("SpellCheckingInspection")
    var EsperScreenshotFolder: String = InternalRootFolder + "Screenshots"

    //Tags
    const val MainActivityTag = "MainActivity"
    const val ListItemsFragmentTag = "ListItemsFragment"
    const val FileUtilsTag = "FileUtils"
    const val VideoViewerActivityTag = "VideoViewerActivity"
    const val ImageViewerActivityTag = "ImageViewerActivity"
    const val SlideShowActivityTag = "SlideShowActivity"
    const val BottomSheetFragmentTag = "BottomSheetFragment"
    const val ContentAdapterTag = "ContentAdapter"
    const val DownloadUtilsTag = "DownloadUtils"
    const val SDKUtilsTag = "SDKUtils"
    const val MyStayFragmentTag = "MyStayFragment"
    const val EntertainmentFragmentTag = "EntertainmentFragment"

    const val SHARED_PREF_DEVICE_ID = "deviceId"

    var allResults: MutableList<HospitalDbItem> = ArrayList()

    //SharedPref
    @kotlin.jvm.JvmField
    var sharedPref: SharedPreferences? = null

    @kotlin.jvm.JvmField
    var sharedPrefManaged: SharedPreferences? = null

    // SharedPreference keys
    const val SHARED_MANAGED_CONFIG_VALUES = "ManagedConfig"

    const val SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW = "kiosk_slideshow"
    const val SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_PATH = "kiosk_slideshow_path"
    const val SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_DELAY = "kiosk_slideshow_delay"
    const val SHARED_MANAGED_CONFIG_KIOSK_SLIDESHOW_IMAGE_STRATEGY =
        "kiosk_slideshow_image_strategy"

    // Healthcare Specific
    const val SHARED_MANAGED_CONFIG_PATIENT_ID = "patient_id"
    const val SHARED_MANAGED_CONFIG_PATIENT_NAME = "patient_name"
    const val SHARED_MANAGED_CONFIG_PATIENT_ROOM = "patient_room"
    const val SHARED_MANAGED_CONFIG_LAUNCHER_WALLPAPER = "launcher_wallpaper"

    // Esper Specific
    const val SHARED_MANAGED_CONFIG_ENDPOINT = "endpoint"
    const val SHARED_MANAGED_CONFIG_ENTERPRISE_ID = "enterprise_id"
    const val SHARED_MANAGED_CONFIG_API_KEY = "api_key"
}