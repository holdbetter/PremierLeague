package dev.holdbetter.core_network.di

import dev.holdbetter.core_di_api.folder.Dikt
import dev.holdbetter.core_network.database.DatabaseFactory
import dev.holdbetter.core_network.database.dao.CreditDao
import dev.holdbetter.core_network.database.dao.CreditDaoImpl
import dev.holdbetter.core_network.util.Mode
import dev.holdbetter.core_network.util.isDevelopment
import dev.shustoff.dikt.ModuleScopes
import dev.shustoff.dikt.resolve

@ModuleScopes(Dikt::class)
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

    private fun getCreditDao(): CreditDaoImpl = resolve()
}