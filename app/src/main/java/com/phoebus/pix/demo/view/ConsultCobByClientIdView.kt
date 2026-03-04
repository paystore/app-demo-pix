package com.phoebus.pix.demo.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.ui.components.PhButton
import com.phoebus.pix.demo.utils.ResponseUtils
import com.phoebus.pix.demo.viewmodels.ConsultPixByClientIdViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultCobByClientIdView(pixClient: PixClient, navigateUp: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.client_id),
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        content = {
            val viewModel: ConsultPixByClientIdViewModel = viewModel()
            ClientIdFind(
                modifier = Modifier.padding(it),
                pixClient = pixClient,
                viewModel = viewModel
            )
        }
    )
}

@Composable
fun ClientIdFind(
    modifier: Modifier = Modifier,
    pixClient: PixClient,
    viewModel: ConsultPixByClientIdViewModel = viewModel()
) {

    val focusManager = LocalFocusManager.current
    var clientId by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    val successMessage = viewModel.successMessage;
    val errorMessage = viewModel.erroMessage;

    var lastShownMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty() && errorMessage != lastShownMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        lastShownMessage = errorMessage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = clientId,
            onValueChange = {
                clientId = it
            },
            label = { Text("Client ID") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        PhButton(
            text = stringResource(R.string.get_pix),
            enabled = clientId.text.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            viewModel.sendRequest(pixClient, clientId.text)
            clientId = TextFieldValue("")
        }

        successMessage?.let { message ->
            val responseUtils = ResponseUtils()
            Toast.makeText(
                context,
                responseUtils.messageConsultPix(context, message),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
