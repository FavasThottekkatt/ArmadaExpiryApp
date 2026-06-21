package com.armada.expiryapp.data.repository;

import com.armada.expiryapp.data.db.dao.CsvMetadataDao;
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
public final class CsvMetadataRepository_Factory implements Factory<CsvMetadataRepository> {
  private final Provider<CsvMetadataDao> daoProvider;

  public CsvMetadataRepository_Factory(Provider<CsvMetadataDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public CsvMetadataRepository get() {
    return newInstance(daoProvider.get());
  }

  public static CsvMetadataRepository_Factory create(Provider<CsvMetadataDao> daoProvider) {
    return new CsvMetadataRepository_Factory(daoProvider);
  }

  public static CsvMetadataRepository newInstance(CsvMetadataDao dao) {
    return new CsvMetadataRepository(dao);
  }
}
