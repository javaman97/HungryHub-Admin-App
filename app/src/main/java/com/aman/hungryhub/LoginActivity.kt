package com.aman.hungryhub

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aman.hungryhub.databinding.ActivityLoginBinding
import com.aman.hungryhub.model.UserModal
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var email: String
    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googlesignInclient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail()
            .build() // Use the web client ID from the JSON file


        //intilize Firebase Auth
        auth = Firebase.auth
        //initialize Firebase database
        database = Firebase.database.reference
        //intialize google Sign in

        googlesignInclient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.loginButton.setOnClickListener {

            //get text from edit text
            email = binding.email.text.toString().trim()

            password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all Details ", Toast.LENGTH_SHORT).show()
            } else {

                CreateUserAccount(email, password)
            }


        }
        binding.signGoogle.setOnClickListener {

            val signIntent = googlesignInclient.signInIntent
            launcher.launch(signIntent)
        }
        binding.dontHaveAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun CreateUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        updateUi(user)
                    } else {
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("LoginActivity", "Authentication failed: ${task.exception?.message}")
                        Log.d("Account", "createUserAccount: Authentication failed", task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        //get text from edit text
        email = binding.email.text.toString().trim()

        password = binding.password.text.toString().trim()
        val user = UserModal(userName, nameOfRestaurant, email, password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }
    }

    //
    // In your launcher callback
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val idToken = account.idToken // Add this line for debugging
                    Log.d(
                        "GoogleSignIn",
                        "ID Token: $idToken"
                    ) // Log the ID token for debugging

                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // Successfully sign in with Google
                            Toast.makeText(
                                this,
                                "Successfully signed in with Google",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUi(authTask.result?.user)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Google Sign-in failed: ${authTask.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(
                                "LoginActivity",
                                "Google Sign-in failed: ${authTask.exception?.message}"
                            )
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Failed to get Google Sign-In account",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("LoginActivity", "Failed to get Google Sign-In account")
                }
            }
        }

    //check if user is already logged in

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateUi(user: FirebaseUser?) {

        Log.d("LoginActivity", "updateUi called with user: $user")

        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

//    private val launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        result ->
//
//        if(result.resultCode == Activity.RESULT_OK){
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            if(task.isSuccessful){
//                val account: GoogleSignInAccount = task.result
//                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
//                auth.signInWithCredential(credential).addOnCompleteListener {  authTask->
//                    if(authTask.isSuccessful){
//                        //successfully sign in with Google
//                        Toast.makeText(this,"Successfully sign with Google", Toast.LENGTH_SHORT).show()
//                       updateUi(null)
//                    }
//                        else{
//                        Toast.makeText(this,"Google Sign-in failed", Toast.LENGTH_SHORT).show()
//                    }
//
//                }
//            }
//            else{
//                Toast.makeText(this,"Google Sign-in failed", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}