package com.dam18.project.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    // Filtro para el LOG
    val TAG = "CONEXION DB"
    // Referencias a la DB
    private var db_Usuarios: DatabaseReference? = null
    private var db_Conversacion: DatabaseReference? = null
    // Token del dispositivo. El token es el que identifica el dispositivo
    //en la base de datos
    private var FCMToken: String? = null
    // key unica creada automaticamente al añadir un child
    lateinit var listaUsuarios : Usuarios
    lateinit var conversacion : Conversacion
    // para actualizar los datos necesito un hash map
    val miHashMapChildUsuario = HashMap<String, Any>()
    val miHashMapChildConversacion = HashMap<String, Any>()

    var online = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usuario = intent.getStringExtra("usuario")

        // referencia a la base de datos del proyecto en firebase
        db_Usuarios = FirebaseDatabase.getInstance().getReference("/Chat/Usuarios")
        db_Conversacion = FirebaseDatabase.getInstance().getReference("/Chat/Conversacion")

        if (savedInstanceState == null) {
            try {
                // Añado el usuario a la lista
                FCMToken = FirebaseInstanceId.getInstance().token
                listaUsuarios = Usuarios(usuario, FCMToken.toString(),online)
                listaUsuarios.crearHashMapDatos()
                miHashMapChildUsuario.put(usuario,listaUsuarios.misDatos)
                db_Usuarios!!.updateChildren(miHashMapChildUsuario)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "Error escribiendo datos ${e}")
            }
        }
        // BOTON ENVIAR
        btnEnviar.setOnClickListener { view ->
            Log.d(TAG,"ACTUALIZAR DATOS en USUARIOS")
            var idConver: String = usuario.toString()+";"+"miriam"
            conversacion =  Conversacion(idConver.toString(),txtArea_Msg.text.toString(),Date())
            conversacion.crearHashMapDatos()

            miHashMapChildConversacion.put(conversacion.idConv,conversacion.misDatos)
            db_Conversacion!!.updateChildren(miHashMapChildConversacion)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        online = false
        listaUsuarios = Usuarios("Laura", FCMToken.toString(),online)
        listaUsuarios.crearHashMapDatos()
        miHashMapChildUsuario.put("Laura",listaUsuarios.misDatos)
        db_Usuarios!!.updateChildren(miHashMapChildUsuario)
    }
}
