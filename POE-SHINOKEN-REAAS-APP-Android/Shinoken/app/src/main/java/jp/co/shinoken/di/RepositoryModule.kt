package jp.co.shinoken.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.shinoken.repository.*
import jp.co.shinoken.repository.impl.*

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {
    @Binds
    abstract fun provideHomeRepository(impl: DataHomeRepository): HomeRepository

    @Binds
    abstract fun provideAuthRepository(impl: DataAuthRepository): AuthRepository

    @Binds
    abstract fun provideAccountRepository(impl: DataAccountRepository): AccountRepository

    @Binds
    abstract fun providePasswordResetRepository(impl: DataPasswordResetRepository): PasswordResetRepository

    @Binds
    abstract fun provideManualRepository(impl: DataManualRepository): ManualRepository

    @Binds
    abstract fun provideContactRepository(impl: DataContactRepository): ContactRepository

    @Binds
    abstract fun provideFaqRepository(impl: DataFaqRepository): FaqRepository

    @Binds
    abstract fun provideChargeRepository(impl: DataChargeRepository): ChargeRepository

    @Binds
    abstract fun provideTopicRepository(impl: DataTopicRepository): TopicRepository

    @Binds
    abstract fun provideCheckFormRepository(impl: DataCheckFormRepository): CheckFormRepository

    @Binds
    abstract fun provideStoreRepository(impl: DataStoreRepository): StoreRepository

    @Binds
    abstract fun provideSearchRepository(impl: DataSearchRepository): SearchRepository

    @Binds
    abstract fun provideVersionCheckRepository(impl: DataVersionCheckRepository): VersionCheckRepository

    @Binds
    abstract fun provideNotificationRepository(impl: DataNotificationRepository): NotificationRepository

    @Binds
    abstract fun provideBenefitRepository(impl: DataBenefitRepository): BenefitRepository

    @Binds
    abstract fun provideRoommateRepository(impl: DataRoommateRepository): RoommateRepository

    @Binds
    abstract fun provideUserInfoRepository(impl: DataUserInfoRepository): UserInfoRepository

    @Binds
    abstract fun provideMetaRepository(impl: DataMetaRepository): MetaRepository


    @Binds
    abstract fun providePushRepository(impl: DataPushRepository): PushRepository
}