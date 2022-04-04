package io.esper.lenovolauncher.model

import java.io.Serializable

@Suppress("unused")
class MyCareTeamItem : Serializable {
    var id: String? = null
    var name: String? = null
    var designation: String? = null
    var image: String? = null

    constructor()
    constructor(
        id: String?,
        name: String?,
        designation: String?,
        image: String?
    ) {
        this.id = id
        this.name = name
        this.designation = designation
        this.image = image
    }
}