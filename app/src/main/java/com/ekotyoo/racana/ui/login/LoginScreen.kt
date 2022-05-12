package com.ekotyoo.racana.ui.login

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ekotyoo.racana.R
import com.ekotyoo.racana.core.composables.REditText
import com.ekotyoo.racana.core.composables.RFilledButton
import com.ekotyoo.racana.core.theme.RacanaTheme
import com.ekotyoo.racana.ui.destinations.HomeScreenDestination
import com.ekotyoo.racana.ui.destinations.RegisterScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination()
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect { event ->
            when (event) {
                LoginScreenEvent.LoginSuccess -> {
                    navigator.navigate(HomeScreenDestination)
                }
                LoginScreenEvent.NavigateToRegisterScreen -> {
                    navigator.navigate(RegisterScreenDestination)
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        LoginContent(
            emailValue = state.emailTextFieldValue,
            passwordValue = state.passwordTextFieldValue,
            onEmailEmailTextFieldChange = viewModel::onEmailTextFieldValueChange,
            onPasswordTextFieldChange = viewModel::onPasswordTextFieldValueChange,
            onLoginButtonClicked = viewModel::onLoginButtonClicked,
            onRegisterTextClicked = viewModel::onRegisterTextClicked,
        )
    }
}

@Composable
fun LoginContent(
    emailValue: String,
    passwordValue: String,
    onEmailEmailTextFieldChange: (String) -> Unit,
    onPasswordTextFieldChange: (String) -> Unit,
    onLoginButtonClicked: () -> Unit,
    onRegisterTextClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Text(
            text = stringResource(id = R.string.welcome),
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.size(size = 32.dp))
        Image(
            modifier = Modifier
                .size(215.dp)
                .align(Alignment.CenterHorizontally),
            alignment = Alignment.Center,
            painter = painterResource(id = R.drawable.login_illustration),
            contentDescription = ""
        )
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.size(size = 32.dp))
        REditText(
            modifier = Modifier.fillMaxWidth(),
            value = emailValue,
            placeholderString = stringResource(id = R.string.email),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "",
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = onEmailEmailTextFieldChange
        )
        Spacer(modifier = Modifier.size(size = 16.dp))
        REditText(
            modifier = Modifier.fillMaxWidth(),
            value = passwordValue,
            placeholderString = stringResource(id = R.string.password),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "",
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = onPasswordTextFieldChange
        )
        Spacer(modifier = Modifier.size(size = 32.dp))
        RFilledButton(
            onClick = onLoginButtonClicked,
            placeholderString = stringResource(id = R.string.login)
        )
        Spacer(modifier = Modifier.size(size = 16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.forgot_password),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.dont_have_an_account))
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                modifier = Modifier.clickable { onRegisterTextClicked() },
                text = stringResource(id = R.string.register),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO, name = "Light Mode Preview")
@Composable
fun LightModePreview() {
    RacanaTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            LoginContent(
                emailValue = "",
                passwordValue = "",
                onLoginButtonClicked = {},
                onPasswordTextFieldChange = {},
                onEmailEmailTextFieldChange = {},
                onRegisterTextClicked = {}
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Dark Mode Preview")
@Composable
fun DarkModePreview() {
    RacanaTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            LoginContent(
                emailValue = "",
                passwordValue = "",
                onLoginButtonClicked = {},
                onPasswordTextFieldChange = {},
                onEmailEmailTextFieldChange = {},
                onRegisterTextClicked = {}
            )
        }
    }
}