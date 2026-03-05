package com.oblivion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.oblivion.data.AuthRepository
import com.oblivion.ui.HomeScreen

class MainActivity : ComponentActivity() {
    private lateinit var authRepo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authRepo = AuthRepository(this)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF121214) // Deep Dark Background
                ) {
                    CerberusLockScreen(authRepo)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CerberusLockScreen(authRepo: AuthRepository) {
    var pinSet by remember { mutableStateOf(authRepo.isPinSet()) }
    var isUnlocked by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    if (isUnlocked) {
        // BOOM: Load the Cyberpunk Home Screen
        HomeScreen()
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (pinSet) "ENTER MASTER PIN" else "INITIALIZE MASTER PIN",
                color = Color(0xFF4ADE80), 
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = pinInput,
                onValueChange = { pinInput = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF4ADE80),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    if (!pinSet) {
                        if (pinInput.length >= 4) {
                            authRepo.setMasterPin(pinInput)
                            pinSet = true
                            pinInput = ""
                            errorMessage = ""
                        } else {
                            errorMessage = "PIN must be at least 4 digits"
                        }
                    } else {
                        if (authRepo.verifyPin(pinInput)) {
                            isUnlocked = true 
                        } else {
                            errorMessage = "ACCESS DENIED"
                            pinInput = "" 
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80))
            ) {
                Text(if (pinSet) "UNLOCK VAULT" else "SECURE VAULT", color = Color.Black)
            }
        }
    }
}