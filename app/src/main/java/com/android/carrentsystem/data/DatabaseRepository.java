package com.android.carrentsystem.data;

import android.net.Uri;

import com.android.carrentsystem.data.models.Agency;
import com.android.carrentsystem.data.models.Car;
import com.android.carrentsystem.data.models.CarCategory;
import com.android.carrentsystem.datasource.FirebaseDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
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
                                                        String from,
                                                        String to) {
        return firebaseDataSource.retrieveSearchCarResults(category, type, model, year, from, to);
    }
}
