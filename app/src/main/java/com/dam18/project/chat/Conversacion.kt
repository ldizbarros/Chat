package com.dam18.project.chat

import java.util.*

/**
 * Para guardar los valores que quiero introducir/actualizar en la base de datos
 * Contiene un HashMap con los datos, ya que las funciones que utilizaré necesitan como parámetro
 * un HashMap
 */
data class Conversacion(var idConv:String, var mensaje: String, var fecha: Date) {
    // contenedor para actualizar los datos
    val miHashMapDatos = HashMap<String, Any>()
    val misDatos =  Datos(mensaje,fecha)

    /**
     * Mete los datos del objeto en el HashMap
     */
    fun crearHashMapDatos() {
        miHashMapDatos.put(idConv,misDatos)
    }

    data class Datos (var mensaje: String, var fecha: Date)
}