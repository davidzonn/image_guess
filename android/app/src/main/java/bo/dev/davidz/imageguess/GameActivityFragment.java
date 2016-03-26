package bo.dev.davidz.imageguess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


/**
 * A placeholder fragment containing a simple view.
 */
public class GameActivityFragment extends Fragment implements ServerAnswerListener<String>{
    //Parser
    ItemParser parser;

    //UI Elements
    TextView[] options = new TextView[3];
    ImageView image;

    //Program Logic variables.
    public static final int NUMBER_OF_SOLUTIONS = 3;
    String currentCategory;
    String correctAnswer;
    Random randomNumberGenerator;

    public GameActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        randomNumberGenerator = new Random();
        String serverAddress = getActivity().getString(R.string.server_address);
        Activity activity = getActivity();
        Intent intent = activity.getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String categoryName = intent.getStringExtra(Intent.EXTRA_TEXT);
            activity.setTitle(categoryName);
            currentCategory = categoryName;
        }

        new ServerJsonRequest(this, serverAddress).execute("items.php", currentCategory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        Activity activity = getActivity();
        image = (ImageView)rootView.findViewById(R.id.item_image);
        for (int i = 0; i < NUMBER_OF_SOLUTIONS; i++) {
            options[0] = (TextView)rootView.findViewById(R.id.item_option_1);
            options[1] = (TextView)rootView.findViewById(R.id.item_option_2);
            options[2] = (TextView)rootView.findViewById(R.id.item_option_3);
        }


        return rootView;
    }


    public void updateCategoryScreen() {
        int answerNumber = randomNumberGenerator.nextInt(NUMBER_OF_SOLUTIONS);

        String[] randomItems = parser.getRandom(NUMBER_OF_SOLUTIONS);
        setTextViews(randomItems);

        String url = parser.getURL(randomItems[answerNumber]);

        correctAnswer = randomItems[answerNumber];

        setImageView(url);
    }

    private void setTextViews(String[] itemNames) {
        for (int i = 0; i < NUMBER_OF_SOLUTIONS; i++) {
            options[i].setText(itemNames[i]);
            options[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itemClicked = ((TextView)v).getText().toString();
                    int duration = Toast.LENGTH_SHORT;
                    String toastDescription = itemClicked.equals(correctAnswer)?"Well Done!":"Nope, that was " + correctAnswer + ", not " + itemClicked;
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastDescription, duration);
                    toast.show();
                    updateCategoryScreen();
                }
            });
        }
    }


    @Override
    public void processServerAnswer(String answer) {
        parser = new ItemParser(answer);
        updateCategoryScreen();
    }

    private void setImageView(String path) {
        String url = getActivity().getString(R.string.server_address);
        new ServerImageRequest(image
            /*new ServerAnswerListener<Bitmap>() {
                @Override
                public void processServerAnswer(Bitmap answer) {
                    Log.v("IMAGE", answer.toString());
                    image.setImageBitmap(answer);
                }
            }*/
        ).execute(url, path);
    }
}
