package com.example.a211684_rabiatul_drnazatulaini_project1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211684_rabiatul_drnazatulaini_project1.R
import com.example.a211684_rabiatul_drnazatulaini_project1.ui.theme.A211684_Rabiatul_DrNazatulAini_Project1Theme
import com.example.a211684_rabiatul_drnazatulaini_project1.data.DataSource
import com.example.a211684_rabiatul_drnazatulaini_project1.data.StationUiState

@Composable
fun SelectStationScreen(
    onStationClicked: (StationUiState) -> Unit = {}
) {
    val stations = DataSource.StationList
    val recommendedStation = stations.firstOrNull()
    val otherStations = stations.drop(1)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: Recommended
        item {
            SectionHeader(title = "Recommended for you", icon = true)
        }

        recommendedStation?.let {
            item {
                RecommendedStationCard(
                    station = it,
                    onClicked = onStationClicked
                )
            }
        }

        // Section: Others
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(title = "Other nearby stations", icon = false)
        }

        items(otherStations) { station ->
            StationListItem(
                station = station,
                onClicked = onStationClicked
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun RecommendedStationCard(
    station: StationUiState,
    onClicked: (StationUiState) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.LightGray))
     {
        Box {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Left: Image
                    Image(
                        painter = painterResource(id = getStationImage(station.stationName)),
                        contentDescription = null,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Right: Details
                    Column {
                        Text( //stationName
                            text = station.stationName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text( //power, distance
                                text = "${station.power} - ${station.distance} ",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.width(6.dp))

                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107)
                            )

                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = station.rating,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                        }

                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Badge(station.dc) //dc num
                            Badge(station.ac) //ac num
                        }
                        
                        Text(
                            text = "Fastest & Nearest",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { onClicked(station) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Charge", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1B5E20)),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Get there", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // "Best Choice" Badge
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = "BEST CHOICE",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun StationListItem(
    station: StationUiState,
    onClicked: (StationUiState) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = getStationImage(station.stationName)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = station.stationName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 2
                    )

                Row(verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = "${station.power} - ${station.distance}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFFFFC107)
                    )
                    
                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "${station.rating}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                    Spacer(modifier = Modifier.height(8.dp))
                    Badge(station.dc)
                    }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onClicked(station) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Charge", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1B5E20)),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1B5E20))
                ) {
                    Text("Get there", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun Badge(text: String) {
    if (text == "0") return
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

private fun getStationImage(name: String): Int {
    return when {
        name.contains("Gateway") -> R.drawable.ev_chargers___bangi_gateway_shopping_mall
        name.contains("Christine") -> R.drawable.christine_bangi_avenue_evcharger
        name.contains("Lotus") -> R.drawable.lotus_bandar_puteri_bangi_ev_charger
        name.contains("Tenera Hotel Bangi") -> R.drawable.tenera_hotel_bangi_evcharger
        name.contains("Bangi Gold Resort") -> R.drawable.bangi_golf_resort

        else -> R.drawable.ev_chargers___bangi_gateway_shopping_mall
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectStationScreenPreview() {
    A211684_Rabiatul_DrNazatulAini_Project1Theme {
        SelectStationScreen()
    }
}
