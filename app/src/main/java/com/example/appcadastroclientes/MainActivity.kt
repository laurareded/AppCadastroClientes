package com.example.appcadastroclientes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await
import android.util.Log

// Importe suas classes de dados e rede
import com.example.appcadastroclientes.data.AppDatabase
import com.example.appcadastroclientes.data.Cliente
import com.example.appcadastroclientes.network.RetrofitInstance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClienteScreen()
        }
    }
}

@Composable
fun ClienteScreen() {

    var clientes by remember { mutableStateOf<List<Cliente>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var erro by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val clienteDao = db.clienteDao()


    LaunchedEffect(Unit) {

        launch(Dispatchers.IO) {
            try {

                var clientesDb = clienteDao.buscarTodos()

                if (clientesDb.isEmpty()) {

                    val clientesApi = RetrofitInstance.api.getClientes().await()

                    if (clientesApi.isNotEmpty()) {

                        clienteDao.inserirTodos(clientesApi)


                        clientesDb = clienteDao.buscarTodos()
                    } else {
                        erro = "Nenhum cliente encontrado na API."
                    }
                }

                clientes = clientesDb
                isLoading = false
            } catch (e: Exception) {

                Log.e("ClienteScreen", "Erro ao carregar clientes: ${e.message}")
                erro = "Erro ao carregar dados: ${e.message}"
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Lista de Clientes", modifier = Modifier.padding(bottom = 16.dp))

        if (isLoading) {

            CircularProgressIndicator()
        } else if (erro != null) {

            Text(text = erro!!)
        } else if (clientes.isEmpty()) {

            Text(text = "Nenhum cliente encontrado.")
        } else {

            LazyColumn {
                items(clientes) { cliente ->
                    ClienteItem(cliente)
                }
            }
        }
    }
}

@Composable
fun ClienteItem(cliente: Cliente) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = "ID: ${cliente.apiId}")
        Text(text = "Nome: ${cliente.nome}")
        Text(text = "Email: ${cliente.email}")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClienteScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Lista de Clientes")
        LazyColumn {
            items(
                listOf(
                    Cliente(1, 1, "Nome Cliente 1", "email1@teste.com"),
                    Cliente(2, 2, "Nome Cliente 2", "email2@teste.com")
                )
            ) { cliente ->
                ClienteItem(cliente)
            }
        }
    }
}