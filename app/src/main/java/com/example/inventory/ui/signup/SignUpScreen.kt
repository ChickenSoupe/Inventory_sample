package com.example.inventory.ui.signup

// SignUpScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.User
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit) {
    val context = LocalContext.current
    val userDao = InventoryDatabase.getDatabase(context).userDao()
    val coroutineScope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var signUpError by remember { mutableStateOf<String?>(null) }

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
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    if (password != confirmPassword) {
                        signUpError = "Passwords do not match"
                        return@launch
                    }
                    val existingUser = userDao.getUser(username)
                    if (existingUser != null) {
                        signUpError = "Username already exists"
                    } else {
                        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
                        val newUser = User(username = username, passwordHash = hashedPassword)
                        userDao.insertUser(newUser)
                        onSignUpSuccess()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
        signUpError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}
