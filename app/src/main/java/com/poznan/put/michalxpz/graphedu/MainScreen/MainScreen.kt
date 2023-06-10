package com.poznan.put.michalxpz.graphedu.MainScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.poznan.put.michalxpz.graphedu.components.animatedCircle.AnimatedCircle
import com.poznan.put.michalxpz.graphedu.drawerMenu.TopBar
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTypography

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainScreen(
    graphNum: Int,
    verticesNum: Int,
    edgesNum: Int,
    navController: NavController,
    quote: String,
    openDrawer: () -> Unit
) {

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TopBar(
            title = "GraphEdu",
            buttonIcon = Icons.Filled.Menu,
            onButtonClicked = { openDrawer() }
        )
        Spacer(modifier = Modifier.height(26.dp))
        Box(Modifier.padding(16.dp)) {
            AnimatedCircle(
                modifier = Modifier
                    .height(300.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                proportions = listOf(1f, graphNum.toFloat()/100, verticesNum.toFloat()/100, edgesNum.toFloat()/100)
            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlideImage(
                        model = Firebase.auth.currentUser?.photoUrl,
                        contentDescription = "profile thumbnail",
                        modifier = Modifier
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Welcome ${Firebase.auth.currentUser?.displayName}",
                        style = GraphEduTypography.body1,
                        )
                }
                Text(
                    text = "Your summary:",
                    style = GraphEduTypography.h2,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "So far you've created\n$graphNum graphs\n$verticesNum vertices\n$edgesNum edges",
                    style = GraphEduTypography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(Modifier.height(36.dp))
        Text(
            text = "Did you know...",
            style = GraphEduTypography.h3,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 48.dp)
        )
        Text(
            text = quote,
            style = GraphEduTypography.body2,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 48.dp)
                .padding(bottom = 24.dp)
        )
    }
}