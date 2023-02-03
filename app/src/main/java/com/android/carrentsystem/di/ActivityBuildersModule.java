package com.android.carrentsystem.di;


import com.android.carrentsystem.di.modules.AddCarViewModelModule;
import com.android.carrentsystem.di.modules.AuthenticationViewModelModule;
import com.android.carrentsystem.di.modules.ManagerOrdersViewModelModule;
import com.android.carrentsystem.di.modules.RentCarViewModelModule;
import com.android.carrentsystem.di.modules.SearchCarViewModelModule;
import com.android.carrentsystem.presentation.SplashActivity;
import com.android.carrentsystem.presentation.agency.AddCarActivity;
import com.android.carrentsystem.presentation.agency.ManageOrdersActivity;
import com.android.carrentsystem.presentation.agency.OrderDetailsActivity;
import com.android.carrentsystem.presentation.client.RentCarOrderActivity;
import com.android.carrentsystem.presentation.client.SearchCarsActivity;
import com.android.carrentsystem.presentation.client.SearchResultsActivity;
import com.android.carrentsystem.presentation.start.LoginActivity;
import com.android.carrentsystem.presentation.start.RegisterActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector(modules = AuthenticationViewModelModule.class)
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector(modules = AuthenticationViewModelModule.class)
    abstract RegisterActivity contributeRegisterActivity();

    @ContributesAndroidInjector(modules = AddCarViewModelModule.class)
    abstract AddCarActivity contributeAddCarActivity();

    @ContributesAndroidInjector(modules = SearchCarViewModelModule.class)
    abstract SearchCarsActivity contributeSearchCarActivity();

    @ContributesAndroidInjector(modules = SearchCarViewModelModule.class)
    abstract SearchResultsActivity contributeSearchResultsActivity();

    @ContributesAndroidInjector(modules = RentCarViewModelModule.class)
    abstract RentCarOrderActivity contributeRentCarOrderActivity();

    @ContributesAndroidInjector(modules = ManagerOrdersViewModelModule.class)
    abstract ManageOrdersActivity contributeManageOrdersActivity();

    @ContributesAndroidInjector(modules = ManagerOrdersViewModelModule.class)
    abstract OrderDetailsActivity contributeOrderDetailsActivity();
}
