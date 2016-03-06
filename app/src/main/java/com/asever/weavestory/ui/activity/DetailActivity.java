package com.asever.weavestory.ui.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.asever.weavestory.R;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.ui.DonutProgress;
import com.asever.weavestory.ui.fragment.AlbumListFragment;
import com.asever.weavestory.util.CustomAnimatorListener;
import com.asever.weavestory.util.CustomTransitionListener;
import com.asever.weavestory.util.FileManager;
import com.asever.weavestory.util.Utils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by Asever on 2016-01-22.
 */

public class DetailActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION_SHORT = 150;
    private static final int ANIMATION_DURATION_MEDIUM = 300;
    private static final int ANIMATION_DURATION_LONG = 450;
    private static final int ANIMATION_DURATION_EXTRA_LONG = 850;

    private ImageView mFabViewButton;
    private ImageView mFabUploadButton;
    private ImageView mFabRemoveButton;
    private DonutProgress mFabProgress;
    private View mTitleContainer;
    private View mTitlesContainer;

    private Drawable mDrawableView;
    private Drawable mDrawableSuccess;
    private Drawable mDrawableError;

    private int selectPosition;

    private boolean isRemove = false;


    private Animation mProgressFabAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        selectPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_ALBUM_POSITION, 0);

        mDrawableView = new IconicsDrawable(this, FontAwesome.Icon.faw_eye).color(Color.WHITE).sizeDp(24);
        mDrawableSuccess = new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(24);
        mDrawableError = new IconicsDrawable(this, FontAwesome.Icon.faw_exclamation).color(Color.WHITE).sizeDp(24);

        mTitlesContainer = findViewById(R.id.activity_detail_titles);

        // Fab progress
        mFabProgress = (DonutProgress) findViewById(R.id.activity_detail_progress);
        mFabProgress.setMax(100);
        mFabProgress.setScaleX(0);
        mFabProgress.setScaleY(0);

        // Fab button
        mFabViewButton = (ImageView) findViewById(R.id.activity_detail_fab);
        mFabViewButton.setScaleX(0);
        mFabViewButton.setScaleY(0);
        mFabViewButton.setImageDrawable(mDrawableView);
        mFabViewButton.setOnClickListener(onFabViewButtonListener);
        //just allow the longClickAction on Devices newer than api level v19
        if (Build.VERSION.SDK_INT >= 19) {
            mFabViewButton.setOnLongClickListener(onFabButtonLongListener);
        }

        // Fab share button
        mFabUploadButton = (ImageView) findViewById(R.id.activity_detail_fab_upload);
        mFabUploadButton.setScaleX(0);
        mFabUploadButton.setScaleY(0);
        mFabUploadButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_upload).color(Color.WHITE).sizeDp(16));
        mFabUploadButton.setOnClickListener(onFabUploadButtonListener);

        // Fab download button
        mFabRemoveButton = (ImageView) findViewById(R.id.activity_detail_fab_remove);
        mFabRemoveButton.setScaleX(0);
        mFabRemoveButton.setScaleY(0);
        mFabRemoveButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_remove).color(Color.WHITE).sizeDp(16));
        mFabRemoveButton.setOnClickListener(onFabRemoveButtonListener);

        // Title container
        mTitleContainer = findViewById(R.id.activity_detail_title_container);
        Utils.configuredHideYView(mTitleContainer);

        // Define toolbar as the shared element
        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_detail_toolbar);
        setSupportActionBar(toolbar);

        //get the imageHeader and set the coverImage
        final ImageView image = (ImageView) findViewById(R.id.activity_detail_image);
        Bitmap imageCoverBitmap = AlbumListFragment.photoCache.get(selectPosition);
        //safety check to prevent nullPointer in the palette if the detailActivity was in the background for too long
        if (imageCoverBitmap == null || imageCoverBitmap.isRecycled()) {
            this.finish();
            return;
        }
        image.setImageBitmap(imageCoverBitmap);

        //override text
        setTitle("");

        if (Build.VERSION.SDK_INT >= 21) {
            image.setTransitionName("cover");
            // Add a listener to get noticed when the transition ends to animate the fab button
            getWindow().getSharedElementEnterTransition().addListener(new CustomTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    animateActivityStart();
                }
            });
        } else {
            Utils.showViewByScale(image).setDuration(ANIMATION_DURATION_LONG).start();
            animateActivityStart();
        }

        //check if we already had the colors during click
        int swatch_title_text_color = getIntent().getIntExtra("swatch_title_text_color", -1);
        int swatch_rgb = getIntent().getIntExtra("swatch_rgb", -1);

        if (swatch_rgb != -1 && swatch_title_text_color != -1) {
            setColors(swatch_title_text_color, swatch_rgb);
        } else { //default color 추가하기
            setColors(swatch_title_text_color, swatch_rgb);
        }

    }

    private View.OnClickListener onFabUploadButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {//NOT

        }
    };

    private View.OnClickListener onFabRemoveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new MaterialDialog.Builder(DetailActivity.this)
                    .content("앨범을 삭제하시겠습니까?\n삭제시 다시 복구 할 수 없습니다.")
                    .positiveText("삭제")
                    .negativeText("취소")
                    .theme(Theme.LIGHT)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            String deltePath = AppConfig.allAlbumdata.get(selectPosition).getSavedAlbumPath();
                            AppConfig.allAlbumdata.remove(selectPosition);
                            FileManager.deleteAlbum(deltePath);
                            isRemove = true;
                            animateStart();
                            mFabViewButton.animate().rotation(360).setDuration(ANIMATION_DURATION_EXTRA_LONG).setListener(new CustomAnimatorListener() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    animateCompleteFirst(true);
                                    onBackPressed();
                                    super.onAnimationEnd(animation);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    animateComplete(true);
                                    onBackPressed();
                                    super.onAnimationCancel(animation);
                                }
                            }).start();
                        }
                    })
                    .show();

        }
    };


    private View.OnClickListener onFabViewButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            AlbumData selectedAlbum = AppConfig.allAlbumdata.get(selectPosition);
            Intent intent = new Intent(DetailActivity.this, AlbumViewActivity.class);
            intent.putExtra(ActivityCallKey.SELECT_ALBUM_POSITION, selectPosition);
            startActivity(intent);
        }
    };

    private View.OnLongClickListener onFabButtonLongListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            return true;
        }
    };


