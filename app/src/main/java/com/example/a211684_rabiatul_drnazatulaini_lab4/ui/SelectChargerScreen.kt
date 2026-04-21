package com.example.a211684_rabiatul_drnazatulaini_lab4.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.ChargerUiState
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.Connector
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.DataSource
import com.example.a211684_rabiatul_drnazatulaini_lab4.ui.theme.A211684_Rabiatul_DrNazatulAini_Lab4Theme

@Composable
fun SelectChargerScreen(
    viewModel: ChargingViewModel?= null,
    onProceedClicked: (ChargerUiState, Connector) -> Unit
) {
    var selectedCharger by remember { mutableStateOf<ChargerUiState?>(null) }
    var selectedConnector by remember { mutableStateOf<Connector?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f)
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

        Button(
            onClick = {
                if (selectedCharger != null && selectedConnector != null) {
                    onProceedClicked(selectedCharger!!, selectedConnector!!)
                }
            },
            enabled = selectedConnector != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "Start Charging",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(charger.chargerName, fontWeight = FontWeight.Bold)
                Text(charger.power)
            }

            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

            charger.connectors.forEach { connector ->

                ConnectorRow(
                    connector = connector,
                    isSelected = selectedConnector == connector,
                    onSelect = { onConnectorSelected(connector) }
                )
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = connector.isAvailable) { onSelect() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (connector.isAvailable) Color(0xFFE0F2F1)
                    else Color.LightGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = connector.number.toString(),
                color = if (connector.isAvailable) Color(0xFF00796B) else Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(text = connector.type)

        Spacer(modifier = Modifier.weight(1f))

        if (connector.isAvailable) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
        } else {
            Text(
                text = "CHARGING",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChargerScreen() {
    A211684_Rabiatul_DrNazatulAini_Lab4Theme {
        SelectChargerScreen(
            viewModel = null,
            onProceedClicked = {_, _->}
        )
    }
}