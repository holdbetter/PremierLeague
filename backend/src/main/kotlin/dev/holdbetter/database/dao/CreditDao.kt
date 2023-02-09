package dev.holdbetter.database.dao

import dev.holdbetter.model.Credit

interface CreditDao {
    fun getCredit(): Credit
}