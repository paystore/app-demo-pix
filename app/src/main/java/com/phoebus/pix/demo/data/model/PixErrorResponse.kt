package com.phoebus.pix.demo.data.model

import com.google.gson.annotations.SerializedName

class PixErrorResponse (

    @SerializedName("error_message")
    val errorMessage: String

)