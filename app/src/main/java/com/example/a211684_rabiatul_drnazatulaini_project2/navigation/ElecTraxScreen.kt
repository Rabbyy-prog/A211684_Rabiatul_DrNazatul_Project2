package com.example.a211684_rabiatul_drnazatulaini_project2.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.a211684_rabiatul_drnazatulaini_project2.model.Connector
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.viewModel.ChargingViewModel
import com.example.a211684_rabiatul_drnazatulaini_project2.data.DataSource
import com.example.a211684_rabiatul_drnazatulaini_project2.model.StationUiState
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens.ChargingScreen
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.provider.ChargingViewModelProvider
import com.example.a211684_rabiatul_drnazatulaini_project1.ui.SelectChargerScreen
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens.SelectStationScreen
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens.PaymentScreen
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens.ChargingHistoryScreen
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens.PaymentStatusScreen
import com.example.a211684_rabiatul_drnazatulaini_project2.data.repository.FirebaseRepository
import com.example.a211684_rabiatul_drnazatulaini_project2.model.Review
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens.ReviewScreen
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens.ElecTraxHomepage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ElecTraxScreen(val title: String) { //route
    Start("Home"), //1.HomeScreen.kt
    SelectStation("Select Station"), //2. SelectStationScreen.kt
    SelectCharger("Select Charger"), //3. SelectChargerScreen.kt
    ChargingStatus("Charging Status"), //4. ChargingScreen.kt
    Payment("Confirm Payment"),//5. PaymentScreen.kt
    PaymentStatus("Payment Status"), //6.PaymentStatus.kt
    Review("Rating & Feedback"), //7. ReviewScreen.kt
    ChargingHistory("Charging History"), //8. ChargingHistoryScreen.kt

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElecTraxAppBar(
    currentScreen: ElecTraxScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(currentScreen.title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun ElecTraxApp(
    viewModel: ChargingViewModel = viewModel(factory = ChargingViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    
    val route = backStackEntry?.destination?.route ?: ElecTraxScreen.Start.name
    val currentScreen =
        when {
            route.startsWith(ElecTraxScreen.Review.name) ->
                ElecTraxScreen.Review

            else ->
                try {
                    ElecTraxScreen.valueOf(route)
                } catch (e: Exception) {
                    ElecTraxScreen.Start
                }
        }

    val uiState by viewModel.uiState.collectAsState() //read data from ViewModel, this is important!!!

    val firebaseRepository = FirebaseRepository()

    Scaffold(
        topBar = {
            // Hide TopAppBar on the Start screen
            if (currentScreen != ElecTraxScreen.Start) {
                ElecTraxAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null && currentScreen != ElecTraxScreen.Payment,
                    navigateUp = { navController.navigateUp() }
                )
            }
        }
    ) { innerPadding -> //content auto turun bawah topBar, tak overlap
        NavHost(
            navController = navController,
            startDestination = ElecTraxScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ElecTraxScreen.Start.name) {
                ElecTraxHomepage( //from ElecTraxHomepage Composable function
                    onViewAllClicked = { //viewAll button
                        navController.navigate(
                            ElecTraxScreen.SelectStation.name
                        )
                    },

                    onHistoryClicked = {
                        navController.navigate(
                            ElecTraxScreen.ChargingHistory.name
                        )
                    },


                    onFastChargeClicked = {
                        navController.navigate(ElecTraxScreen.SelectStation.name)
                    },
                    session = 0,

                    energyUsed =
                        uiState.currentUsage.ifBlank {
                            "0.00 kWh"
                        },


                    totalPaid =
                        uiState.totalPrice.ifBlank {
                            "MYR0.00"
                        },

                    co2Saved = "0.00 kg"
                )
            }

            composable(ElecTraxScreen.SelectStation.name) {
                SelectStationScreen(
                    onStationClicked = { station: StationUiState ->
                        viewModel.updateStation(station) //remember which station user selected and store selected station data temporarily
                        navController.navigate(ElecTraxScreen.SelectCharger.name)
                    },
                    onViewReviewsClick = { stationName ->
                        navController.navigate(
                            "${ElecTraxScreen.Review.name}/$stationName"
                        )
                    }
                )
            }

            composable(ElecTraxScreen.SelectCharger.name) {
                SelectChargerScreen(
                    onProceedClicked = { charger, connector ->
                        viewModel.updateCharger(charger)
                        viewModel.updateConnector(connector)
                        if(uiState.timeRemaining.isEmpty()) {
                            viewModel.generateChargingSession()
                        }
                            navController.navigate(ElecTraxScreen.ChargingStatus.name)

                    }
                )
            }

            composable(ElecTraxScreen.Payment.name) { //no viewModel bcs it is reading data, not updating data
                val selectedStation = uiState.selectedStation ?: DataSource.StationList[0]
                val selectedCharger = uiState.selectedCharger?.chargerName ?: "Charger A"
                val selectedConnector = uiState.selectedConnector
                
                PaymentScreen(
                    station = selectedStation,
                    charger = selectedCharger,
                    connector = selectedConnector,
                    currentUsage = uiState.currentUsage,
                    totalPrice = uiState.totalPrice,
                    onConfirmClicked = {
                        navController.navigate(ElecTraxScreen.PaymentStatus.name)

                    }
                )
            }

            composable(ElecTraxScreen.PaymentStatus.name){
                PaymentStatusScreen(
                    onSubmitClick = { rating, reviewText ->

                        val currentDateTime = SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss",
                            Locale.getDefault()
                        ).format(
                            Date()

                        )
                            val review = Review(
                                stationName = uiState.selectedStation?.stationName ?: "Unknown Station",
                                chargerName = uiState.selectedCharger?.chargerName?:"Unknown Charger",
                                connectorNumber = uiState.selectedConnector?.number?: 0,
                                chargingPort = uiState.selectedConnector?.type?: "Unknown Port",
                                rating = rating,
                                review = reviewText,
                                dateTime = currentDateTime
                            )
                        firebaseRepository.saveReview(review)
                        viewModel.resetSession()
                        navController.popBackStack(
                            ElecTraxScreen.Start.name,
                            inclusive = false
                        )
                    },
                    onBackHomeClick = {
                        viewModel.resetSession()

                        navController.popBackStack(
                            ElecTraxScreen.Start.name,
                            inclusive = false
                        )
                    }
                )
            }

            composable(ElecTraxScreen.ChargingStatus.name) {
                val selectedConnector = uiState.selectedConnector
                    ?:Connector(
                        number = 1,
                        type = "CCS 2",
                        isAvailable = false
                    )
                ChargingScreen(
                    connector = selectedConnector,
                    batteryPercent = uiState.batteryPercent,
                    timeRemaining = uiState.timeRemaining,
                    currentUsage = uiState.currentUsage,
                    totalPrice = uiState.totalPrice,
                    onStopCharging = {
                        viewModel.saveChargingHistory()
                        navController.navigate(ElecTraxScreen.Payment.name){
                            popUpTo(
                                ElecTraxScreen.Start.name
                            ) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            composable(route = "${ElecTraxScreen.Review.name}/{stationName}"){
                backStackEntry->

                val stationName = backStackEntry.arguments?.getString("stationName")?:""
                ReviewScreen(
                    stationName = stationName,
                )
            }

            composable(
                ElecTraxScreen.ChargingHistory.name
            ) {
                ChargingHistoryScreen()
            }
        }
    }
}
