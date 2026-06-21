package com.armada.expiryapp.ui.screens.dashboard;

import com.armada.expiryapp.data.repository.CsvMetadataRepository;
import com.armada.expiryapp.data.repository.DeviceLockRepository;
import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.OutletRepository;
import com.armada.expiryapp.data.repository.TeamLinkRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<OutletRepository> outletRepositoryProvider;

  private final Provider<ExpiryEntryRepository> entryRepositoryProvider;

  private final Provider<ItemRepository> itemRepositoryProvider;

  private final Provider<CsvMetadataRepository> csvMetadataRepositoryProvider;

  private final Provider<SessionHolder> sessionHolderProvider;

  private final Provider<DeviceLockRepository> deviceLockRepositoryProvider;

  private final Provider<TeamLinkRepository> teamLinkRepositoryProvider;

  public DashboardViewModel_Factory(Provider<OutletRepository> outletRepositoryProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<CsvMetadataRepository> csvMetadataRepositoryProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<DeviceLockRepository> deviceLockRepositoryProvider,
      Provider<TeamLinkRepository> teamLinkRepositoryProvider) {
    this.outletRepositoryProvider = outletRepositoryProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.itemRepositoryProvider = itemRepositoryProvider;
    this.csvMetadataRepositoryProvider = csvMetadataRepositoryProvider;
    this.sessionHolderProvider = sessionHolderProvider;
    this.deviceLockRepositoryProvider = deviceLockRepositoryProvider;
    this.teamLinkRepositoryProvider = teamLinkRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(outletRepositoryProvider.get(), entryRepositoryProvider.get(), itemRepositoryProvider.get(), csvMetadataRepositoryProvider.get(), sessionHolderProvider.get(), deviceLockRepositoryProvider.get(), teamLinkRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<OutletRepository> outletRepositoryProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<CsvMetadataRepository> csvMetadataRepositoryProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<DeviceLockRepository> deviceLockRepositoryProvider,
      Provider<TeamLinkRepository> teamLinkRepositoryProvider) {
    return new DashboardViewModel_Factory(outletRepositoryProvider, entryRepositoryProvider, itemRepositoryProvider, csvMetadataRepositoryProvider, sessionHolderProvider, deviceLockRepositoryProvider, teamLinkRepositoryProvider);
  }

  public static DashboardViewModel newInstance(OutletRepository outletRepository,
      ExpiryEntryRepository entryRepository, ItemRepository itemRepository,
      CsvMetadataRepository csvMetadataRepository, SessionHolder sessionHolder,
      DeviceLockRepository deviceLockRepository, TeamLinkRepository teamLinkRepository) {
    return new DashboardViewModel(outletRepository, entryRepository, itemRepository, csvMetadataRepository, sessionHolder, deviceLockRepository, teamLinkRepository);
  }
}
