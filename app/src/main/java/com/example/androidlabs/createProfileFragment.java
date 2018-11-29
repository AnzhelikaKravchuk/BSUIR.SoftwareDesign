package com.example.yura.androidlabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import static android.app.Activity.RESULT_OK;

public class createProfileFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private MainActivity mainActivity;

    private EditText  userNameInputText;
    private EditText  userSurnameInputText;
    private EditText  userPhoneNumberInputText;
    private EditText userEmailInputText;
    private EditText userPasswordInputText;
    private ImageView userPhotoCreateImageView;

    public createProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        mainActivity = (MainActivity) getActivity();

        userNameInputText = view.findViewById(R.id.nameCreateTextInput);
        userSurnameInputText = view.findViewById(R.id.surnameCreateTextInput);
        userPhoneNumberInputText = view.findViewById(R.id.phoneNumberCreateTextInput);
        userEmailInputText = view.findViewById(R.id.emailCreateTextInput);
        userPasswordInputText = view.findViewById(R.id.passwordCreateTextInput);
        userPhotoCreateImageView = view.findViewById(R.id.userPhotoCreateImageView);

        Button createUserButton = view.findViewById(R.id.createProfileButton);
        createUserButton.setOnClickListener(this);
        userPhotoCreateImageView.setOnClickListener(this);

        return view;
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
            case R.id.createProfileButton:
                mListener.createUser(
                        userEmailInputText.getText().toString(),
                        userPasswordInputText.getText().toString(),
                        userNameInputText.getText().toString(),
                        userSurnameInputText.getText().toString(),
                        userPhoneNumberInputText.getText().toString(),
                        ((BitmapDrawable) userPhotoCreateImageView.getDrawable()).getBitmap()
                );
                break;
            case R.id.userPhotoCreateImageView:
                showPictureDialog();
                break;
        }
    }

    private void showPictureDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(mainActivity);
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


    private static final int REQUEST_IMAGE_PHOTO = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private void dispatchTakePhotoFromCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_PHOTO);
    }

    private void dispatchTakePhotoFromGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            userPhotoCreateImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            userPhotoCreateImageView.setImageURI(selectedImage);
        }
    }

    public interface OnFragmentInteractionListener {

        void createUser(
            String email, String password,
            String name, String surname,
            String phoneNUmber, Bitmap photo);
    }
}
