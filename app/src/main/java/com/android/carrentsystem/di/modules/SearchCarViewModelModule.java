package com.android.carrentsystem.di.modules;

import com.android.carrentsystem.di.ViewModelKey;
import com.android.carrentsystem.presentation.viewmodels.SearchCarsViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SearchCarViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchCarsViewModel.class)
    public abstract ViewModel bindViewModel(SearchCarsViewModel viewModel);
}
