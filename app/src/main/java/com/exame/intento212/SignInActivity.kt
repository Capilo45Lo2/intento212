package com.exame.intento212

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import com.exame.intento212.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWebException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


///SIN UP ACTIVITYYY


class SignInActivity : BaseActivity() {

    private var binding: ActivitySignInBinding? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth

        binding?.tvLoginPage?.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

            binding?.btnSignUp?.setOnClickListener { registerUser() }

    }

    private fun registerUser() {
        val name = binding?.etSinUpName?.text.toString()
        val email = binding?.etSinUpEmail?.text.toString()
        val password = binding?.etSinUpPassword?.text.toString()
        if (validateForm(name, email, password)) {
            showProgressBar()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast(this, "El usuario fue creado correctamente")
                        hideProgressBar()
                        startActivity(Intent(this, MainActivityNavegation::class.java))
                        finish()
                    } else {
                        showToast(this, "El usuario no fue creado")
                        hideProgressBar()
                    }
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                binding?.tilName?.error = "ingrese nombre"
                false
            }

            TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()-> {
                binding?.tilEmail?.error = "ingrese correo"
                false
            }

            TextUtils.isEmpty(password) -> {
                binding?.tilPassword?.error = "ingrese contraseña"
                false
            }

            else -> {
                true
            }
        }
    }
}