package com.itafuta;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class MenuActivity extends AppCompatActivity {


    final Context context = this;
    BottomBar mBottomBar;  //Bottom bar

    FragmentTransaction ft;
    FragmentManager fm;
    BlankFragment myFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_common_menu);

        //*********Start Bottom bar nav

        //-----------The new bottom menu----------

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.bottomBarItemFavourites) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.

                }
                if (tabId == R.id.bottomBarItemSearch) {
                    Toast.makeText(MenuActivity.this, "You selected " + tabId, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MenuActivity.this, SearchActivity.class);
                    startActivity(i);
                }
                if (tabId ==  R.id.bottomBarItemHome) {
                    Toast.makeText(MenuActivity.this, "You selected " + tabId, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                    ft = fm.beginTransaction();
                    ft.remove(myFragment);
                    ft.addToBackStack(null);
                    ft.commit();

                    Intent intent = new Intent(MenuActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                }
                if (tabId == R.id.bottomBarItemInfo) {
                    // add
                    ft = fm.beginTransaction();
                    ft.add(R.id.my_fragment, myFragment);
                    ft.addToBackStack(null);
                    ft.commit();

                }
                if (tabId == R.id.bottomBarItemRegister) {
                    Intent i = new Intent(MenuActivity.this, RegisterActivity.class);
                    startActivity(i);
                }

            }


        });

        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.bottomBarItemHome) {
                    // The tab with id R.id.tab_favorites was reselected,
                    // change your content accordingly.
                    Toast.makeText(MenuActivity.this, "You UNselected " + tabId, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //-------------- END BITTOM MENU -------



    }
}
