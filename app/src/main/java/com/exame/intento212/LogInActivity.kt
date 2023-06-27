package com.exame.intento212

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts
import com.exame.intento212.databinding.ActivityLogInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

///////SININ<--ACTIVITYYYYY


class LogInActivity : BaseActivity() {

    private var binding: ActivityLogInBinding? = null
    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding?.tvRegister?.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding?.tvForgotPassword?.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))

        }

        binding?.btnSignIn?.setOnClickListener {
            sinInUser()
        }

        binding?.btnSignInWithGoogle?.setOnClickListener { signInWithGoogle() }

    }

    private fun sinInUser() {

        val email = binding?.etSinInEmail?.text.toString()
        val password = binding?.etSinInPassword?.text.toString()
        if (validateForm(email, password)) {

            showProgressBar()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivityNavegation::class.java))
                        finish()
                        hideProgressBar()
                    } else {
                        showToast(this, "Ha ocurrido un error al iniciar session")
                        hideProgressBar()
                    }
                }
        }

    }

    private fun signInWithGoogle()
    {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account!=null){
                updateUI(account)
            }
        }

        else
        {
            showToast(this, "Inicio de session fallido")
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        showProgressBar()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful) {
                startActivity(Intent(this,MainActivityNavegation::class.java))
                finish()
                hideProgressBar()
            } else {
                showToast(this, "Ha ocurrido un error al iniciar session")
                hideProgressBar()
            }
        }

    }


    private fun validateForm(email: String, password: String): Boolean {
        return when {

            TextUtils.isEmpty(email) -> {
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

