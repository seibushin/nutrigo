package de.seibushin.nutrigo.view.fragments;

import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executors;

import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;

public abstract class FragmentList extends Fragment {
    //    private OnListFragmentInteractionListener mListener;
    protected NutritionAdapter adapter;

    protected SearchView searchView;
    protected CharSequence query;

    /**
     * Filter the list
     *
     * @param query
     */
    protected void filter(CharSequence query) {
        this.query = query;
        adapter.getFilter().filter(query);
    }

    protected void showSnackbar(Snackbar snack) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snack.getView().getLayoutParams();
        params.setAnchorId(R.id.tabs);
        params.gravity = Gravity.BOTTOM;
        params.anchorGravity = Gravity.BOTTOM;
        snack.getView().setLayoutParams(params);
        snack.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
        snack.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_day).setVisible(false);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // filter the list
                filter(s);
                return true;
            }
        });

        // check for previous query and show filter in searchView
        if (query != null && !"".equals(query.toString())) {
            searchView.setIconified(false);
            searchView.setQuery(query, false);
            searchView.clearFocus();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }
}
