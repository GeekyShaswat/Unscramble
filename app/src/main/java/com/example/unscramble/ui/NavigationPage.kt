package com.example.unscramble.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.unscramble.ui.theme.Username

enum class Names() {
    Username,
    Play,
    Score
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Start(modelClass : GameViewModel,
          navController : NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = Names.Username.name
    ) {
        composable(route = Names.Username.name) {
            Username(modelClass, navController)
        }
        composable(route = Names.Play.name) {
            CheckState(modelClass,navController)
        }
        composable(route = Names.Score.name) {
            // ScoreScreen(modelClass)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.route == Names.Play.name) {
                navController.popBackStack(Names.Username.name, false)
            }
        }
    }

}
