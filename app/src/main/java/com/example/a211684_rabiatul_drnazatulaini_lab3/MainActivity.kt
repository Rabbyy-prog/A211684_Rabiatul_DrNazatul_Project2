package com.example.a211684_rabiatul_drnazatulaini_lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.draw.shadow
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import com.example.a211684_rabiatul_drnazatulaini_lab3.ui.theme.A211684_Rabiatul_DrNazatulAini_Lab3Theme
import com.example.a211684_rabiatul_drnazatulaini_lab3.ui.theme.secondaryContainerDark
import com.example.a211684_rabiatul_drnazatulaini_lab3.ui.theme.secondaryContainerLight
import com.example.a211684_rabiatul_drnazatulaini_lab3.ui.theme.tertiaryContainerLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //make UI go full screen
        setContent {
            A211684_Rabiatul_DrNazatulAini_Lab3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ElecTraxHomepage() //Referance: Gentari Go App
                }
            }
        }
    }
}

@Composable
fun ElecTraxHomepage() {  //need for overlay/layering (nearest location is displayed on top
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp)  // Starts just below wallpaper
                .padding(horizontal = 13.dp)  //lgi besar padding, lagi kecil margin
                .padding(bottom = 16.dp)
        ) {
            Column(
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

            Spacer(modifier = Modifier.height(16.dp))

            NearestLocation() //card
            Spacer(modifier = Modifier.height(12.dp)) //spacing between two component (below it)

            AlternativeLocations()
            Spacer(modifier = Modifier.height(12.dp))

            FeaturesGrid()
            Spacer(modifier = Modifier.height(20.dp)) //occupy remaining space & push BottomNav to the bottom

        }
        BottomNav()
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
fun NearestLocation() {
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
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = color)
    )
    {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .animateContentSize(spring (dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Your nearest station:",
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 12.sp,
                    color =textColor
                )
                Spacer(modifier=Modifier.height(8.dp))

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
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Expand",
                    tint = MaterialTheme.colorScheme.primary
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
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Get Directions",
                style = MaterialTheme.typography.displayMedium)
        }
        }
    }
}

@Composable
fun AlternativeLocations() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceAround

        ) {
            Box(modifier = Modifier.weight(1f)) {
                SmallLocation("CHRISTINE'S BAKERY BANGI AVENUE", "2.6 km")
            }
            Box(modifier = Modifier.weight(1f)) {
                SmallLocation("Tenera Hotel Bangi", "3.7 km")
            }
        }
    }


@Composable
fun SmallLocation(name: String, distance: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp)
        .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        )
    {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        Text(name, fontSize = 12.sp, maxLines = 2, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.displayMedium)
        Text(distance, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.displaySmall)
    }
 }
}

@Composable
fun FeaturesGrid() {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            FeatureItem("FastCharge \nStations", R.drawable.fastcharge_stations_icon, Modifier.weight(1f)) {} //
            FeatureItem("AutoCharge", R.drawable.auto_charge, Modifier.weight(1f)) {}
            FeatureItem("Offline Mode", R.drawable.offline, Modifier.weight(1f)) {}
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            FeatureItem("EAST Chargers", R.drawable.east_coast, Modifier.weight(1f)) {}
            FeatureItem("Report Fault", R.drawable.report_fault, Modifier.weight(1f)) {}
            FeatureItem("New Stations", R.drawable.new_site, Modifier.weight(1f)) {}
        }
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
        BottomItem("Scan", R.drawable.scan, Modifier.weight(1f))
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
    A211684_Rabiatul_DrNazatulAini_Lab3Theme {
        ElecTraxHomepage()
    }
}

@Preview
@Composable
fun ElecTraxDarkThemePreview(){
    A211684_Rabiatul_DrNazatulAini_Lab3Theme(darkTheme = true) {
        ElecTraxHomepage()
    }
}

