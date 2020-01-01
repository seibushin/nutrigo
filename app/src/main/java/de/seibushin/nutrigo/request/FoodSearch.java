package de.seibushin.nutrigo.request;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.List;

import de.seibushin.nutrigo.model.nutrition.Food;

public class FoodSearch {
    private static FoodSearch instance;

    private RequestQueue queue;

    public FoodSearch(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Singleton
     *
     * @return
     */
    public synchronized static FoodSearch getInstance(Context context) {
        if (instance == null) {
            instance = new FoodSearch(context);
        }
        return instance;
    }

    public void search(String query, Response.Listener<List<FoodLink>> listener) {
        try {
            queue.cancelAll(SearchRequest.TAG);
            SearchRequest request = new SearchRequest(query, listener);
            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFood(String query, Response.Listener<Food> listener) {
        try {
            queue.cancelAll(FoodRequest.TAG);
            FoodRequest request = new FoodRequest(query, listener);
            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
