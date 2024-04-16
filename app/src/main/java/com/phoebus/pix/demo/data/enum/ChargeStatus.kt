package com.phoebus.pix.demo.data.enum

enum class ChargeStatus(private val value: String) {
    ACTIVE("ATIVA"),
    CONCLUDED("CONCLUIDA"),
    REFUNDED("DEVOLVIDO"),
    REFUND_PROCESSING("EM_PROCESSAMENTO"),
    REFUND_NOT_DONE("NAO_REALIZADO"),
    REMOVED_BY_USER("REMOVIDA_PELO_USUARIO_RECEBEDOR"),
    REMOVED_BY_PSP("REMOVIDA_PELO_PSP"),
    EXPIRED("EXPIRADA");

    fun getValue(): String {
        return value
    }
}