package com.armada.expiryapp;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.armada.expiryapp.data.auth.AuthRepository;
import com.armada.expiryapp.data.db.AppDatabase;
import com.armada.expiryapp.data.db.dao.CsvMetadataDao;
import com.armada.expiryapp.data.db.dao.ExpiryEntryDao;
import com.armada.expiryapp.data.db.dao.ItemDao;
import com.armada.expiryapp.data.db.dao.OutletDao;
import com.armada.expiryapp.data.db.dao.OutletItemLinkDao;
import com.armada.expiryapp.data.db.dao.StockEntryDao;
import com.armada.expiryapp.data.repository.CsvImportRepository;
import com.armada.expiryapp.data.repository.CsvMetadataRepository;
import com.armada.expiryapp.data.repository.ExpiryEntryRepository;
import com.armada.expiryapp.data.repository.ItemRepository;
import com.armada.expiryapp.data.repository.OutletItemLinkRepository;
import com.armada.expiryapp.data.repository.OutletRepository;
import com.armada.expiryapp.data.repository.StockEntryRepository;
import com.armada.expiryapp.data.session.SessionHolder;
import com.armada.expiryapp.di.DatabaseModule_ProvideCsvMetadataDaoFactory;
import com.armada.expiryapp.di.DatabaseModule_ProvideDatabaseFactory;
import com.armada.expiryapp.di.DatabaseModule_ProvideExpiryEntryDaoFactory;
import com.armada.expiryapp.di.DatabaseModule_ProvideItemDaoFactory;
import com.armada.expiryapp.di.DatabaseModule_ProvideOutletDaoFactory;
import com.armada.expiryapp.di.DatabaseModule_ProvideOutletItemLinkDaoFactory;
import com.armada.expiryapp.di.DatabaseModule_ProvideStockEntryDaoFactory;
import com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel;
import com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel_HiltModules;
import com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel;
import com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel_HiltModules;
import com.armada.expiryapp.ui.screens.entry.NewEntryViewModel;
import com.armada.expiryapp.ui.screens.entry.NewEntryViewModel_HiltModules;
import com.armada.expiryapp.ui.screens.history.HistoryViewModel;
import com.armada.expiryapp.ui.screens.history.HistoryViewModel_HiltModules;
import com.armada.expiryapp.ui.screens.itemlinking.ItemLinkingViewModel;
import com.armada.expiryapp.ui.screens.itemlinking.ItemLinkingViewModel_HiltModules;
import com.armada.expiryapp.ui.screens.login.LoginViewModel;
import com.armada.expiryapp.ui.screens.login.LoginViewModel_HiltModules;
import com.armada.expiryapp.ui.screens.reports.ReportsViewModel;
import com.armada.expiryapp.ui.screens.reports.ReportsViewModel_HiltModules;
import com.armada.expiryapp.ui.screens.settings.SettingsViewModel;
import com.armada.expiryapp.ui.screens.settings.SettingsViewModel_HiltModules;
import com.armada.expiryapp.ui.screens.startup.StartupViewModel;
import com.armada.expiryapp.ui.screens.startup.StartupViewModel_HiltModules;
import com.armada.expiryapp.ui.screens.stock.StockViewModel;
import com.armada.expiryapp.ui.screens.stock.StockViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerArmadaApplication_HiltComponents_SingletonC {
  private DaggerArmadaApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public ArmadaApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements ArmadaApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public ArmadaApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements ArmadaApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public ArmadaApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements ArmadaApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public ArmadaApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements ArmadaApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ArmadaApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements ArmadaApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ArmadaApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements ArmadaApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public ArmadaApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements ArmadaApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public ArmadaApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends ArmadaApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends ArmadaApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends ArmadaApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends ArmadaApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(10).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_csvimport_CsvImportViewModel, CsvImportViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_dashboard_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_history_HistoryViewModel, HistoryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_itemlinking_ItemLinkingViewModel, ItemLinkingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_login_LoginViewModel, LoginViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_entry_NewEntryViewModel, NewEntryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_reports_ReportsViewModel, ReportsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_startup_StartupViewModel, StartupViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_stock_StockViewModel, StockViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_armada_expiryapp_ui_screens_settings_SettingsViewModel = "com.armada.expiryapp.ui.screens.settings.SettingsViewModel";

      static String com_armada_expiryapp_ui_screens_entry_NewEntryViewModel = "com.armada.expiryapp.ui.screens.entry.NewEntryViewModel";

      static String com_armada_expiryapp_ui_screens_reports_ReportsViewModel = "com.armada.expiryapp.ui.screens.reports.ReportsViewModel";

      static String com_armada_expiryapp_ui_screens_dashboard_DashboardViewModel = "com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel";

      static String com_armada_expiryapp_ui_screens_login_LoginViewModel = "com.armada.expiryapp.ui.screens.login.LoginViewModel";

      static String com_armada_expiryapp_ui_screens_startup_StartupViewModel = "com.armada.expiryapp.ui.screens.startup.StartupViewModel";

      static String com_armada_expiryapp_ui_screens_stock_StockViewModel = "com.armada.expiryapp.ui.screens.stock.StockViewModel";

      static String com_armada_expiryapp_ui_screens_itemlinking_ItemLinkingViewModel = "com.armada.expiryapp.ui.screens.itemlinking.ItemLinkingViewModel";

      static String com_armada_expiryapp_ui_screens_history_HistoryViewModel = "com.armada.expiryapp.ui.screens.history.HistoryViewModel";

      static String com_armada_expiryapp_ui_screens_csvimport_CsvImportViewModel = "com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel";

      @KeepFieldType
      SettingsViewModel com_armada_expiryapp_ui_screens_settings_SettingsViewModel2;

      @KeepFieldType
      NewEntryViewModel com_armada_expiryapp_ui_screens_entry_NewEntryViewModel2;

      @KeepFieldType
      ReportsViewModel com_armada_expiryapp_ui_screens_reports_ReportsViewModel2;

      @KeepFieldType
      DashboardViewModel com_armada_expiryapp_ui_screens_dashboard_DashboardViewModel2;

      @KeepFieldType
      LoginViewModel com_armada_expiryapp_ui_screens_login_LoginViewModel2;

      @KeepFieldType
      StartupViewModel com_armada_expiryapp_ui_screens_startup_StartupViewModel2;

      @KeepFieldType
      StockViewModel com_armada_expiryapp_ui_screens_stock_StockViewModel2;

      @KeepFieldType
      ItemLinkingViewModel com_armada_expiryapp_ui_screens_itemlinking_ItemLinkingViewModel2;

      @KeepFieldType
      HistoryViewModel com_armada_expiryapp_ui_screens_history_HistoryViewModel2;

      @KeepFieldType
      CsvImportViewModel com_armada_expiryapp_ui_screens_csvimport_CsvImportViewModel2;
    }
  }

  private static final class ViewModelCImpl extends ArmadaApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CsvImportViewModel> csvImportViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<HistoryViewModel> historyViewModelProvider;

    private Provider<ItemLinkingViewModel> itemLinkingViewModelProvider;

    private Provider<LoginViewModel> loginViewModelProvider;

    private Provider<NewEntryViewModel> newEntryViewModelProvider;

    private Provider<ReportsViewModel> reportsViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<StartupViewModel> startupViewModelProvider;

    private Provider<StockViewModel> stockViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.csvImportViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.historyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.itemLinkingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.loginViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.newEntryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.reportsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.startupViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.stockViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(10).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_csvimport_CsvImportViewModel, ((Provider) csvImportViewModelProvider)).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_dashboard_DashboardViewModel, ((Provider) dashboardViewModelProvider)).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_history_HistoryViewModel, ((Provider) historyViewModelProvider)).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_itemlinking_ItemLinkingViewModel, ((Provider) itemLinkingViewModelProvider)).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_login_LoginViewModel, ((Provider) loginViewModelProvider)).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_entry_NewEntryViewModel, ((Provider) newEntryViewModelProvider)).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_reports_ReportsViewModel, ((Provider) reportsViewModelProvider)).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_startup_StartupViewModel, ((Provider) startupViewModelProvider)).put(LazyClassKeyProvider.com_armada_expiryapp_ui_screens_stock_StockViewModel, ((Provider) stockViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_armada_expiryapp_ui_screens_dashboard_DashboardViewModel = "com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel";

      static String com_armada_expiryapp_ui_screens_stock_StockViewModel = "com.armada.expiryapp.ui.screens.stock.StockViewModel";

      static String com_armada_expiryapp_ui_screens_history_HistoryViewModel = "com.armada.expiryapp.ui.screens.history.HistoryViewModel";

      static String com_armada_expiryapp_ui_screens_csvimport_CsvImportViewModel = "com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel";

      static String com_armada_expiryapp_ui_screens_startup_StartupViewModel = "com.armada.expiryapp.ui.screens.startup.StartupViewModel";

      static String com_armada_expiryapp_ui_screens_login_LoginViewModel = "com.armada.expiryapp.ui.screens.login.LoginViewModel";

      static String com_armada_expiryapp_ui_screens_itemlinking_ItemLinkingViewModel = "com.armada.expiryapp.ui.screens.itemlinking.ItemLinkingViewModel";

      static String com_armada_expiryapp_ui_screens_entry_NewEntryViewModel = "com.armada.expiryapp.ui.screens.entry.NewEntryViewModel";

      static String com_armada_expiryapp_ui_screens_settings_SettingsViewModel = "com.armada.expiryapp.ui.screens.settings.SettingsViewModel";

      static String com_armada_expiryapp_ui_screens_reports_ReportsViewModel = "com.armada.expiryapp.ui.screens.reports.ReportsViewModel";

      @KeepFieldType
      DashboardViewModel com_armada_expiryapp_ui_screens_dashboard_DashboardViewModel2;

      @KeepFieldType
      StockViewModel com_armada_expiryapp_ui_screens_stock_StockViewModel2;

      @KeepFieldType
      HistoryViewModel com_armada_expiryapp_ui_screens_history_HistoryViewModel2;

      @KeepFieldType
      CsvImportViewModel com_armada_expiryapp_ui_screens_csvimport_CsvImportViewModel2;

      @KeepFieldType
      StartupViewModel com_armada_expiryapp_ui_screens_startup_StartupViewModel2;

      @KeepFieldType
      LoginViewModel com_armada_expiryapp_ui_screens_login_LoginViewModel2;

      @KeepFieldType
      ItemLinkingViewModel com_armada_expiryapp_ui_screens_itemlinking_ItemLinkingViewModel2;

      @KeepFieldType
      NewEntryViewModel com_armada_expiryapp_ui_screens_entry_NewEntryViewModel2;

      @KeepFieldType
      SettingsViewModel com_armada_expiryapp_ui_screens_settings_SettingsViewModel2;

      @KeepFieldType
      ReportsViewModel com_armada_expiryapp_ui_screens_reports_ReportsViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.armada.expiryapp.ui.screens.csvimport.CsvImportViewModel 
          return (T) new CsvImportViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.csvImportRepositoryProvider.get(), singletonCImpl.csvMetadataRepositoryProvider.get(), singletonCImpl.itemRepositoryProvider.get());

          case 1: // com.armada.expiryapp.ui.screens.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.outletRepositoryProvider.get(), singletonCImpl.expiryEntryRepositoryProvider.get(), singletonCImpl.itemRepositoryProvider.get(), singletonCImpl.csvMetadataRepositoryProvider.get(), singletonCImpl.sessionHolderProvider.get());

          case 2: // com.armada.expiryapp.ui.screens.history.HistoryViewModel 
          return (T) new HistoryViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.sessionHolderProvider.get(), singletonCImpl.expiryEntryRepositoryProvider.get(), singletonCImpl.outletRepositoryProvider.get());

          case 3: // com.armada.expiryapp.ui.screens.itemlinking.ItemLinkingViewModel 
          return (T) new ItemLinkingViewModel(singletonCImpl.sessionHolderProvider.get(), singletonCImpl.itemRepositoryProvider.get(), singletonCImpl.outletItemLinkRepositoryProvider.get());

          case 4: // com.armada.expiryapp.ui.screens.login.LoginViewModel 
          return (T) new LoginViewModel(singletonCImpl.authRepositoryProvider.get());

          case 5: // com.armada.expiryapp.ui.screens.entry.NewEntryViewModel 
          return (T) new NewEntryViewModel(singletonCImpl.sessionHolderProvider.get(), singletonCImpl.expiryEntryRepositoryProvider.get(), singletonCImpl.itemRepositoryProvider.get(), singletonCImpl.outletItemLinkRepositoryProvider.get());

          case 6: // com.armada.expiryapp.ui.screens.reports.ReportsViewModel 
          return (T) new ReportsViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.sessionHolderProvider.get(), singletonCImpl.expiryEntryRepositoryProvider.get(), singletonCImpl.outletRepositoryProvider.get());

          case 7: // com.armada.expiryapp.ui.screens.settings.SettingsViewModel 
          return (T) new SettingsViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.csvMetadataRepositoryProvider.get(), singletonCImpl.sessionHolderProvider.get(), singletonCImpl.outletItemLinkRepositoryProvider.get());

          case 8: // com.armada.expiryapp.ui.screens.startup.StartupViewModel 
          return (T) new StartupViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.itemRepositoryProvider.get(), singletonCImpl.outletRepositoryProvider.get(), singletonCImpl.expiryEntryRepositoryProvider.get(), singletonCImpl.csvMetadataRepositoryProvider.get());

          case 9: // com.armada.expiryapp.ui.screens.stock.StockViewModel 
          return (T) new StockViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.sessionHolderProvider.get(), singletonCImpl.stockEntryRepositoryProvider.get(), singletonCImpl.itemRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends ArmadaApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends ArmadaApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends ArmadaApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideDatabaseProvider;

    private Provider<ExpiryEntryRepository> expiryEntryRepositoryProvider;

    private Provider<CsvImportRepository> csvImportRepositoryProvider;

    private Provider<CsvMetadataRepository> csvMetadataRepositoryProvider;

    private Provider<ItemRepository> itemRepositoryProvider;

    private Provider<OutletRepository> outletRepositoryProvider;

    private Provider<SessionHolder> sessionHolderProvider;

    private Provider<OutletItemLinkRepository> outletItemLinkRepositoryProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<StockEntryRepository> stockEntryRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private ExpiryEntryDao expiryEntryDao() {
      return DatabaseModule_ProvideExpiryEntryDaoFactory.provideExpiryEntryDao(provideDatabaseProvider.get());
    }

    private ItemDao itemDao() {
      return DatabaseModule_ProvideItemDaoFactory.provideItemDao(provideDatabaseProvider.get());
    }

    private OutletDao outletDao() {
      return DatabaseModule_ProvideOutletDaoFactory.provideOutletDao(provideDatabaseProvider.get());
    }

    private CsvMetadataDao csvMetadataDao() {
      return DatabaseModule_ProvideCsvMetadataDaoFactory.provideCsvMetadataDao(provideDatabaseProvider.get());
    }

    private OutletItemLinkDao outletItemLinkDao() {
      return DatabaseModule_ProvideOutletItemLinkDaoFactory.provideOutletItemLinkDao(provideDatabaseProvider.get());
    }

    private StockEntryDao stockEntryDao() {
      return DatabaseModule_ProvideStockEntryDaoFactory.provideStockEntryDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 1));
      this.expiryEntryRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ExpiryEntryRepository>(singletonCImpl, 0));
      this.csvImportRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<CsvImportRepository>(singletonCImpl, 2));
      this.csvMetadataRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<CsvMetadataRepository>(singletonCImpl, 3));
      this.itemRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ItemRepository>(singletonCImpl, 4));
      this.outletRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<OutletRepository>(singletonCImpl, 5));
      this.sessionHolderProvider = DoubleCheck.provider(new SwitchingProvider<SessionHolder>(singletonCImpl, 6));
      this.outletItemLinkRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<OutletItemLinkRepository>(singletonCImpl, 7));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 8));
      this.stockEntryRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<StockEntryRepository>(singletonCImpl, 9));
    }

    @Override
    public void injectArmadaApplication(ArmadaApplication armadaApplication) {
    }

    @Override
    public ExpiryEntryRepository expiryEntryRepository() {
      return expiryEntryRepositoryProvider.get();
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.armada.expiryapp.data.repository.ExpiryEntryRepository 
          return (T) new ExpiryEntryRepository(singletonCImpl.expiryEntryDao());

          case 1: // com.armada.expiryapp.data.db.AppDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.armada.expiryapp.data.repository.CsvImportRepository 
          return (T) new CsvImportRepository(singletonCImpl.itemDao(), singletonCImpl.outletDao(), singletonCImpl.csvMetadataDao());

          case 3: // com.armada.expiryapp.data.repository.CsvMetadataRepository 
          return (T) new CsvMetadataRepository(singletonCImpl.csvMetadataDao());

          case 4: // com.armada.expiryapp.data.repository.ItemRepository 
          return (T) new ItemRepository(singletonCImpl.itemDao());

          case 5: // com.armada.expiryapp.data.repository.OutletRepository 
          return (T) new OutletRepository(singletonCImpl.outletDao());

          case 6: // com.armada.expiryapp.data.session.SessionHolder 
          return (T) new SessionHolder();

          case 7: // com.armada.expiryapp.data.repository.OutletItemLinkRepository 
          return (T) new OutletItemLinkRepository(singletonCImpl.outletItemLinkDao());

          case 8: // com.armada.expiryapp.data.auth.AuthRepository 
          return (T) new AuthRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.armada.expiryapp.data.repository.StockEntryRepository 
          return (T) new StockEntryRepository(singletonCImpl.stockEntryDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
