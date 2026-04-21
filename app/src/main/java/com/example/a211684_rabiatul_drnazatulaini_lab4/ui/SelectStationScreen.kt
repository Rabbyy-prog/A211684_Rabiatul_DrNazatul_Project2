package com.example.a211684_rabiatul_drnazatulaini_lab4.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211684_rabiatul_drnazatulaini_lab4.ui.theme.A211684_Rabiatul_DrNazatulAini_Lab4Theme
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.DataSource
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.StationUiState


@Composable
fun SelectStationScreen(
    onStationClicked: (StationUiState) -> Unit = {}
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(DataSource.StationList) { station->
            StationItem(
                station = station,
                onClicked = onStationClicked
            )
        }
    }
}

@Composable
fun StationItem(
    station: StationUiState,
    onClicked: (StationUiState) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box( //logo pic later
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Gray, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = station.stationName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${station.power} • ${station.distance} ⭐${station.rating} ",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row {
                    ChargerBadge(station.dc, Color(0xFFB2DFDB))
                    if (station.ac != "0") {
                        Spacer(modifier = Modifier.width(6.dp))
                        ChargerBadge(station.ac, Color(0xFFC8E6C9))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {onClicked(station)},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Charge", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Get there", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ChargerBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectStationScreenPreview() {
    A211684_Rabiatul_DrNazatulAini_Lab4Theme {
        SelectStationScreen(onStationClicked = {station ->})
    }
}
