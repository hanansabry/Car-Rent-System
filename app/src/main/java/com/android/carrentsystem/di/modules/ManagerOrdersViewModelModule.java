package com.android.carrentsystem.di.modules;

import com.android.carrentsystem.di.ViewModelKey;
import com.android.carrentsystem.presentation.viewmodels.ManageOrdersViewModel;
import com.android.carrentsystem.presentation.viewmodels.SearchCarsViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ManagerOrdersViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ManageOrdersViewModel.class)
    public abstract ViewModel bindViewModel(ManageOrdersViewModel viewModel);
}
