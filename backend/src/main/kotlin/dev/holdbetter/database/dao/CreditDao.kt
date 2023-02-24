package dev.holdbetter.database.dao

import dev.holdbetter.innerApi.model.Credit

interface CreditDao {
    fun getCredit(): Credit
}