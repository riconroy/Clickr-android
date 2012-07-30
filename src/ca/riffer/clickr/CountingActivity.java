package ca.riffer.clickr;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class CountingActivity extends Activity {
	// private static final String TAG = "SetupActivity";
	private int buttonCount;
	private ArrayList<String> textArray = new ArrayList<String>();
	private ArrayList<Integer> colourPointers = new ArrayList<Integer>();

	// this should be moved out of here, and into a resource
	private ArrayList<Integer> colourValues;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.counting_page4);

		// retrieve values from the parent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			buttonCount = extras.getInt("buttonCount");
			textArray = extras.getStringArrayList("textArray");
			colourPointers = extras.getIntegerArrayList("colourPtrs");
		}

		// this should be moved out of here, and into a resource
		colourValues = new ArrayList<Integer> (Arrays.asList(
				getResources().getColor(R.color.white), getResources().getColor(R.color.blue), getResources().getColor(R.color.green),
				getResources().getColor(R.color.orange), getResources().getColor(R.color.pink), getResources().getColor(R.color.violet)));
		
		Button button1 = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		Button button3 = (Button) findViewById(R.id.button3);
		Button button4 = (Button) findViewById(R.id.button4);
		if (textArray.get(0) != "") {
			button1.setText(textArray.get(0));
		} else {
			button1.setText("one");
		}
		button1.setBackgroundColor(colourValues.get(colourPointers.get(0)));

		if (textArray.get(1) != "") {
			button2.setText(textArray.get(1));
		} else {
			button2.setText("two");
		}
		button2.setBackgroundColor(colourValues.get(colourPointers.get(1)));

		if (textArray.get(2) != "") {
			button3.setText(textArray.get(2));
		} else {
			button3.setText("three");
		}
		button3.setBackgroundColor(colourValues.get(colourPointers.get(2)));

		if (textArray.get(3) != "") {
			button4.setText(textArray.get(3));
		} else {
			button4.setText("four");
		} 
		button4.setBackgroundColor(colourValues.get(colourPointers.get(3)));
	}

}
