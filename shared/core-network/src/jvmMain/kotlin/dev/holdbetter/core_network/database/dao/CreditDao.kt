package dev.holdbetter.core_network.database.dao

import dev.holdbetter.core_network.model.Credit

internal interface CreditDao {
    fun getCredit(): Credit
}