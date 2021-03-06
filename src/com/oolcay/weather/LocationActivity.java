package com.oolcay.weather;

import android.app.ListActivity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.oolcay.weather.Models.WeatherLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends ListActivity {

  private EditText addLocationEditText;
  private Context mContext;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.locations);
    mContext = this;

    DatabaseHandler databaseHandler = new DatabaseHandler(this);

    //TODO: customer array adapter, we need more than the name
    List<WeatherLocation> weatherLocations = databaseHandler.getAllLocations();
    List<String> locationsArry = new ArrayList<String>();

    for (int x=0; x< weatherLocations.size(); x++){
      locationsArry.add(weatherLocations.get(x).getName());
      setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationsArry ));

    }

    addLocationEditText = (EditText)findViewById(R.id.add_location);

    setupListeners();

  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id)
    ;
  }

  private void findLocation(String search){
    Geocoder geocoder = new Geocoder(this);
    Address address = null;
    List<Address> addresses;
    WeatherLocation weatherLocation = new WeatherLocation();

    try {
      addresses = geocoder.getFromLocationName(search, 1);
      if(addresses.size() > 0) {
        weatherLocation.setLatitude(addresses.get(0).getLatitude());
        weatherLocation.setLongitude(addresses.get(0).getLongitude());
        weatherLocation.setName(addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea());
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.addLocation(weatherLocation);
      }
    } catch (IOException e) {
      Log.e(getString(R.string.error_tag), e.toString());
    }
  }
  private void setupListeners(){
    addLocationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          findLocation(addLocationEditText.getText().toString());
          InputMethodManager inputManager = (InputMethodManager)
              getSystemService(Context.INPUT_METHOD_SERVICE);
          inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
              InputMethodManager.HIDE_NOT_ALWAYS);
          return true;
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
          Toast.makeText(mContext, "hi", Toast.LENGTH_LONG).show();
          findLocation(addLocationEditText.getText().toString());
          return true;
        }
        return false;
      }
    });
  }
}