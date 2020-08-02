package de.seibushin.nutrigo.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
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
public class NutritionAdapterSimple extends RecyclerView.Adapter<NutritionAdapterSimple.ViewHolder> {
    private final List<NutritionUnit> data = new ArrayList<>();

    public NutritionAdapterSimple() {
    }

    /**
     * Update the items list
     *
     * @param items
     */
    public synchronized void setItems(List<NutritionUnit> items) {
        this.data.clear();
        this.data.addAll(items);
        Collections.sort(data, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        notifyDataSetChanged();
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
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NutritionUnit nu = data.get(position);
        holder.setNutri(nu);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(NutritionUnit food) {
        if (!data.contains(food)) {
            data.add(food);
            data.add(food);
        }
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
        private final ImageView expand;

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
            expand = view.findViewById(R.id.foods_exp);
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
                expand.setVisibility(View.GONE);
            } else {
                ic_nutri.setImageResource(R.drawable.ic_meal);
                portion.setText(String.format("%s | %1.0fg", item.getPortion(), item.getWeight()));
            }
        }
    }
}
