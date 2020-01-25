package de.seibushin.nutrigo.request;

import android.text.Html;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchRequest extends Request<List<FoodLink>> {
    public static final String TAG = "searchFood";
    private static final String SEARCH = "https://fddb.info/xml/ac/1/de/";

    private static final String CHARSET = "ISO-8859-1";

    private final Response.Listener<List<FoodLink>> searchListener;

    public SearchRequest(String query, Response.Listener<List<FoodLink>> listener) throws UnsupportedEncodingException {
        super(Method.GET, SEARCH + URLEncoder.encode(query, CHARSET), null);
        setTag(TAG);

        this.searchListener = listener;
    }

    @Override
    protected Response<List<FoodLink>> parseNetworkResponse(NetworkResponse response) {
        try {
            String html = new String(response.data, CHARSET);

            List<FoodLink> links = new ArrayList<>();
            Document doc = Jsoup.parse(html);
            for (Element element : doc.select("table tr")) {
                if (element.hasClass("aclista") || element.hasClass("aclistb")) {
                    try {
                        String name = Html.fromHtml(element.select("td + td div").text()).toString();
                        String link = element.select("td + td").attr("onclick").replaceAll(".*?href='(.*?)';.*", "$1");
                        String img = "https:" + element.select("td img").attr("src").replaceAll("16x16", "48x48");

                        links.add(new FoodLink(name, link, img));
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
            return Response.success(links, getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Response.error(new VolleyError("parse error"));
    }

    @Override
    protected void deliverResponse(List<FoodLink> response) {
        searchListener.onResponse(response);
    }

}
