package com.foundation.feature.example

import com.foundation.core.navigation.FeatureEntry
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface ExampleFeatureEntryModule {

    @Binds
    @IntoSet
    fun bindExampleFeatureEntry(
        entry: ExampleFeatureEntry,
    ): FeatureEntry
}
