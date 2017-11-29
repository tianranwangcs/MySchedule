package ca.wlu.hztw.myschedule.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import ca.wlu.hztw.myschedule.R;
import ca.wlu.hztw.myschedule.data.EventRepository;
import ca.wlu.hztw.myschedule.edit.EditActivity;
import ca.wlu.hztw.myschedule.util.ColorManager;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainPresenter presenter;
    private MainListFragment listFragment;

    public final static int EDIT_ACTIVITY = 215;
    public final static String EDIT_PARAM = "edit_param";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar-------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // fab-----------------------------------------------------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ColorManager colorManager = ColorManager.getInstance(getResources());
        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getMuted()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), EditActivity.class);
                intent.putExtra(EDIT_PARAM, -1);
                startActivityForResult(intent, EDIT_ACTIVITY);
            }
        });

        // drawer layout-------------------------------------------------------
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // navigationView------------------------------------------------------
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // navigation header---------------------------------------------------
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("AS", ColorGenerator.MATERIAL.getColor("Android Studio"));
        ImageView avatarImage = navigationView.getHeaderView(0).findViewById(R.id.avatar_image);
        avatarImage.setImageDrawable(drawable);

        // presenter-----------------------------------------------------------
        presenter = new MainPresenter(EventRepository.getInstance());

        // MainListFragment----------------------------------------------------
        listFragment = (MainListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_list_main);
        if (listFragment == null) {
            listFragment = MainListFragment.newInstance(presenter);
        }
        if (findViewById(R.id.fragment_container) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ACTIVITY && resultCode == RESULT_OK) {
            int pos = data.getIntExtra(EDIT_PARAM, -1);
            if (pos >= 0) {
                Snackbar.make(getWindow().getDecorView(), "Updating event success.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(getWindow().getDecorView(), "Adding event success.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    // show icon and text together in menu
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
