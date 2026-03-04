package com.phoebus.pix.demo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoebus.pix.demo.R

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.loading_message), // Texto opcional
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.primary
        )
        if (text.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = text,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}