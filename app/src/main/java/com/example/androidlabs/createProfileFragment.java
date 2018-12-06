package com.example.androidlabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;


import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
public class createProfileFragment extends androidx.fragment.app.Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
//    private MainActivity mainActivity;

    private EditText userNameInputText;
    private EditText userSurnameInputText;
    private EditText userPhoneNumberInputText;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        setupUserNameInputText((EditText) view.findViewById(R.id.nameCreateTextInput));
        setupUserSurnameInputText((EditText) view.findViewById(R.id.surnameCreateTextInput));
        setupUserPhoneNumberInputText((EditText)view.findViewById(R.id.phoneNumberCreateTextInput));
        setupUserEmailInputText((EditText) view.findViewById(R.id.emailCreateTextInput));
        setupUserPasswordInputText((EditText) view.findViewById(R.id.passwordCreateTextInput));
        userPhotoCreateImageView = view.findViewById(R.id.userPhotoCreateImageView);

        Button createUserButton = view.findViewById(R.id.createProfileButton);
        createUserButton.setOnClickListener(this);
        userPhotoCreateImageView.setOnClickListener(this);



        return view;
    }

    private void setupUserNameInputText(EditText userNameInputTextInitial){
        this.userNameInputText = userNameInputTextInitial;

        this.userNameInputText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userNameInputText.getText().toString().length() < 1) {
                    userNameInputText.setError("Enter valid name");
                } else {
                    userNameInputText.setError(null);
                }
            }
        });
    }

    private void setupUserSurnameInputText(EditText userSurnameInputTextInitial){
        this.userSurnameInputText = userSurnameInputTextInitial;

        this.userSurnameInputText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userSurnameInputText.getText().toString().length() < 1) {
                    userSurnameInputText.setError("Enter valid surname");
                } else {
                    userSurnameInputText.setError(null);
                }
            }
        });
    }

    private void setupUserPhoneNumberInputText(EditText userPhoneNumberInputTextInitial){
        this.userPhoneNumberInputText = userPhoneNumberInputTextInitial;

        this.userPhoneNumberInputText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userPhoneNumberInputText.getText().toString().length() < 5) {
                    userPhoneNumberInputText.setError("Enter valid phone number");
                } else {
                    userPhoneNumberInputText.setError(null);
                }
            }
        });
    }

    private void setupUserEmailInputText(final EditText userEmailInputTextInitial){
        this.userEmailInputText = userEmailInputTextInitial;

        this.userEmailInputText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (!Patterns.EMAIL_ADDRESS.matcher(
                        userEmailInputText.getText().toString()
                ).matches()) {
                    userEmailInputText.setError("Enter valid email");
                } else {
                    userEmailInputText.setError(null);
                }
            }
        });
    }

    private void setupUserPasswordInputText(final EditText userPasswordInputTextInitial){
        this.userPasswordInputText = userPasswordInputTextInitial;
        this.userPasswordInputText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userPasswordInputText.getText().toString().length() < 6) {
                    userPasswordInputText.setError("Enter at least 6 sumbols");
                } else {
                    userPasswordInputText.setError(null);
                }


            }
        });
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

                if (userNameInputText.getText().toString().length() == 0) {
                    userNameInputText.setError("required");
                }
                if (userNameInputText.getError() != null){
                    userNameInputText.requestFocus();
                    return;
                }
                if (userSurnameInputText.getText().toString().length() == 0) {
                    userSurnameInputText.setError("required");
                }
                if (userSurnameInputText.getError() != null){
                    userSurnameInputText.requestFocus();
                    return;
                }
                if (userPhoneNumberInputText.getText().toString().length() == 0) {
                    userPhoneNumberInputText.setError("required");
                }
                if (userPhoneNumberInputText.getError() != null){
                    userPhoneNumberInputText.requestFocus();
                    return;
                }
                if (userEmailInputText.getText().toString().length() == 0) {
                    userEmailInputText.setError("required");
                }
                if (userEmailInputText.getError() != null){
                    userEmailInputText.requestFocus();
                    return;
                }
                if (userPasswordInputText.getText().toString().length() == 0) {
                    userPasswordInputText.setError("required");
                }
                if (userPasswordInputText.getError() != null){
                    userPasswordInputText.requestFocus();
                    return;
                }
                String pathToPhoto = null;
                try {
                    pathToPhoto = mListener.saveProfilePhoto(
                            ((BitmapDrawable) userPhotoCreateImageView.getDrawable()).getBitmap(), userEmailInputText.getText().toString()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mListener.createUser(
                        userEmailInputText.getText().toString(),
                        userPasswordInputText.getText().toString(),
                        userNameInputText.getText().toString(),
                        userSurnameInputText.getText().toString(),
                        userPhoneNumberInputText.getText().toString(),
                        pathToPhoto
                );
                break;
            case R.id.userPhotoCreateImageView:
                showGetPhotoDialog();
                break;
        }
    }

    private void showGetPhotoDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"
        };
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
            Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
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
                String phoneNUmber, String pathToPhoto
        );

        String saveProfilePhoto(Bitmap photo, String email) throws IOException;
    }
}