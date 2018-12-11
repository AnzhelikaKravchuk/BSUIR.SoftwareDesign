package com.example.androidlabs;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidlabs.businessLogic.CacheManager;
import com.example.androidlabs.businessLogic.UserManagementService;
import com.example.androidlabs.dataAccess.entities.User;
import com.example.androidlabs.dataAccess.roomdDb.AppDatabase;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity implements
        IndexFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        EditProfileFragment.OnFragmentInteractionListener, NewsLoaderFragment.OnFragmentInteractionListener
{

    private DrawerLayout mDrawerLayout;
    private NavController navController;
    private boolean navDataSet;
    private CacheManager cacheManager;
    public static User currentUser;

    private UserManagementService userManager;

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

        userManager = new UserManagementService(appAdditionalInfoDatabase);
        cacheManager = new CacheManager(appAdditionalInfoDatabase);

        setupNavigationView();

        if (userManager.isUserSignedIn()){
            currentUser = userManager.getCurrentUser();
        }
        else{
            currentUser = null;
            navigateToSignInDestination(R.id.signInFragment);
        }
    }

    public void setupNavigationView(){
        NavigationView navigationView = findViewById(R.id.nav_view);

        NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.my_nav_host_fragment);
                Fragment current = Objects.requireNonNull(navHostFragment).getChildFragmentManager().getFragments().get(0);
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.index_menu_item:
                        navController.navigate(R.id.indexFragment);
                        return true;
                    case R.id.About:
                        navController.navigate(R.id.About);
                        return true;
                    case R.id.first_menu_item:
                        navController.navigate(R.id.profileFragment);
                        return true;
                    case R.id.rss_reader_menu_item:
                        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.editProfileFragment
                                && ((EditProfileFragment) current).isUnsavedChanges()) {

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            dialogBuilder.setMessage("You have unstaged changes");
                            dialogBuilder.setCancelable(false);
                            dialogBuilder.setPositiveButton(
                                    "Stay",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    }
                            );
                            dialogBuilder.setNegativeButton(
                                    "Discard",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            navController.navigate(R.id.rssReaderFragment);

                                        }
                                    }
                            );

                            dialogBuilder.show();
                        } else {

                            navController.navigate(R.id.rssReaderFragment);
                        }
                        return true;
                    case R.id.logout_menu_item: {
                        userManager.logout();
                        currentUser = null;
                        Intent mainActivityIntent = new Intent(MainActivity.this, AuthActivity.class);
                        startActivity(mainActivityIntent);
                        finish();
                        return true;
                    }
                }
                return false;
            }
        };



        View navHeader = navigationView.getHeaderView(0);
        ImageView navHeaderUserPhotoImageView = navHeader.findViewById(R.id.userImage);
        navHeaderUserPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userManager.getCurrentUser() != null
                        && navController.getCurrentDestination().getId() != R.id.profileFragment) {
                    navController.navigate(R.id.profileFragment);
                }
                mDrawerLayout.closeDrawers();

            }
        });

        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }


            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Respond when the drawer is closed
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                ImageView navHeaderUserPhoto = findViewById(R.id.userImage);

                if (userManager.getCurrentUser() != null) {
                    TextView email = findViewById(R.id.emailTextView);
                    TextView fullName = findViewById(R.id.fullNameTextView);

                    email.setText(userManager.getCurrentUser().email);
                    fullName.setText(userManager.getCurrentUser().name + " " + userManager.getCurrentUser().surname);
                    navHeaderUserPhoto.setImageBitmap(
                            uploadProfilePhoto(userManager.getCurrentUser().pathToPhoto)
                    );

                } else {
                    navHeaderUserPhoto.setImageResource(R.mipmap.default_profile_photo_round);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //setupImage();
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
                           String phoneNumber, String pathToPhoto, String currentPassword, String link) {
        userManager.setOnUpdateResultListener(new UserManagementService.OnUpdateResultListener() {
            @Override
            public void OnUpdateResultSuccess() {

                Toast.makeText(getApplicationContext(), "Updated successfully",
                        Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.profileFragment);
                tearDownProgressBar(R.id.editProgressBar);
            }

            @Override
            public void onUpdateFailed(String failedTextException) {

                Toast.makeText(
                        getApplicationContext(),
                        failedTextException,
                        Toast.LENGTH_SHORT).show();
                tearDownProgressBar(R.id.editProgressBar);
            }


        });

        setUpProgressBar(R.id.editProgressBar);
        userManager.setOnConfirmResultListener(new UserManagementService.OnConfirmResultListener() {
            @Override
            public void onConfirmFailed(String exceptionText) {
                Toast.makeText(
                        getApplicationContext(),
                        exceptionText,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });


        userManager.updateUser(uid, email, password, name, surname, phoneNumber, pathToPhoto, currentPassword, link);
        hideKeyboard();
    }

    @Override
    public String saveProfilePhoto(Bitmap photo, String email) throws IOException {
        return ImageHelper.saveToInternalStorage(photo, email,getApplicationContext());
    }

    @Override
    public void navigateToEditProfile(){
        navController.navigate(R.id.editProfileFragment);
    }


    @Override
    public void navigateToNewsDetails(NewsLoaderFragmentDirections.ActionRssReaderFragmentToNewsDetails action) {
        navController.navigate(action);
    }

    @Override
    public void navigateToSignInDestination(int nextDestinationId) {
        navController.navigate(R.id.signInFragment);
    }


    @Override
    public Bitmap uploadProfilePhoto(String pathToPhoto){
        return ImageHelper.loadImageFromStorage(pathToPhoto);
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


    @Override
    public void onBackPressed() {

        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.my_nav_host_fragment);
        Fragment current = navHostFragment.getChildFragmentManager().getFragments().get(0);
        if (navController.getCurrentDestination().getId() == R.id.editProfileFragment
                && ((EditProfileFragment) current).isUnsavedChanges()){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage("You have unstaged changes");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton(
                    "Stay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }
            );
            dialogBuilder.setNegativeButton(
                    "Discard",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            navController.navigate(R.id.profileFragment);
                        }
                    }
            );

            dialogBuilder.show();
        } else {

            super.onBackPressed();
        }

    }

}

