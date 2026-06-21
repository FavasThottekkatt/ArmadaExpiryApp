package com.armada.expiryapp.ui.screens.csvimport;

import android.content.Context;
import com.armada.expiryapp.data.repository.CsvImportRepository;
import com.armada.expiryapp.data.repository.CsvMetadataRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
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
public final class CsvImportViewModel_Factory implements Factory<CsvImportViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<CsvImportRepository> csvImportRepositoryProvider;

  private final Provider<CsvMetadataRepository> csvMetadataRepositoryProvider;

  private final Provider<ItemRepository> itemRepositoryProvider;

  public CsvImportViewModel_Factory(Provider<Context> contextProvider,
      Provider<CsvImportRepository> csvImportRepositoryProvider,
      Provider<CsvMetadataRepository> csvMetadataRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.csvImportRepositoryProvider = csvImportRepositoryProvider;
    this.csvMetadataRepositoryProvider = csvMetadataRepositoryProvider;
    this.itemRepositoryProvider = itemRepositoryProvider;
  }

  @Override
  public CsvImportViewModel get() {
    return newInstance(contextProvider.get(), csvImportRepositoryProvider.get(), csvMetadataRepositoryProvider.get(), itemRepositoryProvider.get());
  }

  public static CsvImportViewModel_Factory create(Provider<Context> contextProvider,
      Provider<CsvImportRepository> csvImportRepositoryProvider,
      Provider<CsvMetadataRepository> csvMetadataRepositoryProvider,
      Provider<ItemRepository> itemRepositoryProvider) {
    return new CsvImportViewModel_Factory(contextProvider, csvImportRepositoryProvider, csvMetadataRepositoryProvider, itemRepositoryProvider);
  }

  public static CsvImportViewModel newInstance(Context context,
      CsvImportRepository csvImportRepository, CsvMetadataRepository csvMetadataRepository,
      ItemRepository itemRepository) {
    return new CsvImportViewModel(context, csvImportRepository, csvMetadataRepository, itemRepository);
  }
}
