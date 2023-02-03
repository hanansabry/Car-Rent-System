package com.android.carrentsystem.presentation.viewmodels;

import com.android.carrentsystem.data.DatabaseRepository;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.CarCategory;
import com.android.carrentsystem.data.models.RentOrder;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchCarsViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<List<CarCategory>> retrieveCarCategoriesLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<List<Car>> carListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<RentOrder> orderLiveDate = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();

    @Inject
    public SearchCarsViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void retrieveCarCategories() {
        databaseRepository.retrieveCarCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<CarCategory>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<CarCategory> categoryList) {
                        retrieveCarCategoriesLiveData.setValue(categoryList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        retrieveCarCategoriesLiveData.setValue(null);
                    }
                });
    }

    public void retrieveSearchCarResults(String category,
                                         String type,
                                         String model,
                                         String year,
                                         long from,
                                         long to) {
        databaseRepository.retrieveSearchCarResults(category, type, model, year, from , to)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<Car>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Car> carList) {
                        carListLiveData.setValue(carList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        carListLiveData.setValue(null);
                    }
                });
    }

    public void retrieveOrdersByPhone(String phone) {
        databaseRepository.retrieveOrdersByPhone(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<RentOrder>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(RentOrder order) {
                        orderLiveDate.setValue(order);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        orderLiveDate.setValue(null);
                    }
                });
    }

    public MediatorLiveData<List<CarCategory>> observeRetrieveCarCategoriesLiveData() {
        return retrieveCarCategoriesLiveData;
    }

    public MediatorLiveData<List<Car>> observeCarSearchResultLiveData() {
        return carListLiveData;
    }

    public MediatorLiveData<RentOrder> observeOrderLiveDate() {
        return orderLiveDate;
    }

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }
}
