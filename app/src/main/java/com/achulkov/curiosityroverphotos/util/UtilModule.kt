package com.achulkov.curiosityroverphotos.util

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilModule {
    @Binds
    abstract fun imageLoader(impl: PicassoImageLoader) : ImageLoader
}