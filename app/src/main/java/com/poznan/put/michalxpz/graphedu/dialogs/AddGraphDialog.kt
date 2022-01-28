package com.poznan.put.michalxpz.graphedu.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.poznan.put.michalxpz.graphedu.activity.MainActivityContract
import com.poznan.put.michalxpz.graphedu.activity.MainActivityViewModel
import com.poznan.put.michalxpz.graphedu.data.GraphsItem


@Composable
fun AddGraphDialog(
    message: MutableState<String>,
    editText: MutableState<String>,
    openDialog: Boolean,
    graphs: ArrayList<GraphsItem>,
    viewModel: MainActivityViewModel
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.background)
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = "Your graph name: ")

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = editText.value,
                onValueChange = {
                    viewModel.setEvent(MainActivityContract.Event.OnDialogTextEdit)
                    viewModel.uiState.value.editText = it
                    editText.value = it },
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.align(Alignment.End)
        ) {
            Button(
                onClick = {
                    viewModel.setEvent(MainActivityContract.Event.OnCloseDialogClicked)
                }
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    viewModel.setEvent(MainActivityContract.Event.OnDialogTextEdit)
                    viewModel.setEvent(MainActivityContract.Event.OnOkDialogClicked)
                    viewModel.setEvent(MainActivityContract.Event.OnCloseDialogClicked)
                    editText.value = ""
                    viewModel.uiState.value.editText = ""
                }
            ) {
                Text("OK")
            }
        }
    }
}