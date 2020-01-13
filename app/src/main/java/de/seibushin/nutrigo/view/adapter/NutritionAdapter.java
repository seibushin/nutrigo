package de.seibushin.nutrigo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;
import de.seibushin.nutrigo.Helper;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.NutritionType;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;

/**
 * {@link RecyclerView.Adapter} that can display a {@link NutritionUnit}
 */
public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder> implements Filterable {
    private NutritionFilter filter = new NutritionFilter();

    private final List<NutritionUnit> data = new ArrayList<>();

    private SortedList<NutritionUnit> dataFiltered = new SortedList(NutritionUnit.class, new SortedListAdapterCallback(this) {
        @Override
        public int compare(Object o1, Object o2) {
            return (((NutritionUnit) o1).getName().toLowerCase()).compareTo(((NutritionUnit) o2).getName().toLowerCase());
        }

        @Override
        public boolean areContentsTheSame(Object o1, Object o2) {
            return false;
        }

        @Override
        public boolean areItemsTheSame(Object o1, Object o2) {
            return o1 == o2;
        }
    });

//    private final OnListFragmentInteractionListener mListener;

//    public MyItemRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
//        data = items;
//        mListener = listener;
//    }

    public NutritionAdapter() {
    }

    /**
     * Update the items list
     *
     * @param items
     */
    public synchronized void setItems(List<NutritionUnit> items) {
        this.data.clear();
        this.data.addAll(items);
        this.dataFiltered.replaceAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrition_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setNutri(dataFiltered.get(position));

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataFiltered.size();
    }

    public void clear() {
        data.clear();
        dataFiltered.clear();
    }

    public List<NutritionUnit> getData() {
        return data;
    }

    public NutritionUnit getItem(int index) {
        return dataFiltered.get(index);
    }

    public void add(NutritionUnit food) {
        if (!data.contains(food)) {
            data.add(food);
        }
        dataFiltered.add(food);
    }

    public void remove(NutritionUnit food) {
        data.remove(food);
        dataFiltered.remove(food);
    }

    public void remove(int position) {
        NutritionUnit nu = dataFiltered.removeItemAt(position);
        data.remove(nu);

        notifyDataSetChanged();
    }

    @Override
    public NutritionFilter getFilter() {
        return filter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private NutritionUnit item;

        private final TextView name;
        private final TextView kcal;
        private final TextView fat;
        private final TextView carbs;
        private final TextView sugar;
        private final TextView protein;
        private final TextView weight;
        private final TextView portion;
        private final ImageView ic_nutri;

        /**
         * Constructor
         *
         * @param view view
         */
        ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.tv_name);
            kcal = view.findViewById(R.id.tv_kcal);
            fat = view.findViewById(R.id.tv_fat);
            carbs = view.findViewById(R.id.tv_carbs);
            sugar = view.findViewById(R.id.tv_sugar);
            protein = view.findViewById(R.id.tv_protein);
            weight = view.findViewById(R.id.tv_weight);
            portion = view.findViewById(R.id.tv_portion);
            ic_nutri = view.findViewById(R.id.ic_nutri);
        }

        /**
         * Set the nutrition unit
         *
         * @param item nutrition unit
         */
        void setNutri(NutritionUnit item) {
            this.item = item;
            name.setText(item.getName());
            kcal.setText(kcal.getContext().getString(R.string.kcal_unit, item.getKcal()));
            fat.setText(Helper.formatDecimal(item.getFat()));
            carbs.setText(Helper.formatDecimal(item.getCarbs()));
            sugar.setText(Helper.formatDecimal(item.getSugar()));
            protein.setText(Helper.formatDecimal(item.getProtein()));

            if (item.getType() == NutritionType.FOOD) {
                ic_nutri.setImageResource(R.drawable.ic_food);
                // a meal portion has no metric
                portion.setText(portion.getContext().getString(R.string.weight_unit, item.getPortion()));
            } else {
                ic_nutri.setImageResource(R.drawable.ic_meal);
                portion.setText(String.format("%s", item.getPortion()));
            }
        }
    }

    public class NutritionFilter extends Filter {
        private CharSequence query;

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String charString = charSequence.toString();

            List<NutritionUnit> filtered = new ArrayList<>();

            if (charString.isEmpty()) {
                filtered.addAll(data);
            } else {
                for (NutritionUnit f : data) {
                    if (f.getName().toLowerCase().contains(charString.toLowerCase())) {
                        filtered.add(f);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            dataFiltered.replaceAll((List<NutritionUnit>) filterResults.values);
            notifyDataSetChanged();
        }

        public void setFilter(CharSequence constraint) {
            super.filter(constraint);

            query = constraint;
        }

        public CharSequence getQuery() {
            return query;
        }
    }
}
