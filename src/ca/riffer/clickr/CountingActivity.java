package ca.riffer.clickr;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class CountingActivity extends Activity {
	private static final String TAG = "SetupActivity";
	private int buttonCount;
	private ArrayList<String> textArray = new ArrayList<String>();
	private ArrayList<Integer> colourPointers = new ArrayList<Integer>();

	// this should be moved out of here, and into a resource
	private ArrayList<Integer> colourValues;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ArrayList<Integer> activeButtons;
		// setContentView(R.layout.counting_page4);

		// this should be moved out of here, and into a resource
		colourValues = new ArrayList<Integer> (Arrays.asList(
				getResources().getColor(R.color.white), getResources().getColor(R.color.blue), getResources().getColor(R.color.green),
				getResources().getColor(R.color.orange), getResources().getColor(R.color.pink), getResources().getColor(R.color.violet)));
		final ArrayList<String> defaultButtonNames = new ArrayList<String> (Arrays.asList(
				"one", "two", "three", "four", "five", "six"));

		// retrieve values from the parent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			buttonCount = extras.getInt("buttonCount");
			textArray = extras.getStringArrayList("textArray");
			colourPointers = extras.getIntegerArrayList("colourPtrs");
		}

		if (buttonCount == 2) {
			setContentView(R.layout.counting_page2);
			activeButtons = new ArrayList<Integer> (Arrays.asList(
					R.id.button1, R.id.button2));

		} else if (buttonCount == 3) {
			setContentView(R.layout.counting_page3);
			activeButtons = new ArrayList<Integer> (Arrays.asList(
					R.id.button1, R.id.button2, R.id.button3));
			
		} else {
			setContentView(R.layout.counting_page4);
			activeButtons = new ArrayList<Integer> (Arrays.asList(
					R.id.button1, R.id.button2, R.id.button3, R.id.button4));
		}

		for (int i=0; i < activeButtons.size(); i++) {
			Button button = (Button) findViewById(activeButtons.get(i));
			if (textArray.get(i).length() == 0) {
				button.setText(defaultButtonNames.get(i));
			} else {
				button.setText(textArray.get(i));
			}
			button.setBackgroundColor(colourValues.get(colourPointers.get(0)));
			button.setOnTouchListener(myCountButtonTouchListener);
		}
	}

	Button.OnTouchListener myCountButtonTouchListener = new Button.OnTouchListener() {
		@Override
		public boolean onTouch(View theButton, MotionEvent theEvent) {
			Button clickOn = (Button) theButton;

			switch ( theEvent.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG, "action down");
				clickOn.setBackgroundColor(colourValues.get(colourPointers.get(0)));
				break;
			case MotionEvent.ACTION_UP:
				Log.i(TAG, "action up");
				clickOn.setBackgroundColor(colourValues.get(colourPointers.get(1)));
				break;
			}
			return true;

			// if (clickOn == button1) index = 0;
			// else if (clickOn == button2) index = 1;
			// else if (clickOn == button3) index = 2;
			// else if (clickOn == button4) index = 3;
			// else if (clickOn == button5) index = 4;
			// else if (clickOn == button6) index = 5;
		}
	};

}
