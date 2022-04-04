package io.esper.lenovolauncher.model

import java.io.Serializable

@Suppress("unused")
class HospitalDbItem : Serializable {
    var patientId: String? = null
    var patientName: String? = null
    var patientRoom: String? = null
    var careTeam: MutableList<MyCareTeamItem>? = null
    var schedule: MutableList<ScheduleItem>? = null
    var featuredApp: MutableList<FeaturedAppItem>? = null
    var mealInfo: MutableList<MealInfoItem>? = null

    constructor()
    constructor(
        patientId: String?,
        patientName: String?,
        patientRoom: String?,
        careTeam: MutableList<MyCareTeamItem>?,
        schedule: MutableList<ScheduleItem>?,
        featuredApp: MutableList<FeaturedAppItem>?,
        mealInfo: MutableList<MealInfoItem>?
    ) {
        this.patientId = patientId
        this.patientName = patientName
        this.patientRoom = patientRoom
        this.careTeam = careTeam
        this.schedule = schedule
        this.featuredApp = featuredApp
        this.mealInfo = mealInfo
    }
}