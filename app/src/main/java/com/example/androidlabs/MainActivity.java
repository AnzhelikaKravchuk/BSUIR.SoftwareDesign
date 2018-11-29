package com.example.androidlabs;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.androidlabs.services.UserService;
import com.example.androidlabs.models.User;
import com.example.androidlabs.roomdDb.AppDatabase;
import com.google.android.material.navigation.NavigationView;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

    private static AppDatabase appRoomDatabase ;
    public static User currentUser;

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
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        appRoomDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app_database").allowMainThreadQueries().build();;

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.index_menu_item:
                        navController.navigate(R.id.indexFragment);
                        return true;
                    case R.id.first_menu_item:
                        navController.navigate(R.id.profileFragment);
                        return true;
                    case R.id.logout_menu_item:
                        UserService.logout();
                        navigateToSignInDestination();
                        return true;
                    case R.id.create_account_item:
                        navController.navigate(R.id.createProfileFragment);
                        return true;
                }
                return false;
            }
        };
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (UserService.isUserSignedIn()) {
            currentUser = UserService.getCurrentUser(appRoomDatabase);
        }
        else{
            navigateToSignInDestination();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            //case R.id.about_menu_item:
             //   navController.navigate(R.id.aboutFragment);
              //  return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void popFromStackExcessDestination(){
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void navigateAfterSavingChanges(int nextDestinationId){
        navController.navigate(nextDestinationId);
    }

    @Override
    public int getExcessDestinationId(){
        return Objects.requireNonNull(navController.getCurrentDestination()).getId();
    }

    @Override
    public void updateUser() {


    }

    @Override
    public void navigateToEditProfile(){
        navController.navigate(R.id.editProfileFragment);
    }

    //@Override
    public void navigateToSignInDestination() {
        navController.navigate(R.id.signInFragment);
    }

    @Override
    public void createUser(String email, String password, String name, String surname, String phoneNUmber, Bitmap photo) {
        UserService.createUser(
                email, password,
                name, surname,
                phoneNUmber, photo,
                this, navController, R.id.indexFragment, appRoomDatabase);
    }

    @Override
    public void moveToCreateUserDestination() {
        navController.navigate(R.id.createProfileFragment);
    }

    @Override
    public void signInUser(String email, String password){
        UserService.signInUser(
                email, password,
                this, navController, R.id.indexFragment, appRoomDatabase
        );
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}