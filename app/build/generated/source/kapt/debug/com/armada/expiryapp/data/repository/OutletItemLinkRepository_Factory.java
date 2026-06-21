package com.armada.expiryapp.data.repository;

import com.armada.expiryapp.data.db.dao.OutletItemLinkDao;
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
public final class OutletItemLinkRepository_Factory implements Factory<OutletItemLinkRepository> {
  private final Provider<OutletItemLinkDao> daoProvider;

  public OutletItemLinkRepository_Factory(Provider<OutletItemLinkDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public OutletItemLinkRepository get() {
    return newInstance(daoProvider.get());
  }

  public static OutletItemLinkRepository_Factory create(Provider<OutletItemLinkDao> daoProvider) {
    return new OutletItemLinkRepository_Factory(daoProvider);
  }

  public static OutletItemLinkRepository newInstance(OutletItemLinkDao dao) {
    return new OutletItemLinkRepository(dao);
  }
}
