@file:Suppress("NAME_SHADOWING")

package io.esper.lenovolauncher.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.esper.lenovolauncher.R
import io.esper.lenovolauncher.activity.MainActivity
import io.esper.lenovolauncher.adapter.FeaturedAppAdapter
import io.esper.lenovolauncher.adapter.MealInfoAdapter
import io.esper.lenovolauncher.adapter.MyCareTeamAdapter
import io.esper.lenovolauncher.adapter.ScheduleAdapter
import io.esper.lenovolauncher.constants.Constants
import io.esper.lenovolauncher.constants.Constants.allResults
import io.esper.lenovolauncher.constants.Constants.sharedPrefManaged
import kotlinx.android.synthetic.main.fragment_mystay.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * A simple [Fragment] subclass.
 */
class MyStayFragment : Fragment() {

    private lateinit var ctx: Context


    private var myCareTeamAdapter: MyCareTeamAdapter? = null
    private var featuredAppAdapter: FeaturedAppAdapter? = null
    private var mealInfoAdapter: MealInfoAdapter? = null
    private var scheduleAdapter: ScheduleAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mystay, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            MainActivity.addItemsFromJSON(ctx)
            assignNewCareTeamData()
            assignApprovedAppsData()
            assignMealInfoData()
            assignScheduleData()
        } catch (e: Exception) {
            Log.d(Constants.MyStayFragmentTag, "addItemsFromJSON: ", e)
        }
        video_visit_layout.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_MyStayFragment_to_VideoVisitFragment)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun assignScheduleData() {
        layoutManager = LinearLayoutManager(ctx)
        schedule.layoutManager = layoutManager
        if (!allResults.isNullOrEmpty()) {
            for (i in 0 until allResults!!.size) {
                sharedPrefManaged = ctx.getSharedPreferences(Constants.SHARED_MANAGED_CONFIG_VALUES,
                    Context.MODE_PRIVATE)
                if (sharedPrefManaged?.getString(
                        Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                        null
                    ) != null && allResults!![i].patientId == sharedPrefManaged?.getString(
                        Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                        null
                    )
                ) {
                    if (allResults!![i].schedule!!.isNotEmpty()) {
                        scheduleAdapter = ScheduleAdapter(ctx, allResults!![i].schedule)
                        schedule_layout.visibility = View.VISIBLE
                        empty_schedule.visibility = View.GONE
                    } else {
                        schedule_layout.visibility = View.GONE
                        empty_schedule.visibility = View.VISIBLE
                    }
                    break
                } else {
                    schedule_layout.visibility = View.GONE
                    empty_schedule.visibility = View.VISIBLE
                }
            }
            schedule.adapter = scheduleAdapter
            scheduleAdapter?.notifyDataSetChanged()
        } else {
            schedule_layout.visibility = View.GONE
            empty_schedule.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun assignMealInfoData() {
        layoutManager = LinearLayoutManager(ctx)
        meal_info.layoutManager = layoutManager
        if (!allResults.isNullOrEmpty()) {
            for (i in 0 until allResults!!.size) {
                sharedPrefManaged = ctx.getSharedPreferences(Constants.SHARED_MANAGED_CONFIG_VALUES,
                    Context.MODE_PRIVATE)
                if (sharedPrefManaged?.getString(
                        Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                        null
                    ) != null && allResults!![i].patientId == sharedPrefManaged?.getString(
                        Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                        null
                    )
                ) {
                    if (allResults!![i].mealInfo!!.isNotEmpty()) {
                        mealInfoAdapter = MealInfoAdapter(ctx, allResults!![i].mealInfo)
                        meal_info_layout.visibility = View.VISIBLE
                        empty_meal_info.visibility = View.GONE
                    } else {
                        meal_info_layout.visibility = View.GONE
                        empty_meal_info.visibility = View.VISIBLE
                    }
                    break
                } else {
                    meal_info_layout.visibility = View.GONE
                    empty_meal_info.visibility = View.VISIBLE
                }
            }
            meal_info.adapter = mealInfoAdapter
            mealInfoAdapter?.notifyDataSetChanged()
        } else {
            meal_info_layout.visibility = View.GONE
            empty_meal_info.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun assignApprovedAppsData() {
        layoutManager = LinearLayoutManager(ctx)
        featured_app.layoutManager = GridLayoutManager(ctx, 3)
        if (!allResults.isNullOrEmpty()) {
            for (i in 0 until allResults!!.size) {
                sharedPrefManaged = ctx.getSharedPreferences(Constants.SHARED_MANAGED_CONFIG_VALUES,
                    Context.MODE_PRIVATE)
                if (sharedPrefManaged?.getString(
                        Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                        null
                    ) != null && allResults!![i].patientId == sharedPrefManaged?.getString(
                        Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                        null
                    )
                ) {
                    if (allResults!![i].featuredApp!!.isNotEmpty()) {
                        featuredAppAdapter = FeaturedAppAdapter(ctx, allResults!![i].featuredApp)
                        approved_apps_layout.visibility = View.VISIBLE
                        empty_approved_apps.visibility = View.GONE
                    } else {
                        approved_apps_layout.visibility = View.GONE
                        empty_approved_apps.visibility = View.VISIBLE
                    }
                    break
                } else {
                    approved_apps_layout.visibility = View.GONE
                    empty_approved_apps.visibility = View.VISIBLE
                }
            }
            featured_app.adapter = featuredAppAdapter
            featuredAppAdapter?.notifyDataSetChanged()
        } else {
            approved_apps_layout.visibility = View.GONE
            empty_approved_apps.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun assignNewCareTeamData() {
        layoutManager = LinearLayoutManager(ctx)
        my_care_team.layoutManager = layoutManager
        if (!allResults.isNullOrEmpty()) {
            for (i in 0 until allResults!!.size) {
                sharedPrefManaged = ctx.getSharedPreferences(Constants.SHARED_MANAGED_CONFIG_VALUES,
                    Context.MODE_PRIVATE)
                if (sharedPrefManaged?.getString(
                        Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                        null
                    ) != null && allResults!![i].patientId == sharedPrefManaged?.getString(
                        Constants.SHARED_MANAGED_CONFIG_PATIENT_ID,
                        null
                    )
                ) {
                    if (allResults!![i].careTeam!!.isNotEmpty()) {
                        myCareTeamAdapter = MyCareTeamAdapter(ctx, allResults!![i].careTeam)
                        my_care_team_layout.visibility = View.VISIBLE
                        empty_my_care_team.visibility = View.GONE
                    } else {
                        my_care_team_layout.visibility = View.GONE
                        empty_my_care_team.visibility = View.VISIBLE
                    }
                    break
                } else {
                    my_care_team_layout.visibility = View.GONE
                    empty_my_care_team.visibility = View.VISIBLE
                }
            }
            my_care_team.adapter = myCareTeamAdapter
            scheduleAdapter?.notifyDataSetChanged()
        } else {
            my_care_team_layout.visibility = View.GONE
            empty_my_care_team.visibility = View.VISIBLE
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

    class RefreshNeededInFragment

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RefreshNeededInFragment) {
        MainActivity.addItemsFromJSON(ctx)
        assignNewCareTeamData()
        assignApprovedAppsData()
        assignMealInfoData()
        assignScheduleData()
    }

}