package com.example.androidlabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import androidx.appcompat.app.AlertDialog;

import com.example.androidlabs.businessLogic.UserManagementService;
import com.example.androidlabs.businessLogic.models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private User currentUser;

    private EditText userNameEditText;
    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private EditText userSurnameEditText;
    private EditText userPhoneNumberEditText;
    private ImageView userPhotoImageView;
    private  EditText userLinkEditText;

    private boolean isNewPhotoUploaded = false;

    private OnFragmentInteractionListener mListener;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        currentUser = UserManagementService.getInstance().getCurrentUser();

        if (currentUser == null) {
            mListener.navigateToSignInDestination(R.id.editProfileFragment);
        } else {

            setupUserEmailEditText((EditText) view.findViewById(R.id.emailEditTextView));
            userEmailEditText.setText(currentUser.email);
            setupUserPasswordEditText((EditText)view.findViewById(R.id.passwordEditTextView));
            userNameEditText = view.findViewById(R.id.nameEditTextInput);
            setupUserNameEditText((EditText)view.findViewById(R.id.nameEditTextInput));
            userNameEditText.setText(currentUser.name);
            setupUserSurnameEditText((EditText)view.findViewById(R.id.surnameEditTextInput));
            userSurnameEditText.setText(currentUser.surname);
            setupUserPhoneNumberEditText((EditText)view.findViewById(R.id.phoneNumberEditTextInput));
            userPhoneNumberEditText.setText(currentUser.phoneNumber);
            userPhotoImageView = view.findViewById(R.id.userPhotoEditImageView);
            userPhotoImageView.setImageBitmap(mListener.uploadProfilePhoto(currentUser.pathToPhoto));

            userLinkEditText = view.findViewById(R.id.linkEditTextView);
            userLinkEditText.setText(currentUser.rssNewsUrl);

            if (savedInstanceState != null && savedInstanceState.containsKey("userPhoto")){
                byte[] byteArray = savedInstanceState.getByteArray("userPhoto");
                Bitmap userPhoto = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                userPhotoImageView.setImageBitmap(userPhoto);
                isNewPhotoUploaded = savedInstanceState.getBoolean("isNewPhotoUploaded");
            } else {
                userPhotoImageView.setImageBitmap(mListener.uploadProfilePhoto(currentUser.pathToPhoto));
            }

            Button updateButton = view.findViewById(R.id.updateProfileButton);
            updateButton.setOnClickListener(this);
            userPhotoImageView.setOnClickListener(this);
        }
        return view;
    }



    private void setupUserNameEditText(EditText userNameEditTextInitial){
        this.userNameEditText = userNameEditTextInitial;

        this.userNameEditText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userNameEditText.getText().toString().length() < 1) {
                    userNameEditText.setError("Enter valid name");
                } else {
                    userNameEditText.setError(null);
                }
            }
        });
    }

    private void setupUserSurnameEditText(EditText userSurnameEditTextInitial){
        this.userSurnameEditText = userSurnameEditTextInitial;

        this.userSurnameEditText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userSurnameEditText.getText().toString().length() < 1) {
                    userSurnameEditText.setError("Enter valid surname");
                } else {
                    userSurnameEditText.setError(null);
                }
            }
        });
    }

    public boolean isUnsavedChanges(){
        User currentUser = UserManagementService.getInstance().getCurrentUser();
        if (!currentUser.email.equals(userEmailEditText.getText().toString()))
            return true;
        if (!userPasswordEditText.getText().toString().isEmpty())
            return true;
        if (!currentUser.name.equals(userNameEditText.getText().toString()))
            return true;
        if (!currentUser.surname.equals(userSurnameEditText.getText().toString()))
            return true;
        if (!currentUser.phoneNumber.equals(userPhoneNumberEditText.getText().toString()))
            return true;
        return isNewPhotoUploaded;
    }


    private void setupUserPhoneNumberEditText(EditText userPhoneNumberEditTextInitial){
        this.userPhoneNumberEditText = userPhoneNumberEditTextInitial;

        this.userPhoneNumberEditText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (userPhoneNumberEditText.getText().toString().length() < 5) {
                    userPhoneNumberEditText.setError("Enter valid phone number");
                } else {
                    userPhoneNumberEditText.setError(null);
                }
            }
        });
    }

    private void setupUserEmailEditText(final EditText userEmailEditTextInitial){
        this.userEmailEditText = userEmailEditTextInitial;

        this.userEmailEditText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (!Patterns.EMAIL_ADDRESS.matcher(
                        userEmailEditText.getText().toString()
                ).matches()) {
                    userEmailEditText.setError("Enter valid email");
                } else {
                    userEmailEditText.setError(null);
                }
            }
        });
    }

    private void setupUserPasswordEditText(final EditText userPasswordEditTextInitial){
        this.userPasswordEditText = userPasswordEditTextInitial;
        this.userPasswordEditText.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                int passwordLength = userPasswordEditText.getText().toString().length();
                if (passwordLength < 6 && passwordLength != 0) {
                    userPasswordEditText.setError("Enter at least 6 sumbols");
                } else {
                    userPasswordEditText.setError(null);
                }
            }
        });
    }

    @Override
    public void onStop() {
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
        // mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateProfileButton:
                try {
                    updateUserProfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.userPhotoEditImageView:
                showPictureDialog();
                break;
        }
    }

    private void showPictureDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
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

        void updateUser(String uid, String email, String password,
                        String name, String surname, String phoneNumber, String pathToPhoto,
                        String currentPassword, String link
        );

        String saveProfilePhoto(Bitmap photo, String email) throws IOException;

        Bitmap uploadProfilePhoto(String pathToPhoto);

        void navigateToSignInDestination(int nextDestinationId);
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
            isNewPhotoUploaded = true;
            userPhotoImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            isNewPhotoUploaded = true;
            userPhotoImageView.setImageURI(selectedImage);
        }
    }

    private void updateUserProfile() throws IOException {

        if (userNameEditText.getText().toString().length() == 0) {
            userNameEditText.setError("required");
        }
        if (userNameEditText.getError() != null){
            userNameEditText.requestFocus();
            return;
        }
        if (userSurnameEditText.getText().toString().length() == 0) {
            userSurnameEditText.setError("required");
        }
        if (userSurnameEditText.getError() != null){
            userSurnameEditText.requestFocus();
            return;
        }
        if (userPhoneNumberEditText.getText().toString().length() == 0) {
            userPhoneNumberEditText.setError("required");
        }
        if (userPhoneNumberEditText.getError() != null){
            userPhoneNumberEditText.requestFocus();
            return;
        }
        if (userEmailEditText.getText().toString().length() == 0) {
            userEmailEditText.setError("required");
        }
        if (userEmailEditText.getError() != null){
            userEmailEditText.requestFocus();
            return;
        }
        if (userPasswordEditText.getError() != null){
            userPasswordEditText.requestFocus();
            return;
        }

        isNewPhotoUploaded = false;

        Bitmap photo = ((BitmapDrawable) userPhotoImageView.getDrawable()).getBitmap();

        final String uid = currentUser.uid;
        final String email = userEmailEditText.getText().toString();
        final String password = userPasswordEditText.getText().toString();
        final String name = userNameEditText.getText().toString();
        final String surname = userSurnameEditText.getText().toString();
        final String phoneNumber = userPhoneNumberEditText.getText().toString();
        final String pathToPhoto = mListener.saveProfilePhoto(photo, email);
        final String editLink = userLinkEditText.getText().toString();

        if (!currentUser.email.equals(email) || !password.isEmpty()){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            alertDialog.setTitle("Dangerous operation need to confirmation");
            alertDialog.setMessage("Current password");

            final EditText input = new EditText(getContext());

            alertDialog.setView(input);
            alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String currentPassword = input.getText().toString();
                    mListener.updateUser(
                            uid, email, password,
                            name, surname, phoneNumber, pathToPhoto,
                            currentPassword, editLink
                    );
                }
            });
            alertDialog.show();
        } else {

            mListener.updateUser(uid, email, password, name, surname, phoneNumber, pathToPhoto, "", editLink);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        if (isNewPhotoUploaded){
            Bitmap photo = ((BitmapDrawable) userPhotoImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] photoByteArray = stream.toByteArray();
            outState.putByteArray("userPhoto", photoByteArray);

            outState.putBoolean("isNewPhotoUploaded", isNewPhotoUploaded);
        }
        super.onSaveInstanceState(outState);
    }

}