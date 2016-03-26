package bo.dev.davidz.imageguess;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by DavidAhmad on 08/05/2015.
 */

public class CategoryParser {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private String jsonStr;

    public CategoryParser(String json) {
        jsonStr = json;
    }



    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     */
    public String[] getTopLevelCategories()
            throws JSONException {
        Log.v("JSON", jsonStr == null? "NULL":jsonStr);
        JSONObject json = new JSONObject(jsonStr);
        Iterator<String> iter = json.keys();

        ArrayList<String> categories = new ArrayList<String>();
        while (iter.hasNext()) {
            String key = iter.next();
            categories.add(key);
        }

        return categories.toArray(new String[categories.size()]);

    }

    public String[] getSubCategories(String category) throws JSONException{
        JSONObject json = new JSONObject(jsonStr);
        JSONObject subcategoriesJson = json.getJSONObject(category);
        boolean isItemCategory = false;

        Iterator<String> iter = subcategoriesJson.keys();
        ArrayList<String> subcategories = new ArrayList<String>();
        while (iter.hasNext()) {
            String key = iter.next();
            subcategories.add(key);
            if (key.equals("type")) {isItemCategory=true;}
        }
        if(isItemCategory) {
            JSONObject items = subcategoriesJson.getJSONObject("items");
            json = items;
            return null;
        }
        jsonStr = subcategoriesJson.toString();
        return subcategories.toArray(new String[subcategories.size()]);
    }
}