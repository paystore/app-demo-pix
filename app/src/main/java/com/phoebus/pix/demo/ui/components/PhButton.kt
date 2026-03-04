package com.phoebus.pix.demo.ui.components

import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PhButton(text: String, enabled: Boolean = true, modifier: Modifier = Modifier, onClick: () -> Unit = {}){
    ElevatedButton(
        onClick = {
            onClick()
        },
        modifier = modifier
    ) {
        Text(text)
    }
}