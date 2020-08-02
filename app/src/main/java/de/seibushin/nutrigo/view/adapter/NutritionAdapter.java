package de.seibushin.nutrigo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;
import de.seibushin.nutrigo.Helper;
import de.seibushin.nutrigo.R;
import de.seibushin.nutrigo.model.nutrition.Meal;
import de.seibushin.nutrigo.model.nutrition.MealDay;
import de.seibushin.nutrigo.model.nutrition.NutritionDay;
import de.seibushin.nutrigo.model.nutrition.NutritionType;
import de.seibushin.nutrigo.model.nutrition.NutritionUnit;
import de.seibushin.nutrigo.view.widget.SwipeRevealLayout;

/**
 * {@link RecyclerView.Adapter} that can display a {@link NutritionUnit}
 */
public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder> implements Filterable {
    private NutritionFilter filter = new NutritionFilter();

    private final List<NutritionUnit> data = new ArrayList<>();

    private boolean isDay = false;

    private SortedList<NutritionUnit> dataFiltered = new SortedList(NutritionUnit.class, new SortedListAdapterCallback(this) {
        @Override
        public int compare(Object o1, Object o2) {
            if (isDay) {
                return ((NutritionDay) o1).getTimestamp().compareTo(((NutritionDay) o2).getTimestamp());
            }
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

    public NutritionAdapter() {
    }

    public NutritionAdapter(boolean isDay) {
        this.isDay = isDay;
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

    private List<NutritionUnit> meals = new ArrayList<>();
    private List<NutritionUnit> foods = new ArrayList<>();

    public synchronized void setFoods(List<NutritionUnit> foods) {
        this.foods = new ArrayList<>(foods);
        refreshItems();
    }

    public synchronized void setMeals(List<NutritionUnit> meals) {
        this.meals = new ArrayList<>(meals);
        refreshItems();
    }

    public synchronized void refreshItems() {
        this.data.clear();
        this.data.addAll(foods);
        this.data.addAll(meals);
        this.dataFiltered.replaceAll(new ArrayList<>(data));
    }

    private ButtonAction editAction;
    private ButtonAction cloneAction;
    private ButtonAction deleteAction;
    private ButtonAction clickAction;

    public void onClick(ButtonAction clickAction) {
        this.clickAction = clickAction;
    }

    public void onEdit(ButtonAction editAction) {
        this.editAction = editAction;
    }

    public void onDelete(ButtonAction deleteAction) {
        this.deleteAction = deleteAction;
    }

    public void onClone(ButtonAction cloneAction) {
        this.cloneAction = cloneAction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrition_item, parent, false);
        return new ViewHolder(view);
    }

    public interface ButtonAction {
        void action(NutritionUnit nu, int pos);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NutritionUnit nu = dataFiltered.get(position);
        holder.unreveal(false);
        holder.setNutri(nu);

        if (clickAction != null) {
            holder.nuView.setOnClickListener(v -> clickAction.action(nu, position));
        }

        if (editAction != null) {
            holder.edit.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(v -> {
                holder.unreveal(true);
                editAction.action(nu, position);
            });
        } else {
            holder.edit.setVisibility(View.GONE);
        }

        if (cloneAction != null) {
            holder.clone.setVisibility(View.VISIBLE);
            holder.clone.setOnClickListener(v -> {
                holder.unreveal(true);
                cloneAction.action(nu, position);
            });
        } else {
            holder.clone.setVisibility(View.GONE);
        }

        if (deleteAction != null) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(v ->  {
                holder.unreveal(true);
                deleteAction.action(nu, position);
            });
        } else {
            holder.delete.setVisibility(View.GONE);
        }
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
            dataFiltered.add(food);
        }
    }

    public void addItems(List<NutritionUnit> nus) {
        data.forEach(nu -> System.out.println(nu.getName() + " " + nu.getId() + " " + nu.getPortion()));
        nus.forEach(nu -> {
            System.out.println("ADD ?" + !data.contains(nu) + " " + nu.getName() + " " + nu.getId() + " " + nu.getPortion());
            if (!data.contains(nu)) {
                data.add(nu);
                dataFiltered.add(nu);
            }
        });
    }

    public void remove(NutritionUnit food) {
        data.remove(food);
        dataFiltered.remove(food);
    }

    public void remove(int position) {
        NutritionUnit nu = dataFiltered.removeItemAt(position);
        data.remove(nu);

        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }

    @Override
    public NutritionFilter getFilter() {
        return filter;
    }

    public void reset() {
        meals.clear();
        foods.clear();
        data.clear();
        dataFiltered.clear();
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
        private final TextView portion;
        private final ImageView ic_nutri;

        private final ImageButton edit;
        private final ImageButton delete;
        private final ImageButton clone;
        private final ConstraintLayout nuView;
        private final SwipeRevealLayout reveal;

        private final ImageButton expand;
        private final RecyclerView meal_foods;

        private NutritionAdapterSimple adapter;

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
            portion = view.findViewById(R.id.tv_portion);
            ic_nutri = view.findViewById(R.id.ic_nutri);

            edit = view.findViewById(R.id.edit_button);
            delete = view.findViewById(R.id.delete_button);
            clone = view.findViewById(R.id.clone_button);
            nuView = view.findViewById(R.id.nuView);
            reveal = view.findViewById(R.id.reveal);

            meal_foods = view.findViewById(R.id.meal_foods_rv);
            expand = view.findViewById(R.id.foods_exp);
            expand.setOnClickListener(v -> {
                if (meal_foods.getVisibility() == View.VISIBLE) {
                    meal_foods.setVisibility(View.GONE);
                    expand.setImageResource(R.drawable.ic_expand_more);
                } else {
                    List<NutritionUnit> meal_nu;
                    if (item.getClass() == MealDay.class) {
                        meal_nu = new ArrayList<>(((MealDay) item).meal.foods);
                    } else {
                        meal_nu = new ArrayList<>(((Meal) item).foods);
                    }
                    adapter.setItems(meal_nu);
                    meal_foods.setVisibility(View.VISIBLE);
                    expand.setImageResource(R.drawable.ic_expand_less);
                }
            });

            adapter = new NutritionAdapterSimple();
            LinearLayoutManager llm = new LinearLayoutManager(mView.getContext());
            meal_foods.setAdapter(adapter);
            meal_foods.setLayoutManager(llm);
        }

        void unreveal(boolean animate) {
            reveal.close(animate);
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
            sugar.setText(String.format("(%s)", Helper.formatDecimal(item.getSugar())));
            protein.setText(Helper.formatDecimal(item.getProtein()));

            if (item.getType() == NutritionType.FOOD) {
                ic_nutri.setImageResource(R.drawable.ic_food);
                // a meal portion has no metric
                portion.setText(portion.getContext().getString(R.string.weight_unit, item.getPortion()));
                meal_foods.setVisibility(View.GONE);
                expand.setVisibility(View.GONE);
            } else {
                ic_nutri.setImageResource(R.drawable.ic_meal);
                portion.setText(String.format("%s | %1.0fg", item.getPortion(), item.getWeight()));
                expand.setVisibility(View.VISIBLE);
                meal_foods.setVisibility(View.GONE);
                expand.setImageResource(R.drawable.ic_expand_more);
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
