package ca.riffer.clickr;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class CountingActivity extends Activity {
	private static final String TAG = "SetupActivity";
	private int buttonCount;
	private String layout_name;
	private ArrayList<String> textArray = new ArrayList<String>();
	private ArrayList<Integer> colourPointers = new ArrayList<Integer>();
	private boolean countBackwards = false;
	
	// this is where we'll store the colour variables
	private TypedArray colourValues, colourValuesLight;
	private Drawable defaultDrawable;

	// maintain a counter for each button
	private ArrayList<Integer> catCounters = new ArrayList<Integer> (Arrays.asList(0, 0, 0, 0, 0, 0));

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "creating counting activity");
		
		final ArrayList<Integer> activeButtons;
		
		// we'll need the colours array from the resources
		colourValues = getResources().obtainTypedArray(R.array.colors);
		colourValuesLight = getResources().obtainTypedArray(R.array.colors_light);

		// retrieve values from the parent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			buttonCount = extras.getInt("buttonCount");
			textArray = extras.getStringArrayList("textArray");
			colourPointers = extras.getIntegerArrayList("colourPtrs");
			catCounters = extras.getIntegerArrayList("catCounters");
			layout_name = extras.getString("layout_name");
		}
		   
	    // Check whether we're recreating a previously destroyed instance
	    if (savedInstanceState != null) {
	        // Restore value of members from saved state
			  buttonCount = savedInstanceState.getInt("buttonCount");
			  catCounters = savedInstanceState.getIntegerArrayList("catCounters");
			  textArray = savedInstanceState.getStringArrayList("textArray");
	    }

	    // number of categories the user chose determines which content view we use 
		if (buttonCount == 1) {
			setContentView(R.layout.counting_page1);
			activeButtons = new ArrayList<Integer> (Arrays.asList(
					R.id.button1));

		} else if (buttonCount == 2) {
				setContentView(R.layout.counting_page2);
				activeButtons = new ArrayList<Integer> (Arrays.asList(
						R.id.button1, R.id.button2));

		} else if (buttonCount == 3) {
			setContentView(R.layout.counting_page3);
			activeButtons = new ArrayList<Integer> (Arrays.asList(
					R.id.button1, R.id.button2, R.id.button3));

		} else if (buttonCount == 4) {
			setContentView(R.layout.counting_page4);
			activeButtons = new ArrayList<Integer> (Arrays.asList(
					R.id.button1, R.id.button2, R.id.button3, R.id.button4));

		} else if (buttonCount == 5) {
			setContentView(R.layout.counting_page5);
			activeButtons = new ArrayList<Integer> (Arrays.asList(
					R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5));
			
		} else {
			setContentView(R.layout.counting_page6);
			activeButtons = new ArrayList<Integer> (Arrays.asList(
					R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6));
		}
		
		// if the buttons don't have user-given names, give generic 1-6 names
		final ArrayList<String> defaultButtonNames = new ArrayList<String> (Arrays.asList(
				"one", "two", "three", "four", "five", "six"));
		for (int i=0; i < textArray.size(); i++) {
			if (textArray.get(i).length() == 0) {
				textArray.set(i, defaultButtonNames.get(i));
			}
		}

		// set the name, colour and listeners for each counting button
		for (int i=0; i < activeButtons.size(); i++) {
			Button button = (Button) findViewById(activeButtons.get(i));
			if (textArray.get(i).length() == 0) {
				button.setText(defaultButtonNames.get(i));
			} else {
				button.setText(textArray.get(i));
			}
			button.setBackgroundColor(colourValues.getColor(colourPointers.get(i), 0));
			button.setOnTouchListener(myCountButtonTouchListener);
		}
		
		// put the counting numbers into the button names
	    setAllTextCounters();
	}
	
	/**
	 *  Listeners
	 */

	// all this, to make the button slightly lighter upon touch! (must be an easier way..)
	//   probably this: http://developer.android.com/guide/topics/resources/color-list-resource.html
	Button.OnTouchListener myCountButtonTouchListener = new Button.OnTouchListener() {
		@Override
		public boolean onTouch(View theButton, MotionEvent theEvent) {
			int index = 0;
			switch (theButton.getId()) {
			case (R.id.button1):
				index = 0;
			break;
			case (R.id.button2):
				index = 1;
			break;
			case (R.id.button3):
				index = 2;
			break;
			case (R.id.button4):
				index = 3;
			break;
			case (R.id.button5):
				index = 4;
			break;
			case (R.id.button6):
				index = 5;
			break;
			}

			Button clickOn = (Button) theButton;
			switch ( theEvent.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				clickOn.setBackgroundColor(colourValuesLight.getColor(colourPointers.get(index), 0));
				break;
			case MotionEvent.ACTION_UP:
				clickOn.setBackgroundColor(colourValues.getColor(colourPointers.get(index), 0));
				if (countBackwards) {
					catCounters.set(index, catCounters.get(index) - 1);
			    	Button minus = (Button) findViewById(R.id.minus);
		        	minus.setBackgroundDrawable(defaultDrawable);
					countBackwards = false;
				} else {
					catCounters.set(index, catCounters.get(index) + 1);
				}
				clickOn.setText(textArray.get(index) + "\n" + catCounters.get(index));
				break;
			}
			return true;
		}
	};
	
	// user clicked on "reset counters" button
	public void resetCounters(View view) {
		Log.i(TAG, "reset button clicked on");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to reset the counters?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   catCounters = new ArrayList<Integer> (Arrays.asList(0, 0, 0, 0, 0, 0));
		        	   setAllTextCounters();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}

	// user clicked on "setup" button
	public void backToSetup(View view) {
		Log.i(TAG, "setup button clicked on");
        Intent myIntent = new Intent(CountingActivity.this, SetupActivity.class);
        myIntent.putExtra("buttonCount", buttonCount);
        myIntent.putExtra("textArray", textArray);
        myIntent.putExtra("catCounters", catCounters);
        myIntent.putExtra("colourPtrs", colourPointers);
        myIntent.putExtra("layout_name", layout_name);
		startActivityForResult(myIntent, 0);
	}

	// user clicked on "minus" button
	public void reverseNextCount(View view) {
		Log.i(TAG, "minus button clicked on");
    	Button minus = (Button) findViewById(R.id.minus);
    	
        if (countBackwards) {
        	countBackwards = false;
        	minus.setBackgroundDrawable(defaultDrawable);
        } else {
        	countBackwards = true;
        	defaultDrawable = minus.getBackground();
        	minus.setBackgroundColor(Color.RED);
        }
	}
	
	/**
	 *  Saving and Restoring State
	 */
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		
		savedInstanceState.putInt("buttonCount", buttonCount);
		savedInstanceState.putIntegerArrayList("catCounters", catCounters);
		savedInstanceState.putStringArrayList("textArray", textArray);
		savedInstanceState.putString("layout_name", layout_name);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		  super.onRestoreInstanceState(savedInstanceState);

		  buttonCount = savedInstanceState.getInt("buttonCount");
		  catCounters = savedInstanceState.getIntegerArrayList("catCounters");
		  textArray = savedInstanceState.getStringArrayList("textArray");
		  layout_name = savedInstanceState.getString("layout_name");
		  setAllTextCounters();
	}
	
	/**
	 *  Helper Routines
	 */

	private void setAllTextCounters() {
		final int[] allButtons = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6};
		for (int i = 0; i < buttonCount; i++) {
			Button button = (Button) findViewById(allButtons[i]);
			Log.i(TAG, "i=" + i + "..");
			Log.i(TAG, "i=" + i + "..text=" + textArray.get(i) + "..");
			Log.i(TAG, "i=" + i + "..text=" + textArray.get(i) + "..counter=" + catCounters.get(i));
			button.setText(textArray.get(i) + " \n" + catCounters.get(i));
		}
	}

}
