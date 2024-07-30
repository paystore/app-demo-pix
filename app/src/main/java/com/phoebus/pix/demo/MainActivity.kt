package com.phoebus.pix.demo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.phoebus.pix.demo.services.SyncDataService
import com.phoebus.pix.demo.ui.theme.components.Destinations
import com.phoebus.pix.demo.utils.ConstantsUtils
import com.phoebus.pix.demo.view.CheckAppPixView
import com.phoebus.pix.demo.view.ClientIDView
import com.phoebus.pix.demo.view.CobCreateView
import com.phoebus.pix.demo.view.ConsultLoading
import com.phoebus.pix.demo.view.FilterPixView
import com.phoebus.pix.demo.view.FindPixView
import com.phoebus.pix.demo.view.HomeView
import com.phoebus.pix.demo.view.ListPixView
import com.phoebus.pix.demo.view.RefundByTxIdView
import com.phoebus.pix.demo.view.RefundPixView
import com.phoebus.pix.demo.view.TxIdView
import com.phoebus.pix.sdk.PixClient
import com.phoebus.pix.ui.theme.AppsmartdemopixTheme


class MainActivity : ComponentActivity() {

    private lateinit var pixClient: PixClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pixClient = PixClient(applicationContext)

        setContent {
            AppsmartdemopixTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Destinations.HOME.name) {
                    composable(
                        route = Destinations.HOME.name
                    ) {
                        HomeView(pixClient, navController)
                    }

                    composable(
                        route = Destinations.COBCREATE.name
                    ) {
                        CobCreateView(pixClient) { navController.navigateUp() }
                    }
                    composable(
                        route = Destinations.CHEACKAPPPIX.name
                    ) {
                        CheckAppPixView(pixClient) { navController.navigateUp() }
                    }
                    composable(
                        route = Destinations.FILTERPIX.name
                    ) {
                        FilterPixView(navController) {
                            navController.navigateUp()
                        }
                    }
                    composable(
                        route = "${Destinations.LISTPIX.name}/{startDate}/{endDate}",
                        arguments = listOf(
                            navArgument("startDate") { type = NavType.StringType },
                            navArgument("endDate") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val startDate = backStackEntry.arguments?.getString("startDate")
                        val endDate = backStackEntry.arguments?.getString("endDate")
                        ListPixView(pixClient, startDate, endDate) { navController.navigateUp() }
                    }
                    composable(
                        route = Destinations.CONSULTPIX.name
                    ) {
                        FindPixView(navController) {
                            navController.navigateUp()
                        }
                    }
                    composable(
                        route = Destinations.CLIENTID.name
                    ) {
                        ClientIDView(pixClient) {
                            navController.navigateUp()
                        }
                    }
                    composable(
                        route = Destinations.TXID.name
                    ) {
                        TxIdView(pixClient) {
                            navController.navigateUp()
                        }
                    }
                    composable(
                        route = Destinations.PIXDREFUND.name
                    ) {
                        RefundByTxIdView(pixClient) {
                            navController.navigateUp()
                        }
                    }

                    composable(
                        route = Destinations.CONSULTLOADING.name
                     ) {
                        ConsultLoading(
                            navController,
                            pixClient = pixClient
                        )
                    }

                    composable(
                        route = Destinations.SYNCDATAPIX.name
                    )
                    {
                        // Execute o serviço sem sair da tela atual
                        LaunchedEffect(Unit) {
                            try {
                                val syncDataService = SyncDataService(this@MainActivity)
                                syncDataService.execute(pixClient)
                                // Voltar para a tela HOME após a execução do serviço
                                navController.navigate(Destinations.HOME.name) {
                                    popUpTo(Destinations.HOME.name) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                println(e.message.toString())
                            }
                        }
                    }
                    composable(
                        route = Destinations.REFUND.name
                    ){
                        RefundPixView(pixClient, navController) {
                            navController.navigateUp()

                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        pixClient.bind(object : PixClient.BindCallback {
            override fun onServiceDisconnected() {
                Log.d(ConstantsUtils().TAG, "Servico desconectado")
                Toast.makeText(applicationContext, "Servico desconectado", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onServiceConnected() {
                Log.d(ConstantsUtils().TAG, "Servico conectado")
                Toast.makeText(applicationContext, "Servico conectado", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
