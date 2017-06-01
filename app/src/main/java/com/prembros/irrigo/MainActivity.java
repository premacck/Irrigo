package com.prembros.irrigo;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentWaterRequirement.OnWaterRequirementInteractionListener,
        FragmentSprinkler.OnSprinklerInteractionListener, FragmentLateral.OnLateralInteractionListener,
        FragmentSubmain.OnSubmainInteractionListener, FragmentMainLine.OnMainLineInteractionListener,
        FragmentLayout.OnLayoutInteractionListener, FragmentPump.OnPumpInteractionListener,
        FragmentQuotation.OnQuotationInteractionListener, FragmentBenefitCost.OnBenefitCostInteractionListener {

    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private FloatingActionButton fabStart;
    private FloatingActionButton fabNext;
    private FloatingActionButton fabPrevious;

    private String cropName;
    private String typeOfSprinkler;
    private int areaOfField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setScrimColor(Color.TRANSPARENT);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
                                .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                                .add(R.id.fragment_container, FragmentWaterRequirement.newInstance("Onion"), "waterRequirement")
                                .commit();

                    }
                }, 200);
            }
        });

    }

    void loadNextFragment() {
        switch (fragmentManager.findFragmentById(R.id.fragment_container).getTag()) {
            case "waterRequirement":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentSprinkler.newInstance(), "sprinkler")
                        .commit();
                fabPrevious.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_appear));
                fabPrevious.setVisibility(View.VISIBLE);
                break;
            case "sprinkler":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentLateral.newInstance(cropName, typeOfSprinkler, areaOfField), "lateral")
                        .commit();
                break;
            case "lateral":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentSubmain.newInstance(cropName, typeOfSprinkler, areaOfField), "submain")
                        .commit();
                break;
            case "submain":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentMainLine.newInstance(cropName, typeOfSprinkler, areaOfField), "mainLine")
                        .commit();
                break;
            case "mainLine":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentLayout.newInstance(), "layout")
                        .commit();
                break;
            case "layout":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentPump.newInstance(cropName, areaOfField), "pump")
                        .commit();
                break;
            case "pump":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentQuotation.newInstance(), "quotation")
                        .commit();
                break;
            case "quotation":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentBenefitCost.newInstance(), "benefitCost")
                        .commit();
                fabNext.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_disappear));
                fabNext.setVisibility(View.INVISIBLE);
                break;
            case "benefitCost":
                break;
            default:
                break;
        }
    }

    void loadPreviousFragment() {
        switch (fragmentManager.findFragmentById(R.id.fragment_container).getTag()) {
            case "waterRequirement":
                fabNext.setVisibility(View.INVISIBLE);
                fabStart.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction()
                        .remove(fragmentManager.findFragmentByTag("waterRequirement"))
                        .commit();
                fabStart.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_appear));
                fabNext.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_disappear));
                break;
            case "sprinkler":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentWaterRequirement.newInstance(cropName), "waterRequirement")
                        .commit();
                fabPrevious.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_disappear));
                fabPrevious.setVisibility(View.INVISIBLE);
                break;
            case "lateral":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentSprinkler.newInstance(), "sprinkler")
                        .commit();
                break;
            case "submain":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentLateral.newInstance(cropName, typeOfSprinkler, areaOfField), "lateral")
                        .commit();
                break;
            case "mainLine":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentSubmain.newInstance(cropName, typeOfSprinkler, areaOfField), "submain")
                        .commit();
                break;
            case "layout":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentMainLine.newInstance(cropName, typeOfSprinkler, areaOfField), "mainLine")
                        .commit();
                break;
            case "pump":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentLayout.newInstance(), "layout")
                        .commit();
                break;
            case "quotation":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentPump.newInstance(cropName, areaOfField), "pump")
                        .commit();
                break;
            case "benefitCost":
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.sink_down)
                        .replace(R.id.fragment_container, FragmentQuotation.newInstance(), "quotation")
                        .commit();
                fabNext.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_appear));
                fabNext.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
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

    void addFragmentIfNotAdded(String tag) {
        if (!fragmentManager.findFragmentByTag(tag).isAdded()) {
            switch (tag) {
                case "waterRequirement":
                    break;
                case "sprinkler":
                    break;
                case "lateral":
                    break;
                case "submain":
                    break;
                case "mainLine":
                    break;
                case "layout":
                    break;
                case "pump":
                    break;
                case "quotation":
                    break;
                case "benefitCost":
                    break;
                default:
                    break;
            }
        }
    }

    void removeFragmentIfAdded(String tag) {
        if (fragmentManager.findFragmentByTag(tag).isAdded()) {
            fragmentManager.beginTransaction()
                    .remove(fragmentManager.findFragmentByTag(tag))
                    .commit();
        }
    }

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

    @Override
    public void onWaterRequirementInteraction(View[] views) {
        if (views[1] != null && views[2] != null && views[3] != null) {
            switch (views[0].getId()) {
                case R.id.radio_select_area_auto:
                    if (!views[1].isEnabled()) {
                        views[1].setEnabled(true);
                        views[2].setEnabled(false);
                        views[3].setEnabled(false);
                    }
                    break;
                case R.id.radio_select_area_manual:
                    if (views[1].isEnabled()) {
                        views[1].setEnabled(false);
                        views[2].setEnabled(true);
                        views[3].setEnabled(true);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSprinklerInteraction(Uri uri) {

    }

    @Override
    public void onLateralFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSubmainInteraction(Uri uri) {

    }

    @Override
    public void onMainLineInteraction(Uri uri) {

    }

    @Override
    public void onLayoutInteraction(Uri uri) {

    }

    @Override
    public void onPumpInteraction(Uri uri) {

    }

    @Override
    public void onQuotationInteraction(Uri uri) {

    }

    @Override
    public void onBenefitCostInteraction(Uri uri) {

    }
}
