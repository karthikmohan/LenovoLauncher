package io.esper.lenovolauncher.model

import java.io.Serializable

@Suppress("unused")
class MealInfoItem : Serializable {
    var mealTiming: String? = null
    var meal: String? = null

    constructor()
    constructor(
        mealTiming: String?,
        meal: String?,
    ) {
        this.mealTiming = mealTiming
        this.meal = meal
    }
}