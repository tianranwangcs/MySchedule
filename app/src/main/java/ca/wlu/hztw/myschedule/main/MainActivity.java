package ca.wlu.hztw.myschedule.main;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.wlu.hztw.myschedule.R;
import ca.wlu.hztw.myschedule.data.EventRepository;
import ca.wlu.hztw.myschedule.event.EventActivity;
import ca.wlu.hztw.myschedule.limit.LimitActivity;
import ca.wlu.hztw.myschedule.login.LoginActivity;
import ca.wlu.hztw.myschedule.util.ColorManager;
import com.amulyakhare.textdrawable.TextDrawable;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainPresenter presenter;
    private EventListFragment listFragment;
    private SharedPreferences userInfo;

    public final static int EDIT_ACTIVITY = 215;
    public final static int LIMIT_ACTIVITY = 920;
    public final static String EDIT_PARAM = "edit_param";
    public final static String PREFS_NAME = "prefs_name";

    public static boolean isLoggedIn;
    public static int id;
    public static String name;
    public static String email;
    public static int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load user info or turn to login-------------------------------------
        userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isLoggedIn = userInfo.getBoolean("isLoggedIn", false);
        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        id = userInfo.getInt("id", 0);
        name = userInfo.getString("name", "");
        email = userInfo.getString("email", "");
        type = userInfo.getInt("type", 0);

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
                if (type == 1) {
                    Intent intent = new Intent(getApplication(), LimitActivity.class);
                    startActivityForResult(intent, LIMIT_ACTIVITY);
                } else {
                    Intent intent = new Intent(getApplication(), EventActivity.class);
                    intent.putExtra(EDIT_PARAM, -1);
                    startActivityForResult(intent, EDIT_ACTIVITY);
                }
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
        LinearLayout linearLayout = navigationView.getHeaderView(0).findViewById(R.id.header_layout);
        linearLayout.setBackgroundTintList(ColorStateList.valueOf(colorManager.getVibrant()));
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(name.substring(0, 1), colorManager.getMuted());
        ImageView headerImage = navigationView.getHeaderView(0).findViewById(R.id.header_image);
        headerImage.setImageDrawable(drawable);
        TextView headerName = navigationView.getHeaderView(0).findViewById(R.id.header_name);
        headerName.setText(name);
        TextView headerEmail = navigationView.getHeaderView(0).findViewById(R.id.header_email);
        headerEmail.setText(email);

        // presenter-----------------------------------------------------------
        presenter = new MainPresenter(EventRepository.getInstance());

        // EventListFragment----------------------------------------------------
        listFragment = (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_event_list);
        if (listFragment == null) {
            listFragment = EventListFragment.newInstance(presenter);
        }
        if (findViewById(R.id.fragment_container) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        } else if (requestCode == LIMIT_ACTIVITY && resultCode == RESULT_OK) {
            // TODO
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

        } else if (id == R.id.nav_logout) {
            userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = userInfo.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.remove("id");
            editor.remove("name");
            editor.remove("email");
            editor.remove("type");
            editor.commit();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
