package com.armada.expiryapp.data.repository;

import com.armada.expiryapp.data.db.dao.StockEntryDao;
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
public final class StockEntryRepository_Factory implements Factory<StockEntryRepository> {
  private final Provider<StockEntryDao> daoProvider;

  public StockEntryRepository_Factory(Provider<StockEntryDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public StockEntryRepository get() {
    return newInstance(daoProvider.get());
  }

  public static StockEntryRepository_Factory create(Provider<StockEntryDao> daoProvider) {
    return new StockEntryRepository_Factory(daoProvider);
  }

  public static StockEntryRepository newInstance(StockEntryDao dao) {
    return new StockEntryRepository(dao);
  }
}
