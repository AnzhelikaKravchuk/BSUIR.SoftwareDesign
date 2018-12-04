package com.example.androidlabs;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androidlabs.businessLogic.AuthentificationService;
import com.example.androidlabs.businessLogic.UserManagementServicece;
import com.example.androidlabs.dataAccess.entities.User;
import com.example.androidlabs.dataAccess.roomdDb.AppDatabase;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity implements
        IndexFragment.OnFragmentInteractionListener, profileFragment.OnFragmentInteractionListener,
        editProfileFragment.OnFragmentInteractionListener, createProfileFragment.OnFragmentInteractionListener,
        signInFragment.OnFragmentInteractionListener
{

    private DrawerLayout mDrawerLayout;
    private NavController navController;

    public static User currentUser;

    private UserManagementServicece userManager;

    public void updateCurrentUser(User newUser) {
        currentUser = newUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        Objects.requireNonNull(actionbar).setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        AppDatabase appAdditionalInfoDatabase = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "app_database"
        ).allowMainThreadQueries().build();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        userManager = new UserManagementServicece(appAdditionalInfoDatabase);

        setupNavigationView();

        if (userManager.isUserSignedIn()){
            currentUser = userManager.getCurrentUser();
        }
        else{
            currentUser = null;
            navigateToSignInDestination(R.id.signInFragment);
        }
        setupImage();
    }

    public void setupImage() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        ImageView imageView = navigationView.findViewById(R.id.userImage);
        if (userManager.getCurrentUser() != null)
        {
            String path = userManager.getCurrentUser().pathToPhoto;
            File imgFile = new  File(path);

            if(imgFile.exists() && imageView != null){
                imageView.setImageBitmap(ImageHelper.loadImageFromStorage(path));
            }
        }

        else {
            if(imageView != null)
                imageView.setImageResource(R.mipmap.default_profile_photo_round);
        }
    }

    public void setupNavigationView(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.index_menu_item:
                        setupImage();
                        navController.navigate(R.id.indexFragment);
                        return true;
                    case R.id.About:
                        if (currentUser != null) {
                            setupImage();
                            navController.navigate(R.id.About);
                        }
                        else navController.navigate(R.id.signInFragment);
                        return true;
                    case R.id.first_menu_item:
                        if (currentUser != null) {
                            setupImage();
                            navController.navigate(R.id.profileFragment);
                        }
                        else navController.navigate(R.id.signInFragment);
                        return true;
                    case R.id.logout_menu_item: {
                        userManager.logout();
                        currentUser = null;
                        setupImage();
                        navigateToSignInDestination(R.id.indexFragment);
                        return true;
                    }
                }
                return false;
            }
        };
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setupImage();
        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void updateUser(String uid, String email, String password, String name, String surname,
                           String phoneNumber, String pathToPhoto, String currentPassword) {
        userManager.setOnUpdateResultListener(new UserManagementServicece.OnUpdateResultListener() {
            @Override
            public void OnUpdateResultSuccess() {

                Toast.makeText(getApplicationContext(), "Updated successfully",
                        Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.profileFragment);
            }

            @Override
            public void onUpdateFailed(String failedTextException) {

                Toast.makeText(
                        getApplicationContext(),
                        failedTextException,
                        Toast.LENGTH_SHORT).show();
            }

        });
        userManager.setOnConfirmResultListener(new UserManagementServicece.OnConfirmResultListener() {
            @Override
            public void onConfirmFailed(String exceptionText) {
                Toast.makeText(
                        getApplicationContext(),
                        exceptionText,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        userManager.updateUser(uid, email, password, name, surname, phoneNumber, pathToPhoto, currentPassword);
        hideKeyboard();
        setupImage();
    }

    @Override
    public void navigateToEditProfile(){
        navController.navigate(R.id.editProfileFragment);
    }

    @Override
    public void navigateToSignInDestination(int nextDestinationId) {
        navController.navigate(R.id.signInFragment);
    }

    @Override
    public void createUser(
            String email, String password,
            String name, String surname, String phoneNUmber, String pathToPhoto
    ) {
        userManager.setOnCreationResultListener(new UserManagementServicece.OnCreationResultListener() {
            @Override
            public void onCreationResultSuccess() {
                //tearDownProgressBar(R.id.signInProgressBar);
                Toast.makeText(
                        getApplicationContext(),
                        "You are welcome.",
                        Toast.LENGTH_SHORT
                ).show();
                navController.navigate(R.id.indexFragment);
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
        hideKeyboard();
        setupImage();
    }

    @Override
    public String saveProfilePhoto(Bitmap photo) {
        return ImageHelper.saveToInternalStorage(photo, getApplicationContext());
    }

    @Override
    public Bitmap uploadProfilePhoto(String pathToPhoto){
        return ImageHelper.loadImageFromStorage(pathToPhoto);
    }

    @Override
    public void moveToCreateUserDestination() {
        navController.navigate(R.id.createProfileFragment);
    }

    @Override
    public void signInUser(String email, String password){
        userManager.setOnAuthenticateResultListener(new UserManagementServicece.OnAuthenticateResultListener(){
            @Override
            public void onAuthenticateSuccess() {
                tearDownProgressBar(R.id.signInProgressBar);
                Toast.makeText(
                        getApplicationContext(),
                        "You are authenticated.",
                        Toast.LENGTH_SHORT
                ).show();

                updateCurrentUser(userManager.getCurrentUser());

                navController.navigate(R.id.indexFragment);
            }

            @Override
            public void onAuthenticateFailed() {
                tearDownProgressBar(R.id.signInProgressBar);
                Toast.makeText(
                        getApplicationContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        setUpProgressBar(R.id.signInProgressBar);
        userManager.signInUser(email, password);
        hideKeyboard();
        setupImage();
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

