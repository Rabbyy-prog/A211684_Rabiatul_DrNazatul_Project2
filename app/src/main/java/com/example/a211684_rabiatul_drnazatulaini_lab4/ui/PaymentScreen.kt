package com.example.a211684_rabiatul_drnazatulaini_lab4.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.Connector
import com.example.a211684_rabiatul_drnazatulaini_lab4.R
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.DataSource
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.StationUiState
import com.example.a211684_rabiatul_drnazatulaini_lab4.ui.theme.A211684_Rabiatul_DrNazatulAini_Lab4Theme

@Composable
fun PaymentScreen(
    station: StationUiState,
    charger: String,
    connector: Connector?,
    onConfirmClicked: () -> Unit,
    onBackClicked: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            BottomPayBar(
                price = "MYR 1.60/kWh", 
                onConfirmClicked = onConfirmClicked
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Start Charging",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Order Summary Card
            SectionHeader(title = "Order Summary")
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        Icons.Default.Place, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(station.id, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(station.stationName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                Surface(color = Color(0xFF4CAF50), shape = CircleShape) {
                    Text(
                        charger, // 🔹 Using the passed charger number
                        color = Color.White, 
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), 
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                BadgeBox(text = station.power, icon = Icons.Default.FlashOn)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // 3. Detailed Fees Section
            SectionHeader(title = "Fees")
            FeeRow("Charging Fee", "MYR 1.60/kWh")
            FeeRow("Idling Fee", "MYR 0.4/1 min", hasInfo = true)
            FeeRow("Idling Grace Period", "15 mins")

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // 4. Offers & Benefits
            SectionHeader(title = "Offers & benefits")
            BenefitItem("Apply Membership", Icons.Default.CreditCard)
            BenefitItem("Select Promo", Icons.Default.LocalOffer)

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // 5. Payment Method
            SectionHeader(title = "Payment method")
            PaymentMethodRow("Wallet", "Current Balance: MYR 0", isWallet = true)
            PaymentMethodRow("Mastercard xxxx 3789", "Credit card", isSelected = true)
            
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
        color = MaterialTheme.colorScheme.onBackground
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
fun BenefitItem(text: String, icon: ImageVector) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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
fun PaymentMethodRow(title: String, subtitle: String, isWallet: Boolean = false, isSelected: Boolean = false) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
                    Icon(
                        if (isWallet) Icons.Default.AccountBalanceWallet else Icons.Default.CreditCard, 
                        null, 
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    subtitle, 
                    color = if (isWallet) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline, 
                    fontSize = 12.sp
                )
            }
            if (isWallet) {
                Button(
                    onClick = {}, 
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) { 
                    Text("Top Up", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer) 
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
                Icon(Icons.Default.FlashOn, null)
                Text("Start charging", Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentScreenPreview() {
    A211684_Rabiatul_DrNazatulAini_Lab4Theme {
        PaymentScreen(
            station = DataSource.StationList[0],
            charger = "GEN_CHARGER_01",
            connector = null,
            onConfirmClicked = {},
            onBackClicked = {}
        )
    }
}
