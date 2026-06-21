package com.armada.expiryapp.ui.screens.itemlinking;

import com.armada.expiryapp.data.repository.DeviceLockRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.OutletItemLinkRepository;
import com.armada.expiryapp.data.repository.TeamLinkRepository;
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
public final class ItemLinkingViewModel_Factory implements Factory<ItemLinkingViewModel> {
  private final Provider<DeviceLockRepository> deviceLockRepositoryProvider;

  private final Provider<TeamLinkRepository> teamLinkRepositoryProvider;

  private final Provider<ItemRepository> itemRepositoryProvider;

  private final Provider<OutletItemLinkRepository> linkRepositoryProvider;

  public ItemLinkingViewModel_Factory(Provider<DeviceLockRepository> deviceLockRepositoryProvider,
      Provider<TeamLinkRepository> teamLinkRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<OutletItemLinkRepository> linkRepositoryProvider) {
    this.deviceLockRepositoryProvider = deviceLockRepositoryProvider;
    this.teamLinkRepositoryProvider = teamLinkRepositoryProvider;
    this.itemRepositoryProvider = itemRepositoryProvider;
    this.linkRepositoryProvider = linkRepositoryProvider;
  }

  @Override
  public ItemLinkingViewModel get() {
    return newInstance(deviceLockRepositoryProvider.get(), teamLinkRepositoryProvider.get(), itemRepositoryProvider.get(), linkRepositoryProvider.get());
  }

  public static ItemLinkingViewModel_Factory create(
      Provider<DeviceLockRepository> deviceLockRepositoryProvider,
      Provider<TeamLinkRepository> teamLinkRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<OutletItemLinkRepository> linkRepositoryProvider) {
    return new ItemLinkingViewModel_Factory(deviceLockRepositoryProvider, teamLinkRepositoryProvider, itemRepositoryProvider, linkRepositoryProvider);
  }

  public static ItemLinkingViewModel newInstance(DeviceLockRepository deviceLockRepository,
      TeamLinkRepository teamLinkRepository, ItemRepository itemRepository,
      OutletItemLinkRepository linkRepository) {
    return new ItemLinkingViewModel(deviceLockRepository, teamLinkRepository, itemRepository, linkRepository);
  }
}
