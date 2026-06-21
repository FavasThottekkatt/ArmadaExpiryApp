package com.armada.expiryapp.ui.screens.startup;

import com.armada.expiryapp.data.auth.AuthRepository;
import com.armada.expiryapp.data.repository.CsvMetadataRepository;
import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.OutletRepository;
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
public final class StartupViewModel_Factory implements Factory<StartupViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<ItemRepository> itemRepositoryProvider;

  private final Provider<OutletRepository> outletRepositoryProvider;

  private final Provider<ExpiryEntryRepository> entryRepositoryProvider;

  private final Provider<CsvMetadataRepository> csvMetadataRepositoryProvider;

  public StartupViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<OutletRepository> outletRepositoryProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<CsvMetadataRepository> csvMetadataRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.itemRepositoryProvider = itemRepositoryProvider;
    this.outletRepositoryProvider = outletRepositoryProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.csvMetadataRepositoryProvider = csvMetadataRepositoryProvider;
  }

  @Override
  public StartupViewModel get() {
    return newInstance(authRepositoryProvider.get(), itemRepositoryProvider.get(), outletRepositoryProvider.get(), entryRepositoryProvider.get(), csvMetadataRepositoryProvider.get());
  }

  public static StartupViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider,
      Provider<OutletRepository> outletRepositoryProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<CsvMetadataRepository> csvMetadataRepositoryProvider) {
    return new StartupViewModel_Factory(authRepositoryProvider, itemRepositoryProvider, outletRepositoryProvider, entryRepositoryProvider, csvMetadataRepositoryProvider);
  }

  public static StartupViewModel newInstance(AuthRepository authRepository,
      ItemRepository itemRepository, OutletRepository outletRepository,
      ExpiryEntryRepository entryRepository, CsvMetadataRepository csvMetadataRepository) {
    return new StartupViewModel(authRepository, itemRepository, outletRepository, entryRepository, csvMetadataRepository);
  }
}
