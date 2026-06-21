package com.armada.expiryapp.data.repository;

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
public final class OutletRepository_Factory implements Factory<OutletRepository> {
  private final Provider<OutletDao> outletDaoProvider;

  public OutletRepository_Factory(Provider<OutletDao> outletDaoProvider) {
    this.outletDaoProvider = outletDaoProvider;
  }

  @Override
  public OutletRepository get() {
    return newInstance(outletDaoProvider.get());
  }

  public static OutletRepository_Factory create(Provider<OutletDao> outletDaoProvider) {
    return new OutletRepository_Factory(outletDaoProvider);
  }

  public static OutletRepository newInstance(OutletDao outletDao) {
    return new OutletRepository(outletDao);
  }
}
