package com.armada.expiryapp.ui.screens.settings;

import android.content.Context;
import com.armada.expiryapp.data.repository.CsvMetadataRepository;
import com.armada.expiryapp.data.repository.DeviceLockRepository;
import com.armada.expiryapp.data.repository.OutletItemLinkRepository;
import com.armada.expiryapp.data.repository.TeamLinkRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<CsvMetadataRepository> csvMetadataRepositoryProvider;

  private final Provider<SessionHolder> sessionHolderProvider;

  private final Provider<OutletItemLinkRepository> linkRepositoryProvider;

  private final Provider<DeviceLockRepository> deviceLockRepositoryProvider;

  private final Provider<TeamLinkRepository> teamLinkRepositoryProvider;

  public SettingsViewModel_Factory(Provider<Context> contextProvider,
      Provider<CsvMetadataRepository> csvMetadataRepositoryProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<OutletItemLinkRepository> linkRepositoryProvider,
      Provider<DeviceLockRepository> deviceLockRepositoryProvider,
      Provider<TeamLinkRepository> teamLinkRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.csvMetadataRepositoryProvider = csvMetadataRepositoryProvider;
    this.sessionHolderProvider = sessionHolderProvider;
    this.linkRepositoryProvider = linkRepositoryProvider;
    this.deviceLockRepositoryProvider = deviceLockRepositoryProvider;
    this.teamLinkRepositoryProvider = teamLinkRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(contextProvider.get(), csvMetadataRepositoryProvider.get(), sessionHolderProvider.get(), linkRepositoryProvider.get(), deviceLockRepositoryProvider.get(), teamLinkRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<CsvMetadataRepository> csvMetadataRepositoryProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<OutletItemLinkRepository> linkRepositoryProvider,
      Provider<DeviceLockRepository> deviceLockRepositoryProvider,
      Provider<TeamLinkRepository> teamLinkRepositoryProvider) {
    return new SettingsViewModel_Factory(contextProvider, csvMetadataRepositoryProvider, sessionHolderProvider, linkRepositoryProvider, deviceLockRepositoryProvider, teamLinkRepositoryProvider);
  }

  public static SettingsViewModel newInstance(Context context,
      CsvMetadataRepository csvMetadataRepository, SessionHolder sessionHolder,
      OutletItemLinkRepository linkRepository, DeviceLockRepository deviceLockRepository,
      TeamLinkRepository teamLinkRepository) {
    return new SettingsViewModel(context, csvMetadataRepository, sessionHolder, linkRepository, deviceLockRepository, teamLinkRepository);
  }
}
