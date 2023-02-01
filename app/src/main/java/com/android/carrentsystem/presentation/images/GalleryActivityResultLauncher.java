package com.android.carrentsystem.presentation.images;

import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivityResultLauncher {

    private AppCompatActivity activity;
    private OnPickGalleryImages onPickGalleryImages;
    private List<Uri> selectedImages;

    public GalleryActivityResultLauncher(AppCompatActivity activity, OnPickGalleryImages onPickGalleryImages) {
        this.activity = activity;
        this.onPickGalleryImages = onPickGalleryImages;
    }

    public ActivityResultLauncher register() {
        return activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new OpenGalleryActivityResultCallback(new OpenGalleryActivityResultCallback.OpenGalleryCallback() {
                    @Override
                    public void onSingleImageOrVideoSelected(Uri fileUri) {
                        selectedImages = new ArrayList<>();
                        selectedImages.add(fileUri);
                        onPickGalleryImages.onPickImages(selectedImages);
                    }

                    @Override
                    public void onMultipleImagesSelected(ArrayList<Uri> imagesUris) {
                        selectedImages = imagesUris;
                        onPickGalleryImages.onPickImages(selectedImages);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
                        onPickGalleryImages.onPickImagesError(error);
                    }
                }));
    }

    public interface OnPickGalleryImages {
        void onPickImages(List<Uri> selectedImages);

        void onPickImagesError(String error);
    }
}
