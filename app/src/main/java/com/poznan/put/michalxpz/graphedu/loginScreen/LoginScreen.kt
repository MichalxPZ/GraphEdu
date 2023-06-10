package com.poznan.put.michalxpz.graphedu.loginScreen
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.poznan.put.michalxpz.graphedu.R
import com.poznan.put.michalxpz.graphedu.navigation.GraphEduNavigation
import io.uniflow.core.threading.launchOnIO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState = viewModel.state
    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    val firebaseAuth = Firebase.auth
    val coroutineScope = rememberCoroutineScope()
    val firebaseFirestore = Firebase.firestore

    LaunchedEffect(key1 = uiState) {
        when (uiState.status) {
            Status.NONE -> {}
            Status.LOGGEDIN -> { navController.navigate(GraphEduNavigation.MainScreen.name) }
            Status.ERROR -> {
                Toast.makeText(context, "Error while logging in", Toast.LENGTH_LONG).show()
            }
        }
    }
    Column {

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { authResult ->
                        if (authResult.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            viewModel.onLoginSuccess(user)
                        } else {
                            viewModel.onLoginError(authResult.exception)
                        }
                    }
            } catch (e: ApiException) {
                viewModel.onLoginError(e)
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ) {
            Button(
                onClick = {
                    coroutineScope.launchOnIO {

                        signInWithGoogle(googleSignInClient, launcher)
                        if (firebaseAuth.currentUser != null) {
                            val user = hashMapOf(
                                "uid" to firebaseAuth.currentUser!!.uid,
                                "name" to firebaseAuth.currentUser!!.displayName,
                                "email" to firebaseAuth.currentUser!!.email,
                                "photoUrl" to firebaseAuth.currentUser!!.photoUrl,
                            )
                            var userInDb = false
                            firebaseFirestore.collection("users")
                                .get().addOnSuccessListener {
                                    userInDb = it.documents.filter {
                                        it.data?.get("uid") == firebaseAuth.currentUser!!.uid
                                    }.isNotEmpty()
                                }

                            if (!userInDb) {
                                firebaseFirestore.collection("users").add(
                                    user
                                ).addOnSuccessListener { documentReference ->
                                    Log.e(
                                        TAG,
                                        "DocumentSnapshot added with ID: ${documentReference.id}"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, "Error adding document", e)
                                }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(32.dp)
            ) {
                Text(text = stringResource(R.string.login_with_google))
            }
        }
    }
}

fun signInWithGoogle(
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>
) {
    val signInIntent = googleSignInClient.signInIntent
    launcher.launch(signInIntent)
}
