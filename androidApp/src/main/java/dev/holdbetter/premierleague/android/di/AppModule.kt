package dev.holdbetter.premierleague.android.di

import dev.holdbetter.core_di_api.folder.HasSharedDependencies
import dev.holdbetter.core_di_impl.MutableModuleDependencies
import dev.holdbetter.core_network.di.NetworkModule
import dev.holdbetter.shared.core_database.di.DatabaseModule
import dev.shustoff.dikt.UseModules

@UseModules(NetworkModule::class)
internal class AppModule private constructor(
    val networkModule: NetworkModule,
    val databaseModule: DatabaseModule
) {

    object Factory {
        fun create(
            networkModule: NetworkModule,
            databaseModule: DatabaseModule,
            dependencies: MutableModuleDependencies
        ): AppModule {

            registerDependencies(
                dependencies,
                networkModule,
                databaseModule
            )

            return AppModule(
                networkModule = networkModule,
                databaseModule = databaseModule
            )
        }

        private fun transformToPair(sharedDependency: HasSharedDependencies) =
            sharedDependency::class to sharedDependency

        private fun registerDependencies(
            dependencies: MutableModuleDependencies,
            vararg sharedModules: HasSharedDependencies
        ): Unit = dependencies.putAll(sharedModules.map(::transformToPair))
    }
}