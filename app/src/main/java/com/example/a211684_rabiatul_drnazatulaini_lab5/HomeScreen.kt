package com.example.a211684_rabiatul_drnazatulaini_lab5
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211684_rabiatul_drnazatulaini_project1.ui.theme.A211684_Rabiatul_DrNazatulAini_Project1Theme

@Composable
fun ElecTraxHomepage(
    onViewAllClicked: () -> Unit,
    onFastChargeClicked: () -> Unit, // Added callback for FastCharge
    onHistoryClicked: () -> Unit,
    session: Int,
    energyUsed: String,
    totalPaid: String,
    co2Saved: String
) {  //need for overlay/layering (nearest location is displayed on top
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Changed from Color(0xFFF0FFFF)
    ) {
        var destinationInput by remember { mutableStateOf("") } //user input //var = can change
        var isMessageVisible by remember { mutableStateOf(false) } //show & hide notification message
        var searchResult by remember { mutableStateOf("") } //use "" to update showing stations near "(places)"
        var showSuggestions by remember { mutableStateOf(false) } //whether the window is open/closed
        val username = "Nabila"

        TopWallpaper(username = username)

        Column( //main layout
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 115.dp)  // Starts just below wallpaper
                .padding(horizontal = 13.dp)  //lgi besar padding, lagi kecil margin
                .padding(bottom = 16.dp)
        ) {
            Column( //scrollable content
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ){
                EditSearchField(
                    label = R.string.where,
                    leadingIcon = R.drawable.pin_destination_finder,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text, //can show both number and text
                        imeAction = ImeAction.Search //the keypad button label as search
                    ),
                    value = destinationInput,  // value = what to DISPLAY
                    onValueChange = { //what to DO when user types
                        destinationInput = it // it = new text
                        if (it.isEmpty()) {
                            showSuggestions = false
                            searchResult = ""
                        }
                    },
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                )

                Button(
                    onClick = {
                        searchResult = "Showing stations near $destinationInput"
                        showSuggestions = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text("Search station")
                    }
                }

                if (searchResult.isNotEmpty()) {
                    Text(
                        text = searchResult,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                AnimatedVisibility( //inside column sbb nak dia appear below search button and above nearest stations card
                    visible = showSuggestions && destinationInput.isNotEmpty(), //not using if because the searchSuggestions card will blinking frequently, visible = switch already
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    SearchSuggestions(query = destinationInput) { selected ->
                        destinationInput =
                            selected //selected -> temporary variable clicked by user, the next selected = update
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Your Nearest Stations",
                        style = MaterialTheme.typography.displayMedium,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold, //
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    TextButton(
                        onClick = onViewAllClicked
                    ) {
                        Text(
                            text = "View All",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            textDecoration = TextDecoration.Underline,
                        )
                    }

                }
                Spacer(modifier = Modifier.height(6.dp))

                NearestLocation() //card
                Spacer(modifier = Modifier.height(20.dp)) //spacing between two component (below it)

                FeaturesGrid(onFastChargeClicked = onFastChargeClicked, onHistoryClicked = onHistoryClicked) // 🔹 Passed callback here
                Spacer(modifier = Modifier.height(20.dp)) //occupy remaining space & push BottomNav to the bottom

                InsightBoard(
                    session = session,
                    energyUsed = energyUsed,
                    totalPaid = totalPaid,
                    co2Saved = co2Saved
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            BottomNav() //not inside the first column because i want the bottom nav to be on the bottom and cannot be scrolled
        }
        Notification( //not inside column because i want the bell to be on top of background wallpaper
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { isMessageVisible = !isMessageVisible }
        )

        if (isMessageVisible) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 95.dp)
                    .width(260.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), //shadows, low elevation, sharper shadows
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = "No new notifications yet",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun TopWallpaper(username:String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.homescreen_wallpaper), //take pic from res/drawable folder
            contentDescription = "Hari Raya Banner", //for accessibility tools
            modifier = Modifier
                .fillMaxWidth() //occupy full width only, not full height
                .height(160.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(start = 18.dp, top = 24.dp)
        ){
            Text(
                text = stringResource(R.string.welcome_user, username),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))

        }

    }
}

@Composable
fun Notification(modifier: Modifier = Modifier, onClick: () -> Unit = {}) { //query = destinationInput, {} = no input, Unit = return nothing, Unit mean function declaration
    IconButton( //white opacity circle button
        onClick = onClick,
        modifier = modifier
            .padding(top = 40.dp, end = 16.dp)
            .size(48.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    ) {
        Icon( //my bell icon
            painter = painterResource(id = R.drawable.bell),
            contentDescription = "Notification",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun EditSearchField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value, //
        onValueChange = onValueChange,//
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        label = { Text(stringResource(id = label)) },
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.colors(//
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp)
            )
    )
}

@Composable
fun SearchSuggestions(query: String, onSelect: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Suggestions for: \"$query\"",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(8.dp)
            )
            SuggestionItem("Bangi Gateway Mall", "1.8 km", onSelect)
            SuggestionItem("Christine's Bakery Bangi Avenue Mall", "2.6 km", onSelect)
        }
    }
}

