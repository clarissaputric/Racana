package com.ekotyoo.racana.data.model

import android.os.Parcelable
import com.ekotyoo.racana.ui.main.createtourplan.model.DATE_FORMAT
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TourPlan(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val dailyList: List<DailyItem>,
) : Parcelable {
    val imageUrl: String
        get() = dailyList.first().destinationList.first().imageUrl
    val period: String
        get() = dailyList.first().dateFormatted + " - " + dailyList.last().dateFormatted
}

@Parcelize
data class DailyItem(
    val number: Int,
    val date: LocalDate,
    val destinationList: List<TravelDestination>,
) : Parcelable {
    val dateFormatted: String
        get() {
            return date.format(DATE_FORMAT)
        }
}

fun getDummyTourPlan(): TourPlan {
    val dailyItemList: MutableList<DailyItem> = mutableListOf()
    repeat(7) {
        dailyItemList.add(
            DailyItem(
                number = it + 1,
                date = LocalDate.now().plusDays(it.toLong()),
                destinationList = getDummyDestination()
            )
        )
    }

    return TourPlan(title = "Tour Plan Title", dailyList = dailyItemList)
}