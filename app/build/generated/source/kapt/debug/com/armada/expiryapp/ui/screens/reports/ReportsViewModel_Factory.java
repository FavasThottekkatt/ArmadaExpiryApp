package com.armada.expiryapp.ui.screens.reports;

import android.content.Context;
import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
import com.armada.expiryapp.data.repository.OutletRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ReportsViewModel_Factory implements Factory<ReportsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<SessionHolder> sessionHolderProvider;

  private final Provider<ExpiryEntryRepository> entryRepositoryProvider;

  private final Provider<OutletRepository> outletRepositoryProvider;

  public ReportsViewModel_Factory(Provider<Context> contextProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<OutletRepository> outletRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.sessionHolderProvider = sessionHolderProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.outletRepositoryProvider = outletRepositoryProvider;
  }

  @Override
  public ReportsViewModel get() {
    return newInstance(contextProvider.get(), sessionHolderProvider.get(), entryRepositoryProvider.get(), outletRepositoryProvider.get());
  }

  public static ReportsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<OutletRepository> outletRepositoryProvider) {
    return new ReportsViewModel_Factory(contextProvider, sessionHolderProvider, entryRepositoryProvider, outletRepositoryProvider);
  }

  public static ReportsViewModel newInstance(Context context, SessionHolder sessionHolder,
      ExpiryEntryRepository entryRepository, OutletRepository outletRepository) {
    return new ReportsViewModel(context, sessionHolder, entryRepository, outletRepository);
  }
}
