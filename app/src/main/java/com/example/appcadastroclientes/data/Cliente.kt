package com.example.appcadastroclientes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cliente")
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("id")
    val apiId: Int,
    @SerializedName("name")
    val nome: String,
    @SerializedName("email")
    val email: String
)