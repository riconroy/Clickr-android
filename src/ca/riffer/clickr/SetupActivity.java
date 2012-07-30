package ca.riffer.clickr;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class SetupActivity extends Activity {
	//private static final String TAG = "SetupActivity";
	RadioButton cat1, cat2, cat3, cat4, cat5, cat6;
	RelativeLayout llcat1, llcat2, llcat3, llcat4, llcat5, llcat6;
	View lldivider1, lldivider2, lldivider3, lldivider4, lldivider5, lldivider6;
	EditText layoutName, catName1, catName2, catName3, catName4, catName5, catName6; 
	Button buttonCat1, buttonCat2, buttonCat3, buttonCat4, buttonCat5, buttonCat6;
	
	int buttonCount;

	// maintain a list of different colours, and pointers to what those colours are (one for each button in setup activity)
	private ArrayList<Integer> colourValues;
	private ArrayList<Integer> colourPointers = new ArrayList<Integer> (Arrays.asList(
			0, 0, 0, 0, 0, 0));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);

		// choosing the number of categories
		cat1 = (RadioButton) findViewById(R.id.rad1);
		cat2 = (RadioButton) findViewById(R.id.rad2);
		cat3 = (RadioButton) findViewById(R.id.rad3);
		cat4 = (RadioButton) findViewById(R.id.rad4);
		cat5 = (RadioButton) findViewById(R.id.rad5);
		cat6 = (RadioButton) findViewById(R.id.rad6);
		cat1.setOnClickListener(myOptionOnClickListener);
		cat2.setOnClickListener(myOptionOnClickListener);
		cat3.setOnClickListener(myOptionOnClickListener);
		cat4.setOnClickListener(myOptionOnClickListener);
		cat5.setOnClickListener(myOptionOnClickListener);
		cat6.setOnClickListener(myOptionOnClickListener);
		
		// start off (arbitrarily) at 6
		buttonCount = 6;
		cat6.setChecked(true);

		// if fewer than 6 categories chosen, the un-needed ones can disappear
		llcat1 = (RelativeLayout) findViewById(R.id.layout_cat1);
		llcat2 = (RelativeLayout) findViewById(R.id.layout_cat2);
		llcat3 = (RelativeLayout) findViewById(R.id.layout_cat3);
		llcat4 = (RelativeLayout) findViewById(R.id.layout_cat4);
		llcat5 = (RelativeLayout) findViewById(R.id.layout_cat5);
		llcat6 = (RelativeLayout) findViewById(R.id.layout_cat6);
		lldivider1 = (View) findViewById(R.id.cat_divider1);
		lldivider2 = (View) findViewById(R.id.cat_divider2);
		lldivider3 = (View) findViewById(R.id.cat_divider3);
		lldivider4 = (View) findViewById(R.id.cat_divider4);
		lldivider5 = (View) findViewById(R.id.cat_divider5);
		lldivider6 = (View) findViewById(R.id.cat_divider6);

		// the clear button clears the values in the setup screen
		Button clearButton = (Button) findViewById(R.id.clear_layout);
		clearButton.setOnClickListener(myClearButtonClickListener);

		// the start button moves to the next screen (activity)
		Button startButton = (Button) findViewById(R.id.start);
		startButton.setOnClickListener(myStartButtonClickListener);
		
		// layout name, and name of each category
		layoutName = (EditText) findViewById(R.id.layout_name);
		catName1 = (EditText) findViewById(R.id.text_cat1);
		catName2 = (EditText) findViewById(R.id.text_cat2);
		catName3 = (EditText) findViewById(R.id.text_cat3);
		catName4 = (EditText) findViewById(R.id.text_cat4);
		catName5 = (EditText) findViewById(R.id.text_cat5);
		catName6 = (EditText) findViewById(R.id.text_cat6);

		// choosing the colours for the buttons
		buttonCat1 = (Button) findViewById(R.id.colours_cat1);
		buttonCat2 = (Button) findViewById(R.id.colours_cat2);
		buttonCat3 = (Button) findViewById(R.id.colours_cat3);
		buttonCat4 = (Button) findViewById(R.id.colours_cat4);
		buttonCat5 = (Button) findViewById(R.id.colours_cat5);
		buttonCat6 = (Button) findViewById(R.id.colours_cat6);
		buttonCat1.setOnClickListener(myColourListener);
		buttonCat2.setOnClickListener(myColourListener);
		buttonCat3.setOnClickListener(myColourListener);
		buttonCat4.setOnClickListener(myColourListener);
		buttonCat5.setOnClickListener(myColourListener);
		buttonCat6.setOnClickListener(myColourListener);

		// this should be moved out of here, and into a resource
		colourValues = new ArrayList<Integer> (Arrays.asList(
				getResources().getColor(R.color.white), getResources().getColor(R.color.blue), getResources().getColor(R.color.green),
				getResources().getColor(R.color.orange), getResources().getColor(R.color.pink), getResources().getColor(R.color.violet)));
	}

	RadioButton.OnClickListener myOptionOnClickListener = new RadioButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			RadioButton clickOn = (RadioButton) v;
			uncheckAllRadioButtons();
			clickOn.setChecked(true);
			// scroll to top
			ScrollView mainScroll = (ScrollView) findViewById(R.id.scroller);
			mainScroll.fullScroll(ScrollView.FOCUS_UP);

			if (clickOn == cat1) {
				llcat6.setVisibility(View.INVISIBLE);
				llcat5.setVisibility(View.INVISIBLE);
				llcat4.setVisibility(View.INVISIBLE);
				llcat3.setVisibility(View.INVISIBLE);
				llcat2.setVisibility(View.INVISIBLE);
				llcat1.setVisibility(0);
				lldivider6.setVisibility(View.INVISIBLE);
				lldivider5.setVisibility(View.INVISIBLE);
				lldivider4.setVisibility(View.INVISIBLE);
				lldivider3.setVisibility(View.INVISIBLE);
				lldivider2.setVisibility(View.INVISIBLE);
				lldivider1.setVisibility(0);
				buttonCount = 1;
				
			} else if (clickOn == cat2) {
				llcat6.setVisibility(View.INVISIBLE);
				llcat5.setVisibility(View.INVISIBLE);
				llcat4.setVisibility(View.INVISIBLE);
				llcat3.setVisibility(View.INVISIBLE);
				llcat2.setVisibility(0);
				llcat1.setVisibility(0);
				lldivider6.setVisibility(View.INVISIBLE);
				lldivider5.setVisibility(View.INVISIBLE);
				lldivider4.setVisibility(View.INVISIBLE);
				lldivider3.setVisibility(View.INVISIBLE);
				lldivider2.setVisibility(0);
				lldivider1.setVisibility(0);
				buttonCount = 2;

			} else if (clickOn == cat3) {
				llcat6.setVisibility(View.INVISIBLE);
				llcat5.setVisibility(View.INVISIBLE);
				llcat4.setVisibility(View.INVISIBLE);
				llcat3.setVisibility(0);
				llcat2.setVisibility(0);
				llcat1.setVisibility(0);
				lldivider6.setVisibility(View.INVISIBLE);
				lldivider5.setVisibility(View.INVISIBLE);
				lldivider4.setVisibility(View.INVISIBLE);
				lldivider3.setVisibility(0);
				lldivider2.setVisibility(0);
				lldivider1.setVisibility(0);
				buttonCount = 3;

			} else if (clickOn == cat4) {
				llcat6.setVisibility(View.INVISIBLE);
				llcat5.setVisibility(View.INVISIBLE);
				llcat4.setVisibility(0);
				llcat3.setVisibility(0);
				llcat2.setVisibility(0);
				llcat1.setVisibility(0);
				lldivider6.setVisibility(View.INVISIBLE);
				lldivider5.setVisibility(View.INVISIBLE);
				lldivider4.setVisibility(0);
				lldivider3.setVisibility(0);
				lldivider2.setVisibility(0);
				lldivider1.setVisibility(0);
				buttonCount = 4;

			} else if (clickOn == cat5) {
				llcat6.setVisibility(View.INVISIBLE);
				llcat5.setVisibility(0);
				llcat4.setVisibility(0);
				llcat3.setVisibility(0);
				llcat2.setVisibility(0);
				llcat1.setVisibility(0);
				lldivider6.setVisibility(View.INVISIBLE);
				lldivider5.setVisibility(0);
				lldivider4.setVisibility(0);
				lldivider3.setVisibility(0);
				lldivider2.setVisibility(0);
				lldivider1.setVisibility(0);
				buttonCount = 5;

			} else if (clickOn == cat6) {
				llcat6.setVisibility(0);
				llcat5.setVisibility(0);
				llcat4.setVisibility(0);
				llcat3.setVisibility(0);
				llcat2.setVisibility(0);
				llcat1.setVisibility(View.VISIBLE);
				lldivider6.setVisibility(0);
				lldivider5.setVisibility(0);
				lldivider4.setVisibility(0);
				lldivider3.setVisibility(0);
				lldivider2.setVisibility(0);
				lldivider1.setVisibility(0);
				buttonCount = 6;
			}
		}
	};

	public void uncheckAllRadioButtons() {
		cat1.setChecked(false);
		cat2.setChecked(false);
		cat3.setChecked(false);
		cat4.setChecked(false);
		cat5.setChecked(false);
		cat6.setChecked(false);
	}

	Button.OnClickListener myClearButtonClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			layoutName.setText("");
			catName1.setText("");
			catName2.setText("");
			catName3.setText("");
			catName4.setText("");
			catName5.setText("");
			catName6.setText("");
		}
	};

	// move to the next screen (activity): counting page
	Button.OnClickListener myStartButtonClickListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
            Intent myIntent = new Intent(SetupActivity.this, CountingActivity.class);
            myIntent.putExtra("buttonCount", buttonCount);
            myIntent.putExtra("textArray", new ArrayList<String> (Arrays.asList(
    				catName1.getText().toString(), catName2.getText().toString(), catName3.getText().toString(), catName4.getText().toString(), catName5.getText().toString(), catName6.getText().toString())));
            myIntent.putExtra("colourPtrs", colourPointers);
    		startActivityForResult(myIntent, 0);
		}
	};

	Button.OnClickListener myColourListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			Button clickOn = (Button) v;
			int index = 0;

			if (clickOn == buttonCat1) index = 0;
			else if (clickOn == buttonCat2) index = 1;
			else if (clickOn == buttonCat3) index = 2;
			else if (clickOn == buttonCat4) index = 3;
			else if (clickOn == buttonCat5) index = 4;
			else if (clickOn == buttonCat6) index = 5;

			int currentColour = colourPointers.get(index);
			currentColour++;
			if (currentColour >= colourValues.size())
				currentColour = 0;
			colourPointers.set(index, currentColour);
			clickOn.setBackgroundColor(colourValues.get(currentColour));
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_setup, menu);
		return true;
	}
}
