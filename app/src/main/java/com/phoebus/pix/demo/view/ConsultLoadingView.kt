package com.phoebus.pix.demo.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.phoebus.pix.demo.ui.theme.components.Destinations
import com.phoebus.pix.demo.viewmodels.ConsultViewModel
import com.phoebus.pix.sdk.PixClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultLoading(
    navController: NavController,
    pixClient: PixClient
) {
    val context = LocalContext.current
    val viewModel: ConsultViewModel = viewModel()

    LaunchedEffect(key1 = Unit) {

        try{
            viewModel.consultTransactions(pixClient, context)

            navController.navigate(Destinations.HOME.name) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }

        }catch (e: Exception){
            Log.e("Consult", "Error ao chamar o consult", e)
        }

    }

    val isLoading = viewModel.isLoading.collectAsState()
    val canBack = viewModel.canBack.collectAsState()
    val canBackErr = viewModel.canBackErr.collectAsState()



    LaunchedEffect(key1 = canBack.value, key2 = canBackErr.value) {
        if (canBack.value) {
            navController.navigate(Destinations.HOME.name) {
            }
        } else if (canBackErr.value) {
            navController.navigate(Destinations.HOME.name) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    if (isLoading.value) {
        Scaffold(
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                }
            }
        )
    }
}


