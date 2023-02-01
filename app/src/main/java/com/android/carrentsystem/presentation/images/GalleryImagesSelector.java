package com.android.carrentsystem.presentation.images;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

public class GalleryImagesSelector implements
        GalleryActivityResultLauncher.OnPickGalleryImages {

    private static final int MAX_IMAGES_NUM = 4;
    private ImagesSelector.ImagesSelectorCallback imagesSelectorCallback;
    private Context context;
    private ActivityResultLauncher openGalleryLauncher;
    private int pickedImagesNum;


    public GalleryImagesSelector(Context context) {
        this.context = context;
        openGalleryLauncher = new GalleryActivityResultLauncher((AppCompatActivity) context, this).register();
    }

    public void setImagesSelectorCallback(ImagesSelector.ImagesSelectorCallback imagesSelectorCallback) {
        this.imagesSelectorCallback = imagesSelectorCallback;
    }

    public void openImagesSelector() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        openGalleryLauncher.launch(photoPickerIntent);
    }

    @Override
    public void onPickImages(List<Uri> selectedImages) {
        int i = 1;
        for (Uri selectedImageUri : selectedImages) {
            imagesSelectorCallback.onImageAdded(selectedImageUri, i++);
        }
    }

    @Override
    public void onPickImagesError(String error) {
        imagesSelectorCallback.onImageSelectedError(error);
    }
}
