package com.oblivion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatScreen(onClose: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var showScanner by remember { mutableStateOf(false) }

    if (showScanner) {
        QrScanner(onScanResult = { result -> 
            searchQuery = result
            showScanner = false 
        })
    } else {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                
                TextField(
                    value = searchQuery, 
                    onValueChange = { searchQuery = it }, 
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search names or addresses") },
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                )
                
                // THE FIX: Changed from Camera to Add (which is in the core library)
                IconButton(onClick = { showScanner = true }) { 
                    Icon(Icons.Default.Add, contentDescription = "Scan QR") 
                }
            }
        }
    }
}