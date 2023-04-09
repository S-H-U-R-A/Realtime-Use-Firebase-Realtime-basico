 package com.example.holafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.holafirebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

 class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ACEDEMOS A LA RAIZ DE LA BD EN REMOTO
        val dataBase = Firebase.database.reference
        //OBTENEMOS EL NODO/PROPIEDAD QUE QUEREMOS OBSERVAR
        val dataRef = dataBase.child("hola_firebase").child("data")

        //SE CREA EL LISTENER DE LA PROPIEDAD QUE QUEREMOS OBSERVAR
        val listener = object : ValueEventListener{
            //ESTE MÉTODO ES EL OYENTE DE LA BD EN REMOTO
            override fun onDataChange(snapshot: DataSnapshot) {

                //SI LA PROPIEDAD EXISTE
                if(snapshot.exists()){
                    //SE OBTIENE EL VALOR EN EL MOMENTO DE LA PROPIEDAD EN REMOTO
                    //A LA CUAL SE LE ASIGNE ESTE LISTENER
                    val data = snapshot.getValue( String::class.java )

                    binding.tvData.text = "FireBase Remote: $data"
                }else{
                    binding.tvData.text = "Ruta sin datos"
                }

            }

            //SI OCURRE UN ERROR AL ESCUCHAR
            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@MainActivity, "Error al leer datos", Toast.LENGTH_SHORT).show()

            }
        }

        //ASIGNAMOS A EL NODO EL LISTENER
        dataRef.addValueEventListener(listener)

        //COMO YA TENEMOS LA REFERENCIA DE NODO EN FIREBASE,
        //PODEMOS EDITAR LA VARIABLE DESDE AQUI
        binding.btnSend.setOnClickListener {
            val data = binding.etData.text.toString()
            dataRef.setValue(data)
                .addOnSuccessListener { Toast.makeText(this@MainActivity, "Dato Enviado", Toast.LENGTH_SHORT).show()  }
                .addOnFailureListener { Toast.makeText(this@MainActivity, "Error al Enviar", Toast.LENGTH_SHORT).show() }
                .addOnCompleteListener { Toast.makeText(this@MainActivity, "Acción Completada", Toast.LENGTH_SHORT).show() }
        }

        //COMO ELIMINAR UNA REFERENCIA
        binding.btnSend.setOnLongClickListener{
            dataRef.removeValue()
            true
        }

    }
}