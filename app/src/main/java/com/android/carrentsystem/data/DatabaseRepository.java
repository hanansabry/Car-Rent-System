package com.android.carrentsystem.data;

import android.net.Uri;

import com.android.carrentsystem.data.models.Agency;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.CarCategory;
import com.android.carrentsystem.data.models.Color;
import com.android.carrentsystem.data.models.RentOrder;
import com.android.carrentsystem.datasource.FirebaseDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class DatabaseRepository {

    private final FirebaseDataSource firebaseDataSource;

    @Inject
    public DatabaseRepository(FirebaseDataSource firebaseDataSource) {
        this.firebaseDataSource = firebaseDataSource;
    }

    public Single<Agency> loginAsAgency(String email, String password) {
        return firebaseDataSource.loginAsAgency(email, password);
    }

    public Single<Agency> registerNewAgency(Agency agency) {
        return firebaseDataSource.registerNewAgency(agency);
    }

    public Flowable<List<CarCategory>> retrieveCarCategories() {
        return firebaseDataSource.retrieveCarCategories();
    }

    public Single<String> uploadImageToFirebaseStorage(Uri imageUri, String folderName) {
        return firebaseDataSource.uploadImageToFirebaseStorage(imageUri, folderName);
    }

    public Single<Boolean> addNewCar(String agencyId, Car car) {
        return firebaseDataSource.addNewCar(agencyId, car);
    }

    public Flowable<List<Car>> retrieveSearchCarResults(String category,
                                                        String type,
                                                        String model,
                                                        String year,
                                                        long from,
                                                        long to) {
        return firebaseDataSource.retrieveSearchCarResults(category, type, model, year, from, to);
    }

    public Flowable<List<Color>> retrieveColors() {
        return firebaseDataSource.retrieveColors();
    }

    public Single<Boolean> addNewRentOrder(RentOrder order) {
        return firebaseDataSource.addNewOrder(order);
    }

    public Flowable<List<RentOrder>> retrieveAgencyOrders(String agencyId) {
        return firebaseDataSource.retrieveAgencyOrders(agencyId);
    }

    public Flowable<RentOrder> retrieveOrdersByPhone(String phone) {
        return firebaseDataSource.retrieveOrdersByPhone(phone);
    }

    public Single<Boolean> addStatusToRentOrder(String orderId, String agencyNotes, boolean isConfirmed) {
        return firebaseDataSource.addStatusToRentOrder(orderId, agencyNotes, isConfirmed);
    }

    public Single<Boolean> confirmRentOrder(String orderId, String carId) {
        return firebaseDataSource.confirmRentOrder(orderId, carId);
    }
}