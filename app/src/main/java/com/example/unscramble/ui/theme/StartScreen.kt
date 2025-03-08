package com.example.unscramble.ui.theme

import android.util.Log
import android.widget.Toast
import androidx.collection.emptyLongSet
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.unscramble.ui.GameViewModel
import com.example.unscramble.ui.Names


@Composable
fun Username(viewModel : GameViewModel  , navController: NavHostController){
    val username = viewModel.username.collectAsState().value
    val selectedLevel = viewModel.selectedLevel.collectAsState().value
    Card(elevation = CardDefaults.cardElevation(15.dp) , modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally , modifier = Modifier.fillMaxSize()) {
            Text(text = "Enter Your Name and Select Level" , modifier = Modifier.padding(6.dp), fontSize = 16.sp)
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            val isUsernameValid = viewModel.isUsernameValid.collectAsState().value;

            OutlinedTextField(
                value = username,
                onValueChange = { viewModel.updateUsername(it) },
                label = { Text("Username") },
                placeholder = { Text("Enter your username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                isError = !isUsernameValid
            )
            Text(text = "Select Level", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            val levels = listOf("easy", "medium", "hard")
            levels.forEach { level ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable { viewModel.updateLevel(level) }
                ) {
                    RadioButton(
                        selected = selectedLevel == level,
                        onClick = { viewModel.updateLevel(level) },
                        modifier = Modifier.padding(start = 30.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = level, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            SubmitButton(viewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitButton(
    viewModel : GameViewModel ,
    navController: NavHostController
    )
{
    Card(
        elevation = CardDefaults.cardElevation(15.dp),
        colors = CardDefaults.cardColors(containerColor =Color.LightGray),
        modifier = Modifier
            .height(50.dp)
            .width(120.dp)
            .padding(top = 8.dp),
        onClick = {if(viewModel.validateUsername(viewModel.username.value) && viewModel.validateLevel(viewModel.selectedLevel.value)) {
                navController.navigate(Names.Play.name) {
                popUpTo(Names.Username.name) { inclusive = true }
            }
            }
            else{
            Toast.makeText(navController.context, "Username and level cannot be empty", Toast.LENGTH_SHORT).show()
        }
            viewModel.pickWordOnSubmit()
            Log.d("Submit","Submit button clicked")
        }
    ) {

        Text(
            text = "SUBMIT" ,
            fontSize = 20.sp ,
            fontWeight = FontWeight.Bold ,
            textAlign = TextAlign.Center ,
            color = Color.Black,
            modifier = Modifier.padding(start = 22.dp , top = 10.dp)
        )
    }
}
