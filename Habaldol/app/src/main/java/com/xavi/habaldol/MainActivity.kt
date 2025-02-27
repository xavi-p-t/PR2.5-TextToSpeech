package com.xavi.habaldol

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.xavi.habaldol.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), OnInitListener {

    private lateinit var textToSpeech: TextToSpeech
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializamos ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el TextToSpeech
        textToSpeech = TextToSpeech(this, this)

        // Configuración del botón para que lea el texto del EditText cuando se pulse
        binding.speakButton.setOnClickListener {
            val textToRead = binding.editText.text.toString()
            if (textToRead.isNotEmpty()) {
                speakText(textToRead)
            } else {
                Toast.makeText(this, "Por favor, ingresa algún texto", Toast.LENGTH_SHORT).show()
            }
        }

        // Ajustar el padding para la barra del sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Establecer el idioma como español de España
            val loc = Locale("es","ES")  // Español de España
            val langResult = textToSpeech.setLanguage(loc)

            // Verificar si el idioma está disponible
            if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Idioma no soportado o no disponible", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Error al inicializar Text-to-Speech", Toast.LENGTH_SHORT).show()
        }
    }

    private fun speakText(text: String) {
        // Hacer que el TextToSpeech lea el texto ingresado
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        // Liberar los recursos del TextToSpeech cuando la actividad se destruye
        if (this::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}