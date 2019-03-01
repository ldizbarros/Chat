package com.dam18.project.chat

import java.util.*

/**
 * Para guardar los valores que quiero introducir/actualizar en la base de datos
 * Contiene un HashMap con los datos, ya que las funciones que utilizaré necesitan como parámetro
 * un HashMap
 */
data class Usuarios(var nombre: String, var token: String, var online: Boolean) {
    // contenedor para actualizar los datos
    val miHashMapDatos = HashMap<String, Any>()
    val misDatos =  Datos(token,online)

    /**
     * Mete los datos del objeto en el HashMap
     */
    fun crearHashMapDatos() {
        miHashMapDatos.put(nombre, misDatos)
    }

    data class Datos (var token: String, var online: Boolean)
}