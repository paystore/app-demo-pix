@file:Suppress("UNUSED_EXPRESSION")

package com.phoebus.pix.demo.view

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
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.ui.components.CheckboxPrint
import com.phoebus.pix.demo.utils.CurrencyAmountInputVisualTransformation
import com.phoebus.pix.demo.viewmodels.CobCreateViewModel
import com.phoebus.pix.sdk.PixClient

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
                        text = stringResource(R.string.app_name),
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
            CobCreate(Modifier.padding(it), pixClient, viewModel)
        }
    )
}

@Composable
fun CobCreate(
    modifier: Modifier = Modifier,
    pixClient: PixClient,
    viewModel: CobCreateViewModel = viewModel()
) {
    val flagValue = viewModel.flagValue.collectAsState()
    val printCustomerReceipt = viewModel.printCustomerReceipt.collectAsState()
    val printMerchantReceipt = viewModel.printMerchantReceipt.collectAsState()
    val cobValue = viewModel.cobValue.collectAsState()
    val pixClientId by viewModel.pixClientId.collectAsState()
    val context = LocalContext.current

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
                fontWeight = FontWeight.SemiBold
            )
            ElevatedButton(
                modifier = Modifier.padding(vertical = 10.dp),
                onClick = {
                    viewModel.sendRequest(context, pixClient, viewModel)
                }
            ) {
                Text(stringResource(R.string.cob_gen))
            }

            CheckboxPrint(
                printCustomerReceiptChecked = printCustomerReceipt.value,
                printMerchantReceiptChecked = printMerchantReceipt.value,
                onPrintCustomerReceiptChange = {
                    viewModel.changePrintCustomerReceipt()
                }
            ) {
                viewModel.changePrintMerchantReceipt()
            }

        }
    }
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