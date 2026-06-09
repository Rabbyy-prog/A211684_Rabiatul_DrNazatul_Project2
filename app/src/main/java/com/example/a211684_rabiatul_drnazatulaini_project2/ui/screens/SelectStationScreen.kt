package com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens

import androidx.compose.foundation.Image
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.viewModel.StationViewModel
import com.example.a211684_rabiatul_drnazatulaini_project2.R
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.utils.getStationImage
import com.example.a211684_rabiatul_drnazatulaini_project1.ui.theme.A211684_Rabiatul_DrNazatulAini_Project1Theme
import com.example.a211684_rabiatul_drnazatulaini_project2.data.repository.FirebaseRepository
import com.example.a211684_rabiatul_drnazatulaini_project2.model.Review
import com.example.a211684_rabiatul_drnazatulaini_project2.model.StationUiState
import com.example.a211684_rabiatul_drnazatulaini_project2.data.location.LocationHelper


//using StationViewModel to get station data from Room database
@Composable
fun SelectStationScreen(
    onStationClicked: (StationUiState) -> Unit = {},
    onViewReviewsClick: (String) -> Unit = {}

) {
    val context = LocalContext.current
    val stationViewModel: StationViewModel = viewModel()
    val stations by stationViewModel.stations.collectAsState()
    val recommendedStation = stations.firstOrNull()
    val otherStations = stations.drop(1) //remove first item

    LaunchedEffect(Unit){

    }
    val launcher = rememberLauncherForActivityResult( //ask user for location permission at runtime
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
           val locationHelper = LocationHelper(context)

            locationHelper.getCurrentLocation{ latitude, longitude ->

                println("GPS: $latitude, $longitude")

                stationViewModel.loadStations(
                    latitude = latitude,
                    longitude = longitude
                )
            }
        }
    }

    LaunchedEffect(Unit){ //to allow elecTrax to access user location
      launcher.launch(
          Manifest.permission.ACCESS_FINE_LOCATION
      )
    }

    LazyColumn( //enable to scroll automatically
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section: Recommended
        item { //because i use lazyColumn
            SectionHeader(title = "Recommended for you", icon = true)
        }

        recommendedStation?.let { //other way: items(otherStations){ station -> StationListItem(station = station, onClicked = onStationClicked) }
            item {
                RecommendedStationCard(
                    station = it, //refer to ?.let { it }
                    onClicked = onStationClicked,
                    onViewReviewsClick = onViewReviewsClick)

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
                onClicked = onStationClicked,
                onViewReviewsClick = onViewReviewsClick
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
                modifier = Modifier.size(24.dp),
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
    onClicked: (StationUiState) -> Unit,
    onViewReviewsClick: (String) -> Unit
) {
    val firebaseRepository = FirebaseRepository()
    var reviews by remember {
        mutableStateOf<List<Review>>(emptyList())
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(station.stationName) {
        firebaseRepository.getReviewsByStation(
            station.stationName
        ) { result ->

            reviews = result
        }
    }

    val averageRating =
        if(reviews.isNotEmpty()){
            reviews.map{ it.rating}
                .average()
        } else{
            0.0
        }

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
                                text =   "${String.format("%.1f", averageRating)} (${reviews.size})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            IconButton(
                                onClick = {
                                    expanded = !expanded
                                },
                                modifier = Modifier.size(20.dp)
                            ){
                                Icon(
                                    imageVector = if(expanded)
                                        Icons.Default.KeyboardArrowUp
                                        else
                                            Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )

                            }

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

                if(expanded){
                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = " ${String.format("%.1f", averageRating)} Based on ${reviews.size} reviews",
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    reviews.take(2).forEach { review ->

                        Text(
                            text = "\"${review.review}\""
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    TextButton(
                        onClick = {
                            onViewReviewsClick(
                                station.stationName
                            )
                        }
                    ) {
                        Text("View All Reviews")
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
                        border = BorderStroke(1.dp, Color(0xFF1B5E20)),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Get there", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                }
            }

            // "Best Choice" Badge
            Surface( //create an empty box shape
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
    onClicked: (StationUiState) -> Unit,
    onViewReviewsClick: (String) -> Unit
) {
    val firebaseRepository = FirebaseRepository()
    var reviews by remember {
        mutableStateOf<List<Review>>(emptyList())
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(station.stationName) {
        firebaseRepository.getReviewsByStation(
            station.stationName
        ) { result ->
            reviews = result
        }
    }

    val averageRating= if(reviews.isNotEmpty()){
        reviews.map{it.rating}
            .average()
    }else{
        0.0
    }
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
                        text = station.stationName, //refer to station:StationUiState from funStationListItem()
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
                        text = "${String.format("%.1f", averageRating)} (${reviews.size})",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    IconButton(
                        onClick = {
                            expanded = !expanded
                        },
                        modifier = Modifier.size(20.dp)
                    ){
                        Icon(
                            imageVector = if(expanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )

                    }
                }
                    Spacer(modifier = Modifier.height(8.dp))
                    Badge(station.dc)
                    }
            }

            if (expanded) {

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text =
                        "⭐ ${String.format("%.1f", averageRating)} Based on ${reviews.size} reviews",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                reviews.take(2).forEach { review ->

                    Text(
                        text = "\"${review.review}\""
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }

                TextButton(
                    onClick = {
                       onViewReviewsClick(
                           station.stationName
                       )
                    }
                ) {
                    Text("View All Reviews")
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
                    border = BorderStroke(1.dp, Color(0xFF1B5E20)),
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectStationScreenPreview() {
    A211684_Rabiatul_DrNazatulAini_Project1Theme {
        SelectStationScreen()
    }
}
