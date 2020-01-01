package de.seibushin.nutrigo.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.seibushin.nutrigo.model.nutrition.Food;

public class FoodRequest extends Request<Food> {
    public static final String TAG = "getFood";
    private final Response.Listener<Food> foodListener;

    private final String CHARSET = "ISO-8859-1";

    public FoodRequest(String link, Response.Listener<Food> listener) {
        super(Method.GET, link, null);
        setTag(TAG);
        this.foodListener = listener;
    }

    @Override
    protected Response<Food> parseNetworkResponse(NetworkResponse response) {
        try {
            String html = new String(response.data, CHARSET);
            Document doc = Jsoup.parse(html);

            String name = doc.selectFirst("#fddb-headline1").text();
            double weight = Double.valueOf(doc.selectFirst(".leftblock .standardcontent h2").text().replaceAll(".*?(\\d+).*", "$1").replaceAll(",", "."));
            double portion = Double.valueOf(doc.selectFirst(".rightblock .rightblue-complete .serva .servb").text().replaceAll(".*\\((\\d+).*", "$1").replaceAll(",", "."));

            double kcal = 0;
            double protein = 0;
            double fat = 0;
            double carbs = 0;
            double sugar = 0;

            for (Element element : doc.select(".leftblock .standardcontent .sidrow")) {
                String val = element.parent().select(".sidrow + div").text();

                // todo extract strings!?
                switch (element.text()) {
                    case "Kalorien":
                        kcal = Double.valueOf(val.replaceAll("(.*?) .*", "$1").replaceAll(",", "."));
                        break;
                    case "Protein":
                        protein = Double.valueOf(val.replaceAll("(.*?) .*", "$1").replaceAll(",", "."));
                        break;
                    case "Kohlenhydrate":
                        carbs = Double.valueOf(val.replaceAll("(.*?) .*", "$1").replaceAll(",", "."));
                        break;
                    case "davon Zucker":
                        sugar = Double.valueOf(val.replaceAll("(.*?) .*", "$1").replaceAll(",", "."));
                        break;
                    case "Fett":
                        fat = Double.valueOf(val.replaceAll("(.*?) .*", "$1").replaceAll(",", "."));
                        break;
                }
            }

            Food food = new Food(name, kcal, fat, carbs, sugar, protein, weight, portion);

            return Response.success(food, getCacheEntry());
        } catch (Exception e) {
            return Response.error(new VolleyError("parse error"));
        }
    }

    @Override
    protected void deliverResponse(Food response) {
        foodListener.onResponse(response);
    }
}
