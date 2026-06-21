package com.armada.expiryapp.di;

import com.armada.expiryapp.data.db.AppDatabase;
import com.armada.expiryapp.data.db.dao.StockEntryDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideStockEntryDaoFactory implements Factory<StockEntryDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideStockEntryDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public StockEntryDao get() {
    return provideStockEntryDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideStockEntryDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideStockEntryDaoFactory(dbProvider);
  }

  public static StockEntryDao provideStockEntryDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideStockEntryDao(db));
  }
}
