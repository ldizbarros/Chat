package com.dam18.project.chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
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
    var usuario: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usuario = intent.getStringExtra("usuario")

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
            var idConver: String = usuario.toString()+";"+destinatario.text
            conversacion =  Conversacion(idConver.toString(),txtArea_Msg.text.toString(),Date())
            conversacion.crearHashMapDatos()

            miHashMapChildConversacion.put(conversacion.idConv,conversacion.misDatos)
            db_Conversacion!!.updateChildren(miHashMapChildConversacion)
        }

        initListener()
    }

    private fun initListener() {
        val childEventListener = object : ChildEventListener {
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Mensaje Recibido ")
                val info = p0.value
                val clave = p0.key.toString()
                val usuarios  = clave.split(";")
                Log.d(TAG, "Usuarios: "+usuario + " - " +usuarios.get(1))
                if (usuario.equals(usuarios.get(1))){
                    val mensaje = "Mensaje recibido de "+usuarios.get(0)+": \n"+
                            p0.child("mensaje").value
                    Log.d(TAG, mensaje)
                    toast(mensaje)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                Log.d(TAG, "Datos borrados: ")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Datos movidos")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Mensaje Recibido ")
                val info = p0.value
                val clave = p0.key.toString()
                val usuarios  = clave.split(";")
                Log.d(TAG, "Usuarios: "+usuario + " - " +usuarios.get(1))
                if (usuario.equals(usuarios.get(1))){
                    val mensaje = "Mensaje recibido de "+usuarios.get(0)+": \n"+
                            p0.child("mensaje").value
                    Log.d(TAG, mensaje)
                    toast(mensaje)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG, "Error cancelacion")
            }
        }

        // attach el evenListener a la basededatos
        db_Conversacion!!.addChildEventListener(childEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        online = false
        listaUsuarios = Usuarios(usuario, FCMToken.toString(),online)
        listaUsuarios.crearHashMapDatos()
        miHashMapChildUsuario.put(usuario,listaUsuarios.misDatos)
        db_Usuarios!!.updateChildren(miHashMapChildUsuario)
    }

    override fun onPause(){
        super.onPause()
        online = false
        listaUsuarios = Usuarios( usuario, FCMToken.toString(),online)
        listaUsuarios.crearHashMapDatos()
        miHashMapChildUsuario.put(usuario,listaUsuarios.misDatos)
        db_Usuarios!!.updateChildren(miHashMapChildUsuario)
    }

    override fun onStop(){
        super.onStop()
        online = false
        listaUsuarios = Usuarios( usuario, FCMToken.toString(),online)
        listaUsuarios.crearHashMapDatos()
        miHashMapChildUsuario.put(usuario,listaUsuarios.misDatos)
        db_Usuarios!!.updateChildren(miHashMapChildUsuario)
    }

}
