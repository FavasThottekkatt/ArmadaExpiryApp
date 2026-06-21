package com.armada.expiryapp.data.repository;

import com.armada.expiryapp.data.db.dao.ExpiryEntryDao;
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
public final class ExpiryEntryRepository_Factory implements Factory<ExpiryEntryRepository> {
  private final Provider<ExpiryEntryDao> daoProvider;

  public ExpiryEntryRepository_Factory(Provider<ExpiryEntryDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ExpiryEntryRepository get() {
    return newInstance(daoProvider.get());
  }

  public static ExpiryEntryRepository_Factory create(Provider<ExpiryEntryDao> daoProvider) {
    return new ExpiryEntryRepository_Factory(daoProvider);
  }

  public static ExpiryEntryRepository newInstance(ExpiryEntryDao dao) {
    return new ExpiryEntryRepository(dao);
  }
}
