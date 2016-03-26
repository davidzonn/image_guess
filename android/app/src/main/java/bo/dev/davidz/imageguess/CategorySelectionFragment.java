package bo.dev.davidz.imageguess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class CategorySelectionFragment extends Fragment implements ServerAnswerListener<String> {

    ArrayAdapter<String> categoryAdapter;
    CategoryParser parser;

    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        Log.v("LifecicleTest", "ONSTART");
        setCategoryScreen();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_get_categories, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            setCategoryScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCategoryScreen() {
        String serverAddress = getResources().getString(R.string.server_address);
        new ServerJsonRequest(this, serverAddress).execute("categoriesAndItems.php");
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("LifecicleTest", "ONCREATE");
        //Executes BEFORE onCreateView (here we create, inflate, etc, everything else.
        //Report that we'll contribute to the creation of the options menu.
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayList<String> currentCategories = new ArrayList<String>();
        categoryAdapter = new ArrayAdapter <String> (getActivity(), R.layout.list_item_category, R.id.list_categories_textview,currentCategories);

        ListView categoryList = (ListView)rootView.findViewById(R.id.listview_categories);
        categoryList.setAdapter(categoryAdapter);

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);

                try {
                    String[] subcategories = parser.getSubCategories(item);
                    if (subcategories == null) {
                        //it was an item category, change Activity
                        Intent gameScreenIntent = new Intent(getActivity(), GameActivity.class);
                        gameScreenIntent.putExtra(Intent.EXTRA_TEXT, item);
                        startActivity(gameScreenIntent);
                    } else {
                        updateView(subcategories);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        return rootView;
    }

    @Override
    public void processServerAnswer(String json) {
        parser = new CategoryParser(json);
        try {
            String[] topLevelCategories = parser.getTopLevelCategories();
            updateView(topLevelCategories);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateView(String[] categories) {
        categoryAdapter.clear();
        for (String category: categories) {
            categoryAdapter.add(category);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("LifecicleTest", "PAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("LifecicleTest", "STOP");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("LifecicleTest", "RESUME");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("LifecicleTest", "DESTROY");
    }


}

