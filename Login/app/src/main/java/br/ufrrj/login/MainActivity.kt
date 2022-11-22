package br.ufrrj.login

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.ufrrj.login.ui.theme.MainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                Container()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainTheme {
        MainTheme {
            Container()
        }
    }
}

@Composable
fun Container() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Form()
        }
    }
}

@Composable
fun Form() {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Fields(
        username,
        password,
        onUsernameChange = { username = it },
        onPasswordChange = { password = it })
    Buttons(username, password)
}

@Composable
fun Fields(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    val padding = 5.dp
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = username,
        onValueChange = onUsernameChange,
        label = {
            Text(text = "Usuário")
        },
        placeholder = {
            Text(text = "fulanoDeTal")
        },
        modifier = Modifier.padding(0.dp, padding)
    )
    TextField(
        value = password,
        onValueChange = onPasswordChange,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = {
            Text(text = "Senha")
        },
        placeholder = {
            Text(text = "123456")
        },
        modifier = Modifier.padding(0.dp, padding),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Esconder senha" else "Exibir senha"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        }
    )
}

@Composable
fun Buttons(username: String, password: String) {
    val padding = 100.dp
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding, 0.dp),
            onClick = { login(context, username, password) },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Text(text = "Fazer Login")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding, 0.dp),
            onClick = {
                Toast.makeText(context, "Sistema indisponível", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
        ) {
            Text(text = "Cadastrar")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding, 0.dp),
            onClick = {
                Toast.makeText(context, "Sistema indisponível", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
        ) {
            Text(text = "Esqueci minha senha")
        }
    }
}

fun login(context: Context, username: String, password: String) {
    if (username.isNotBlank() && password.isNotBlank()) {
        if (username.equals("eduardo") && password.equals("senha")) {
            Toast.makeText(context, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show()
            return
        }
    }
    Toast.makeText(context, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
}