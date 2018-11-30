package com.example.androidlabs;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.androidlabs.businessLogic.AuthentificationService;
import com.example.androidlabs.businessLogic.UserManagementServicece;
import com.example.androidlabs.dataAccess.entities.User;
import com.example.androidlabs.dataAccess.roomdDb.AppDatabase;
import com.google.android.material.navigation.NavigationView;
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
    private AuthentificationService authService;

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

        AppDatabase appAdditionalInfoDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app_database").allowMainThreadQueries().build();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        authService = new AuthentificationService(this,navController, appAdditionalInfoDatabase);
        userManager = new UserManagementServicece(this, navController, appAdditionalInfoDatabase);


        setupNavigationView();

        if (authService.isUserSignedIn()){
            currentUser = authService.getCurrentUserAsUser();
        }
        else{
            currentUser = null;
            navigateToSignInDestination(R.id.indexFragment);
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
                        navController.navigate(R.id.indexFragment);
                        return true;
                    case R.id.first_menu_item:
                        navController.navigate(R.id.profileFragment);
                        return true;
                     case R.id.create_account_item:
                        navController.navigate(R.id.createProfileFragment);
                        return true;
                    case R.id.logout_menu_item: {
                        authService.logout();
                        currentUser = null;
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

        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

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
    public void updateUser(String uid, String email, String password, String name, String surname,
                           String phoneNumber, Bitmap photo) {
        userManager.updateUser(uid, email, password, name, surname, phoneNumber, photo);
        MainActivity.currentUser = authService.getCurrentUserAsUser();
    }

    @Override
    public void navigateToEditProfile(){
        navController.navigate(R.id.editProfileFragment);
    }

    @Override
    public void navigateToSignInDestination(int nextDestinationId) {
        userManager.nextDestinationId = nextDestinationId;
        navController.navigate(R.id.signInFragment);
    }

    @Override
    public void createUser(
            String email, String password,
            String name, String surname, String phoneNUmber, Bitmap photo,
            int nextDestinationId) {
        userManager.nextDestinationId = nextDestinationId;
        userManager.createUser(
                email, password,
                name, surname,
                phoneNUmber, photo);
    }

    @Override
    public AlertDialog.Builder showGetPhotoDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"
        };
        return pictureDialog;
    }

    @Override
    public void moveToCreateUserDestination() {
        navController.navigate(R.id.createProfileFragment);
    }

    @Override
    public void signInUser(String email, String password){
        userManager.nextDestinationId = R.id.indexFragment;
        authService.signInUser(email, password);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}