package com.example.a211684_rabiatul_drnazatulaini_lab4

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211684_rabiatul_drnazatulaini_lab4.ui.theme.A211684_Rabiatul_DrNazatulAini_Lab4Theme

@Composable
fun ElecTraxHomepage(
    onViewAllClicked: () -> Unit,
    onFastChargeClicked: () -> Unit // 🔹 Added callback for FastCharge
) {  //need for overlay/layering (nearest location is displayed on top
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Changed from Color(0xFFF0FFFF)
    ) {
        var destinationInput by remember { mutableStateOf("") } //user input
        var isMessageVisible by remember { mutableStateOf(false) } //show & hide notification message
        var searchResult by remember { mutableStateOf("") } //use "" to update showing stations near "(places)"
        var showSuggestions by remember { mutableStateOf(false) } //whether the window is open/closed

        TopWallpaper()

        Column( //main layout
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp)  // Starts just below wallpaper
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
                ) { Text("Search") }

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

                    TextButton(onClick = onViewAllClicked) {
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

                FeaturesGrid(onFastChargeClicked = onFastChargeClicked) // 🔹 Passed callback here
                Spacer(modifier = Modifier.height(20.dp)) //occupy remaining space & push BottomNav to the bottom

                InsightBoard()
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
fun TopWallpaper() {
    Image(
        painter = painterResource(id = R.drawable.hari_raya_wallpaper_2), //take pic from res/drawable folder
        contentDescription = "Hari Raya Banner", //for accessibility tools
        modifier = Modifier
            .fillMaxWidth() //occupy full width only, not full height
            .height(190.dp),
        contentScale = ContentScale.Crop
    )
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
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
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
fun FeaturesGrid(onFastChargeClicked: () -> Unit) { // 🔹 Updated with callback
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            FeatureItem("FastCharge \nStations", R.drawable.ev_station_chargers, Modifier.weight(1f), onClick = onFastChargeClicked) // 🔹 Added navigation action
            FeatureItem("AutoCharge", R.drawable.auto_charge, Modifier.weight(1f)) {}
            FeatureItem("New Sites", R.drawable.new_site, Modifier.weight(1f)) {}
            FeatureItem("My Favourite", R.drawable.ic_heart, Modifier.weight(1f)) {}
        }
        Spacer(modifier = Modifier.height(12.dp))

    }
}

@Composable
fun FeatureItem(label: String, iconRes: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {

    val bgColor = MaterialTheme.colorScheme.tertiaryContainer
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .shadow(4.dp, RoundedCornerShape(30.dp))
                .background(bgColor, RoundedCornerShape(30.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),

            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 10.sp, maxLines = 2, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun InsightBoard(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ){
        Column(
            modifier = Modifier.padding(20.dp)
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(50))
                )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Your insights this month:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,

                    )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text("0", fontWeight = FontWeight.Bold)
                    Text("Session", fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("0.00 kWh", fontWeight = FontWeight.Bold)
                    Text("Energy used", fontSize = 12.sp)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("MYR 0.00", fontWeight = FontWeight.Bold)
                    Text("Total Paid", fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("0.00 kg", fontWeight = FontWeight.Bold)
                    Text("CO2 saved", fontSize = 12.sp)
                    }
                }
            }
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
        BottomItem("Explore", R.drawable.explore, Modifier.weight(1f))
        BottomItem("Maps", R.drawable.map, Modifier.weight(1f))
        BottomItem("Scan", R.drawable.qr_code_scanner, Modifier.weight(1f))
        BottomItem("Rewards", R.drawable.rewards, Modifier.weight(1f))
        BottomItem("Me", R.drawable.mee, Modifier.weight(1f))
    }
}

@Composable
fun BottomItem(label: String, iconRes: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(24.dp),

            )
        }
        Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ElecTraxPreview() {
    A211684_Rabiatul_DrNazatulAini_Lab4Theme {
        ElecTraxHomepage(onViewAllClicked = {}, onFastChargeClicked = {})
    }
}

@Preview
@Composable
fun ElecTraxDarkThemePreview(){
    A211684_Rabiatul_DrNazatulAini_Lab4Theme(darkTheme = true) {
        ElecTraxHomepage(onViewAllClicked = {}, onFastChargeClicked = {})
    }
}
