package com.example.a211684_rabiatul_drnazatulaini_project1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211684_rabiatul_drnazatulaini_lab5.R
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.Connector
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.DataSource
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.PaymentType
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.StationUiState
import com.example.a211684_rabiatul_drnazatulaini_project1.ui.theme.A211684_Rabiatul_DrNazatulAini_Project1Theme

@Composable
fun PaymentScreen(
    station: StationUiState,
    charger: String,
    connector: Connector?,
    currentUsage: String,
    totalPrice: String,
    onConfirmClicked: () -> Unit,
    onBackClicked: () -> Unit = {}
) {
    var selectedPayment by remember{
        mutableStateOf(PaymentType.CARD)
    }
    Scaffold(
        bottomBar = {
            BottomPayBar(
                price = totalPrice,
                onConfirmClicked = onConfirmClicked
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Order Summary Card
            SectionHeader(title = "Payment Summary")
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painterResource(id=getStationImage(station.stationName)),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                            .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop

                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(station.id, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(station.stationName, fontWeight = FontWeight.Bold, fontSize = 16.sp) //gets value from uiState.selectedStation, which come from viewModel.updateStation(station)
                        Text(station.address, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                        Text("Operator: ${station.operator}", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }

            // Specialized Badges Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painterResource(R.drawable.fastcharge_stations_icon),
                    null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = connector?.type?:"Unknown",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface)

                // Green circle badge for number
                Surface(color = MaterialTheme.colorScheme.primary, shape = CircleShape) {
                    Text(
                        text = charger.takeLast(2),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                BadgeBox(text = station.power, icon = Icons.Default.FlashOn)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            ReceiptSummaryCard(
                chargingRate = "RM1.60/kWh",
                energyUsed = currentUsage,
                chargingCost = totalPrice,
                idleFee = "RM0.00",
                totalAmount = totalPrice
            )

            // 4. Payment Method
            SectionHeader(title = "Payment method")
            DataSource.PaymentMethodList.forEach { payment ->

                PaymentMethodRow(
                    title = payment.name,
                    subtitle =
                        when(payment.type){
                          PaymentType.EWALLET ->
                              "Current Balance: MYR 0"

                            PaymentType.CARD ->
                                "Debit or Credit Card"

                            PaymentType.FPX ->
                                "Online Banking"
                        },

                    paymentType = payment.type,
                    isWallet =
                        payment.type == PaymentType.EWALLET,

                    isSelected =
                      selectedPayment == payment.type,

                    onClick = {
                        selectedPayment = payment.type
                      }
                )
        }

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            // 5. Offers & Benefits
            SectionHeader(title = "Offers & benefits")
            BenefitItem("Apply Membership", Icons.Default.CreditCard, onClick= { })
            BenefitItem("Select Promo", Icons.Default.LocalOffer, onClick = { })

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- Reusable UI Components ---



@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun BadgeBox(text: String, icon: ImageVector? = null) {
    Row(
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) Icon(
            icon,
            null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Text(text, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline, modifier = Modifier.padding(start = if(icon != null) 4.dp else 0.dp))
    }
}

@Composable
fun FeeRow(label: String, value: String, hasInfo: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            if (hasInfo) Icon(
                Icons.Default.Info,
                null,
                modifier = Modifier.size(14.dp).padding(start = 4.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun BenefitItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable{
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                }
                Text(
                    text,
                    Modifier.padding(start = 12.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun ReceiptSummaryCard(
    chargingRate: String,
    energyUsed: String,
    chargingCost: String,
    idleFee: String,
    totalAmount: String){
    Box(
        modifier = Modifier
            .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
    ){
        Image(
            painter = painterResource(id = R.drawable.receipt_background),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ){
            Text(
                text = "Payment Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = Color.Black, thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Fees",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            ReceiptRow(
                label = "Charging Rate:",
                value = chargingRate
            )

            Spacer(modifier = Modifier.height(10.dp))

            ReceiptRow(
                label = "Energy Used:",
                value = energyUsed
            )

            Spacer(modifier = Modifier.height(10.dp))

            ReceiptRow(
                label = "Charging Cost:",
                value = chargingCost
            )

            Spacer(modifier = Modifier.height(10.dp))

            ReceiptRow(
                label = "Idle Fee:",
                value = idleFee
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                color = Color.Black,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Total:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Text(
                    text = totalAmount,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ReceiptRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black
        )

        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun PaymentMethodRow(
    title: String,
    subtitle: String,
    paymentType: PaymentType,
    isWallet: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable{
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor)
    ) {
        Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    val paymentIcon =
                        when (paymentType) {
                            PaymentType.EWALLET ->
                                Icons.Default.AccountBalanceWallet

                            PaymentType.CARD ->
                                Icons.Default.CreditCard

                            PaymentType.FPX ->
                                Icons.Default.AccountBalance
                        }
                    Icon(
                        imageVector = paymentIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    subtitle,
                    color = if (isWallet) Color.Red else Color.Gray,
                    fontSize = 12.sp
                )
            }
            if (isWallet) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Top Up", fontSize = 12.sp, color = Color.White)
                }
            } else {
                RadioButton(
                    selected = isSelected,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

@Composable
fun BottomPayBar(price: String, onConfirmClicked: () -> Unit) {
    Surface(shadowElevation = 8.dp, color = MaterialTheme.colorScheme.surface) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("You will pay", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                Text(price, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
            }
            Button(
                onClick = onConfirmClicked,
                modifier = Modifier.height(50.dp).width(180.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Payment, null)
                Text("Pay Now", Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
            }
        }
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

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    A211684_Rabiatul_DrNazatulAini_Project1Theme {
        PaymentScreen(
            station = DataSource.StationList[0],
            charger = "ELECTRAX CHARGER 01",
            connector = null,
            currentUsage = "28 kWh",
            totalPrice = "RM16.80",
            onConfirmClicked = {},
            onBackClicked = {}
        )
    }
}
