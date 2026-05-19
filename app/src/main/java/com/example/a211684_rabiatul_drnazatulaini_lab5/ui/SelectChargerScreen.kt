package com.example.a211684_rabiatul_drnazatulaini_project1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211684_rabiatul_drnazatulaini_lab5.R
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.ChargerUiState
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.Connector
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.DataSource
import com.example.a211684_rabiatul_drnazatulaini_lab5.ui.ChargingViewModel
import com.example.a211684_rabiatul_drnazatulaini_project1.ui.theme.A211684_Rabiatul_DrNazatulAini_Project1Theme

@Composable
fun SelectChargerScreen(
    viewModel: ChargingViewModel? = null,
    onProceedClicked: (ChargerUiState, Connector) -> Unit
) {
    var selectedCharger by remember { mutableStateOf<ChargerUiState?>(null) }
    var selectedConnector by remember { mutableStateOf<Connector?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(DataSource.ChargerList) { charger ->
                ChargerCard(
                    charger = charger,
                    selectedConnector = if (selectedCharger == charger) selectedConnector else null,
                    onConnectorSelected = { connector ->
                        selectedCharger = charger
                        selectedConnector = connector
                        viewModel?.updateCharger(charger)
                        viewModel?.updateConnector(connector)
                    }
                )
            }
        }

        //Bottom Action Section
        Surface( //bottom panel
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (selectedCharger != null && selectedConnector != null) {//only show summary if user selected both
                    SelectionSummary(selectedCharger!!, selectedConnector!!)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {

                        val activeConnector = //is users already charging
                            DataSource.ChargerList
                                .flatMap { it.connectors } //take all connectors & merge into one list
                                .find { it.isCurrentUser } //find connector owned by current user

                        // If user already charging
                        if (activeConnector != null) {

                            onProceedClicked( //resume charging
                                selectedCharger ?: DataSource.ChargerList[0],
                                activeConnector
                            )

                        }

                        // Start new charging
                        else if ( //only continue if user selected both charger and connector
                            selectedCharger != null &&
                            selectedConnector != null
                        ) {

                            selectedConnector!!.isAvailable = false //make connector unavailable after click
                            selectedConnector!!.isCurrentUser = true

                            onProceedClicked(
                                selectedCharger!!,
                                selectedConnector!!
                            )
                        }
                    },

                    //can button 'start charging' be clicked
                    enabled =
                        selectedConnector != null || //user selected connector
                                DataSource.ChargerList.any { charger ->
                                    charger.connectors.any { it.isCurrentUser }
                                },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    shape = RoundedCornerShape(28.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )

                ) {

                    Text(
                        text =
                            if (
                                DataSource.ChargerList.any { charger ->
                                    charger.connectors.any { it.isCurrentUser } //is there any connector currently used by this user?
                                }
                            )
                                "Resume Charging"
                            else
                                "Start Charging",

                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    }
}

@Composable
fun ChargerCard(
    charger: ChargerUiState,
    selectedConnector: Connector?,
    onConnectorSelected: (Connector) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = charger.chargerName,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = charger.power,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            charger.connectors.forEach { connector ->
                ConnectorRow(
                    connector = connector,
                    isSelected = selectedConnector == connector,
                    onSelect = { onConnectorSelected(connector) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ConnectorRow(
    connector: Connector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.secondaryContainer
        !connector.isAvailable -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = connector.isAvailable) { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (isSelected) border(1.dp, borderColor, RoundedCornerShape(16.dp)) else null
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Port Number
            Surface(
                modifier = Modifier.size(32.dp),
                shape = RoundedCornerShape(8.dp),
                color = if (connector.isAvailable) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.errorContainer
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = connector.number.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (connector.isAvailable) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Connector Icon
            Image(
                painter = painterResource(id = getConnectorImage(connector.type)),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text(
                    text = connector.type,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                if (connector.isAvailable) Color.Green else Color.Red,
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when {
                            connector.isCurrentUser -> "Your Charging"
                            connector.isAvailable -> "Available"
                            else -> "In Use"
                        },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (!connector.isAvailable) {
                                    Text(
                                        text =
                                            if(connector.isCurrentUser)
                                                " (35 min left)"
                                            else
                                                "(~12 min left)",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.error
                                    )
                        }
                }

            Spacer(modifier = Modifier.weight(1f))

            if (connector.isAvailable) { //show radio button
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
            } else {
                Text(
                    text = "ANOTHER CAR\nCHARGING",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    lineHeight = 10.sp
                )
            }
        }
    }
}


private fun getConnectorImage(type: String): Int {
    return when(type){
        "CCS 2" -> R.drawable.port_ccs_2
        "Type 2" -> R.drawable.type_2_port
        else->R.drawable.port_ccs_2
    }
}
@Composable
fun SelectionSummary(charger: ChargerUiState, connector: Connector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "You've selected",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Slot ${connector.number} - ${connector.type} - ${charger.power}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.car_charging),
                contentDescription = null,
                modifier = Modifier.height(32.dp)
            )
        }
    }
}

private fun border(width: androidx.compose.ui.unit.Dp, color: Color, shape: androidx.compose.ui.graphics.Shape) =
    androidx.compose.foundation.BorderStroke(width, color)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChargerScreen() {
    A211684_Rabiatul_DrNazatulAini_Project1Theme {
        SelectChargerScreen(
            viewModel = null,
            onProceedClicked = { _, _ -> }
        )
    }
}
