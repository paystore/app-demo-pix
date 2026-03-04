@file:Suppress("UNUSED_EXPRESSION")

package com.phoebus.pix.demo.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.ui.components.CheckboxPrint
import com.phoebus.pix.demo.ui.components.PhButton
import com.phoebus.pix.demo.ui.components.dialogs.PhDialog
import com.phoebus.pix.demo.utils.CurrencyAmountInputVisualTransformation
import com.phoebus.pix.demo.viewmodels.CobCreateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CobCreateView(
    pixClient: PixClient,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.cob_gen),
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
            val viewModel: CobCreateViewModel = viewModel()

            CobCreate(Modifier.padding(it), pixClient, viewModel, onModalClick = navigateUp )
        }
    )
}

@Composable
fun CobCreate(
    modifier: Modifier = Modifier,
    pixClient: PixClient,
    viewModel: CobCreateViewModel = viewModel(),
    onModalClick: () -> Unit = {}
) {
    val flagValue = viewModel.flagValue.collectAsState()
    val printCustomerReceipt = viewModel.printCustomerReceipt.collectAsState()
    val printMerchantReceipt = viewModel.printMerchantReceipt.collectAsState()
    val previewCustomerReceipt = viewModel.previewCustomerReceipt.collectAsState()
    val previewMerchantReceipt = viewModel.previewMerchantReceipt.collectAsState()
    val dialogMessage = viewModel.dialogMessage.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value
    val cobValue = viewModel.cobValue.collectAsState()
    val pixClientId by viewModel.pixClientId.collectAsState()
    val context = LocalContext.current

    var lastShownMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty() && errorMessage != lastShownMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        lastShownMessage = errorMessage
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.send_value))
                Switch(
                    checked = flagValue.value,
                    onCheckedChange = {
                        viewModel.changeFlag()
                    }
                )
                if (!flagValue.value) {
                    viewModel.upgradeCobValue("")
                }
            }
            InputValue(checked = flagValue, value = cobValue, viewModel = viewModel)
            Text(
                modifier = Modifier.padding(0.dp, 5.dp),
                text = stringResource(id = R.string.client_id)
            )
            Text(
                text = pixClientId,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            PhButton(
                onClick = { viewModel.sendRequest(pixClient, context) },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.cob_gen),
                enabled = isButtonEnabled(flagValue.value, cobValue.value)
            )

            CheckboxPrint(
                printCustomerReceiptChecked = printCustomerReceipt.value,
                printMerchantReceiptChecked = printMerchantReceipt.value,
                previewCustomerReceiptChecked = previewCustomerReceipt.value,
                previewMerchantReceiptChecked = previewMerchantReceipt.value,
                onPreviewCustomerReceiptChange = {
                    viewModel.changePreviewCustomerReceipt()
                },
                onPreviewMerchantReceiptChange = {
                    viewModel.changePreviewMerchantReceipt()
                },
                onPrintCustomerReceiptChange = {
                    viewModel.changePrintCustomerReceipt()
                },
                onPrintMerchantReceiptChange = {
                    viewModel.changePrintMerchantReceipt()
                },
            )

            dialogMessage?.let { message ->
                PhDialog(
                    onDismissRequest = {},
                    onConfirm = {
                        onModalClick()
                    },
                    message = message
                )
            }
        }
    }
}

fun isButtonEnabled(flagValue: Boolean, value: String): Boolean {
    return if (!flagValue) {
        true
    };
    else value.isNotEmpty();
}

@Composable
fun InputValue(
    checked: State<Boolean>,
    value: State<String>,
    viewModel: CobCreateViewModel
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp)
            .focusRequester(focusRequester),
        prefix = { Text(text = "R$") },
        value = value.value,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            autoCorrect = false,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            },
        ),
        visualTransformation = CurrencyAmountInputVisualTransformation(
            fixedCursorAtTheEnd = true
        ),
        onValueChange = {
            if (it.length <= 9 && viewModel.validateInput(it)) {
                if (!it.startsWith("0")) {
                    viewModel.upgradeCobValue(it)
                }
            } else {
                value.value
            }
        },
        singleLine = true,
        enabled = checked.value,
        label = { Text("Valor:") }
    )
}