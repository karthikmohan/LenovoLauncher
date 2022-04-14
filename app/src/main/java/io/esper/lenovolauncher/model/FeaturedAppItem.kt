package io.esper.lenovolauncher.model

import java.io.Serializable

@Suppress("unused")
class FeaturedAppItem : Serializable {
    var appName: String? = null
    var packageName: String? = null
    var appIcon: String? = null

    constructor()
    constructor(
        app_name: String?,
        package_name: String?,
        app_icon: String?,
    ) {
        this.appName = app_name
        this.packageName = package_name
        this.appIcon = app_icon
    }
}