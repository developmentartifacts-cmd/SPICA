package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.gibson.spica.data.AuthService
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import java.util.concurrent.TimeUnit

class PhoneVerifyViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    var verificationId by mutableStateOf<String?>(null)
    var code by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var isSending by mutableStateOf(false)
    var isVerifying by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    fun initPhone(number: String) {
        phoneNumber = number
    }

    fun sendCode() {
        if (phoneNumber.isBlank()) {
            errorMessage = "Phone number cannot be empty."
            return
        }
        isSending = true
        errorMessage = null
        successMessage = null

        val activity = AuthService.currentActivity ?: return

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    verifyWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    isSending = false
                    errorMessage = e.message
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    isSending = false
                    successMessage = "Verification code sent."
                    this@PhoneVerifyViewModel.verificationId = verificationId
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode() {
        val id = verificationId ?: run {
            errorMessage = "No verification ID found."
            return
        }
        if (code.isBlank()) {
            errorMessage = "Please enter the verification code."
            return
        }
        isVerifying = true
        errorMessage = null

        val credential = PhoneAuthProvider.getCredential(id, code)
        verifyWithCredential(credential)
    }

    private fun verifyWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                isVerifying = false
                if (task.isSuccessful) {
                    markPhoneVerified()
                } else {
                    errorMessage = task.exception?.message ?: "Verification failed."
                }
            }
    }

    private fun markPhoneVerified() {
        val user = AuthService.getCurrentUser() ?: return
        val data = mapOf("phoneVerified" to true)
        db.collection("users").document(user.uid)
            .update(data)
            .addOnSuccessListener {
                successMessage = "Phone verified successfully."
                Router.navigate(Screen.Home.route)
            }
            .addOnFailureListener { e ->
                errorMessage = e.message
            }
    }

    fun skip() {
        Router.navigate(Screen.Home.route)
    }
}
