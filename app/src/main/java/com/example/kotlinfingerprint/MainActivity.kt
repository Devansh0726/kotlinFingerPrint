package com.example.kotlinfingerprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import pl.droidsonroids.gif.GifImageView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    val btnLogin = findViewById<Button>(R.id.btnLogin)

        val biometricManager = BiometricManager.from(this@MainActivity)
        val biometricStatus: Int = biometricManager.canAuthenticate()

        if (biometricStatus == BiometricManager.BIOMETRIC_SUCCESS){
            Toast.makeText(this, "Biometric Authentication is supported Click on login", Toast.LENGTH_SHORT).show()
            btnLogin.visibility = View.VISIBLE
        } else if (biometricStatus == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE){
            Toast.makeText(this, "This device doesn't have a finer print sensor you can use passcode",Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this,"The biometric sensor is currently unavailable", Toast.LENGTH_SHORT).show()
        }

        val executor = ContextCompat.getMainExecutor(this@MainActivity)

        val biometricPrompt = BiometricPrompt(this@MainActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@MainActivity, "Please authenticate again", Toast.LENGTH_SHORT).show()
                }
//              This method is called when authentication is success
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(this@MainActivity, "Login Success", Toast.LENGTH_SHORT).show()
                }
//
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@MainActivity, "Login failed unknown user detected", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication")
            .setDescription("Use your fingerprint to unlock").setNegativeButtonText("Cancel")
            .build()

        btnLogin.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

    }
}