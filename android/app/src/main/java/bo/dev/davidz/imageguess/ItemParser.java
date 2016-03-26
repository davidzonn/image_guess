package bo.dev.davidz.imageguess;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by DavidAhmad on 10/05/2015.
 */
public class ItemParser {
    //item representation. Goes from name to URL.
    HashMap<String, String> items;
    Random randomGenerator;

    public ItemParser(String itemsJsonStr) {
        randomGenerator = new Random();
        items = new HashMap<>();

        try {
            JSONObject itemsJson = new JSONObject(itemsJsonStr);
            Iterator<String> iter = itemsJson.keys();
            while (iter.hasNext()) {
                String itemName = iter.next();
                JSONObject item = itemsJson.getJSONObject(itemName);
                String url = item.getString("image");
                items.put(itemName, url);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public String getURL(String itemName) {
        return items.get(itemName);
    }

    public String[] getRandom(int n) {
        String[] ans = new String[n];
        Set<String> names = items.keySet();
        ArrayList<String> list = new ArrayList<String>(names);
        Collections.shuffle(list, randomGenerator);
        for (int i = 0; i < n; i++) {
            ans[i] = list.get(i);
        }
        return ans;
    }
}
