package com.phoebus.pix.demo.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.phastpay.sdk.client.PixClient
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.ui.components.CheckboxPrint
import com.phoebus.pix.demo.ui.components.LoadingIndicator
import com.phoebus.pix.demo.ui.components.PhButton
import com.phoebus.pix.demo.ui.components.dialogs.PhDialog
import com.phoebus.pix.demo.viewmodels.ConsultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultCobView(
    navController: NavController,
    pixClient: PixClient
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
                        text = stringResource(R.string.pix_find),
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            CobConsult(
                Modifier.padding(it),
                pixClient,
                onModalClick = { navController.popBackStack() })
        }
    )

}

@Composable()
fun CobConsult(
    modifier: Modifier = Modifier,
    pixClient: PixClient,
    viewModel: ConsultViewModel = viewModel(),
    onModalClick: () -> Unit = {}
) {

    val printCustomerReceipt = viewModel.printCustomerReceipt.collectAsState()
    val printMerchantReceipt = viewModel.printMerchantReceipt.collectAsState()
    val previewCustomerReceipt = viewModel.previewCustomerReceipt.collectAsState()
    val previewMerchantReceipt = viewModel.previewMerchantReceipt.collectAsState()
    val dialogMessage = viewModel.dialogMessage.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val context = LocalContext.current;
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

        if (isLoading) {
            LoadingIndicator(text = stringResource(R.string.loading_getting))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {

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

                PhButton(
                    onClick = { viewModel.consultTransactions(pixClient) },
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.get_pix)
                )
            }

        }

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


