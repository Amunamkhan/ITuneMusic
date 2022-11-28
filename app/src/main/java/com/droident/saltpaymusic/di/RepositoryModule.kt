package com.droident.saltpaymusic.di

import com.droident.saltpaymusic.data.repository.MusicRepositoryImpl
import com.droident.saltpaymusic.domain.repository.MusicRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        musicRepositoryImpl: MusicRepositoryImpl,
    ): MusicRepository
}