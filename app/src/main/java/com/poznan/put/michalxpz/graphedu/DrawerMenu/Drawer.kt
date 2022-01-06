package com.poznan.put.michalxpz.graphedu.DrawerMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.poznan.put.michalxpz.graphedu.R
import com.poznan.put.michalxpz.graphedu.navigation.GraphEduNavigation
import com.poznan.put.michalxpz.graphedu.ui.GraphEduTypography

private val entries = GraphEduNavigation.values()

@Composable
fun DrawerMenu(
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Image(
            //TODO change icon for graph img
            painter = painterResource(R.drawable.ic_baseline_baby_changing_station_24),
            contentDescription = "App icon"
        )
        entries.forEach { screen ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = screen.name,
                style = GraphEduTypography.h4,
                modifier = Modifier.clickable{
                    onDestinationClicked(screen.name)
                }
            )
        }
    }
}