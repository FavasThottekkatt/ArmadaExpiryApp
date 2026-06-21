package com.armada.expiryapp.ui.screens.entry;

import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.OutletItemLinkRepository;
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
public final class NewEntryViewModel_Factory implements Factory<NewEntryViewModel> {
  private final Provider<SessionHolder> sessionHolderProvider;

  private final Provider<ExpiryEntryRepository> entryRepositoryProvider;

  private final Provider<ItemRepository> itemRepositoryProvider;

  private final Provider<OutletItemLinkRepository> linkRepositoryProvider;

  public NewEntryViewModel_Factory(Provider<SessionHolder> sessionHolderProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<OutletItemLinkRepository> linkRepositoryProvider) {
    this.sessionHolderProvider = sessionHolderProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.itemRepositoryProvider = itemRepositoryProvider;
    this.linkRepositoryProvider = linkRepositoryProvider;
  }

  @Override
  public NewEntryViewModel get() {
    return newInstance(sessionHolderProvider.get(), entryRepositoryProvider.get(), itemRepositoryProvider.get(), linkRepositoryProvider.get());
  }

  public static NewEntryViewModel_Factory create(Provider<SessionHolder> sessionHolderProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<OutletItemLinkRepository> linkRepositoryProvider) {
    return new NewEntryViewModel_Factory(sessionHolderProvider, entryRepositoryProvider, itemRepositoryProvider, linkRepositoryProvider);
  }

  public static NewEntryViewModel newInstance(SessionHolder sessionHolder,
      ExpiryEntryRepository entryRepository, ItemRepository itemRepository,
      OutletItemLinkRepository linkRepository) {
    return new NewEntryViewModel(sessionHolder, entryRepository, itemRepository, linkRepository);
  }
}
