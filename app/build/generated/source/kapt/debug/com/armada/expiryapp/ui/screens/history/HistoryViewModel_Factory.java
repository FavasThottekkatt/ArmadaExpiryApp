package com.armada.expiryapp.ui.screens.history;

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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<SessionHolder> sessionHolderProvider;

  private final Provider<ExpiryEntryRepository> entryRepositoryProvider;

  private final Provider<OutletRepository> outletRepositoryProvider;

  public HistoryViewModel_Factory(Provider<Context> contextProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<OutletRepository> outletRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.sessionHolderProvider = sessionHolderProvider;
    this.entryRepositoryProvider = entryRepositoryProvider;
    this.outletRepositoryProvider = outletRepositoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(contextProvider.get(), sessionHolderProvider.get(), entryRepositoryProvider.get(), outletRepositoryProvider.get());
  }

  public static HistoryViewModel_Factory create(Provider<Context> contextProvider,
      Provider<SessionHolder> sessionHolderProvider,
      Provider<ExpiryEntryRepository> entryRepositoryProvider,
      Provider<OutletRepository> outletRepositoryProvider) {
    return new HistoryViewModel_Factory(contextProvider, sessionHolderProvider, entryRepositoryProvider, outletRepositoryProvider);
  }

  public static HistoryViewModel newInstance(Context context, SessionHolder sessionHolder,
      ExpiryEntryRepository entryRepository, OutletRepository outletRepository) {
    return new HistoryViewModel(context, sessionHolder, entryRepository, outletRepository);
  }
}
