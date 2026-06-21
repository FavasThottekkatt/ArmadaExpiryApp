package com.armada.expiryapp.ui.screens.stock;

import android.content.Context;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.StockEntryRepository;
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
public final class StockViewModel_Factory implements Factory<StockViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<SessionHolder> sessionHolderProvider;

  private final Provider<StockEntryRepository> stockRepositoryProvider;

  private final Provider<ItemRepository> itemRepositoryProvider;

  public StockViewModel_Factory(Provider<Context> contextProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<StockEntryRepository> stockRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.sessionHolderProvider = sessionHolderProvider;
    this.stockRepositoryProvider = stockRepositoryProvider;
    this.itemRepositoryProvider = itemRepositoryProvider;
  }

  @Override
  public StockViewModel get() {
    return newInstance(contextProvider.get(), sessionHolderProvider.get(), stockRepositoryProvider.get(), itemRepositoryProvider.get());
  }

  public static StockViewModel_Factory create(Provider<Context> contextProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<StockEntryRepository> stockRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider) {
    return new StockViewModel_Factory(contextProvider, sessionHolderProvider, stockRepositoryProvider, itemRepositoryProvider);
  }

  public static StockViewModel newInstance(Context context, SessionHolder sessionHolder,
      StockEntryRepository stockRepository, ItemRepository itemRepository) {
    return new StockViewModel(context, sessionHolder, stockRepository, itemRepository);
  }
}
