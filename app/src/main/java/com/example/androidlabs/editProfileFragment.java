package com.example.yura.androidlabs;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.example.yura.androidlabs.MainActivity.currentUser;


public class editProfileFragment extends Fragment implements View.OnClickListener {

    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private ImageView userPhotoImageView;

    private FragmentActivity homeActivity;

    private OnFragmentInteractionListener mListener;

    public editProfileFragment() {
        // Required empty public constructor
    }

    public static editProfileFragment newInstance(String param1, String param2) {
        editProfileFragment fragment = new editProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        homeActivity = getActivity();

        userEmailEditText = view.findViewById(R.id.emailEditTextView);
        userPasswordEditText = view.findViewById(R.id.passwordEditTextView);
        userPhotoImageView = view.findViewById(R.id.userPhotoEditImageView);

        userEmailEditText.setText(currentUser.email);
        userPasswordEditText.setText(currentUser.password);
        userPhotoImageView.setImageBitmap(loadImageFromStorage(MainActivity.currentUser.pathToPhoto));

        Button updateButton = view.findViewById(R.id.updateProfileButton);
        Button updatePhotoButton = view.findViewById(R.id.updateUserPhotoButton);
        updateButton.setOnClickListener(this);
        updatePhotoButton.setOnClickListener(this);

        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        switch (view.getId()){
            case R.id.updateProfileButton:
                updateUserProfile();
                break;
            case R.id.updateUserPhotoButton:
                dispatchTakePictureIntent();
            break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userPhotoImageView.setImageBitmap(imageBitmap);
        }
    }

    public void updateUserProfile(){

        String userEmail = userEmailEditText.getText().toString();
        String userPassword = userPasswordEditText.getText().toString();
        String pathToPhoto = saveToInternalStorage(((BitmapDrawable)userPhotoImageView.getDrawable()).getBitmap());


        currentUser.email = userEmail;
        currentUser.password = userPassword;
        currentUser.pathToPhoto = pathToPhoto;

        MainActivity.appDatabase.userDao().updateUser(currentUser);
        Toast.makeText(homeActivity.getApplicationContext(),"Changes are saved", Toast.LENGTH_SHORT).show();
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(homeActivity.getApplicationContext());

        File directory = cw.getDir("profilePhotosDirectory", Context.MODE_PRIVATE);

        File myPath = new File(directory,"profile.jpg");

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

    private Bitmap loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "profile.jpg");
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
