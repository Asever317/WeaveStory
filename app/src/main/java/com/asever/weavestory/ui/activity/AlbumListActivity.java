package com.asever.weavestory.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.asever.weavestory.R;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.ui.ToolbarActionItemTarget;
import com.asever.weavestory.util.FileManager;
import com.asever.weavestory.util.Utils;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

/**
 * Created by Asever on 2015-11-30
 */
public class AlbumListActivity extends AppCompatActivity {
    public static AppCompatActivity ALBUMLISTACTIVITY;
    private static final int PROFILE_SETTING = 1012;

    private BackPressCloseHandler backPressCloseHandler;
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppConfig.initAllAlbumdata();
        backPressCloseHandler = new BackPressCloseHandler(this);
        ALBUMLISTACTIVITY = this;

        if (AppConfig.isAlbumRead == false) {
            FileManager.readImagePath();
            AppConfig.isAlbumRead = true;
        }
        if (AppConfig.allAlbumdata.isEmpty())
            AppConfig.isSaveAlbum = false;
        else
            AppConfig.isSaveAlbum = true;

        setContentView(R.layout.activity_album_list);
        setUpNavigationDrawer(savedInstanceState);

    }

    private void setUpNavigationDrawer(Bundle savedIntsanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeAsUpIndicator(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_bars).color(Color.DKGRAY).sizeDp(20), new IconicsDrawable(this, FontAwesome.Icon.faw_bars).color(Color.WHITE).sizeDp(20)));
        getSupportActionBar().setTitle("내 앨범");
        final IProfile profile = new ProfileDrawerItem().withName("Raju").withEmail("8596na@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460");
        //사이드 메뉴 헤더 생성
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,
                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBarSize().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedIntsanceState)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new SectionDrawerItem().withName("내 정보"),
                        new PrimaryDrawerItem().withName("목록").withIcon(GoogleMaterial.Icon.gmd_list).withIdentifier(1).withCheckable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                            }
                            if (intent != null) {
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedIntsanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();


        if (savedIntsanceState == null) {
            result.setSelectionByIdentifier(11, false);
            headerResult.setActiveProfile(profile);
        }

        if (AppConfig.isSaveAlbum == false) {
            setShowCaseBuilder(toolbar);
        }
    }

    private void setShowCaseBuilder(Toolbar toolbar) {

        ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                .withNewStyleShowcase()
                .setTarget(new ToolbarActionItemTarget(toolbar, R.id.action_gallery))
                .setStyle(R.style.CustomShowcaseTheme)
                .setContentTitle("저장된 앨범이 없습니다")
                .setContentText("자신만의 새로운 앨범을 만들어보세요!")
                .build();

        showcaseView.setDetailTextAlignment(Layout.Alignment.ALIGN_CENTER);
        showcaseView.setTitleTextAlignment(Layout.Alignment.ALIGN_CENTER);
        showcaseView.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present. getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_album_list, menu);
        MenuItem galleryitem = menu.findItem(R.id.action_gallery);
        galleryitem.setIcon(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_file_image_o).color(Color.DKGRAY).sizeDp(20), new IconicsDrawable(this, FontAwesome.Icon.faw_file_image_o).color(Color.WHITE).sizeDp(20)));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_gallery:
                Intent intent = new Intent(AlbumListActivity.this,
                        GalleryPickActivity.class);
                intent.putExtra(ActivityCallKey.ACTIVITY_WHAT_ACTION, ActivityCallKey.ALBUM_MAKE);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                if (result != null && result.isDrawerOpen()) {
                    result.closeDrawer();
                } else
                    result.openDrawer();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

    public class BackPressCloseHandler {

        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity,
                    "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == DETAIL_ACTIVITY_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//
//            }
//
//        }
//    }
}
