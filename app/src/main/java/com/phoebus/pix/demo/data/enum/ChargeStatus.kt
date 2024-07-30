package com.phoebus.pix.demo.data.enum

import com.google.gson.annotations.SerializedName

enum class ChargeStatus(private val value: String) {
    @SerializedName("ATIVA")
    ACTIVE("ATIVA"),
    @SerializedName("CONCLUIDA")
    CONCLUDED("CONCLUIDA"),
    @SerializedName("DEVOLVIDO")
    REFUNDED("DEVOLVIDO"),
    @SerializedName("EM_PROCESSAMENTO")
    REFUND_PROCESSING("EM_PROCESSAMENTO"),
    @SerializedName("NAO_REALIZADO")
    REFUND_NOT_DONE("NAO_REALIZADO"),
    @SerializedName("REMOVIDA_PELO_USUARIO_RECEBEDOR")
    REMOVED_BY_USER("REMOVIDA_PELO_USUARIO_RECEBEDOR"),
    @SerializedName("REMOVIDA_PELO_PSP")
    REMOVED_BY_PSP("REMOVIDA_PELO_PSP"),
    @SerializedName("EXPIRADA")
    EXPIRED("EXPIRADA"),
    UNKNOWN("UNKNOWN");

    fun getValue(): String {
        return value
    }
}