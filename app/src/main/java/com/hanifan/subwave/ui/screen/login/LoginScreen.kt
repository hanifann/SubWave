package com.hanifan.subwave.ui.screen.login

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hanifan.subwave.navigation.Routes
import com.hanifan.subwave.ui.component.CustomTextField
import com.hanifan.subwave.ui.component.ErrorDialog
import com.hanifan.subwave.ui.component.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigate: (route: Routes) -> Unit
) {
    var isHidden by remember { mutableStateOf(true) }

    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    val isExpanded by viewModel.isExpanded.collectAsStateWithLifecycle()
    val httpScheme by viewModel.httpScheme.collectAsStateWithLifecycle()
    val options = viewModel.options

    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
    )

    Scaffold { contentPadding ->
        if (authState.isLoading) {
            LoadingDialog {
                viewModel.resetAuthState()
            }
        }

        if (authState.errorMessage != null) {
            ErrorDialog(
                errorMessage = authState.errorMessage,
                onDismissRequest = { viewModel.resetAuthState() }
            )
        }

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            Row (
                modifier = Modifier
                    .height(IntrinsicSize.Min)
            ) {
                Card (
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier
                        .weight(.2f)
                        .fillMaxHeight()
                        .padding(end = 4.dp)
                        .clickable(
                            onClick = { viewModel.setExpanded(!isExpanded) }
                        )

                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        Text(
                            httpScheme,
                        )
                        Icon(
                            Icons.Rounded.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier
                                .rotate(rotation)
                        )
                    }
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = {
                            viewModel.setExpanded(false)
                        },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        offset = DpOffset(
                            x = 0.dp,
                            y = 4.dp
                        )
                    ) {
                        options.forEach {protocol ->
                            DropdownMenuItem(
                                text = {Text(protocol)},
                                onClick = {
                                    viewModel.updateSelectedItem(protocol)
                                },
                            )
                        }
                    }
                }
                CustomTextField(
                    value = loginState.url,
                    onValueChange = { viewModel.updateUrl(it) },
                    placeholder = "your server url",
                    modifier = Modifier
                        .weight(.8f)
                )
            }
            CustomTextField(
                value = loginState.username,
                onValueChange = { viewModel.updateUsername(it) },
                placeholder = "username",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
            CustomTextField(
                value = loginState.password,
                onValueChange = { viewModel.updatePassword(it) },
                placeholder = "password",
                isHidden = isHidden,
                keyboardType = KeyboardType.Password,
                modifier = Modifier
                    .fillMaxWidth()
            )
            ElevatedButton (
                enabled = loginState.password.isNotEmpty() &&
                        loginState.username.isNotEmpty() &&
                        loginState.url.isNotEmpty(),
                onClick = {
                    viewModel.login(onNavigate)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxWidth(.65f)
                    .padding(top = 24.dp)
            ) {
                Text("Login")
            }
        }
    }
}