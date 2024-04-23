package com.phoebus.pix.demo.view

import android.annotation.SuppressLint
import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phoebus.pix.demo.R
import com.phoebus.pix.demo.utils.DateUtils
import com.phoebus.pix.demo.utils.ResponseUtils
import com.phoebus.pix.demo.viewmodels.ListPixViewModel
import com.phoebus.pix.sdk.PixClient
import com.phoebus.pix.ui.theme.YellowLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPixView(
    pixClint: PixClient,
    startDate: String?,
    endDate: String?,
    navigateUp: () -> Unit,
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
            val viewModel: ListPixViewModel = viewModel()
            ListPixScreen(modifier = Modifier.padding(it), pixClint, startDate, endDate, viewModel)
        }
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ListPixScreen(
    modifier: Modifier = Modifier,
    pixClient: PixClient,
    startDate: String?,
    endDate: String?,
    viewModel: ListPixViewModel = viewModel(),
) {
    val startDateTime = DateUtils().stringDateTimeToDate(startDate.toString())
    val endDateTime = DateUtils().stringDateTimeToDate(endDate.toString())
    viewModel.upgradeListPix(pixClient, startDateTime!!, endDateTime!!)
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val listPix = viewModel.listPix.collectAsState()
            if (listPix.value.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(listPix.value) { item ->
                        Pix(
                            statusPix = item.status,
                            pixClientId = item.clientId,
                            valuePix = item.cobValue,
                            txId = item.txId
                        )
                    }
                }
            } else {
                Text(text = stringResource(R.string.empty_list))
            }
        }
    }
}

@Composable
fun Pix(
    statusPix: String,
    pixClientId: String?,
    valuePix: String,
    txId: String
) {
    Surface(
        modifier = Modifier
            .padding(5.dp),
        color = YellowLight
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            ) {
                Text(
                    text = ResponseUtils().chargeStatus(context = LocalContext.current, status = statusPix),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "R$ ${valuePix.replace(".", ",")}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            TextWithCopyIcon("TxID", txId)
            if (pixClientId != null) TextWithCopyIcon("ClientID", pixClientId)
        }
    }
}

@Composable
fun TextWithCopyIcon(
    type: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val clipboardManager = getSystemService(context, ClipboardManager::class.java)
    Row(
        modifier = modifier.clickable {
            clipboardManager?.setPrimaryClip(newPlainText("$type copiado", text))
            Toast.makeText(context, "$type copiado", Toast.LENGTH_SHORT).show()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$type: $text",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(top = 10.dp,bottom = 10.dp)
        )
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "Copy",
            tint = Color.Gray,
            modifier = Modifier
                .padding(start = 4.dp)
                .size(12.dp)
        )


    }
}