package io.esper.lenovolauncher.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.utils.GeneralUtils.openApp
import kotlinx.android.synthetic.main.fragment_entertainment.*

class EntertainmentFragment : Fragment() {

    var ctx: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entertainment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        netflix.setOnClickListener { ctx?.let { it1 -> openApp(it1, "com.netflix.mediaclient") } }
        youtube.setOnClickListener { ctx?.let { it1 -> openApp(it1, "com.google.android.youtube") } }
        hulu.setOnClickListener { ctx?.let { it1 -> openApp(it1, "com.hulu.plus") } }
        primevideo.setOnClickListener { ctx?.let { it1 -> openApp(it1, "com.amazon.avod.thirdpartyclient") } }
        disney.setOnClickListener { ctx?.let { it1 -> openApp(it1, "com.disney.disneyplus") } }
        hbomax.setOnClickListener { ctx?.let { it1 -> openApp(it1, "com.hbo.hbonow") } }
    }
}