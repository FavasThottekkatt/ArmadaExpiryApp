package com.armada.expiryapp.data.session;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class SessionHolder_Factory implements Factory<SessionHolder> {
  @Override
  public SessionHolder get() {
    return newInstance();
  }

  public static SessionHolder_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SessionHolder newInstance() {
    return new SessionHolder();
  }

  private static final class InstanceHolder {
    private static final SessionHolder_Factory INSTANCE = new SessionHolder_Factory();
  }
}
