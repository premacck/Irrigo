package com.prembros.irrigo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import static com.prembros.irrigo.FragmentCropSelection.cropName;
import static com.prembros.irrigo.FragmentCropSelection.fieldArea;
import static com.prembros.irrigo.FragmentCropSelection.totalWaterRequirement;
import static com.prembros.irrigo.FragmentSprinkler.selectedSprinkler;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private FloatingActionButton fabStart;
    private FloatingActionButton fabNext;
    private FloatingActionButton fabPrevious;

    protected static boolean nextValid;
//    private String cropName;
//    private String selectedSprinkler;
//    private int fieldArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new InsertInDatabase(getApplicationContext(), this).execute();                              /*WILL ONLY COUNT ON FIRST LAUNCH*/

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();

        drawer = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        drawer.setScrimColor(Color.TRANSPARENT);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fabNext = (FloatingActionButton) this.findViewById(R.id.fab_next);
        fabPrevious = (FloatingActionButton) this.findViewById(R.id.fab_previous);
        fabStart = (FloatingActionButton) this.findViewById(R.id.fab_start);

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextFragment();
            }
        });

        fabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPreviousFragment();
            }
        });

        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabStart.setVisibility(View.INVISIBLE);
                fabStart.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_disappear));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fabNext.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_appear));
                        fabNext.setVisibility(View.VISIBLE);
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.float_down, R.anim.sink_down, R.anim.float_up, R.anim.sink_up)
                                .add(R.id.fragment_container, FragmentCropSelection.newInstance("Onion"), "waterRequirement")
                                .commit();

                    }
                }, 200);
            }
        });

    }

    void scrollToTop() {
        ((NestedScrollView) this.findViewById( R.id.scrollView)).fullScroll(View.FOCUS_UP);
    }

    void loadNextFragment() {
//        if (nextValid) {
            switch (fragmentManager.findFragmentById(R.id.fragment_container).getTag()) {
                case "waterRequirement":
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.float_down, R.anim.sink_down, R.anim.float_up, R.anim.sink_up)
                            .replace(R.id.fragment_container, FragmentSprinkler.newInstance(totalWaterRequirement, fieldArea), "sprinkler")
                            .addToBackStack("sprinkler")
                            .commit();
                    fabPrevious.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_appear));
                    fabPrevious.setVisibility(View.VISIBLE);
                    break;
                case "sprinkler":
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.float_down, R.anim.sink_down, R.anim.float_up, R.anim.sink_up)
                            .replace(R.id.fragment_container, FragmentLateral.newInstance(cropName, selectedSprinkler, fieldArea), "lateral")
                            .addToBackStack("lateral")
                            .commit();
                    break;
                case "lateral":
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.float_down, R.anim.sink_down, R.anim.float_up, R.anim.sink_up)
                            .replace(R.id.fragment_container, FragmentSubmain.newInstance(cropName, selectedSprinkler, fieldArea), "submain")
                            .addToBackStack("submain")
                            .commit();
                    break;
                case "submain":
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.float_down, R.anim.sink_down, R.anim.float_up, R.anim.sink_up)
                            .replace(R.id.fragment_container, FragmentMainLine.newInstance(cropName, selectedSprinkler, fieldArea), "mainLine")
                            .addToBackStack("mainLine")
                            .commit();
                    break;
                case "mainLine":
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.float_down, R.anim.sink_down, R.anim.float_up, R.anim.sink_up)
                            .replace(R.id.fragment_container, FragmentPump.newInstance(cropName, fieldArea), "pump")
                            .addToBackStack("pump")
                            .commit();
                    break;
                case "pump":
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.float_down, R.anim.sink_down, R.anim.float_up, R.anim.sink_up)
                            .replace(R.id.fragment_container, FragmentLayout.newInstance(), "layout")
                            .addToBackStack("layout")
                            .commit();
                    break;
                case "layout":
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.float_down, R.anim.sink_down, R.anim.float_up, R.anim.sink_up)
                            .replace(R.id.fragment_container, FragmentQuotation.newInstance(), "quotation")
                            .addToBackStack("quotation")
                            .commit();
                    break;
                case "quotation":
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.float_down, R.anim.sink_down, R.anim.float_up, R.anim.sink_up)
                            .replace(R.id.fragment_container, FragmentBenefitCost.newInstance(), "benefitCost")
                            .addToBackStack("quotation")
                            .commit();
                    fabNext.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_disappear));
                    fabNext.setVisibility(View.INVISIBLE);
                    break;
                case "benefitCost":
                    break;
                default:
                    break;
            }
        scrollToTop();
//        } else {
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setMessage(R.string.alert_body)
//                    .setTitle(R.string.alert)
//                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    })
//                    .show();
//        }
    }

    void loadPreviousFragment() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else if (fragmentManager.getBackStackEntryCount() == 1) {
            fragmentManager.popBackStack();
            fabPrevious.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_disappear));
            fabPrevious.setVisibility(View.INVISIBLE);
        } else {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(getString(R.string.app_name));
            fabNext.setVisibility(View.INVISIBLE);
            fabStart.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction()
                    .remove(fragmentManager.findFragmentByTag("waterRequirement"))
                    .commit();
            fabStart.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_appear));
            fabNext.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_disappear));
        }
        scrollToTop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_water_requirement:
                break;
            case R.id.nav_sprinkler:
                break;
            case R.id.nav_lateral:
                break;
            case R.id.nav_submain:
                break;
            case R.id.nav_main_line:
                break;
            case R.id.nav_layout:
                break;
            case R.id.nav_pump:
                break;
            case R.id.nav_quotation:
                break;
            case R.id.nav_benefit_cost:
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    void addFragmentIfNotAdded(String tag) {
//        if (!fragmentManager.findFragmentByTag(tag).isAdded()) {
//            switch (tag) {
//                case "waterRequirement":
//                    break;
//                case "sprinkler":
//                    break;
//                case "lateral":
//                    break;
//                case "submain":
//                    break;
//                case "mainLine":
//                    break;
//                case "layout":
//                    break;
//                case "pump":
//                    break;
//                case "quotation":
//                    break;
//                case "benefitCost":
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

//    void removeFragmentIfAdded(String tag) {
//        if (fragmentManager.findFragmentByTag(tag).isAdded()) {
//            fragmentManager.beginTransaction()
//                    .remove(fragmentManager.findFragmentByTag(tag))
//                    .commit();
//        }
//    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!(fabStart.getVisibility() == View.VISIBLE)) {
            loadPreviousFragment();
        }
        else {
            super.onBackPressed();
        }
    }

//    @Override
//    public void onCropSelectionInteraction(long totalWaterRequirement, long area) {
//    }
//
//    @Override
//    public void onSprinklerInteraction(Uri uri) {
//
//    }
//
//    @Override
//    public void onLateralFragmentInteraction(Uri uri) {
//
//    }
//
//    @Override
//    public void onSubmainInteraction(Uri uri) {
//
//    }
//
//    @Override
//    public void onMainLineInteraction(Uri uri) {
//
//    }
//
//    @Override
//    public void onLayoutInteraction(Uri uri) {
//
//    }
//
//    @Override
//    public void onPumpInteraction(Uri uri) {
//
//    }
//
//    @Override
//    public void onQuotationInteraction(Uri uri) {
//
//    }
//
//    @Override
//    public void onBenefitCostInteraction(Uri uri) {
//
//    }
}
