package com.armada.expiryapp.data.repository;

import com.armada.expiryapp.data.db.dao.CsvMetadataDao;
import com.armada.expiryapp.data.db.dao.ItemDao;
import com.armada.expiryapp.data.db.dao.OutletDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class CsvImportRepository_Factory implements Factory<CsvImportRepository> {
  private final Provider<ItemDao> itemDaoProvider;

  private final Provider<OutletDao> outletDaoProvider;

  private final Provider<CsvMetadataDao> csvMetadataDaoProvider;

  public CsvImportRepository_Factory(Provider<ItemDao> itemDaoProvider,
      Provider<OutletDao> outletDaoProvider, Provider<CsvMetadataDao> csvMetadataDaoProvider) {
    this.itemDaoProvider = itemDaoProvider;
    this.outletDaoProvider = outletDaoProvider;
    this.csvMetadataDaoProvider = csvMetadataDaoProvider;
  }

  @Override
  public CsvImportRepository get() {
    return newInstance(itemDaoProvider.get(), outletDaoProvider.get(), csvMetadataDaoProvider.get());
  }

  public static CsvImportRepository_Factory create(Provider<ItemDao> itemDaoProvider,
      Provider<OutletDao> outletDaoProvider, Provider<CsvMetadataDao> csvMetadataDaoProvider) {
    return new CsvImportRepository_Factory(itemDaoProvider, outletDaoProvider, csvMetadataDaoProvider);
  }

  public static CsvImportRepository newInstance(ItemDao itemDao, OutletDao outletDao,
      CsvMetadataDao csvMetadataDao) {
    return new CsvImportRepository(itemDao, outletDao, csvMetadataDao);
  }
}
