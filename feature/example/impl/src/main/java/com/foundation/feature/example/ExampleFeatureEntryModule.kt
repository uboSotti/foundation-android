package com.foundation.feature.example

import com.foundation.core.navigation.FeatureEntryBuilderFactory
import com.foundation.core.navigation.StartDestination
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object ExampleFeatureEntryModule {

    @Provides
    @IntoSet
    fun provideExampleEntryBuilderFactory(): FeatureEntryBuilderFactory =
        FeatureEntryBuilderFactory { onOpenUrl ->
            {
                exampleEntryBuilder(onOpenUrl = onOpenUrl)
            }
        }

    @Provides
    @IntoSet
    fun provideExampleStartDestination(): StartDestination =
        StartDestination(
            key = ExampleNavKey,
            priority = 0,
        )
}
