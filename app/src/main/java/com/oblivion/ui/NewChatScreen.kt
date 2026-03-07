package com.oblivion.ui

// Explicit Imports to prevent "Unresolved Reference"
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatScreen(onClose: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var showScanner by remember { mutableStateOf(false) }

    // Check Camera Permissions
    val context = LocalContext.current
    val hasCameraPermission = remember {
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    if (showScanner) {
        if (hasCameraPermission) {
            QrScanner(onScanResult = { result -> 
                searchQuery = result
                showScanner = false 
            })
        } else {
            // Failsafe if camera permission is missing
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFF16181A)), 
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Camera permission denied.", color = Color(0xFFF87171))
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(onClick = { showScanner = false }) { 
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) 
                    }
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                
                TextField(
                    value = searchQuery, 
                    onValueChange = { searchQuery = it }, 
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search names or addresses") },
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                )
                
                IconButton(onClick = { showScanner = true }) { 
                    Icon(Icons.Default.Camera, contentDescription = "Scan QR") 
                }
            }
            
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                item {
                    Text("Suggested Contacts", color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text("Searching global DHT for: $searchQuery", color = Color(0xFF4ADE80))
                }
            }
        }
    }
}