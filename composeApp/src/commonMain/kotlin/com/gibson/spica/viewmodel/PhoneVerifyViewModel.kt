package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import java.util.concurrent.TimeUnit

data class PhoneVerifyState(
    val phoneNumber: String = "",
    val verificationCode: String = "",
    val isLoading: Boolean = false,
    val codeSent: Boolean = false,
    val verificationId: String? = null,
    val message: String? = null
)

class PhoneVerifyViewModel : ViewModel() {

    var state by mutableStateOf(PhoneVerifyState())
        private set

    private val auth = FirebaseAuth.getInstance()
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun updatePhoneNumber(value: String) {
        state = state.copy(phoneNumber = value)
    }

    fun updateVerificationCode(value: String) {
        state = state.copy(verificationCode = value)
    }

    fun sendVerificationCode() {
        if (state.phoneNumber.isBlank()) {
            state = state.copy(message = "Please enter a valid phone number.")
            return
        }

        state = state.copy(isLoading = true, message = null)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                state = state.copy(isLoading = false, message = "Verification failed: ${e.message}")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                state = state.copy(
                    isLoading = false,
                    codeSent = true,
                    verificationId = verificationId,
                    message = "Code sent successfully."
                )
                resendToken = token
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(state.phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(Router.currentActivity!!)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode() {
        val verificationId = state.verificationId
        if (verificationId == null) {
            state = state.copy(message = "No verification ID found. Please resend code.")
            return
        }

        val credential = PhoneAuthProvider.getCredential(verificationId, state.verificationCode)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        state = state.copy(isLoading = true)
        auth.currentUser?.linkWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state = state.copy(isLoading = false, message = "Phone verified successfully!")
                    Router.navigate(Screen.Home.route)
                } else {
                    state = state.copy(isLoading = false, message = "Error: ${task.exception?.message}")
                }
            }
    }

    fun resendCode() {
        val phoneNumber = state.phoneNumber
        val token = resendToken
        if (phoneNumber.isBlank() || token == null) {
            state = state.copy(message = "Cannot resend yet. Try sending code again.")
            return
        }

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                state = state.copy(isLoading = false, message = "Verification failed: ${e.message}")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                state = state.copy(
                    isLoading = false,
                    codeSent = true,
                    verificationId = verificationId,
                    message = "Code resent successfully."
                )
                resendToken = token
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(Router.currentActivity!!)
            .setCallbacks(callbacks)
            .setForceResendingToken(token)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun skipVerification() {
        Router.navigate(Screen.Home.route)
    }
}
