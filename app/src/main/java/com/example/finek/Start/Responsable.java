package com.example.finek.Start;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.finek.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;


public class Responsable extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsable);
        drawer();
        FirebaseMessaging.getInstance().subscribeToTopic( "cc");
   }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_responsable_drawer, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
       Intent intent ;
        int id=item.getItemId();
        switch (id){

            case R.id.nav_profile:
                if(!getClass().equals(Profile.class)) {
                    intent = new Intent(this, Profile.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                }
                break;
            case R.id.nav_add_accompagnee:
                if(!getClass().equals(AddAccompagnee.class)) {
                    intent = new Intent(this, AddAccompagnee.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                }
                break;
            case R.id.nav_liste_accompagnees:
                if(!getClass().equals(ListeDesAccompagnees.class)) {
                    intent = new Intent(this, ListeDesAccompagnees.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                }
                break;




            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(), Authentification.class));
                finish();
                break;



        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        //customProgressBar.dismiss();
    }



    public void drawer(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header=navigationView.getHeaderView(0);
        toggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
}










































































































/*package com.example.finek;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.example.finek.ui.addAccompagnee.AddAccompagneeFragment;
import com.example.finek.ui.listeDesAcompagnees.ListeDesAcompagneesFragment;
import com.example.finek.ui.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class Responsable extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Intent intent;

    //FOR FRAGMENTS
    // Declare fragment handled by Navigation Drawer
    private Fragment fragment_profile;
    private Fragment fragment_add_accompagnee;
    private Fragment fragment_liste_accompagnee;

    //FOR DATAS
    // Identify each fragment with a number
    private static final int FRAGMENT_PROFILE = 0;
    private static final int FRAGMENT_ADD_ACCOMPAGNEE = 1;
    private static final int FRAGMENT_LISTE_ACCOMPAGNEES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsable);

        // Configure all views

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        // Show First Fragment
        //this.showFirstFragment();

    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.fragment_profile:
                this.showFragment(FRAGMENT_PROFILE);
                break;
            case R.id.fragment_add_accompagnee:
                this.showFragment(FRAGMENT_ADD_ACCOMPAGNEE);
                break;
            case R.id.fragment_liste_accompagnee:
                this.showFragment(FRAGMENT_LISTE_ACCOMPAGNEES);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // Configure Toolbar
    private void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show fragment according an Identifier

    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case FRAGMENT_PROFILE:
                this.showProfileFragment();
                break;
            case FRAGMENT_ADD_ACCOMPAGNEE:
                this.showAddAccompagneeFragment();
                break;
            case FRAGMENT_LISTE_ACCOMPAGNEES:
                this.showListeDesAcompagneesFrgment();
                break;
            default:
                break;
        }
    }
    //  Show first fragment when activity is created
    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_responsable_frame_layout);
        if (visibleFragment == null){
            // Show News Fragment
            this.showFragment(FRAGMENT_PROFILE);
            // Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    // 4 - Create each fragment page and show it

    private void showProfileFragment() {
        intent = new Intent(this, Profile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    private void showAddAccompagneeFragment() {
        intent = new Intent(this, AddAccompagnee.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

        if (this.fragment_add_accompagnee == null)
            this.fragment_add_accompagnee = AddAccompagneeFragment.newInstance();
        this.startTransactionFragment(this.fragment_add_accompagnee);
    }

    private void showListeDesAcompagneesFrgment() {
        intent = new Intent(this, ListeDesAcompagneesFragment.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    // ---

    // 3 - Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_responsable_frame_layout, fragment).commit();
        }
    }
}






*/























/*package com.example.finek;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.example.finek.ui.addAccompagnee.AddAccompagneeFragment;
import com.example.finek.ui.listeDesAcompagnees.ListeDesAcompagneesFragment;
import com.example.finek.ui.profile.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class Responsable extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //FOR FRAGMENTS
    // Declare fragment handled by Navigation Drawer
    private Fragment fragment_profile;
    private Fragment fragment_add_accompagnee;
    private Fragment fragment_liste_accompagnee;

    //FOR DATAS
    // Identify each fragment with a number
    private static final int FRAGMENT_PROFILE = 0;
    private static final int FRAGMENT_ADD_ACCOMPAGNEE = 1;
    private static final int FRAGMENT_LISTE_ACCOMPAGNEES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsable);

        // Configure all views

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        // Show First Fragment
        this.showFirstFragment();

    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.fragment_profile:
                this.showFragment(FRAGMENT_PROFILE);
                break;
            case R.id.fragment_add_accompagnee:
                this.showFragment(FRAGMENT_ADD_ACCOMPAGNEE);
                break;
            case R.id.fragment_liste_accompagnee:
                this.showFragment(FRAGMENT_LISTE_ACCOMPAGNEES);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // Configure Toolbar
    private void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show fragment according an Identifier

    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case FRAGMENT_PROFILE:
                this.showProfileFragment();
                break;
            case FRAGMENT_ADD_ACCOMPAGNEE:
                this.showAddAccompagneeFragment();
                break;
            case FRAGMENT_LISTE_ACCOMPAGNEES:
                this.showListeDesAcompagneesFrgment();
                break;
            default:
                break;
        }
    }
    //  Show first fragment when activity is created
    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_responsable_frame_layout);
        if (visibleFragment == null){
            // Show News Fragment
            this.showFragment(FRAGMENT_PROFILE);
            // Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    // 4 - Create each fragment page and show it

    private void showProfileFragment() {
        if (this.fragment_profile == null) this.fragment_profile = ProfileFragment.newInstance();
        this.startTransactionFragment(this.fragment_profile);
    }

    private void showAddAccompagneeFragment() {
        if (this.fragment_add_accompagnee == null)
            this.fragment_add_accompagnee = AddAccompagneeFragment.newInstance();
        this.startTransactionFragment(this.fragment_add_accompagnee);
    }

    private void showListeDesAcompagneesFrgment() {
        if (this.fragment_liste_accompagnee == null)
            this.fragment_liste_accompagnee = ListeDesAcompagneesFragment.newInstance();
        this.startTransactionFragment(this.fragment_liste_accompagnee);
    }

    // ---

    // 3 - Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_responsable_frame_layout, fragment).commit();
        }
    }
}

*/


















/*{

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responsable);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_add_accompagnee, R.id.nav_list_accompagnees,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.responsable, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
*/