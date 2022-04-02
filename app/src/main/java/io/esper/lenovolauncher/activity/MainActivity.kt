@file:Suppress("DEPRECATION")

package io.esper.lenovolauncher.activity

import android.annotation.SuppressLint
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.databinding.ActivityMainBinding
import io.esper.lenovolauncher.utils.GeneralUtils.initManagedConfig
import io.esper.lenovolauncher.utils.GeneralUtils.initNetworkConfigs
import io.esper.lenovolauncher.utils.GeneralUtils.initPremissions
import io.esper.lenovolauncher.utils.GeneralUtils.initSharedPrefs
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class MainActivity : AppCompatActivity() {
    private val mHideHandler = Handler()

//    private lateinit var mHandler: Handler
//    private lateinit var mRunnable: Runnable
//    private var mTime: Long = 2000

    private var mContentView: View? = null
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (VERSION.SDK_INT >= 30) {
            mContentView!!.windowInsetsController!!.hide(
                WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()
            )
        } else {
            mContentView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

    private var mVisible = false
    private val mHideRunnable = Runnable { hide() }
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
        mContentView = binding.greetingText
        preInit()

        greetingSetter()
//        interactionDetector()


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        binding.bottomBar.setOnTouchListener(mDelayHideTouchListener)
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

    private fun greetingSetter() {
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        val hour = cal[Calendar.HOUR_OF_DAY]
        var greeting: String? = null
        when {
            hour in 6..11 -> {
                greeting = "Good Morning"
            }
            hour in 12..16 -> {
                greeting = "Good Afternoon"
            }
            hour in 17..20 -> {
                greeting = "Good Evening"
            }
            hour >= 21 -> {
                greeting = "Good Night"
            }
        }
        binding.greetingText.text = "$greeting, Jonathan"
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun preInit() {
        initSharedPrefs(this)
        initNetworkConfigs()
        initPremissions(this)
        initManagedConfig(this)
    }

    override fun onResume() {
        mHideHandler.postDelayed(Runnable {
            runnable?.let { mHideHandler.postDelayed(it, delay.toLong()) }
            hide()
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }

    private fun hide() {
        // Hide UI first
        val actionBar = supportActionBar
        actionBar?.hide()
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RefreshNeeded) {
        if (event.equals("ui")) {
            //Todo Added UI Refresher
        } else if (event.equals("wallpaper")) {
            //Todo Wallpaper Setter
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

    class RefreshNeeded(val event: String)

    companion object {
        private const val AUTO_HIDE = true
        private const val AUTO_HIDE_DELAY_MILLIS = 3000
        private const val UI_ANIMATION_DELAY = 300
        private var runnable: Runnable? = null
        private var delay = 20000
    }
}