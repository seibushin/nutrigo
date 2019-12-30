package de.seibushin.nutrigo.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.view.activity.CalendarActivity;
import de.seibushin.nutrigo.view.adapter.NutritionAdapter;
import de.seibushin.nutrigo.view.dialog.ProfileDialog;
import de.seibushin.nutrigo.view.widget.ProgressCircle;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FragmentDay extends Fragment {
//    private OnListFragmentInteractionListener mListener;

    private NutritionAdapter adapter;

    private ProgressCircle pc_kcal;
    private ProgressCircle pc_fat;
    private ProgressCircle pc_carbs;
    private ProgressCircle pc_protein;
    private TextView tv_date;

    private final DateFormat df = SimpleDateFormat.getDateInstance();

    private long selectedDay = 0;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        // progress circle
        pc_kcal = view.findViewById(R.id.pc_kcal);
        pc_fat = view.findViewById(R.id.pc_fat);
        pc_carbs = view.findViewById(R.id.pc_carbs);
        pc_protein = view.findViewById(R.id.pc_protein);
        tv_date = view.findViewById(R.id.tv_date);

        view.findViewById(R.id.show_profile).setOnClickListener(v -> showProfile());

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        DividerItemDecoration did = new DividerItemDecoration(getActivity(), llm.getOrientation());
        did.setDrawable(getContext().getDrawable(R.drawable.divider));

        adapter = new NutritionAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(did);

        return view;
    }

    /**
     * Show profile dialog
     */
    private void showProfile() {
        ProfileDialog dialog = new ProfileDialog();
        dialog.show(getFragmentManager());
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
                startActivity(new Intent(getContext(), CalendarActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
