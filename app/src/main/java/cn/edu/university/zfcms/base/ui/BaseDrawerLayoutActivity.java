

package cn.edu.university.zfcms.base.ui;

import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;


import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cn.edu.university.zfcms.R;

public abstract class BaseDrawerLayoutActivity extends BaseSwipeRefreshLayout {

    @Bind(R.id.root_view)
    protected DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    protected NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;


    protected HashMap<Integer, MenuItem> mMenuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getNavigationItemSelectedListener() != null)
            this.mNavigationView.setNavigationItemSelectedListener(this.getNavigationItemSelectedListener());
        hideVerticalScrollBar();
        this.mDrawerLayout.setDrawerListener(new DrawerStateListener());

        // 初始化MenuItems
        this.mMenuItems = new HashMap<>();
        int[] menuItemIds = this.getMenuItemIds();
        if (menuItemIds.length > 0) {
            for (int id : menuItemIds) {
                MenuItem menuItem = this.mNavigationView.getMenu().findItem(id);
                if (menuItem != null) this.mMenuItems.put(id, menuItem);
            }
        }

        this.mDrawerToggle = new ActionBarDrawerToggle(
                this,
                this.mDrawerLayout,
                R.string.app_menu,
                R.string.app_name
        );

    }

    /**
     * 隐藏 NavigationView 垂直滑动条
     */
    private void hideVerticalScrollBar() {
        NavigationMenuView navigationMenuView = (NavigationMenuView) mNavigationView.getChildAt(0);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }
    }

    /**
     * Fill in NavigationView.OnNavigationItemSelectedListener
     *
     * @return NavigationView.OnNavigationItemSelectedListener
     */
    protected abstract NavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener();

    /**
     * Fill in NavigationView menu ids
     *
     * @return int[]
     */
    protected abstract int[] getMenuItemIds();

    /**
     * Fill in your menu operation on click
     * <p>
     * 走到这，就不会有两次点击都一样的情况
     * Come to this, there would be no two clicks are all the same
     *
     * @param now Now you choose the item
     */
    protected abstract void onMenuItemOnClick(MenuItem now);

    /**
     * set menu item check status
     *
     * @param itemId itemId
     * @return true to display the item as the selected item
     */
    protected boolean menuItemChecked(int itemId) {
        MenuItem old = null;
        MenuItem now;
        if (this.mMenuItems.containsKey(itemId)) {
            for (Map.Entry<Integer, MenuItem> entry : this.mMenuItems.entrySet()) {
                MenuItem menuItem = entry.getValue();
                /*
                 * 先找这个item是否之前被选过
                 * 不影响下面的设置选中
                 */
                if (menuItem.isChecked()) {
                    old = menuItem;
                }

                /*
                 * 如果之前找到了之前选中过的
                 * 那别玩了，前后两次选了一样的
                 */
                if (old != null && old.getItemId() == itemId)
                    break;

                /*
                 * 到这了，前后两次的选择不会一样的
                 */
                if (menuItem.getItemId() == itemId) {
                    now = menuItem;
                    menuItem.setChecked(true);
                    this.onMenuItemOnClick(now);
                } else {
                    menuItem.setChecked(false);
                }

            }
            this.mDrawerLayout.closeDrawer(this.mNavigationView);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Take care of calling onBackPressed() for pre-Eclair platforms.
     *
     * @param keyCode keyCode
     * @param event   event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果抽屉打开了
        if (keyCode == KeyEvent.KEYCODE_BACK && this.mDrawerLayout.isDrawerOpen(this.mNavigationView)) {
            this.mDrawerLayout.closeDrawer(this.mNavigationView);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When using ActionBarDrawerToggle, all DrawerLayout listener methods should be forwarded
     * if the ActionBarDrawerToggle is not used as the DrawerLayout listener directly.
     */
    private class DrawerStateListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            BaseDrawerLayoutActivity.this.mDrawerToggle.onDrawerOpened(drawerView);
            if (BaseDrawerLayoutActivity.this.mActionBarHelper != null) {
                BaseDrawerLayoutActivity.this.mActionBarHelper.onDrawerOpened();
            }
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            BaseDrawerLayoutActivity.this.mDrawerToggle.onDrawerClosed(drawerView);
            BaseDrawerLayoutActivity.this.mActionBarHelper.onDrawerClosed();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            BaseDrawerLayoutActivity.this.mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            BaseDrawerLayoutActivity.this.mDrawerToggle.onDrawerStateChanged(newState);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


}
