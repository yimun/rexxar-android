package com.douban.rexxar.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.douban.rexxar.example.widget.AlertDialogWidget;
import com.douban.rexxar.example.widget.PullToRefreshWidget;
import com.douban.rexxar.example.widget.TitleWidget;
import com.douban.rexxar.example.widget.ToastWidget;
import com.douban.rexxar.example.widget.menu.MenuItem;
import com.douban.rexxar.example.widget.menu.MenuWidget;
import com.douban.rexxar.view.RexxarWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by linwei on 16-11-21.
 */

public class DemoDialogActivity extends AppCompatActivity {

    public static final String TAG = DemoDialogActivity.class.getSimpleName();

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DemoDialogActivity.class);
        activity.startActivity(intent);
    }

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.webView)
    RexxarWebView mRexxarWebView;
    @InjectView(R.id.bottom_sheet_container)
    View mBottomSheetContainer;

    protected BottomSheetBehavior mBottomSheetBehavior;

    private List<MenuItem> mMenuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rexxar_activity);
        ButterKnife.inject(this);
        mToolbar.setTitle(R.string.title_dialog_rexxar);

        // add widget
        mRexxarWebView.addRexxarWidget(new TitleWidget());
        mRexxarWebView.addRexxarWidget(new AlertDialogWidget());
        mRexxarWebView.addRexxarWidget(new ToastWidget());
        mRexxarWebView.addRexxarWidget(new PullToRefreshWidget());
        mRexxarWebView.addRexxarWidget(new MenuWidget());

        // load uri
        mRexxarWebView.loadUrl("https://m.douban.com/");

        mRexxarWebView.enableRefresh(false);

        // bottomsheet
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetContainer);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    finish();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        slideInAfterCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        testFunc();
        }

    private void testFunc() {
        try {
            JSONObject user = new JSONObject();
            user.put("name", "name");
            user.put("age", 18);
            mRexxarWebView.callFunction("alert", user.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        if (null == menuItems || menuItems.size() == 0) {
            return;
        }
        mMenuItems.clear();
        mMenuItems.addAll(menuItems);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for (MenuItem menuItem : mMenuItems) {
            menuItem.getMenuView(menu, this);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 在onCreate之后上滑展示内容
     */
    public void slideInAfterCreate() {
        mBottomSheetContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }, 100);
    }

    @Override
    public void finish() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            super.finish();
            overridePendingTransition(0, 0);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
