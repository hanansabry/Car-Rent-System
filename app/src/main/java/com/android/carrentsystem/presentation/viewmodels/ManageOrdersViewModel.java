package com.android.carrentsystem.presentation.viewmodels;

import com.android.carrentsystem.data.DatabaseRepository;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.RentOrder;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ManageOrdersViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<List<RentOrder>> orderListLiveDate = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> orderStatusLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();

    @Inject
    public ManageOrdersViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void retrieveAgencyOrder(String agencyId) {
        databaseRepository.retrieveAgencyOrders(agencyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<RentOrder>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<RentOrder> orderList) {
                        orderListLiveDate.setValue(orderList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        orderListLiveDate.setValue(null);
                    }
                });
    }

    public void addStatusToRentOrder(String orderId, String agencyNotes, boolean isConfirmed) {
        SingleObserver<Boolean> singleObserver = databaseRepository.addStatusToRentOrder(orderId, agencyNotes, isConfirmed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        orderStatusLiveData.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void confirmRentOrder(String orderId, String carId) {
        SingleObserver<Boolean> singleObserver = databaseRepository.confirmRentOrder(orderId, carId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        orderStatusLiveData.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

        public MediatorLiveData<List<RentOrder>> observeOrderListLiveDate() {
        return orderListLiveDate;
    }

    public MediatorLiveData<Boolean> observeOrderStatusLiveData() {
        return orderStatusLiveData;
    }

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }
}
