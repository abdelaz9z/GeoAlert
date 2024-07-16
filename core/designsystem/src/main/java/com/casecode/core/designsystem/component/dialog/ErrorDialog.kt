package com.casecode.core.designsystem.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.casecode.core.designsystem.R
import com.casecode.core.designsystem.icon.GeoAlertIcons
import com.casecode.core.designsystem.theme.errorLight

@Composable
fun ErrorDialog(
    error: String,
    onDismissClick: () -> Unit,
    dismissOnBackPress: Boolean = false,
    dismissOnClickOutside: Boolean = false
) {
    if (error.isNotEmpty()) {
        Dialog(
            onDismissRequest = { },
            DialogProperties(
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnClickOutside
            )
        ) {
            DialogErrorContent(error, onDismissClick = onDismissClick)
        }
    }
}

@Composable
fun DialogErrorContent(error: String, onDismissClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, end = 50.dp, top = 30.dp, bottom = 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                imageVector = GeoAlertIcons.Error,
                contentDescription = null,
                colorFilter = ColorFilter.tint(errorLight),
            )

            Text(
                text = error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )

            // button
            CloseButton(onDismissClick = onDismissClick)
        }
    }
}

@Composable
fun CloseButton(onDismissClick: () -> Unit) {
    Button(
        onClick = onDismissClick,
        modifier = Modifier.padding(top = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.close),
            color = Color.White,
        )
    }
}

@Preview
@Composable
fun ErrorDialogPreview() {
    ErrorDialog(
        error = "Error message",
        onDismissClick = { }
    )
}
