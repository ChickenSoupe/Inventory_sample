package com.example.inventory.ui.login

// LoginScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.inventory.data.InventoryDatabase
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onSignUpClick: () -> Unit) {
    val context = LocalContext.current
    val userDao = InventoryDatabase.getDatabase(context).userDao()
    val coroutineScope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    val user = userDao.getUser(username)
                    if (user != null && BCrypt.checkpw(password, user.passwordHash)) {
                        // Login successful
                        onLoginSuccess()
                    } else {
                        loginError = "Invalid username or password"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        TextButton(onClick = onSignUpClick) {
            Text("Don't have an account? Sign Up")
        }
        loginError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}
