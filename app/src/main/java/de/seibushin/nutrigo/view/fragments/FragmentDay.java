package de.seibushin.nutrigo.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.seibushin.nutrigo.Database;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.Day;
import de.seibushin.nutrigo.model.Profile;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;
import de.seibushin.nutrigo.dummy.DummyContent.DummyItem;
import de.seibushin.nutrigo.view.dialog.ProfileDialog;
import de.seibushin.nutrigo.view.widget.ProgressCircle;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentDay extends Fragment {
//    private OnListFragmentInteractionListener mListener;

    private NutritionAdapter adapter;

    private ProgressCircle pc_kcal;
    private ProgressCircle pc_fat;
    private ProgressCircle pc_carbs;
    private ProgressCircle pc_protein;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentDay() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        // progress circle
        pc_kcal = view.findViewById(R.id.pc_kcal);
        pc_fat = view.findViewById(R.id.pc_fat);
        pc_carbs = view.findViewById(R.id.pc_carbs);
        pc_protein = view.findViewById(R.id.pc_protein);

        view.findViewById(R.id.show_profile).setOnClickListener(v -> showProfile());

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        DividerItemDecoration did = new DividerItemDecoration(getActivity(), llm.getOrientation());
        did.setDrawable(getContext().getDrawable(R.drawable.divider));

        adapter = new NutritionAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);

        updateDay(Database.getInstance().getSelectedDay());
        updateProfile(Database.getInstance().getProfile());

        // subscribe to changes
        Database.getInstance().subscribeToProfile(this::updateProfile);
        Database.getInstance().subscribeToDay(this::updateDay);

        return view;
    }

    /**
     * Show profile dialog
     */
    private void showProfile() {
        ProfileDialog dialog = new ProfileDialog();
        dialog.show(getFragmentManager());
    }

    /**
     * Update view by setting the max values for the progress
     */
    private void updateProfile(Profile profile) {
        getActivity().runOnUiThread(() -> {
            pc_kcal.setMax(profile.getKcal());
            pc_fat.setMax(profile.getFat());
            pc_carbs.setMax(profile.getCarbs());
            pc_protein.setMax(profile.getProtein());
        });
    }

    /**
     * Update the day by displaying the correct nutrition items and updating the progress
     */
    private void updateDay(Day day) {
        System.out.println("UPDATE DAY");
        getActivity().runOnUiThread(() -> {
            adapter.setItems(day.getNutrition());
            adapter.notifyDataSetChanged();

            // update progress
            pc_kcal.setProgress((int) day.getKcal());
            pc_fat.setProgress((int) day.getFat());
            pc_carbs.setProgress((int) day.getCarbs());
            pc_protein.setProgress((int) day.getProtein());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_add).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_day:
                // select Day
//                startActivity(new Intent(getContext(), CalendarActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
