package com.armada.expiryapp.ui.screens.itemlinking;

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
public final class ItemLinkingViewModel_Factory implements Factory<ItemLinkingViewModel> {
  private final Provider<SessionHolder> sessionHolderProvider;

  private final Provider<ItemRepository> itemRepositoryProvider;

  private final Provider<OutletItemLinkRepository> linkRepositoryProvider;

  public ItemLinkingViewModel_Factory(Provider<SessionHolder> sessionHolderProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<OutletItemLinkRepository> linkRepositoryProvider) {
    this.sessionHolderProvider = sessionHolderProvider;
    this.itemRepositoryProvider = itemRepositoryProvider;
    this.linkRepositoryProvider = linkRepositoryProvider;
  }

  @Override
  public ItemLinkingViewModel get() {
    return newInstance(sessionHolderProvider.get(), itemRepositoryProvider.get(), linkRepositoryProvider.get());
  }

  public static ItemLinkingViewModel_Factory create(Provider<SessionHolder> sessionHolderProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<OutletItemLinkRepository> linkRepositoryProvider) {
    return new ItemLinkingViewModel_Factory(sessionHolderProvider, itemRepositoryProvider, linkRepositoryProvider);
  }

  public static ItemLinkingViewModel newInstance(SessionHolder sessionHolder,
      ItemRepository itemRepository, OutletItemLinkRepository linkRepository) {
    return new ItemLinkingViewModel(sessionHolder, itemRepository, linkRepository);
  }
}
