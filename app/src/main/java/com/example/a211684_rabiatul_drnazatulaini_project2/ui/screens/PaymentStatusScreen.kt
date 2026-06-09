package com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

import com.example.a211684_rabiatul_drnazatulaini_project1.ui.theme.A211684_Rabiatul_DrNazatulAini_Project1Theme
import com.example.a211684_rabiatul_drnazatulaini_project2.data.repository.FirebaseRepository

import com.example.a211684_rabiatul_drnazatulaini_project2.R



@Composable
fun PaymentStatusScreen(
    onSubmitClick:(Int, String) -> Unit = {_, _->}, //rating, reviewText
    onBackHomeClick:()-> Unit = {}
){
    var reviewText by remember {
        mutableStateOf("")
    }
    var rating by remember{mutableIntStateOf(0)
    }

    val firebaseRepository = FirebaseRepository()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(R.drawable.payment_successful_image),
            contentDescription = "Payment Success",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Payment Successful",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Thank you for charging with us",
            color = Color.Black,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            for (i in 1..5) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint =
                        if(i<=rating)
                            Color(0xFFFFD700)
                        else
                            Color.LightGray,
                    modifier = Modifier
                                .size(40.dp)
                                .clickable{
                                    rating = i
                                }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Review / Report:",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            placeholder = { Text("Leave us a review to help others")},
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(24.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onSubmitClick(
                    rating,
                    reviewText
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1B5E20)
            )

        ){
            Text("Submit")

        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBackHomeClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            )
        ){
        Text(
            "Back to Home",
            color = Color.Black
        )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}


@Preview(showBackground = true, showSystemUi=true)
@Composable
fun PaymentStatusScreenPreview(){
    A211684_Rabiatul_DrNazatulAini_Project1Theme{
        PaymentStatusScreen()
    }
}