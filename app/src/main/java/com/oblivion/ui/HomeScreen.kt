package com.oblivion.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled
import androidx.compose.material.icons.outlined
import androidx.compose.material3.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- CYBERPUNK THEME COLORS ---
val DarkBackground = Color(0xFF16181A)
val SurfaceWhite = Color(0xFFFFFFFF)
val NeonGreen = Color(0xFF4ADE80)
val TextGray = Color(0xFF8E8E93)
val BadgeDark = Color(0xFF2C2C2E)

// --- MOCK DATA FROM YOUR SCREENSHOT ---
data class ChatPreview(
    val name: String,
    val time: String,
    val preview: String,
    val unreadCount: Int,
    val isEncrypted: Boolean
)

val dummyChats = listOf(
    ChatPreview("Julian Croft", "10:15 AM", "Encrypted files attached.", 3, true),
    ChatPreview("Cryptic Team", "9:42 AM", "Sarah: The protocol is ready.", 0, true),
    ChatPreview("Sarah Jenkins", "Yesterday", "Confirming meeting tonight.", 0, true),
    ChatPreview("Operation Chimera", "Wed", "Deployment check required.", 1, true),
    ChatPreview("David Chen", "Mon", "No logs found in the terminal.", 0, true)
)

@Composable
fun HomeScreen() {
    var currentTab by remember { mutableStateOf("Chats") }
    var chatHistory by remember { mutableStateOf(dummyChats) }
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showNewChat by remember { mutableStateOf(false) }

    val filteredChats = if (searchQuery.isBlank()) chatHistory else chatHistory.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Box(modifier = Modifier.fillMaxSize().background(DarkBackground)) {
        if (showNewChat) {
            NewChatScreen(onClose = { showNewChat = false })
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                
                // Animated Top Bar
                Crossfade(targetState = isSearching, label = "Search Transition") { searchActive ->
                    if (searchActive) {
                        SearchTopBar(searchQuery, { searchQuery = it }, { isSearching = false; searchQuery = "" })
                    } else {
                        OblivionTopBar { isSearching = true }
                    }
                }
                
                // The White Main Container with rounded corners
                Surface(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = SurfaceWhite
                ) {
                    ChatListScreen(filteredChats)
                }
            }
            
            // Bottom Navigation overrides the bottom space
            if (!isSearching) {
                BottomNavArea(
                    currentTab = currentTab, 
                    onTabSelected = { currentTab = it }, 
                    onFabClick = { showNewChat = true }, 
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(searchQuery: String, onQueryChange: (String) -> Unit, onCloseSearch: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onCloseSearch) { Icon(Icons.Default.ArrowBack, contentDescription = "Close", tint = Color.White) }
        TextField(
            value = searchQuery, onValueChange = onQueryChange, placeholder = { Text("Search...", color = TextGray) },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = NeonGreen, unfocusedIndicatorColor = Color.DarkGray, focusedTextColor = Color.White, unfocusedTextColor = Color.White),
            singleLine = true, modifier = Modifier.weight(1f).focusRequester(focusRequester)
        )
    }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
fun OblivionTopBar(onSearchClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp), verticalAlignment = Alignment.CenterVertically) {
        // Custom Drawn Shield Logo
        Canvas(modifier = Modifier.size(24.dp)) {
            val path = Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(size.width, size.height * 0.15f)
                lineTo(size.width, size.height * 0.6f)
                quadraticBezierTo(size.width, size.height * 0.9f, size.width / 2f, size.height)
                quadraticBezierTo(0f, size.height * 0.9f, 0f, size.height * 0.6f)
                lineTo(0f, size.height * 0.15f)
                close()
            }
            drawPath(path, color = NeonGreen)
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text("OBLIVION", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, letterSpacing = 1.5.sp)
            Text("Secure Messenger", color = TextGray, fontSize = 13.sp)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White, modifier = Modifier.size(28.dp).clickable { onSearchClick() })
        
        Spacer(modifier = Modifier.width(20.dp))
        
        // Profile Picture / QR Access
        Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable { /* Show QR */ }) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).border(2.dp, NeonGreen, CircleShape).background(BadgeDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.LightGray, modifier = Modifier.size(28.dp))
            }
            Box(
                modifier = Modifier.align(Alignment.BottomEnd).size(16.dp).offset(x = 2.dp, y = 2.dp).background(BadgeDark, CircleShape).border(1.dp, DarkBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Lock, contentDescription = "Secured", tint = Color.White, modifier = Modifier.size(10.dp))
            }
        }
    }
}

@Composable
fun ChatListScreen(chats: List<ChatPreview>) {
    if (chats.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("No Chats Found.", color = Color.DarkGray)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)) {
            items(chats) { chat ->
                ChatItem(chat)
                // Divider handled here ensures it appears between items, not top/bottom of every item
                Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(start = 84.dp, end = 20.dp))
            }
        }
    }
}

@Composable
fun ChatItem(chat: ChatPreview) {
	Divider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(start = 84.dp, end = 20.dp))
    Row(
        modifier = Modifier.fillMaxWidth().clickable { /* Open Chat Action */ }.padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Chat Avatar
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier.size(54.dp).clip(CircleShape).background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF333333), modifier = Modifier.size(40.dp))
            }
            if (chat.isEncrypted) {
                Box(
                    modifier = Modifier.align(Alignment.BottomEnd).size(20.dp).offset(x = 2.dp, y = 2.dp).background(BadgeDark, CircleShape).border(2.dp, SurfaceWhite, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "E2EE", tint = Color.White, modifier = Modifier.size(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Message Details
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = chat.name, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color.Black)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = chat.time, fontSize = 13.sp, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = chat.preview,
                    color = Color.DarkGray,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Unread Badge
        if (chat.unreadCount > 0) {
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier.background(BadgeDark, RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = chat.unreadCount.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BottomNavArea(currentTab: String, onTabSelected: (String) -> Unit, onFabClick: () -> Unit, modifier: Modifier) {
    Box(modifier = modifier.fillMaxWidth().height(110.dp), contentAlignment = Alignment.BottomCenter) {
        
        // The Dark Bottom Nav Bar
        Row(
            modifier = Modifier.fillMaxWidth().height(75.dp).background(DarkBackground, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(Icons.Default.Email, "Chats", currentTab == "Chats") { onTabSelected("Chats") }
            NavItem(Icons.Default.Person, "Contacts", currentTab == "Contacts") { onTabSelected("Contacts") }
            
            Spacer(modifier = Modifier.width(60.dp)) // Space for the Glowing FAB
            
            NavItem(Icons.Default.Notifications, "Notifications", currentTab == "Notifications") { onTabSelected("Notifications") }
            NavItem(Icons.Default.Settings, "Settings", currentTab == "Settings") { onTabSelected("Settings") }
        }

        // The Glowing FAB effect
        Box(
            modifier = Modifier.align(Alignment.TopCenter).size(110.dp).background(Brush.radialGradient(listOf(NeonGreen.copy(alpha = 0.25f), Color.Transparent)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = NeonGreen,
                contentColor = Color.Black,
                shape = CircleShape, // Forces it to be a perfect circle
                modifier = Modifier.size(60.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Secure Chat", modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun NavItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) NeonGreen else TextGray
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = color, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}