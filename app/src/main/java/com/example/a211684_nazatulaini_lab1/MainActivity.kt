package com.example.a211684_nazatulaini_lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.a211684_nazatulaini_lab1.ui.theme.A211684_Nur_Rabiatul_Nabila_NazatulAini_Lab1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //make UI go full screen
        setContent {
            A211684_Nur_Rabiatul_Nabila_NazatulAini_Lab1Theme {
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
    Box(modifier = Modifier
        .fillMaxSize() //make full width & full height
        .background(Color(0xFFF0FFFF))){

        var destinationInput by remember {mutableStateOf("")} //user input
        var isMessageVisible by remember { mutableStateOf(false)}  //show & hide notification message
        var searchResult by remember {mutableStateOf("")} //update showing stations near []
        var showSuggestions by remember {mutableStateOf(false)} //whether the window is open/closed

        TopWallpaper()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 150.dp)
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        )
        {
            EditSearchField(
                label = R.string.where,
                leadingIcon = R.drawable.pin_destination_finder,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                value = destinationInput,
                onValueChange = {
                    destinationInput = it
                    if (it.isEmpty()) {
                        showSuggestions = false
                        searchResult = ""
                    }
       },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            )
            Button(
                onClick = {
                    searchResult = "Showing stations near $destinationInput"
                    showSuggestions = true
                },
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Search")
            }

            if (searchResult.isNotEmpty()) {
                Text(
                    text = searchResult,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
            }

            AnimatedVisibility(
                visible = showSuggestions,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                SearchSuggestions(query = destinationInput) { selected ->
                    destinationInput = selected
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            NearestLocation() //card
            Spacer(modifier = Modifier.height(12.dp)) //spacing between two component (below it)

            AlternativeLocations()
            Spacer(modifier = Modifier.height(12.dp))

            FeaturesGrid()
            Spacer(modifier = Modifier.weight(1f)) //occupy remaining space & push BottomNav to the bottom

            BottomNav()
        }

        Notification(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { isMessageVisible = !isMessageVisible }
        )

        if(isMessageVisible){
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 95.dp)
                    .width(260.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = "No new notifications yet",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun TopWallpaper(){
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
fun Notification(modifier: Modifier = Modifier, onClick: () -> Unit = {}){
        IconButton(
            onClick = onClick,
            modifier = modifier
                .padding(top = 40.dp, end = 16.dp)
                .size(48.dp)
                .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
        ){
            Icon(
                painter = painterResource(id = R.drawable.bell),
                contentDescription = "Notification",
                tint = Color.Unspecified,
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
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        leadingIcon = { Icon (
            painter = painterResource(id=leadingIcon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
            )
            },
        label = {Text (stringResource( label))},
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ){
        Column(modifier = Modifier.padding(8.dp)){
            Text(
                text = "Suggestions for: \"$query\"",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
            SuggestionItem("Bangi Gateway Mall", "1.8 km", onSelect)
            SuggestionItem("Christine's Bakery Bangi Avenue Mall", "2.6 km", onSelect)
        }
    }
}

@Composable
fun SuggestionItem(name:String, distance:String, onSelect: (String) -> Unit){
    Row(
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
            tint = Color(0xFF00AEEF)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = distance, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun NearestLocation() {
    Column(
        modifier = Modifier
            // remove offset bcs (lower -ve = card overlap higher on top of wallpaper) offset = move/shift position
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(12.dp))
            .padding(16.dp) //distance between content & border
            .padding(top = 20.dp) //only between content & top border
    ) {
        Text(
            "Your nearest station:",
            fontSize = 12.sp,
        )
        Text(
            "[Public] BANGI GATEWAY MALL (Shopping Mall)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "60 kW max  - 1.8km  - 0.0",
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {}, //empty onClick first, lead to nowhere
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00AEEF)
            )
        ) {
            Text("1.8 km away - Get Directions")
        }
    }
}

@Composable
fun AlternativeLocations() {
    Row( //horizontal arrangement
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), //sec card follow the same height (take the highest height)
                horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(modifier = Modifier.weight(1f)) { //use box to apply weight easily, 2 box = 2 cards (2 small locations)
            SmallLocation("CHRISTINE'S BAKERY BANGI AVENUE", "2.6 km")
        }
        Box(modifier = Modifier.weight(1f)) {
            SmallLocation("Tenera Hotel Bangi", "3.7 km")
        }
    }
}

@Composable
fun SmallLocation(name: String, distance: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight() //both follow parent height
            .padding(4.dp) //distance outside (between border)
            .background(Color(0xFFEAEAEA), RoundedCornerShape(12.dp))
            .padding(12.dp) //distance inside (between content & border)
    ) {
        Text(name, fontSize = 12.sp, maxLines = 2, color = Color.Black)
        Text(distance, fontSize = 10.sp, color = Color.Black)
    }
}

@Composable
fun FeaturesGrid(){
    Column {
        Row(  //in column 1, there is 3 rows of feature items
            modifier = Modifier.fillMaxWidth()
        ) {
            FeatureItem("FastCharge \nStations", R.drawable.fastcharge_stations_icon, Modifier.weight(1f)){}
            FeatureItem("AutoCharge", R.drawable.auto_charge, Modifier.weight(1f)){}
            FeatureItem("Offline Mode", R.drawable.offline, Modifier.weight(1f)){}
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
        )
        {
            FeatureItem("EAST Chargers", R.drawable.east_coast,Modifier.weight(1f)){}
            FeatureItem("Report Fault", R.drawable.report_fault,Modifier.weight(1f)){}
            FeatureItem("New Stations", R.drawable.new_site, Modifier.weight(1f)){}
        }
    }
}

@Composable
fun FeatureItem(label:String, iconRes: Int, modifier: Modifier = Modifier, onClick:() ->Unit){ //iconRes:Int because R.drawable.xxx is integerID
    Column(
        modifier = modifier
        .clip(RoundedCornerShape(30.dp))
        .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box( //each circle features box
            modifier = Modifier
                .size(60.dp)
                .background(Color(0xFFDDA0DD), RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ){
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )
        }
             Spacer(modifier = Modifier.height(8.dp)) //spacing between icon & text
             Text(
                 text = label,
                 fontSize = 10.sp,
                 maxLines = 2,
                 textAlign = TextAlign.Center
                )
    }
}

@Composable
fun BottomNav() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
    )
    {
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
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(30.dp),
                contentAlignment = Alignment.Center
        ){
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(24.dp))
        }
        Text(label, fontSize = 10.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GGPreview() {
    A211684_Nur_Rabiatul_Nabila_NazatulAini_Lab1Theme {
        ElecTraxHomepage()
    }
}
