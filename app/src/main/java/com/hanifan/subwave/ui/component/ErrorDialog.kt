package com.hanifan.subwave.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.hanifan.subwave.utils.extension.ensureError

@Composable
fun ErrorDialog(
    onDismissRequest: () -> Unit,
    errorMessage: String?
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Error") },
        text = { Text(errorMessage.ensureError()) },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Ok")
            }
        }
    )
}