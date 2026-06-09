package com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a211684_rabiatul_drnazatulaini_project1.ui.theme.A211684_Rabiatul_DrNazatulAini_Project1Theme
import com.example.a211684_rabiatul_drnazatulaini_project2.data.repository.FirebaseRepository
import com.example.a211684_rabiatul_drnazatulaini_project2.model.Review

@Composable
fun ReviewScreen(
    stationName: String,
){

    val firebaseRepository = FirebaseRepository()
    var reviews by remember {
        mutableStateOf<List<Review>>(emptyList())
    }

    LaunchedEffect(stationName) {

        firebaseRepository.getReviewsByStation(
            stationName
        ) { result ->

            reviews = result
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){

            item {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                Text(
                    text = stationName,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            items(reviews){ review ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ){
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ){
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            repeat(review.rating) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color(0xFFFFC107)
                                )
                            }
                        }

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )
                                Text(
                                    text = review.review,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )
                                Text(
                                    text = review.dateTime,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text =
                                        "${review.chargerName} - Port ${review.connectorNumber}"
                                )
                            }

                    }

                }

                }
            }
        }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewScreenPreview(){

    A211684_Rabiatul_DrNazatulAini_Project1Theme {

        ReviewScreen(
            stationName = "Bangi Gateway Mall"
        )
    }
}
