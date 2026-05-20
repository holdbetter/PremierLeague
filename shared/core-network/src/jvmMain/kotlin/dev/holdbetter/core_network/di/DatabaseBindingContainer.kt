package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.database.DatabaseFactory
import dev.holdbetter.core_network.database.dao.CreditDao
import dev.holdbetter.core_network.database.dao.CreditDaoImpl
import dev.holdbetter.core_network.model.Credit
import dev.holdbetter.core_network.util.Mode
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.Provides
import org.jetbrains.exposed.sql.Database

@BindingContainer([DaoProvider::class])
class DatabaseBindingContainer(isDevelopment: Boolean) {

    private val mode = Mode(isDevelopment)

    @Provides
    fun provideMode(): Mode {
        return mode
    }

    @Provides
    fun provideDatabase(): Database {
        return DatabaseFactory.init(mode)
    }

    @Provides
    internal fun provideCredit(creditDao: CreditDao): Credit {
        return creditDao.getCredit()
    }
}

@BindingContainer
abstract class DaoProvider {
    @Binds internal abstract val CreditDaoImpl.bind: CreditDao
}