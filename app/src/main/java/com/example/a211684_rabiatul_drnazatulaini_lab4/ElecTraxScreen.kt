package com.example.a211684_rabiatul_drnazatulaini_lab4

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
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.Connector
import com.example.a211684_rabiatul_drnazatulaini_lab4.ui.ChargingViewModel
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.DataSource
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.StationUiState
import com.example.a211684_rabiatul_drnazatulaini_lab4.ui.SelectChargerScreen
import com.example.a211684_rabiatul_drnazatulaini_lab4.ui.SelectStationScreen
import com.example.a211684_rabiatul_drnazatulaini_lab4.ui.PaymentScreen

enum class ElecTraxScreen(val title: String) { //route
    Start("Home"), //HomeScreen.kt
    SelectStation("Select Station"), //SelectStationScreen.kt
    SelectCharger("Select Charger"), //SelectChargerScreen.kt
    Payment("Confirm Payment") //PaymentScreen.kt
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
    viewModel: ChargingViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    
    val route = backStackEntry?.destination?.route ?: ElecTraxScreen.Start.name
    val currentScreen = try {
        ElecTraxScreen.valueOf(route)
    } catch (e: Exception) {
        ElecTraxScreen.Start
    }

    val uiState by viewModel.uiState.collectAsState() //read data from ViewModel

    Scaffold(
        topBar = {
            // Hide TopAppBar on the Start screen
            if (currentScreen != ElecTraxScreen.Start) {
                ElecTraxAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
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
                        navController.navigate(ElecTraxScreen.SelectStation.name)
                    },
                    onFastChargeClicked = {
                        navController.navigate(ElecTraxScreen.SelectStation.name)
                    }
                )
            }

            composable(ElecTraxScreen.SelectStation.name) {
                SelectStationScreen(
                    onStationClicked = { station: StationUiState ->
                        viewModel.updateStation(station) //remember which station user selecteed and store selected station data temporarily
                        navController.navigate(ElecTraxScreen.SelectCharger.name)
                    }
                )
            }

            composable(ElecTraxScreen.SelectCharger.name) {
                SelectChargerScreen(
                    onProceedClicked = { charger, connector ->
                        viewModel.updateCharger(charger)
                        viewModel.updateConnector(connector)
                        navController.navigate(ElecTraxScreen.Payment.name)
                    }
                )
            }

            composable(ElecTraxScreen.Payment.name) {
                val selectedStation = uiState.selectedStation ?: DataSource.StationList[0]
                val selectedCharger = uiState.selectedCharger?.chargerName ?: "Charger A"
                val selectedConnector = uiState.selectedConnector
                
                PaymentScreen(
                    station = selectedStation,
                    charger = selectedCharger,
                    connector = selectedConnector,
                    onConfirmClicked = {
                        viewModel.resetSession()
                        navController.popBackStack(ElecTraxScreen.Start.name, inclusive = false)
                    }
                )
            }
        }
    }
}
