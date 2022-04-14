package io.esper.lenovolauncher.model

import java.io.Serializable

@Suppress("unused")
class ScheduleItem : Serializable {
    var dateTime: String? = null
    var meetingTitle: String? = null
    var meetingLocation: String? = null

    constructor()
    constructor(
        dateTime: String?,
        meetingTitle: String?,
        meetingLocation: String?,
    ) {
        this.dateTime = dateTime
        this.meetingTitle = meetingTitle
        this.meetingLocation = meetingLocation
    }
}