@Composable
fun SuggestionItem(name: String, distance: String, onSelect: (String) -> Unit) {
    Row( // arrange icon & text section horizontally //side by side
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(name) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.map),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.labelSmall)
            Text(text = distance, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun NearestLocation() { //biggest card
    var expanded by remember { mutableStateOf(false)}
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.secondaryContainer)

    val textColor = if (expanded) {
        Color.Black
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor =color) //colour by animateColorAsState: default - secondaryContainer
    )
    {
        Column(
            modifier = Modifier
                .animateContentSize(spring (dampingRatio = Spring.DampingRatioNoBouncy,//stop nicely, tak melantun
                    stiffness = Spring.StiffnessMedium //fast = normal
                  )
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ev_chargers___bangi_gateway_shopping_mall),
                contentDescription = "Bangi Gateway Mall EV charger",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Row( //row because i want the icon to be on the put beside the text label
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier
                        .weight(1f)) {

                        Text(
                            "[Public] BANGI GATEWAY MALL (Shopping Mall)",
                            style = MaterialTheme.typography.displayMedium,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            "60 kW max  - 1.8km  - 0.0",
                            style = MaterialTheme.typography.displaySmall,
                            fontSize = 12.sp,
                            color = textColor
                        )
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, //get these icons from material icons library, import androidx.compose.material.icons.filled.ExpandLess/ExpandMore
                            contentDescription = "Expand",
                            tint = MaterialTheme.colorScheme.primary //Icon uses tint, Text uses colour
                        )
                    }
                }

                if(expanded){
                    Spacer(modifier=Modifier.height(8.dp))
                    Text(text ="Status: Available",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor)

                    Text(text = "Connector: CCS2",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor)

                    Spacer(modifier=Modifier.height(8.dp))

                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Directions,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Get Directions",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun FeaturesGrid(onFastChargeClicked: () -> Unit, onHistoryClicked:() ->Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            FeatureItem(label = "FastCharge\nStations", iconRes =  R.drawable.ev_station_chargers, onClick = onFastChargeClicked) // Added navigation action
            FeatureItem(label = "AutoCharge", iconRes = R.drawable.auto_charge) {}
            FeatureItem(label ="My History", iconRes = R.drawable.history, onClick = onHistoryClicked)
            FeatureItem(label = "My Favourite", iconRes = R.drawable.ic_heart) {}
        }
        Spacer(modifier = Modifier.height(12.dp))

    }
}

@Composable
fun FeatureItem(
    label: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {

    Card(
        modifier = modifier
            .padding(horizontal = 5.dp)
            .width(78.dp)
            .height(92.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(22.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),

        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = 12.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.SpaceEvenly
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),

                contentAlignment =
                    Alignment.Center
            ) {

                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }


                Text(
                    text = label,
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    color = Color.Black
            )
        }
    }

}


@Composable
fun InsightBoard(
    session: Int,
    energyUsed: String,
    totalPaid: String,
    co2Saved: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),

        shape = RoundedCornerShape(28.dp),

        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme
                    .secondaryContainer
        ),

        border = BorderStroke(
            width = 1.5.dp,
            color =
                MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.4f
                )
        )
    )
    {
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Your insights this month",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceEvenly
            ) {

                InsightItem(
                    icon = Icons.Default.Bolt,
                    value = session.toString(),
                    label = "Session"
                )

                InsightItem(
                    icon = Icons.Default.BatteryChargingFull,
                    value = energyUsed,
                    label = "Energy"
                )

                InsightItem(
                    icon = Icons.Default.AttachMoney,
                    value = totalPaid,
                    label = "Paid"
                )

                InsightItem(
                    icon = Icons.Default.Eco,
                    value = co2Saved,
                    label = "CO₂"
                )
            }
        }
    }
}

@Composable
fun InsightItem(
    icon: ImageVector,
    value: String,
    label: String
) {

    Column(
        modifier = Modifier.width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
          imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BottomNav() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(4.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomItem("Explore", Icons.Default.Search)
        BottomItem("Maps",Icons.Default.LocationOn)
        BottomItem("Scan", Icons.Default.QrCodeScanner)
        BottomItem("Rewards", Icons.Default.Star)
        BottomItem("Me",Icons.Default.Person)
    }
}

@Composable
fun BottomItem(label: String, icon: ImageVector, modifier: Modifier = Modifier, selected: Boolean = false) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant

                )
        }
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ElecTraxPreview() {
    A211684_Rabiatul_DrNazatulAini_Project1Theme() {
        ElecTraxHomepage(
            onViewAllClicked = {},
            onFastChargeClicked = {},
            onHistoryClicked = {},
            session = 0,
            energyUsed = "0.00 kWh",
            totalPaid = "MYR 0.00",
            co2Saved = "0.00 kg")
    }
}

@Preview
@Composable
fun ElecTraxDarkThemePreview(){
    A211684_Rabiatul_DrNazatulAini_Project1Theme(darkTheme = true) {
        ElecTraxHomepage(
            onViewAllClicked = {},
            onFastChargeClicked = {},
            onHistoryClicked = {},
            session = 0,
            energyUsed = "0.00 kWh",
            totalPaid = "MYR 0.00",
            co2Saved = "0.00 kg")
    }
}
