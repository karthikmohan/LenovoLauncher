package io.esper.lenovolauncher.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.activity.MainActivity
import io.esper.lenovolauncher.adapter.*
import io.esper.lenovolauncher.constants.Constants
import io.esper.lenovolauncher.constants.Constants.allResults
import io.esper.lenovolauncher.constants.Constants.sharedPrefManaged
import kotlinx.android.synthetic.main.fragment_hello.*
import kotlinx.android.synthetic.main.fragment_mystay.*

class HelloFragment : Fragment() {

    private lateinit var ctx: Context


    private var helloAdapter: HelloAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        MainActivity.addItemsFromJSON(ctx)
        assignHelloData()
    }

    private fun assignHelloData() {
        layoutManager = LinearLayoutManager(ctx)
        hello_fragment_recycler_view.layoutManager = layoutManager
        if(allResults.isNotEmpty()) {
            helloAdapter = HelloAdapter(ctx, allResults)
        } else {

        }
        hello_fragment_recycler_view.adapter = helloAdapter
        helloAdapter?.notifyDataSetChanged()
    }

//    override fun onStart() {
//        super.onStart()
//        EventBus.getDefault().register(this)
//    }
//
//    override fun onStop() {
//        EventBus.getDefault().unregister(this)
//        super.onStop()
//    }
//
//    class RefreshNeededInFragment(val event: String)
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onMessageEvent(event: RefreshNeededInFragment) {
//        MainActivity.addItemsFromJSON(ctx)
//        assignNewCareTeamData()
//    }

}