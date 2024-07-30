package com.phoebus.pix.demo.services

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.phoebus.pix.demo.R
import com.phoebus.pix.sdk.PixClient
class SyncDataService(val context: Context) {

fun execute(pixClient: PixClient) {
    if (pixClient.isBound()) {
        val callback = object : PixClient.SyncDataCallback {
            override fun onError(response: String?) {
                println("SyncData error: $response")
                showAlertDialogLocal(context, "SyncData error: $response")
            }

            override fun onSuccess(response: String?) {
                println("SyncData success: $response")
                showAlertDialogLocal(context, "SyncData success: $response")
            }
        }

        pixClient.synchronize(callback)

    }
}
private fun showAlertDialogLocal(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.sync_data)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}

