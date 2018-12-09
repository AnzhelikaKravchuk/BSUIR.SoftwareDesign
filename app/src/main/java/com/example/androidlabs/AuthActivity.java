package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androidlabs.businessLogic.UserManagementService;
import com.example.androidlabs.dataAccess.roomdDb.AppDatabase;

import java.io.IOException;

public class AuthActivity extends AppCompatActivity implements
        CreateProfileFragment.OnFragmentInteractionListener,
        SignInFragment.OnFragmentInteractionListener  {

    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private UserManagementService userManager;
    //private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppDatabase appAdditionalInfoDatabase = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "app_database"
        ).allowMainThreadQueries().build();

        userManager = new UserManagementService(appAdditionalInfoDatabase);
        //navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        if (userManager.isUserSignedIn())
        {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.my_nav_host_fragment, new SignInFragment()).commit();


    }

    @Override
    public void createUser(
            String email, String password,
            String name, String surname, String phoneNUmber, String pathToPhoto
    ) {
        userManager.setOnCreationResultListener(new UserManagementService.OnCreationResultListener() {
            @Override
            public void onCreationResultSuccess() {
                //tearDownProgressBar(R.id.signInProgressBar);
                Toast.makeText(
                        getApplicationContext(),
                        "You are welcome.",
                        Toast.LENGTH_SHORT
                ).show();
                Intent mainActivityIntent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }

            @Override
            public void onCreationResultFailed(String exceptionText) {

                Toast.makeText(
                        getApplicationContext(),
                        "Creation failed." + exceptionText,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        userManager.createUser(
                email, password,
                name, surname,
                phoneNUmber, pathToPhoto);
        //hideKeyboard();
        //setupImage();
    }

    @Override
    public String saveProfilePhoto(Bitmap photo, String email) throws IOException {
        return ImageHelper.saveToInternalStorage(photo, email, getApplicationContext());
    }



    @Override
    public void moveToCreateUserDestination() {
        //navController.navigate(R.id.CreateProfileFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.my_nav_host_fragment, new CreateProfileFragment()).commit();

    }

    @Override
    public void signInUser(String email, String password){

        userManager.setOnAuthenticateResultListener(new UserManagementService.OnAuthenticateResultListener(){
            @Override
            public void onAuthenticateSuccess() {
                tearDownProgressBar(R.id.signInProgressBar);
                Toast.makeText(
                        getApplicationContext(),
                        "You are authenticated.",
                        Toast.LENGTH_SHORT
                ).show();
                Intent mainActivityIntent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
            }

            @Override
            public void onAuthenticateFailed() {
                tearDownProgressBar(R.id.signInProgressBar);
                Toast.makeText(
                        getApplicationContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                ).show();
                tearDownProgressBar(R.id.editProgressBar);
            }
        });
        setUpProgressBar(R.id.signInProgressBar);
        userManager.signInUser(email, password);
        hideKeyboard();
    }

    private void setUpProgressBar(int progressBarId){
        ProgressBar progressBar = findViewById(progressBarId);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void tearDownProgressBar(int progressBarId){
        ProgressBar progressBar = findViewById(progressBarId);
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
