package com.armada.expiryapp.di;

import com.armada.expiryapp.data.db.AppDatabase;
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao;
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
public final class DatabaseModule_ProvideExpiryEntryDaoFactory implements Factory<ExpiryEntryDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideExpiryEntryDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ExpiryEntryDao get() {
    return provideExpiryEntryDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideExpiryEntryDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideExpiryEntryDaoFactory(dbProvider);
  }

  public static ExpiryEntryDao provideExpiryEntryDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideExpiryEntryDao(db));
  }
}
