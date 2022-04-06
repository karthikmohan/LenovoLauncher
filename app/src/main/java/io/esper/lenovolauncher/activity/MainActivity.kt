@file:Suppress("DEPRECATION")

package io.esper.lenovolauncher.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.constants.Constants
import io.esper.lenovolauncher.constants.Constants.allResults
import io.esper.lenovolauncher.constants.Constants.sharedPrefManaged
import io.esper.lenovolauncher.databinding.ActivityMainBinding
import io.esper.lenovolauncher.fragment.MyStayFragment
import io.esper.lenovolauncher.model.HospitalDbItem
import io.esper.lenovolauncher.utils.FileUtils
import io.esper.lenovolauncher.utils.GeneralUtils.initManagedConfig
import io.esper.lenovolauncher.utils.GeneralUtils.initNetworkConfigs
import io.esper.lenovolauncher.utils.GeneralUtils.initPremissions
import io.esper.lenovolauncher.utils.GeneralUtils.initSharedPrefs
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import java.io.IOException
import java.lang.reflect.Type
import java.util.*


class MainActivity : AppCompatActivity() {
    private val mHideHandler = Handler()

//    private lateinit var mHandler: Handler
//    private lateinit var mRunnable: Runnable
//    private var mTime: Long = 2000

//    private val mHidePart2Runnable = Runnable {
//        // Delayed removal of status and navigation bar
//        if (VERSION.SDK_INT >= 30) {
//            binding.greetingText.windowInsetsController!!.hide(
//                WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
//            )
//        } else {
//            binding.greetingText.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
//                    or View.SYSTEM_UI_FLAG_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
//        }
//    }

    private var mVisible = false

    //    private val mHideRunnable = Runnable { hide() }
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.main_fragment)
        setupActionBarWithNavController(navController)
        setupSmoothBottomMenu()

        mVisible = true
        preInit()

        addItemsFromJSON(this)
        greetingSetter()
        roomAndIdSetter()
        wallpaperSetter()

        binding.settings.setOnClickListener { startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }

//        interactionDetector()


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        binding.bottomBar.setOnTouchListener(mDelayHideTouchListener)
    }

    @SuppressLint("SetTextI18n")
    private fun roomAndIdSetter() {
        if (sharedPrefManaged != null)
            sharedPrefManaged =
                getSharedPreferences(Constants.SHARED_MANAGED_CONFIG_VALUES, Context.MODE_PRIVATE)
        if (sharedPrefManaged?.getString(
                Constants.SHARED_MANAGED_CONFIG_PATIENT_ROOM,
                null
            ) != null && sharedPrefManaged?.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                null) != null
        )
            binding.greetingText2.text = "Room No.: ${
                sharedPrefManaged?.getString(
                    Constants.SHARED_MANAGED_CONFIG_PATIENT_ROOM,
                    null
                )
            } | " + "ID: ${
                sharedPrefManaged?.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                    null)
            }"
        else
            binding.greetingText2.text = ""
    }

//    private fun interactionDetector() {
//        mHandler = Handler(Looper.getMainLooper())
//        mRunnable = Runnable {
//            hide()
//        }
//        startHandler()
//    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        stopHandler()
//        startHandler()
//        return super.onTouchEvent(event)
//    }
//
//    private fun startHandler() {
//        mHandler.postDelayed(mRunnable, mTime)
//    }
//
//    private fun stopHandler() {
//        mHandler.removeCallbacks(mRunnable)
//    }

    @SuppressLint("SetTextI18n")
    private fun greetingSetter() {
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        val greeting = when (cal[Calendar.HOUR_OF_DAY]) {
            in 6..11 -> {
                "Good Morning"
            }
            in 12..16 -> {
                "Good Afternoon"
            }
            in 17..20 -> {
                "Good Evening"
            }
            else -> {
                "Good Night"
            }
        }
        if (sharedPrefManaged != null)
            sharedPrefManaged =
                getSharedPreferences(Constants.SHARED_MANAGED_CONFIG_VALUES, Context.MODE_PRIVATE)

        if (sharedPrefManaged?.getString(
                Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                null
            ) != null
        ) {
            binding.greetingText.text =
                "$greeting, " + sharedPrefManaged!!.getString(Constants.SHARED_MANAGED_CONFIG_PATIENT_NAME,
                    null) + " !"
        } else {
            binding.greetingText.text = "$greeting !"
        }
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, greetingText)
        popupMenu.inflate(R.menu.menu_bottom)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//    override fun onPostCreate(savedInstanceState: Bundle?) {
