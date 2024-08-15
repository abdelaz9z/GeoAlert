package com.casecode.feature.account

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.casecode.core.common.result.Result
import com.casecode.core.common.utils.moveToAppLauncherActivity
import com.casecode.core.data.model.User
import com.casecode.core.data.model.toUser
import com.casecode.core.designsystem.component.dialog.ConfirmationDialog
import com.casecode.core.designsystem.component.dialog.ErrorDialog
import com.casecode.core.designsystem.component.dialog.LoadingDialog
import com.casecode.core.designsystem.icon.GeoAlertIcons
import com.casecode.core.designsystem.R as designSystemR

@Composable
internal fun AccountRoute(viewModel: AccountViewModel = hiltViewModel()) {
    val activity: Activity = LocalContext.current as? Activity
        ?: throw IllegalStateException("Context is not an Activity")

    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val signOutResult by viewModel.signOutResult.collectAsState()
    val deleteUserResult by viewModel.deleteUserResult.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val isLoading = signOutResult is Result.Loading || deleteUserResult is Result.Loading

    // Get the unknown error message once in a Composable context
    val unknownErrorMessage = stringResource(id = designSystemR.string.unknown_error)

    AccountScreen(
        user = currentUser?.toUser() ?: User(),
        onDeleteAccount = {
            showConfirmDialog = true
        },
        onSignOut = {
            viewModel.signOut()
        }
    )

    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismissRequest = { showErrorDialog = false },
            onConfirm = { showErrorDialog = false }
        )
    }

    if (showConfirmDialog) {
        ConfirmationDialog(
            name = stringResource(id = R.string.account),
            onConfirm = {
                viewModel.deleteUser(activity)
                showConfirmDialog = false
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }

    LoadingDialog(isLoading = isLoading)

    // Handle delete user result using LaunchedEffect
    LaunchedEffect(deleteUserResult) {
        when (deleteUserResult) {
            is Result.Success -> {
                showConfirmDialog = false
                moveToAppLauncherActivity(context)
                // Reset the delete user result
                viewModel.resetDeleteUserResult()
            }

            is Result.Error -> {
                errorMessage =
                    (deleteUserResult as Result.Error).exception.message ?: unknownErrorMessage
                showErrorDialog = true
                viewModel.resetDeleteUserResult() // Reset the delete user result
            }

            else -> {}
        }
    }

    // Handle sign out result using LaunchedEffect
    LaunchedEffect(signOutResult) {
        when (signOutResult) {
            is Result.Success -> {
                showConfirmDialog = false
                moveToAppLauncherActivity(context)
                viewModel.resetSignOutResult() // Reset the sign out result
            }

            is Result.Error -> {
                errorMessage = (signOutResult as Result.Error).exception.message
                    ?: "Unknown error"
                showErrorDialog = true
                viewModel.resetSignOutResult() // Reset the sign out result
            }

            else -> {}
        }
    }
}


@Composable
internal fun AccountScreen(
    user: User,
    onDeleteAccount: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Profile(
            modifier = Modifier.weight(1f),
            user = user
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Delete Account Button
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onDeleteAccount,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = GeoAlertIcons.Delete,
                    contentDescription = stringResource(R.string.delete_account)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.delete_account))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sign Out Button
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSignOut,
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(
                imageVector = GeoAlertIcons.ExitToApp,
                contentDescription = stringResource(R.string.sign_out)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.sign_out))
        }
    }
}

@Composable
fun Profile(modifier: Modifier = Modifier, user: User) {
    Column(
        modifier = modifier.padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        AsyncImage(
            model = user.photo,
            contentDescription = stringResource(R.string.profile_picture),
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = user.name,
            style = MaterialTheme.typography.titleLarge,
        )

        // User Email
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview(showBackground = true, apiLevel = 30)
@Composable
fun AccountScreenPreview() {
    val user = User(name = "Abdelaziz Daoud", email = "abdelaz9z@icloud.com")

    AccountScreen(
        user = user,
        onDeleteAccount = {},
        onSignOut = {}
    )
}
