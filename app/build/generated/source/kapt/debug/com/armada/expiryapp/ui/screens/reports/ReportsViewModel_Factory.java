package com.armada.expiryapp.ui.screens.reports;

import android.content.Context;
import com.armada.expiryapp.data.repository.DeviceLockRepository;
import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
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
public final class ReportsViewModel_Factory implements Factory<ReportsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<SessionHolder> sessionHolderProvider;

  private final Provider<ExpiryEntryRepository> entryRepositoryProvider;

  private final Provider<DeviceLockRepository> deviceLockRepositoryProvider;

  private final Provider<TeamLinkRepository> teamLinkRepositoryProvider;

  public ReportsViewModel_Factory(Provider<Context> contextProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<DeviceLockRepository> deviceLockRepositoryProvider,
      Provider<TeamLinkRepository> teamLinkRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.sessionHolderProvider = sessionHolderProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.deviceLockRepositoryProvider = deviceLockRepositoryProvider;
    this.teamLinkRepositoryProvider = teamLinkRepositoryProvider;
  }

  @Override
  public ReportsViewModel get() {
    return newInstance(contextProvider.get(), sessionHolderProvider.get(), entryRepositoryProvider.get(), deviceLockRepositoryProvider.get(), teamLinkRepositoryProvider.get());
  }

  public static ReportsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<DeviceLockRepository> deviceLockRepositoryProvider,
      Provider<TeamLinkRepository> teamLinkRepositoryProvider) {
    return new ReportsViewModel_Factory(contextProvider, sessionHolderProvider, entryRepositoryProvider, deviceLockRepositoryProvider, teamLinkRepositoryProvider);
  }

  public static ReportsViewModel newInstance(Context context, SessionHolder sessionHolder,
      ExpiryEntryRepository entryRepository, DeviceLockRepository deviceLockRepository,
      TeamLinkRepository teamLinkRepository) {
    return new ReportsViewModel(context, sessionHolder, entryRepository, deviceLockRepository, teamLinkRepository);
  }
}
