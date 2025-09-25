package com.example.appcadastroclientes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appcadastroclientes.data.Cliente

@Dao
interface ClienteDao {
    @Insert
    suspend fun inserirTodos(clientes: List<Cliente>)

    @Query("SELECT * FROM cliente")
    suspend fun buscarTodos(): List<Cliente>
}