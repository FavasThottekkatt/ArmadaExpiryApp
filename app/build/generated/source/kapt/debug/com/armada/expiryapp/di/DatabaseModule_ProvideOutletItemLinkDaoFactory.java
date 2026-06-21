package com.armada.expiryapp.di;

import com.armada.expiryapp.data.db.AppDatabase;
import com.armada.expiryapp.data.db.dao.OutletItemLinkDao;
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
public final class DatabaseModule_ProvideOutletItemLinkDaoFactory implements Factory<OutletItemLinkDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideOutletItemLinkDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public OutletItemLinkDao get() {
    return provideOutletItemLinkDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideOutletItemLinkDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideOutletItemLinkDaoFactory(dbProvider);
  }

  public static OutletItemLinkDao provideOutletItemLinkDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideOutletItemLinkDao(db));
  }
}
