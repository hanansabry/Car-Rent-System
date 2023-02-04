package com.android.carrentsystem.presentation.viewmodels;

import com.android.carrentsystem.data.DatabaseRepository;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.Violation;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PoliceTrafficViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<List<Car>> carListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> violationAddStatusLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();

    @Inject
    public PoliceTrafficViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void retrieveAllCars() {
        databaseRepository.retrieveAllCars()
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

    public void addNewViolation(String carId, Violation violation) {
        SingleObserver<Boolean> singleObserver = databaseRepository.addNewViolation(carId, violation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        violationAddStatusLiveData.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MediatorLiveData<List<Car>> observeCarListLiveData() {
        return carListLiveData;
    }

    public MediatorLiveData<Boolean> observeViolationAddStatusLiveData() {
        return violationAddStatusLiveData;
    }

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }

}
