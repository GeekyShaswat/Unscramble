package com.example.unscramble.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.unscramble.R
import com.example.unscramble.ui.GameViewModel

@Composable
fun CheckState(viewModel: GameViewModel,
               navController: NavController
            )
{
    val words = viewModel.words.collectAsState().value
    val loading = viewModel.loading.collectAsState().value
    val error = viewModel.error.collectAsState().value

    when {
        loading -> {
            Loading()
        }

        error != null && words.isEmpty()  -> {
            ErrorScreen(viewModel)
        }

        else -> {  GameScreen(viewModel, navController as NavHostController)}
    }
}

@Composable
fun Loading(){
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.padding(20.dp))
            Text(text = "Loading...", fontSize = 20.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorScreen(modelClass : GameViewModel){
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
        Column (verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){
            Image(
                painter = painterResource(R.drawable.errorimage),
                contentDescription = "error",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(15.dp))
                    .height(200.dp)
                    .width(200.dp)
                    .border(
                        width = 3.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(15.dp)
                    )
            )
            Spacer(modifier = Modifier.padding(15.dp))
            Card (colors = CardDefaults.cardColors(Color.LightGray), onClick = {
                Log.d("Error", "retry button clicked")
                modelClass.fetchWords()
            }){
                Text("RETRY", fontSize = 12.sp, modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp)), color = Color.Black)
            }
        }
    }
}