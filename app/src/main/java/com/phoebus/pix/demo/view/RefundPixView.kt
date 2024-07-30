package com.phoebus.pix.demo.view

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.ui.theme.components.Destinations
import com.phoebus.pix.demo.viewmodels.RefundPixViewModel
import com.phoebus.pix.sdk.PixClient

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
                        text = stringResource(R.string.pix_refund),
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
                navController = navController)
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

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        try {
            viewModel.request(pixClient, context)

            //Voltar para a tela inicial
            navController.navigate(Destinations.HOME.name) {
                popUpTo(Destinations.HOME.name) { inclusive = true }
            }

        } catch (e: Exception) {

            Log.e("NewRefundPixView", "Error calling refund", e)
        }
    }




}



