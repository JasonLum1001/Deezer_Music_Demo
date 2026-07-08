package com.example.deezermusicdemo.di

import android.content.Context
import com.example.deezermusicdemo.common.player.MusicPlayerConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaybackModule {

    @Provides
    @Singleton
    fun provideMusicPlayerConnection(@ApplicationContext context: Context): MusicPlayerConnection {
        return MusicPlayerConnection(context)
    }
}
