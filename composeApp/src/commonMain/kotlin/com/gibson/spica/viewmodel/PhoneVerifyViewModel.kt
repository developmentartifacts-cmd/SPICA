package com.gibson.spica.viewmodel

import android.app.Activity
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import java.util.concurrent.TimeUnit

data class PhoneVerifyState(
    val phoneNumber: String = "",
    val code: String = "",
    val verificationId: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)

class PhoneVerifyViewModel : ViewModel() {
    var state by mutableStateOf(PhoneVerifyState())
        private set

    private val auth = FirebaseAuth.getInstance()
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun updatePhoneNumber(phone: String) {
        state = state.copy(phoneNumber = phone)
    }

    fun updateCode(code: String) {
        state = state.copy(code = code)
    }

    /**
     * Send verification code to user's phone
     */
    fun sendVerificationCode(activity: Activity) {
        val phone = state.phoneNumber.trim()
        if (phone.isEmpty()) {
            state = state.copy(message = "Please enter your phone number.")
            return
        }

        state = state.copy(isLoading = true, message = "Sending code...")

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto verification on some devices
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    state = state.copy(isLoading = false, message = "Failed: ${e.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    state = state.copy(
                        verificationId = verificationId,
                        isLoading = false,
                        message = "Code sent successfully!"
                    )
                    resendToken = token
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * Verify code user entered
     */
    fun verifyCode() {
        val id = state.verificationId
        val code = state.code.trim()

        if (id == null || code.isEmpty()) {
            state = state.copy(message = "Please enter the verification code.")
            return
        }

        state = state.copy(isLoading = true, message = "Verifying...")

        val credential = PhoneAuthProvider.getCredential(id, code)
        signInWithCredential(credential)
    }

    /**
     * Sign in user with verified phone credential
     */
    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state = state.copy(
                        isLoading = false,
                        message = "Phone verified successfully!"
                    )
                    Router.navigate(Screen.Home.route)
                } else {
                    state = state.copy(
                        isLoading = false,
                        message = "Error: ${task.exception?.message}"
                    )
                }
            }
    }

    /**
     * Skip phone verification and proceed
     */
    fun skipVerification() {
        Router.navigate(Screen.Home.route)
    }
}
