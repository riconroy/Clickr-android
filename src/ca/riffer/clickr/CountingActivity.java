package ca.riffer.clickr;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class CountingActivity extends Activity {
	// private static final String TAG = "SetupActivity";
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
	
	// for now, a timestamp for the first count
	Date startCountTime = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
	 *  Menus
	 */
	 
    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.clickr_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_email:
            	// prepare the email
            	String subj;
            	if (layout_name.equals("")) {
            		subj = "Clickr Data";
            	} else {
            		subj = "Clickr Data: " + layout_name;
            	}
            	// String myTo = "rick.conroy@bellmedia.ca";
            	// String myTo = "riconroy@gmail.com";
            	
            	// send the email (using the favourite email program)
            	Intent intent = new Intent(Intent.ACTION_SEND);
            	intent.setType("text/plain");
            	// intent.putExtra(Intent.EXTRA_EMAIL, new String[] {myTo});
            	intent.putExtra(Intent.EXTRA_SUBJECT, subj);
            	intent.putExtra(Intent.EXTRA_TEXT, prepareEmailBody());

            	startActivity(Intent.createChooser(intent, "Send Email"));
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
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
				
				// mark start time if needed
				if (startCountTime == null)
					startCountTime = new Timestamp(System.currentTimeMillis());
				break;
			}
			return true;
		}
	};
	
	// user clicked on "reset counters" button
	public void resetCounters(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to reset the counters?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   catCounters = new ArrayList<Integer> (Arrays.asList(0, 0, 0, 0, 0, 0));
		        	   setAllTextCounters();
		        	   startCountTime = null; // reset starting time
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
			button.setText(textArray.get(i) + " \n" + catCounters.get(i));
		}
	}
	
	private String prepareEmailBody() {
		// replacing tab (\t) with four spaces. Lame!
		String s1 = "Layout Name: " + layout_name + "\n";
		String s2 = "Number of categories: " + buttonCount + "\n";
		String s3 = "Starting time: " + startCountTime + "\n";
		Date nowTime = new Timestamp(System.currentTimeMillis());
		String s4 = "Sending time: " + nowTime + "\n\n";
		String s5 = "";
		for (int i = 0; i < buttonCount; i++) {
			s5 += "Category " + (i+1) + ": " + textArray.get(i) + "\n";
			s5 += "    Count: " + catCounters.get(i) + "\n";
		}
		
		// JSON version
		String s10 = "\n/* ***************\n *  JSON String\n */\n";
		String s20 = "{\n";
		String s21 = "    \"layoutName\" : \"" + layout_name + "\",\n";
		String s22 = "    \"categoryCount\" : " + buttonCount + ",\n";
		String s23 = "    \"startTimestamp\" : \"" + startCountTime + "\",\n";
		String s24 = "    \"sendTimestamp\" : \"" + nowTime + "\",\n";
		String s25 = "    \"data\" : [\n";
		String s30 = "";
		for (int i = 0; i < buttonCount; i++) {
			s30 += "        {\n";
			s30 += "            \"categoryNumber\" : " + (i+1) + ",\n";
			s30 += "            \"categoryName\" : \"" + textArray.get(i) + "\",\n";
			s30 += "            \"categoryCount\" : " + catCounters.get(i) + "\n";
			if (i < (buttonCount - 1)) {
				s30 += "        },\n";
			} else {
				s30 += "        }\n";
			}
		}
		String s32 = "    ]\n";
		String s33 = "}\n";
		String s40 = "/* ***************\n *  end JSON String\n */";
		
		return s1 + s2 +s3 + s4 + s5 + s10 + s20 + s21 + s22 + s23 + s24 + s25 + s30 + s32 + s33 + s40;
	}

}
