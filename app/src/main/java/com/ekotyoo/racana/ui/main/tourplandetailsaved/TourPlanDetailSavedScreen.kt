package com.ekotyoo.racana.ui.main.tourplandetailsaved

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ekotyoo.racana.R
import com.ekotyoo.racana.core.composables.*
import com.ekotyoo.racana.core.navigation.NavigationTransition
import com.ekotyoo.racana.core.theme.RacanaGreen
import com.ekotyoo.racana.core.theme.RacanaTheme
import com.ekotyoo.racana.core.utils.currencyFormatter
import com.ekotyoo.racana.ui.destinations.DestinationDetailScreenDestination
import com.ekotyoo.racana.ui.destinations.TourPlanMapScreenDestination
import com.ekotyoo.racana.ui.main.tourplandetailsaved.model.TourPlanDetailSavedArgument
import com.ekotyoo.racana.ui.main.tourplandetailsaved.model.TourPlanDetailSavedEvent
import com.ekotyoo.racana.ui.main.tourplandetailsaved.model.TourPlanDetailSavedState
import com.ekotyoo.racana.ui.main.tourplanmap.model.TourPlanMapArgument
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.coil.CoilImage

@Destination(
    style = NavigationTransition::class,
    navArgsDelegate = TourPlanDetailSavedArgument::class
)
@Composable
fun TourPlanDetailSavedScreen(
    navigator: DestinationsNavigator,
    viewModel: TourPlanDetailSavedViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect { event ->
            when (event) {
                is TourPlanDetailSavedEvent.NavigateToDestinationDetail -> {
                    navigator.navigate(DestinationDetailScreenDestination(event.id))
                }
                is TourPlanDetailSavedEvent.DeleteDestinationSuccess -> {
                    snackbarHostState.showSnackbar("Berhasil menghapus destinasi.")
                }
                is TourPlanDetailSavedEvent.DeleteDestinationError -> {
                    snackbarHostState.showSnackbar("Gagal menghapus destinasi.")
                }
                is TourPlanDetailSavedEvent.MarkDestinationDoneSuccess, TourPlanDetailSavedEvent.MarkDestinationNotDoneSuccess -> {
                    snackbarHostState.showSnackbar("Berhasil memperbaharui destinasi.")
                }
                is TourPlanDetailSavedEvent.MarkDestinationDoneError, TourPlanDetailSavedEvent.MarkDestinationNotDoneError -> {
                    snackbarHostState.showSnackbar("Gagal memperbaharui destinasi.")
                }
                is TourPlanDetailSavedEvent.UpdateTourPlanSuccess -> {
                    snackbarHostState.showSnackbar("Berhasil memperbaharui tour plan.")
                }
                is TourPlanDetailSavedEvent.UpdateTourPlanError -> {
                    snackbarHostState.showSnackbar("Gagal memperbaharui tour plan.")
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
            topBar = {
                RTopAppBar(
                    title = state.tourPlan.title ?: "",
                    isBackButtonAvailable = true,
                    onBackButtonCLicked = { navigator.popBackStack() },
                    actionIcon = Icons.Default.LocationOn,
                    onActionsButtonClicked = {
                        navigator.navigate(
                            TourPlanMapScreenDestination(TourPlanMapArgument(state.tourPlan))
                        )
                    }
                )
            }
        ) {
            if (state.isLoading) {
                RListLoadingIndicator()
            } else {
                Box(Modifier.fillMaxSize()) {
                    TourPlanDetailSavedContent(
                        state = state,
                        onDateSelected = viewModel::onDateSelected,
                        onDestinationClicked = viewModel::navigateToDestinationDetail,
                        onDestinationDeleteButtonClicked = viewModel::onDestinationDeleteButtonClicked,
                        onDestinationToggleDoneClicked = viewModel::onDestinationToggleDoneClicked
                    )

                    RFilledButton(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
                        placeholderString = stringResource(id = if (state.tourPlan.isActive) R.string.mark_as_inactive else R.string.mark_as_active),
                        onClick = viewModel::onToggleActive
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TourPlanDetailSavedContent(
    modifier: Modifier = Modifier,
    state: TourPlanDetailSavedState,
    onDateSelected: (Int) -> Unit,
    onDestinationClicked: (Int) -> Unit,
    onDestinationDeleteButtonClicked: (Int) -> Unit,
    onDestinationToggleDoneClicked: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CoilImage(
            imageModel = state.tourPlan.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .aspectRatio(2.45f)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop,
            placeHolder = ImageBitmap.imageResource(id = R.drawable.image_placeholder),
            previewPlaceholder = R.drawable.ic_launcher_background
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.trip_date),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = state.tourPlan.period,
                    style = MaterialTheme.typography.body2,
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                val isActive = state.tourPlan.isActive
                if (isActive) {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(color = RacanaGreen)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        text = stringResource(id = R.string.active),
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Text(
                    text = currencyFormatter(state.tourPlan.totalExpense),
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        //Description
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(id = R.string.description),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = state.tourPlan.description ?: "",
            style = MaterialTheme.typography.body2
        )

        Spacer(modifier = Modifier.height(16.dp))
        //Tour Plan
        DayHeaderSection(
            selectedDate = state.selectedDate,
            dailyList = state.tourPlan.dailyList,
            onItemSelected = onDateSelected
        )
        Spacer(Modifier.height(16.dp))
        AnimatedContent(modifier = Modifier.weight(1f),
            targetState = state.selectedDestinationList) { targetList ->
            AttractionList(
                destinationList = targetList,
                onClick = onDestinationClicked,
                onDelete = onDestinationDeleteButtonClicked,
                onToggleDone = onDestinationToggleDoneClicked,
            )
        }
    }
}

@Preview(
    name = "Light Mode Preview",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode Preview",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun TourPlanDetailSavedPreview() {
    RacanaTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            TourPlanDetailSavedContent(
                state = TourPlanDetailSavedState(),
                onDateSelected = {},
                onDestinationClicked = {},
                onDestinationDeleteButtonClicked = { },
                onDestinationToggleDoneClicked = {}
            )
        }
    }
}