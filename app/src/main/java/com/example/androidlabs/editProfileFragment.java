package com.example.androidlabs;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.androidlabs.MainActivity.currentUser;


public class editProfileFragment extends Fragment implements View.OnClickListener {

    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private ImageView userPhotoImageView;

    private MainActivity homeActivity;
    private boolean isNewPhotoUploaded = false;

    private OnFragmentInteractionListener mListener;

    public editProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        homeActivity = (MainActivity) getActivity();

        userEmailEditText = view.findViewById(R.id.emailEditTextView);
        userPasswordEditText = view.findViewById(R.id.passwordEditTextView);
        userPhotoImageView = view.findViewById(R.id.userPhotoEditImageView);

        userEmailEditText.setText(currentUser.email);
        userPhotoImageView.setImageBitmap(MainActivity.currentUser.loadImageFromStorage());

        Button updateButton = view.findViewById(R.id.updateProfileButton);
        updateButton.setOnClickListener(this);
        userPhotoImageView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStop() {
        if (isNewPhotoUploaded || !currentUser.email.equals(userEmailEditText.getText().toString()))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
            builder.setTitle("Save changes?")
                    .setCancelable(false)
                    .setPositiveButton("Save",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    updateUserProfile();
                                    int currentDestinationId = mListener.getExcessDestinationId();
                                    mListener.popFromStackExcessDestination();
                                    mListener.navigateAfterSavingChanges(currentDestinationId);
                                }
                            })
                    .setNegativeButton(
                            "discard",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();
        }
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateProfileButton:
                updateUserProfile();
                break;
            case R.id.userPhotoEditImageView:
                showPictureDialog();
                break;
        }
    }

    private void showPictureDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(homeActivity);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dispatchTakePhotoFromGalleryIntent();
                                break;
                            case 1:
                                dispatchTakePhotoFromCameraIntent();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    public interface OnFragmentInteractionListener {
        void popFromStackExcessDestination();
        void navigateAfterSavingChanges(int nextDestinationId);
        int getExcessDestinationId();
        void updateUser();
    }

    private static final int REQUEST_IMAGE_PHOTO = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private void dispatchTakePhotoFromCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_PHOTO);
    }

    private void dispatchTakePhotoFromGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_IMAGE_PHOTO) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            isNewPhotoUploaded = true;
            userPhotoImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            isNewPhotoUploaded = true;
            userPhotoImageView.setImageURI(selectedImage);
        }
    }

    private void updateUserProfile() {

        String userEmail = userEmailEditText.getText().toString();
        String userPassword = userPasswordEditText.getText().toString();
        String pathToPhoto = saveToInternalStorage(((BitmapDrawable) userPhotoImageView.getDrawable()).getBitmap());

        currentUser.email = userEmail;
        currentUser.pathToPhoto = pathToPhoto;

        mListener.updateUser();
        Toast.makeText(homeActivity.getApplicationContext(), "Changes are saved", Toast.LENGTH_SHORT).show();
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(homeActivity.getApplicationContext());

        File directory = cw.getDir("profilePhotosDirectory", Context.MODE_PRIVATE);

        File myPath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


}
