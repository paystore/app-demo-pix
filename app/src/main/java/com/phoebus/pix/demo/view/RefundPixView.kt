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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.phoebus.pix.demo.ui.components.PhButton
import com.phoebus.pix.demo.ui.components.dialogs.PhDialog
import com.phoebus.pix.demo.ui.theme.components.Destinations
import com.phoebus.pix.demo.viewmodels.RefundPixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefundPixView(pixClient: PixClient, navController: NavController, navigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.refund),
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
            NewRefundPix(
                modifier = Modifier.padding(it),
                pixClient = pixClient,
                navController = navController
            )
        }
    )
}

@Composable
fun NewRefundPix(
    modifier: Modifier,
    pixClient: PixClient,
    navController: NavController,
    viewModel: RefundPixViewModel = viewModel()
) {

    val printCustomerReceipt = viewModel.printCustomerReceipt.collectAsState()
    val printMerchantReceipt = viewModel.printMerchantReceipt.collectAsState()
    val previewCustomerReceipt = viewModel.previewCustomerReceipt.collectAsState()
    val previewMerchantReceipt = viewModel.previewMerchantReceipt.collectAsState()
    val isSuccess = viewModel.isSucesss.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
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
            text = stringResource(R.string.refund_pix),
            modifier = Modifier.fillMaxWidth(),
        ) {
            viewModel.request(pixClient)
        }

        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        if(isSuccess){
            PhDialog(
                onDismissRequest = {},
                onConfirm = {
                    navController.navigate(Destinations.HOME.name) {
                        popUpTo(Destinations.HOME.name) { inclusive = true }
                    }
                },
                message = "Devolução realizada com sucesso"
            )
        }
    }
}



