package jp.co.shinoken.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.shinoken.api.aws.AmplifyClient
import jp.co.shinoken.api.aws.imple.AmplifyService

@InstallIn(SingletonComponent::class)
@Module
internal abstract class AmplifyModule {
    @Binds
    abstract fun provideAmplifyClient(impl: AmplifyService): AmplifyClient
}