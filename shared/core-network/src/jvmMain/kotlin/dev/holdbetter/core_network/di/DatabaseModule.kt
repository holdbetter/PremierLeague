package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.database.DatabaseFactory
import dev.holdbetter.core_network.database.dao.CreditDao
import dev.holdbetter.core_network.database.dao.CreditDaoImpl
import dev.holdbetter.core_network.util.Mode
import dev.holdbetter.core_network.util.isDevelopment
import dev.shustoff.dikt.CreateSingle

class DatabaseModule {

    private val mode by lazy {
        Mode(isDevelopment)
    }

    private val creditDao: CreditDao by lazy(::getCreditDao)

    internal val credit
        get() = creditDao.getCredit()

    val database by lazy {
        DatabaseFactory.init(mode)
    }

    @CreateSingle
    private fun getCreditDao(): CreditDaoImpl
}