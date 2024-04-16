package com.phoebus.pix.demo.view

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.runtime.collectAsState
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
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.ui.components.CheckboxPrint
import com.phoebus.pix.demo.viewmodels.ConsultCobRequestViewModel
import com.phoebus.pix.sdk.PixClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TxIdView(pixClient: PixClient, navigateUp: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.tx_id),
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        content = {
            val viewModel: ConsultCobRequestViewModel = viewModel()
            TxIdFind(modifier = Modifier.padding(it), pixClient = pixClient, viewModel = viewModel)
        }
    )
}

@Composable
fun TxIdFind(
    modifier: Modifier,
    pixClient: PixClient,
    viewModel: ConsultCobRequestViewModel = viewModel()
) {

    val focusManager = LocalFocusManager.current
    var txId by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    val printCustomerReceipt = viewModel.printCustomerReceipt.collectAsState()
    val printMerchantReceipt = viewModel.printMerchantReceipt.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = txId,
            onValueChange = {
                txId = it
            },
            label = { Text("Tx-ID") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            onClick = {
                viewModel.sendRequest(pixClient, txId.text, context)
                txId = TextFieldValue("")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Consultar")
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


