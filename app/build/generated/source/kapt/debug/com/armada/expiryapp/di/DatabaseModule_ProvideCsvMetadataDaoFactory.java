package com.armada.expiryapp.di;

import com.armada.expiryapp.data.db.AppDatabase;
import com.armada.expiryapp.data.db.dao.CsvMetadataDao;
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
public final class DatabaseModule_ProvideCsvMetadataDaoFactory implements Factory<CsvMetadataDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideCsvMetadataDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CsvMetadataDao get() {
    return provideCsvMetadataDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideCsvMetadataDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideCsvMetadataDaoFactory(dbProvider);
  }

  public static CsvMetadataDao provideCsvMetadataDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCsvMetadataDao(db));
  }
}
