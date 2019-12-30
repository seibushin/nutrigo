package de.seibushin.nutrigo.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;
import de.seibushin.nutrigo.view.activity.CreateMealActivity;
import de.seibushin.nutrigo.view.adapter.ClickListener;
import de.seibushin.nutrigo.view.adapter.ItemTouchListener;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FragmentMeal extends FragmentList {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentMeal() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            LinearLayoutManager llm = new LinearLayoutManager(context);
            DividerItemDecoration did = new DividerItemDecoration(getActivity(), llm.getOrientation());
            did.setDrawable(getContext().getDrawable(R.drawable.divider));

            adapter = new NutritionAdapter();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(llm);
            recyclerView.addItemDecoration(did);
            recyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Meal meal = (Meal) adapter.getItem(position);

                    // add food to the day
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


        }
        return view;
    }

    /**
     * Update meals
     *
     * @param meals
     */
    private void updateMeal(List<NutritionUnit> meals) {
        System.out.println("UPDATE MEALS");
        getActivity().runOnUiThread(() -> {
            adapter.setItems(meals);
            adapter.notifyDataSetChanged();
        });
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(getContext(), CreateMealActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
