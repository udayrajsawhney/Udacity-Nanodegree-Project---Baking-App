package com.udaysawhney.letsbake;

import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.udaysawhney.letsbake.fragments.StepDetailFragment;
import com.udaysawhney.letsbake.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.udaysawhney.letsbake.model.Constants.*;


public class StepDetailActivity extends AppCompatActivity {

    @BindView(R.id.fragment_step) FrameLayout frameLayout;
    @BindView(R.id.button_step_prev) ImageView prevButton;
    @BindView(R.id.step_indicator) TextView stepIndicator;
    @BindView(R.id.button_step_next) ImageView nextButton;
    @BindView(R.id.tools_step) View stepNavigationView;

    private List<Step> stepList;
    private int activePosition;
    private StepDetailFragment stepDetailFragment;
    private FragmentManager fragmentManager;

    @OnClick(R.id.button_step_prev)
    public void onPrevButtonClick(View view) {
        if (activePosition > 0) {
            updateStep(activePosition - 1, false, false);
        }
    }

    @OnClick(R.id.button_step_next)
    public void onNextButtonClick(View view) {
        if (activePosition < (stepList.size() - 1)) {
            updateStep(activePosition + 1, false, false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        applyConfiguration(savedInstanceState);
    }

    private void applyConfiguration(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        activePosition = 0;
        stepList = new ArrayList<>();
        if (frameLayout != null) {
            if (savedInstanceState == null) {
                loadDataFromExtras();
            } else {
                loadDataFromSavedInstance(savedInstanceState);
            }
        }
        updatePrevNextButtonStatus();
        updateStep(activePosition, false, true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadDataFromExtras() {
        if (getIntent().getExtras() != null) {
            List<Step> tempList = getIntent().getExtras().getParcelableArrayList(INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST);
            if (tempList != null) {
                stepList = tempList;
            }
            activePosition = getIntent().getExtras().getInt(INTENT_EXTRA_NAME_STEP_DETAILS_INDEX);
        }
        openStepDetailFragment(isFullScreen());
    }

    private void loadDataFromSavedInstance(Bundle savedInstanceState) {
        List<Step> tempList = savedInstanceState.getParcelableArrayList(INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST);
        if (tempList != null) {
            stepList = tempList;
        }
        activePosition = savedInstanceState.getInt(INTENT_EXTRA_NAME_STEP_DETAILS_INDEX);
        stepDetailFragment = (StepDetailFragment) fragmentManager.findFragmentById(
                frameLayout.getId());
        stepDetailFragment.setFullScreen(isFullScreen());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(INTENT_EXTRA_NAME_STEP_DETAILS_STEP_LIST, new ArrayList<Parcelable>(stepList));
        outState.putInt(INTENT_EXTRA_NAME_STEP_DETAILS_INDEX, activePosition);
        super.onSaveInstanceState(outState);
    }

    private void updateLayoutFullscreenMode(boolean fullScreen) {
        if (fullScreen) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
        }
        stepNavigationView.setVisibility(fullScreen ? View.GONE : View.VISIBLE);
    }

    private void updatePrevNextButtonStatus() {
        boolean prevStatus = activePosition > 0;
        prevButton.setClickable(prevStatus);
        prevButton.setVisibility(prevStatus ? View.VISIBLE : View.INVISIBLE);
        boolean nextStatus = activePosition < (stepList.size() - 1);
        nextButton.setClickable(nextStatus);
        nextButton.setVisibility(nextStatus ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean isFullScreen() {
//        boolean fullScreen = (!getResources().getBoolean(R.bool.isTablet)
        return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                && !TextUtils.isEmpty(stepList.get(activePosition).getVideoUrl());
    }

    private void updateStep(int currentPosition, boolean forceExitFullScreen, boolean fromStart) {
        activePosition = currentPosition;
        stepIndicator.setText(String.format("%s/%s", activePosition, (stepList.size() - 1)));
        setTitle(stepList.get(activePosition).getShortDescription());
        updatePrevNextButtonStatus();
        boolean fullScreen = !forceExitFullScreen && isFullScreen();
        if (!fromStart) {
            openStepDetailFragment(fullScreen);
        }
        if (!TextUtils.isEmpty(stepList.get(activePosition).getVideoUrl())) {
            updateLayoutFullscreenMode(fullScreen);
        }
    }

    private void openStepDetailFragment(boolean fullScreen) {
        Bundle args = new Bundle();
        args.putParcelable(STEP_DETAILS_FRAGMENT_ARGUMENT, stepList.get(activePosition));
        args.putBoolean(STEP_DETAILS_FRAGMENT_FULLSCREEN_ARGUMENT, fullScreen);
        args.putLong(STEP_DETAILS_FRAGMENT_VIDEO_POSITION_ARGUMENT, C.TIME_UNSET);
        stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(frameLayout.getId(), stepDetailFragment)
                .commit();
    }

}
