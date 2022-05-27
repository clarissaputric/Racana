package com.ekotyoo.racana.ui.main.tourplanlist

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ekotyoo.racana.R
import com.ekotyoo.racana.core.composables.RPlanCard
import com.ekotyoo.racana.core.composables.RTopAppBar
import com.ekotyoo.racana.core.navigation.BottomNavGraph
import com.ekotyoo.racana.core.navigation.NavigationTransition
import com.ekotyoo.racana.core.theme.RacanaTheme
import com.ekotyoo.racana.data.model.TourPlan
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@BottomNavGraph
@Destination(style = NavigationTransition::class)
@Composable
fun TourPlanListScreen(
    navigator: DestinationsNavigator,
    viewModel: TourPlanListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect { event ->
            when (event) {

            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        TourPlanListContent(
            tourPlanList = state.tourPlanList
        )
    }
}

@Composable
fun TourPlanListContent(
    tourPlanList: List<TourPlan>,
) {
    Scaffold(topBar = {
        RTopAppBar(title = stringResource(id = R.string.tour_plan_list))
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tourPlanList.size) { index ->
                val plan = tourPlanList[index]
                RPlanCard(
                    name = plan.title ?: "Untitled",
                    imageUrl = plan.imageUrl,
                    date = plan.period,
                    desciption = plan.description ?: "-",
                    onClick = {}
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light Mode Preview"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode Preview"
)
@Composable
fun RPlanCardPreview() {
    RacanaTheme {
        TourPlanListContent(tourPlanList = emptyList())
    }
}