//        super.onPostCreate(savedInstanceState)
//
//        // Trigger the initial hide() shortly after the activity has been
//        // created, to briefly hint to the user that UI controls
//        // are available.
//        delayedHide(100)
//    }

    private fun preInit() {
        hideNavAndStatusBar()
        initSharedPrefs(this)
        initNetworkConfigs()
        initPremissions(this)
        initManagedConfig(this)
    }

    private fun hideNavAndStatusBar() {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

//    override fun onResume() {
//        mHideHandler.postDelayed(Runnable {
//            runnable?.let { mHideHandler.postDelayed(it, delay.toLong()) }
//            hide()
//        }.also { runnable = it }, delay.toLong())
//        super.onResume()
//    }

//    private fun hide() {
//        // Hide UI first
//        val actionBar = supportActionBar
//        actionBar?.hide()
//        mVisible = false
//
//        // Schedule a runnable to remove the status and navigation bar after a delay
//        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
//    }

//    /**
//     * Schedules a call to hide() in delay milliseconds, canceling any
//     * previously scheduled calls.
//     */
//    private fun delayedHide(delayMillis: Int) {
//        mHideHandler.removeCallbacks(mHideRunnable)
//        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RefreshNeeded) {
        addItemsFromJSON(this)
        savePatientNameAndRoom()

        if (event.refresh == "ui") {
            greetingSetter()
            roomAndIdSetter()
            EventBus.getDefault().post(
                MyStayFragment.RefreshNeededInFragment("ui")
            )
        } else if (event.refresh == "wallpaper") {
            wallpaperSetter()
        }
    }

    private fun wallpaperSetter() {
        if (sharedPrefManaged?.getString(
                Constants.SHARED_MANAGED_CONFIG_LAUNCHER_WALLPAPER,
                null
            ) != null
        ) {
            Glide.with(this).load(
                sharedPrefManaged?.getString(
                    Constants.SHARED_MANAGED_CONFIG_LAUNCHER_WALLPAPER,
                    null
                )
            ).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).placeholder(R.drawable.bg2)
                .priority(Priority.HIGH).into(object : CustomTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        @Nullable transition: Transition<in Drawable?>?,
                    ) {
                        main_layout.background = resource
                    }

                    override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                })
        } else {
            main_layout.background = ContextCompat.getDrawable(this, R.drawable.bg2)
        }
    }

    private fun savePatientNameAndRoom() {
        for (i in 0 until allResults.size) {
            sharedPrefManaged =
                getSharedPreferences(Constants.SHARED_MANAGED_CONFIG_VALUES, Context.MODE_PRIVATE)
            if (sharedPrefManaged?.getString(
                    Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                    null
                ) != null && allResults[i].patientId == sharedPrefManaged?.getString(
                    Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                    null
                )
            ) {
                sharedPrefManaged!!.edit().putString(Constants.SHARED_MANAGED_CONFIG_PATIENT_NAME,
                    allResults[i].patientName)
                    .putString(Constants.SHARED_MANAGED_CONFIG_PATIENT_ROOM,
                        allResults[i].patientRoom).apply()
                break
            }
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

    class RefreshNeeded(val refresh: String)

    companion object {
        fun addItemsFromJSON(context: Context) {
            try {
                val jsonDataString = FileUtils.readJSONDataFromFile(context)
                val collectionType: Type = object : TypeToken<List<HospitalDbItem?>?>() {}.type
                allResults = Gson()
                    .fromJson(jsonDataString, collectionType) as MutableList<HospitalDbItem>
            } catch (e: JSONException) {
                Log.d(Constants.MyStayFragmentTag, "addItemsFromJSON: ", e)
            } catch (e: IOException) {
                Log.d(Constants.MyStayFragmentTag, "addItemsFromJSON: ", e)
            }
        }

        private const val AUTO_HIDE = true
        private const val AUTO_HIDE_DELAY_MILLIS = 3000
        private const val UI_ANIMATION_DELAY = 300
        private var runnable: Runnable? = null
        private var delay = 3000
    }
}