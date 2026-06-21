package com.armada.expiryapp.di;

import com.armada.expiryapp.data.db.AppDatabase;
import com.armada.expiryapp.data.db.dao.OutletDao;
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
public final class DatabaseModule_ProvideOutletDaoFactory implements Factory<OutletDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideOutletDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public OutletDao get() {
    return provideOutletDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideOutletDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideOutletDaoFactory(dbProvider);
  }

  public static OutletDao provideOutletDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideOutletDao(db));
  }
}
