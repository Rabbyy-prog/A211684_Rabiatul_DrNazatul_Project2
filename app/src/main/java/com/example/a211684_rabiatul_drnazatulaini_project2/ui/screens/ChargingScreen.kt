package com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211684_rabiatul_drnazatulaini_project2.R
import com.example.a211684_rabiatul_drnazatulaini_project2.model.Connector

val HubotSans = FontFamily(
    Font(R.font.hubot_sans_light, FontWeight.Light),
    Font(R.font.hubot_sans_regular)
)

@Composable
fun ChargingScreen(
    connector: Connector,
    batteryPercent: Int,
    timeRemaining: String,
    currentUsage: String,
    totalPrice: String,
    onStopCharging: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0F6547),
                        Color(0xFF000503)
                    ),
                    center = Offset(500f, 1100f),
                    radius = 1050f
                )
            )
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(34.dp),
            horizontalAlignment = Alignment.Start
        ) {


            // Title
            Text(
                text = "Charging",
                fontFamily = HubotSans,
                color = Color.White,
                fontSize = 44.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 40.dp)
            )

            Text(
                text = "Plug connected...",
                color = Color.LightGray,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Percentage
            Text(
                text = "$batteryPercent%",
                color = Color.White,
                fontFamily = HubotSans,
                fontSize = 60.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Car + Lightning
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                // Car Image
                Image(
                    painter = painterResource(id = R.drawable.car_charging),
                    contentDescription  = "Car",
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    contentScale = ContentScale.Crop
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            // Current Usage Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Time remaining:",
                        fontFamily = HubotSans,
                        color = Color.White,
                        fontSize = 17.sp
                    )
                    Text(
                        text = timeRemaining,
                        color = Color.White,
                        fontFamily = HubotSans,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Row (Time + RM)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Time Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Current Usage:",
                            fontFamily = HubotSans,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentUsage,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // RM Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 6.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Total (RM):",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            fontFamily = HubotSans,
                            text = totalPrice,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Stop Button
            Button(
                onClick = {connector.isAvailable = true
                            connector.isCurrentUser = false
                    onStopCharging()},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB83232)
                )
            ) {
                Text(
                    text = "Stop charging",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChargingScreenPreview() {
    ChargingScreen(
        connector = Connector(
            number = 1,
            type = "CCS 2",
            isAvailable = false
        ),
        batteryPercent = 60,
        timeRemaining = "35 min",
        currentUsage = "15 kWh",
        totalPrice = "RM24.00"
    )
}