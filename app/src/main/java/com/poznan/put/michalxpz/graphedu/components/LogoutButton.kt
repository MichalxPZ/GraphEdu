package com.poznan.put.michalxpz.graphedu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.poznan.put.michalxpz.graphedu.R

@Composable
fun LogoutButton(
    navigation: () -> Unit
) {
    Button(
        onClick = {
            Firebase.auth.signOut()
            navigation()
                  },
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .padding(24.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Red.copy(alpha = 0.6f)
        )
    ) {
        Text(
            text = stringResource(R.string.logout),
        )
    }
}

@Preview
@Composable
fun PreviewLogoutButton() {
    LogoutButton({})
}