// 🔹 Add these imports
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AccountSetupViewModel {
    // ...
    var profileUri by mutableStateOf<Uri?>(null)
    var coverUri by mutableStateOf<Uri?>(null)
    private val storage = FirebaseStorage.getInstance().reference

    fun onProfileSelected(uri: Uri) {
        profileUri = uri
    }

    fun onCoverSelected(uri: Uri) {
        coverUri = uri
    }

    fun saveAccountData() {
        val userId = auth.currentUser?.uid ?: return
        isSaving = true
        errorMessage = null

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 🔹 Upload images first
                val profileUrl = profileUri?.let {
                    val ref = storage.child("users/$userId/profile.jpg")
                    ref.putFile(it).await()
                    ref.downloadUrl.await().toString()
                }

                val coverUrl = coverUri?.let {
                    val ref = storage.child("users/$userId/cover.jpg")
                    ref.putFile(it).await()
                    ref.downloadUrl.await().toString()
                }

                // 🔹 Save user details
                val userMap = mapOf(
                    "firstName" to firstName.trim(),
                    "lastName" to lastName.trim(),
                    "username" to username.trim(),
                    "country" to selectedCountry,
                    "state" to selectedState,
                    "town" to selectedTown,
                    "phone" to phoneNumber.trim(),
                    "bio" to bio.trim(),
                    "profileUrl" to profileUrl,
                    "coverUrl" to coverUrl
                )

                firestore.collection("users").document(userId).set(userMap).await()

                isSaving = false
                showConfirmationDialog = false
                Router.navigate(Screen.Home.route)
            } catch (e: Exception) {
                isSaving = false
                errorMessage = e.localizedMessage
            }
        }
    }
}
