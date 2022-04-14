package io.esper.lenovolauncher.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.activity.MainActivity
import io.esper.lenovolauncher.adapter.HelloAdapter
import io.esper.lenovolauncher.constants.Constants
import io.esper.lenovolauncher.constants.Constants.allResults
import io.esper.lenovolauncher.constants.Constants.sharedPrefManaged
import kotlinx.android.synthetic.main.fragment_hello.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HelloFragment : Fragment() {

    private lateinit var ctx: Context


    private var helloAdapter: HelloAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hello, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkForFeatureAvailability()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun assignHelloData() {
        layoutManager = LinearLayoutManager(ctx)
        hello_fragment_recycler_view.layoutManager = GridLayoutManager(ctx, 2)
        if (!allResults.isNullOrEmpty()) {
            hello_fragment_recycler_view.visibility = View.VISIBLE
            feature_not_available_hello.visibility = View.GONE
            helloAdapter = HelloAdapter(ctx, allResults!!)
        } else {
            hello_fragment_recycler_view.visibility = View.GONE
            feature_not_available_hello.visibility = View.VISIBLE
        }
        hello_fragment_recycler_view.adapter = helloAdapter
        helloAdapter?.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
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
            MainActivity.addItemsFromJSON(ctx)
            assignHelloData()
        } else {
            hello_fragment_recycler_view.visibility = View.GONE
            feature_not_available_hello.visibility = View.VISIBLE
        }
    }

}