//    /**
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        boolean success = resultCode == -1;
//
//        if (requestCode == ACTIVITY_CROP) {
//            //animate the first elements
//            animateCompleteFirst(success);
//        } else if (requestCode == ACTIVITY_SHARE) {
//            //animate the first elements
//            animateCompleteFirst(success);
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    /**
     * animate the start of the activity
     */
    private void animateActivityStart() {
        ViewPropertyAnimator showTitleAnimator = Utils.showViewByScale(mTitleContainer);
        showTitleAnimator.setListener(new CustomAnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animation) {

                super.onAnimationEnd(animation);
                mTitlesContainer.startAnimation(AnimationUtils.loadAnimation(DetailActivity.this, R.anim.alpha_on));
                mTitlesContainer.setVisibility(View.VISIBLE);

                //animate the fab
                Utils.showViewByScale(mFabViewButton).setDuration(ANIMATION_DURATION_MEDIUM).start();

                //animate the share fab
                Utils.showViewByScale(mFabUploadButton)
                        .setDuration(ANIMATION_DURATION_MEDIUM * 2)
                        .start();
                mFabUploadButton.animate()
                        .translationX((-1) * Utils.pxFromDp(DetailActivity.this, 58))
                        .setStartDelay(ANIMATION_DURATION_MEDIUM)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .start();

                //animate the download fab
                Utils.showViewByScale(mFabRemoveButton)
                        .setDuration(ANIMATION_DURATION_MEDIUM * 2)
                        .start();
                mFabRemoveButton.animate()
                        .translationX((-1) * Utils.pxFromDp(DetailActivity.this, 108))
                        .setStartDelay(ANIMATION_DURATION_MEDIUM)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .start();
            }
        });

        showTitleAnimator.start();
    }


    /**
     * animate the start of the download
     */
    private void animateStart() {
        //reset progress to prevent jumping
        mFabProgress.setProgress(0);

        //hide the share fab
        mFabUploadButton.animate().translationX(0).setDuration(ANIMATION_DURATION_SHORT).start();
        mFabRemoveButton.animate().translationX(0).setDuration(ANIMATION_DURATION_SHORT).start();

        //some nice button animations
        Utils.showViewByScale(mFabProgress).setDuration(ANIMATION_DURATION_MEDIUM).start();
        mFabProgress.setProgress(1);

        mProgressFabAnimation = new RotateAnimation(0.0f, 360.0f, mFabProgress.getWidth() / 2, mFabProgress.getHeight() / 2);
        mProgressFabAnimation.setDuration(ANIMATION_DURATION_EXTRA_LONG * 2);
        mProgressFabAnimation.setInterpolator(new LinearInterpolator());
        mProgressFabAnimation.setRepeatCount(Animation.INFINITE);
        mProgressFabAnimation.setRepeatMode(-1);
        mFabProgress.startAnimation(mProgressFabAnimation);

        mFabViewButton.setImageDrawable(null);

        //animate the button back to blue. just do it the first time
        if (mFabViewButton.getTag() != null) {
            TransitionDrawable transition = (TransitionDrawable) mFabViewButton.getBackground();
            transition.reverseTransition(ANIMATION_DURATION_LONG);
            mFabViewButton.setTag(null);
        }

        if (mFabUploadButton.getTag() != null) {
            TransitionDrawable transition = (TransitionDrawable) mFabUploadButton.getBackground();
            transition.reverseTransition(ANIMATION_DURATION_LONG);
            mFabUploadButton.setTag(null);
        }

        if (mFabRemoveButton.getTag() != null) {
            TransitionDrawable transition = (TransitionDrawable) mFabRemoveButton.getBackground();
            transition.reverseTransition(ANIMATION_DURATION_LONG);
            mFabRemoveButton.setTag(null);
        }
    }

    /**
     * animate the reset of the view
     */
    private void animateReset(boolean error) {
//        future.cancel(true);
//        future = null;

        //animating everything back to default :D
        Utils.hideViewByScaleXY(mFabProgress).setDuration(ANIMATION_DURATION_MEDIUM).start();
        mProgressFabAnimation.cancel();
        //Utils.animateViewElevation(mFabViewButton, 0, mElavationPx);

        if (error) {
            mFabViewButton.setImageDrawable(mDrawableError);
        } else {
            mFabViewButton.setImageDrawable(mDrawableView);
        }

        mFabViewButton.animate().rotation(360).setDuration(ANIMATION_DURATION_MEDIUM).start();

        mFabUploadButton.animate().translationX((-1) * Utils.pxFromDp(DetailActivity.this, 58)).setDuration(ANIMATION_DURATION_MEDIUM).start();
        mFabRemoveButton.animate().translationX((-1) * Utils.pxFromDp(DetailActivity.this, 108)).setDuration(ANIMATION_DURATION_MEDIUM).start();
    }

    /**
     * animate the first parts of the UI after the download has successfully finished
     */
    private void animateCompleteFirst(boolean success) {
        //some nice animations so the user knows the wallpaper was set properly
        mFabViewButton.animate().rotation(720).setDuration(ANIMATION_DURATION_EXTRA_LONG).start();
        mFabViewButton.setImageDrawable(mDrawableSuccess);

        //animate the button to green. just do it the first time
        if (mFabViewButton.getTag() == null) {
            TransitionDrawable transition = (TransitionDrawable) mFabViewButton.getBackground();
            transition.startTransition(ANIMATION_DURATION_LONG);
            mFabViewButton.setTag("");
        }

        if (mFabUploadButton.getTag() == null) {
            TransitionDrawable transition = (TransitionDrawable) mFabUploadButton.getBackground();
            transition.startTransition(ANIMATION_DURATION_LONG);
            mFabUploadButton.setTag("");
        }

        if (mFabRemoveButton.getTag() == null) {
            TransitionDrawable transition = (TransitionDrawable) mFabRemoveButton.getBackground();
            transition.startTransition(ANIMATION_DURATION_LONG);
            mFabRemoveButton.setTag("");
        }
    }

    /**
     * finish the animations of the ui after the download is complete. reset the button to the start
     *
     * @param success
     */
    private void animateComplete(boolean success) {
        //hide the progress again :D
        Utils.hideViewByScaleXY(mFabProgress).setDuration(ANIMATION_DURATION_MEDIUM).start();
        mProgressFabAnimation.cancel();

        //show the fab again ;)
        mFabUploadButton.animate().translationX((-1) * Utils.pxFromDp(DetailActivity.this, 58)).setDuration(ANIMATION_DURATION_MEDIUM).start();
        mFabRemoveButton.animate().translationX((-1) * Utils.pxFromDp(DetailActivity.this, 108)).setDuration(ANIMATION_DURATION_MEDIUM).start();

        // if we were not successful remove the x again :D
        if (!success) {
            mFabViewButton.setImageDrawable(mDrawableView);
            mFabViewButton.animate().rotation(360).setDuration(ANIMATION_DURATION_MEDIUM).start();
        }
    }

    /**
     * @param titleTextColor
     * @param rgb
     */
    private void setColors(int titleTextColor, int rgb) {
        mTitleContainer.setBackgroundColor(rgb);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(titleTextColor);
        }

        TextView titleTV = (TextView) mTitleContainer.findViewById(R.id.activity_detail_title);
        titleTV.setTextColor(titleTextColor);
        titleTV.setText(AppConfig.allAlbumdata.get(selectPosition).getTitle());

        TextView subtitleTV = (TextView) mTitleContainer.findViewById(R.id.activity_detail_subtitle);
        subtitleTV.setTextColor(titleTextColor);
        subtitleTV.setText(AppConfig.allAlbumdata.get(selectPosition).getPublishedAt());

        TextView tv_Count = (TextView) mTitleContainer.findViewById(R.id.activity_detail_count);
        tv_Count.setTextColor(titleTextColor);
        tv_Count.setText(AppConfig.allAlbumdata.get(selectPosition).getAlbumSize() + "장");

        ((TextView) mTitleContainer.findViewById(R.id.activity_detail_subtitle))
                .setTextColor(titleTextColor);
    }


    @Override
    public void onBackPressed() {
        mFabRemoveButton.animate()
                .translationX(0)
                .setDuration(ANIMATION_DURATION_MEDIUM)
                .setListener(animationFinishListener1)
                .start();


        //move the share fab below the normal fab (58 because this is the margin top + the half
        mFabUploadButton.animate()
                .translationX(0)
                .setDuration(ANIMATION_DURATION_MEDIUM)
                .setListener(animationFinishListener1)
                .start();

        if (isRemove)
            setResult(ActivityCallKey.EDIT_RESULT);
    }

    private CustomAnimatorListener animationFinishListener1 = new CustomAnimatorListener() {
        private int animateFinish1 = 0;

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            process();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            process();
        }

        private void process() {
            animateFinish1 = animateFinish1 + 1;
            if (animateFinish1 >= 2) {
                //create the fab animation and hide fabProgress animation, set an delay so those will hide after the shareFab is below the main fab
                Utils.hideViewByScaleXY(mFabRemoveButton)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .setListener(animationFinishListener2)
                        .start();
                Utils.hideViewByScaleXY(mFabUploadButton)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .setListener(animationFinishListener2)
                        .start();
                Utils.hideViewByScaleXY(mFabProgress)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .setListener(animationFinishListener2)
                        .start();
                Utils.hideViewByScaleXY(mFabViewButton)
                        .setDuration(ANIMATION_DURATION_MEDIUM)
                        .setListener(animationFinishListener2)
                        .start();
            }
        }
    };

    private CustomAnimatorListener animationFinishListener2 = new CustomAnimatorListener() {
        private int animateFinish2 = 0;

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            process();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            process();
        }

        private void process() {
            animateFinish2 = animateFinish2 + 1;
            if (animateFinish2 >= 4) {
                ViewPropertyAnimator hideFabAnimator = Utils.hideViewByScaleY(mTitleContainer);
                hideFabAnimator.setListener(new CustomAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        coolBack();
                    }
                });
            }
        }
    };

    /**
     *
     */
    private void coolBack() {
        try {
            super.onBackPressed();
        } catch (Exception e) {
            // ew;
            e.printStackTrace();
        }
    }
}
