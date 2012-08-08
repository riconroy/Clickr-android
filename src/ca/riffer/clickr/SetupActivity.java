package ca.riffer.clickr;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class SetupActivity extends Activity {
	private static final String TAG = "SetupActivity";
	
	private int[] allRads = {R.id.rad1, R.id.rad2, R.id.rad3, R.id.rad4, R.id.rad5, R.id.rad6};
	private int[] allCatNames = {R.id.text_cat1, R.id.text_cat2, R.id.text_cat3, R.id.text_cat4, R.id.text_cat5, R.id.text_cat6};
	private int[] allColours = {R.id.colours_cat1, R.id.colours_cat2, R.id.colours_cat3, R.id.colours_cat4, R.id.colours_cat5, R.id.colours_cat6};

	// maintain a counter for each button (not needed until CountingActivity, but start with a zeroed array)
	private ArrayList<Integer> catCounters = new ArrayList<Integer> (Arrays.asList(0, 0, 0, 0, 0, 0));

	// this is where we'll store the colour variables
	private TypedArray colourValues;

	int buttonCount = 6;
	private EditText layout_name;

	// maintain a list of pointers to each button's current colour
	private ArrayList<Integer> colourPointers = new ArrayList<Integer> (Arrays.asList(0, 0, 0, 0, 0, 0));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);

		// choosing the number of categories (by radio buttons)
		for (int i = 0; i < allRads.length; i++) {
			findViewById(allRads[i]).setOnClickListener(myOptionOnClickListener);
		}
		
		// store the layout name for saving
		layout_name = (EditText) findViewById(R.id.layout_name);

		// the clear button clears the values in the setup screen
		findViewById(R.id.clear_layout).setOnClickListener(myClearButtonClickListener);

		// the start button moves to the next screen (activity)
		findViewById(R.id.start).setOnClickListener(myStartButtonClickListener);

		// choosing the colours for the buttons
		for (int i = 0; i < allColours.length; i++) {
			findViewById(allColours[i]).setOnClickListener(myColourListener);
		}

		// we'll need the colours array from the resources
		colourValues = getResources().obtainTypedArray(R.array.colors);

		// retrieve values from the parent
		EditText layout_name = (EditText) findViewById(R.id.layout_name);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			buttonCount = extras.getInt("buttonCount");
			ArrayList<String> textArray = extras.getStringArrayList("textArray");
			colourPointers = extras.getIntegerArrayList("colourPtrs");
			catCounters = extras.getIntegerArrayList("catCounters");
			layout_name.setText(extras.getString("layout_name"));
			resetCategoryNames(textArray);
			changeCatCount(buttonCount);
		}

		// Check whether we're recreating a previously destroyed instance
		if (savedInstanceState != null) {
			// Restore value of members from saved state
			
			buttonCount = savedInstanceState.getInt("buttonCount");
			colourPointers = savedInstanceState.getIntegerArrayList("colourPointers");
			layout_name.setText(savedInstanceState.getString("layout_name"));
			catCounters = savedInstanceState.getIntegerArrayList("catCounters");
			ArrayList<String> catNames = savedInstanceState.getStringArrayList("catNames");
			resetCategoryNames(catNames);
			changeCatCount(buttonCount);
		}
		resetColourButtons(colourPointers);

		RadioButton rad = (RadioButton) findViewById(allRads[buttonCount - 1]);
		rad.setChecked(true);
	}

	/**
	 *  Listeners for Save, Open, and Clear buttons
	 */

	// save button saves the current layout (after checking with a dialog)
	public void saveLayout(View v) {
		// we also shouldn't accept a name with a comma in it!
		if (layout_name.getText().toString().equals("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please name the layout before saving it.")
			.setCancelable(false)
			.setPositiveButton("okay", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Do you want to save this layout under the name \"" + layout_name.getText().toString() + "\"?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// first get the layout "file" from user prefs
					JSONArray layoutArray = new JSONArray();
					SharedPreferences settings = getPreferences(MODE_PRIVATE);
					String layouts = settings.getString("layouts_string", "NA");
					if (layouts.equals("NA")) {
						Log.i(TAG, "user prefs \"layouts\" not found");
					} else {
						// exists; change to JSON
						try {
							JSONArray oldLayoutArray = new JSONArray(layouts);
							for (int i = 0; i < oldLayoutArray.length(); i++) {
								layoutArray.put(oldLayoutArray.get(i));
							}
						} catch (JSONException e) {
							Log.e(TAG, "trouble converting layout file to JSON array");
							e.printStackTrace();
						}
					}

					// create a new JSON object for the array
					JSONObject jo = new JSONObject();
					try {
						jo.put("title", layout_name.getText().toString());
						jo.put("catNames", getCategoryNamesAsString());
						jo.put("catColours", getColourPointersAsString());
						jo.put("catCount", buttonCount);

						layoutArray.put(jo);

					} catch (JSONException e) {
						Log.e(TAG, "error in creating JSON Object");
						e.printStackTrace();
					}

					// finally, write the JSONArray (as a string) back out to the user prefs
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("layouts_string", layoutArray.toString());
					// Commit the edits!
					editor.commit();
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
	}

	// open button gets all the layouts, and allows user to choose one
	public void loadLayout(View v) {
		final ArrayList<String> titles = new ArrayList<String>();
		
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        String layouts = settings.getString("layouts_string", "NA");
        if (layouts.equals("NA")) {
			Log.i(TAG, "user prefs \"layouts\" not found");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("There are no saved layouts.")
			.setCancelable(false)
			.setPositiveButton("okay", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			
        } else {
        	// exists; change to JSON
        	final JSONArray layoutArray = new JSONArray();
        	try {
				JSONArray storedArray = new JSONArray(layouts);
			    for (int i = 0; i < storedArray.length(); i++) {
			    	JSONObject eachLayout = (JSONObject) storedArray.get(i);
			    	if (eachLayout.has("title")) {
			    		titles.add(eachLayout.getString("title"));
			    	}
					layoutArray.put(storedArray.get(i));
			    }
			} catch (JSONException e) {
				Log.e(TAG, "trouble converting layout file to JSON array, or JSONArray to JSONObjects");
				e.printStackTrace();
			}
        	
        	// make a dialog with all the titles			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick a layout");
			builder.setItems(titles.toArray(new CharSequence[titles.size()]), new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int chosen) {
			        // Toast.makeText(getApplicationContext(), titles.get(chosen), Toast.LENGTH_SHORT).show();
			    	JSONObject desiredLayout = new JSONObject();
					try {
						desiredLayout = layoutArray.getJSONObject(chosen);
						buttonCount = desiredLayout.getInt("catCount");
						String colourPtrsString = desiredLayout.getString("catColours");
						colourPointers = stringToArrayListInteger(colourPtrsString, ",");
						layout_name.setText(desiredLayout.getString("title"));
						// we should reset the counters
						catCounters = new ArrayList<Integer> (Arrays.asList(0, 0, 0, 0, 0, 0));
						String catNamesString = desiredLayout.getString("catNames");
						String[] catNamesArray = catNamesString.split(",", -1);
						ArrayList<String> catNames = new ArrayList<String>(Arrays.asList(catNamesArray));

						resetCategoryNames(catNames);
						changeCatCount(buttonCount);
						resetColourButtons(colourPointers);

						uncheckAllRadioButtons();
						RadioButton rad = (RadioButton) findViewById(allRads[buttonCount-1]);
						rad.setChecked(true);
						
					} catch (JSONException e) {
						Log.e(TAG, "problem getting JSON object in loadLayout");
						e.printStackTrace();
					}
			    	
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
        }
	}

	// clear button clears all text fields (and.. ?)
	Button.OnClickListener myClearButtonClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			final int[] allTexts = {R.id.layout_name, R.id.text_cat1, R.id.text_cat2, R.id.text_cat3, R.id.text_cat4, R.id.text_cat5, R.id.text_cat6};
			for (int i = 0; i < allTexts.length; i++) {
				EditText name = (EditText) findViewById(allTexts[i]);
				name.setText("");
			}
			// reset the counters and the colours
			catCounters = new ArrayList<Integer> (Arrays.asList(0, 0, 0, 0, 0, 0));
			colourPointers = new ArrayList<Integer> (Arrays.asList(0, 0, 0, 0, 0, 0));
			resetColourButtons(colourPointers);
			buttonCount = 6;
			changeCatCount(buttonCount);
			uncheckAllRadioButtons();
			RadioButton rad = (RadioButton) findViewById(allRads[buttonCount-1]);
			rad.setChecked(true);
		}
	};

	/**
	 *  Other Listeners
	 */

	// radio buttons change the number of categories needed
	RadioButton.OnClickListener myOptionOnClickListener = new RadioButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			RadioButton clickOn = (RadioButton) v;
			uncheckAllRadioButtons();
			clickOn.setChecked(true);
			// scroll to top
			ScrollView mainScroll = (ScrollView) findViewById(R.id.scroller);
			mainScroll.fullScroll(ScrollView.FOCUS_UP);
			
			// find which radio button
			for (int i = 0; i < allRads.length; i++) {
				if (clickOn.getId() == allRads[i]) {
					buttonCount = i+1;
					changeCatCount(buttonCount);
					break;
				}
			}
		}
	};

	// this cycles through the colours for each category
	Button.OnClickListener myColourListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Button clickOn = (Button) v;
			int index = 0;

			final int[] allTexts = {R.id.colours_cat1, R.id.colours_cat2, R.id.colours_cat3, R.id.colours_cat4, R.id.colours_cat5, R.id.colours_cat6};
			for (int i = 0; i < allTexts.length; i++) {
				if (clickOn.getId() == allTexts[i]) {
					index = i;
					break;
				}
			}

			int currentColour = colourPointers.get(index);
			currentColour++;
			if (currentColour >= colourValues.length())
				currentColour = 0;
			colourPointers.set(index, currentColour);
			clickOn.setBackgroundColor(colourValues.getColor(currentColour, 0));
		}
	};

	// move to the next screen (activity): counting page
	Button.OnClickListener myStartButtonClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(SetupActivity.this, CountingActivity.class);
			EditText layout_name = (EditText) findViewById(R.id.layout_name);
			
			myIntent.putExtra("buttonCount", buttonCount);
			myIntent.putExtra("textArray", getCategoryNames());
			myIntent.putExtra("colourPtrs", colourPointers);
	        myIntent.putExtra("catCounters", catCounters);
			myIntent.putExtra("layout_name", layout_name.getText().toString());
			startActivityForResult(myIntent, 0);
		}
	};

	/**
	 *  Menus
	 */

	//	@Override
	//	public boolean onCreateOptionsMenu(Menu menu) {
	//		getMenuInflater().inflate(R.menu.activity_setup, menu);
	//		return true;
	//	}

	/**
	 *  Saving and Restoring State
	 */

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		EditText layout_name = (EditText) findViewById(R.id.layout_name);

		savedInstanceState.putInt("buttonCount", buttonCount);
		savedInstanceState.putIntegerArrayList("colourPointers", colourPointers);
		savedInstanceState.putIntegerArrayList("catCounters", catCounters);
		savedInstanceState.putString("layout_name", layout_name.getText().toString());
		savedInstanceState.putStringArrayList("catNames", getCategoryNames());
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		EditText layout_name = (EditText) findViewById(R.id.layout_name);

		buttonCount = savedInstanceState.getInt("buttonCount");
		colourPointers = savedInstanceState.getIntegerArrayList("colourPointers");
		catCounters = savedInstanceState.getIntegerArrayList("catCounters");
		layout_name.setText(savedInstanceState.getString("layout_name"));
		ArrayList<String> catNames = savedInstanceState.getStringArrayList("catNames");
		resetColourButtons(colourPointers);
		resetCategoryNames(catNames);
		changeCatCount(buttonCount);
	}

	/**
	 *  Helper Routines
	 */

	private void uncheckAllRadioButtons() {
		for (int i = 0; i < allRads.length; i++) {
			RadioButton rad = (RadioButton) findViewById(allRads[i]);
			rad.setChecked(false);
		}
	}

	private void changeCatCount(int count) {
		final int[] allCats = {R.id.layout_cat1, R.id.layout_cat2, R.id.layout_cat3, R.id.layout_cat4, R.id.layout_cat5, R.id.layout_cat6};
		final int[] allDivs = {R.id.cat_divider1, R.id.cat_divider2, R.id.cat_divider3, R.id.cat_divider4, R.id.cat_divider5, R.id.cat_divider6};
		
		for (int i = 0; i < count; i++) {
			RelativeLayout llCat = (RelativeLayout) findViewById(allCats[i]);
			View llDiv = (View) findViewById(allDivs[i]);
			
			llCat.setVisibility(View.VISIBLE);
			llDiv.setVisibility(View.VISIBLE);
		}
		for (int i = count; i < allCats.length; i++) {
			RelativeLayout llCat = (RelativeLayout) findViewById(allCats[i]);
			View llDiv = (View) findViewById(allDivs[i]);
			
			llCat.setVisibility(View.INVISIBLE);
			llDiv.setVisibility(View.INVISIBLE);
		}
	}

	private void resetColourButtons(ArrayList<Integer> ptrs) {
		for (int i = 0; i < allColours.length; i++) {
			findViewById(allColours[i]).setBackgroundColor(colourValues.getColor(ptrs.get(i), 0));
		}
	}

	// returns all six category names in an arrayList
	private ArrayList<String> getCategoryNames() {
		ArrayList<String> output = new ArrayList<String>();
		for (int i = 0; i < allCatNames.length; i++) {
			EditText catName = (EditText) findViewById(allCatNames[i]);
			output.add(catName.getText().toString());
		}
		return output;
	}

	// returns all six category names in a String
	private String getCategoryNamesAsString() {
		String output = "";
		for (int i = 0; i < allCatNames.length; i++) {
			if (! output.equals("")) {
				output += ",";
			}
			EditText catName = (EditText) findViewById(allCatNames[i]);
			output += catName.getText().toString();
		}
		return output;
	}

	// returns all six colour pointers as a String
	private String getColourPointersAsString() {
		String output = "";
		for (int i = 0; i < colourPointers.size(); i++) {
			if (! output.equals("")) {
				output += ",";
			}
			Integer theNumber = colourPointers.get(i);
			output += theNumber.toString();
		}
		return output;
	}
	
	// given a string, return an ArrayList<Integer>
	private ArrayList<Integer> stringToArrayListInteger(String incoming, String limiter) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		String[] stringArr = incoming.split(limiter, -1);
		// Log.i(TAG, "stringArr = " + Arrays.toString(stringArr));
		for (String s:stringArr) {
			int inty = Integer.valueOf(s);
			output.add(inty);
		}
		return output;
	}

	private void resetCategoryNames(ArrayList<String> newCatNames) {
		for (int i = 0; i < allCatNames.length; i++) {
			EditText catName = (EditText) findViewById(allCatNames[i]);
			catName.setText(newCatNames.get(i));
		}
	}
}
