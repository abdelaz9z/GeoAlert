package com.casecode.core.designsystem.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.casecode.core.designsystem.R

@Composable
fun ErrorDialog(
    title: String = stringResource(R.string.error),
    message: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    confirmButtonText: String = stringResource(R.string.ok)
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmButtonText)
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}

@Preview
@Composable
fun ErrorDialogPreview() {
    var showDialog by remember { mutableStateOf(false) }

    ErrorDialog(
        title = "Error",
        message = "Something went wrong. Please try again.",
        onDismissRequest = { showDialog = false },
        onConfirm = { showDialog = false },
        confirmButtonText = "OK"
    )
}