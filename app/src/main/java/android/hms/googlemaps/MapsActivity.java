package android.hms.googlemaps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.preference.TwoStatePreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    LatLng AGENT;
    JSONObject agent_inside;
    List<Markers> markersList;
    JSONArray agent= null;
    JSONArray atm= null;
    JSONObject atm_inside;
    JSONObject jo_inside;
    JSONArray mr_jArry;
    LinearLayout llmarkerclick;
    Button btnCall;
    Button btnFeedBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        markersList = new ArrayList<>();
        llmarkerclick = findViewById(R.id.llforDeatil);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        // Phone Calling
        btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapsActivity.this, PhoneCallActivity.class);
                startActivity(intent);

            }
        });

        btnFeedBack = findViewById(R.id.btnFeedBack);
        btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(MapsActivity.this, FeedBackActivity.class);
                startActivity(intent1);

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        Log.i("ButtonClick","before starting Button");
        Button btnAGENT = findViewById(R.id.btnAgent);
        btnAGENT.setOnClickListener(new View.OnClickListener() {
            // String Agent = "hospital";
            @Override
            public void onClick(View v) {
                Log.d("ButtonClick", "ATM Button is Clicked");

//                Toast.makeText(getApplicationContext(),
//                        "Value of AGENT" +AGENT, Toast.LENGTH_LONG).show();

                mMap.clear();

                for (int k=0; k<agent.length(); k++) {

                    try {
                        agent_inside = agent.getJSONObject(k);
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.bgreen);

                        AGENT = new LatLng(Double.parseDouble(agent_inside.getString("lat")),
                                Double.parseDouble(agent_inside.getString("long")));

                        mMap.addMarker(new MarkerOptions().position(AGENT).title(agent_inside.getString("name"))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bgreen)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(AGENT));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        Button btnATM = findViewById(R.id.btnAtm);
        btnATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();
                for (int k=0; k<atm.length(); k++)
                {

                    try {
                        atm_inside = atm.getJSONObject(k);

                        LatLng ATM = new LatLng(Double.parseDouble(atm_inside.getString("lat")),
                                Double.parseDouble(atm_inside.getString("long")));

                        mMap.addMarker(new MarkerOptions().position(ATM).title(atm_inside.getString("name")));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(ATM));

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                llmarkerclick.setVisibility(View.VISIBLE);
                                return false;
                            }


                        });

                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                llmarkerclick.setVisibility(View.GONE);
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

        Button btnALL = findViewById(R.id.btnAll);
        btnALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();

                for (int k=0; k<agent.length(); k++)
                {

                    try {
                        agent_inside = agent.getJSONObject(k);
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.bgreen);

                        AGENT = new LatLng(Double.parseDouble(agent_inside.getString("lat")),
                                Double.parseDouble(agent_inside.getString("long")));

                    mMap.addMarker(new MarkerOptions().position(AGENT).title(agent_inside.getString("name"))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bgreen)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(AGENT));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                for (int k=0; k<atm.length(); k++)
                {

                    try {
                        atm_inside = atm.getJSONObject(k);
                        LatLng ATM = new LatLng(Double.parseDouble(atm_inside.getString("lat")),
                                Double.parseDouble(atm_inside.getString("long")));

                        mMap.addMarker(new MarkerOptions().position(ATM).title(atm_inside.getString("name")));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(ATM));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
              }

            }
        });

        // On Click CALL BUTTON


        // Reading New JSON File including Agents and ATMs

        try {
            Log.i("onClick","before starting array");
            JSONObject objm = new JSONObject(loadJSONFromAsset());
            // JSONObject obj2 = new JSONObject(loadJSONFromAsset());
            mr_jArry = objm.getJSONArray("markers");
            //JSONArray atm_jArry = obj2.getJSONArray("atm");

            Log.d("Markers Array size>>>",mr_jArry.length()+"");


            //    Log.d("AGArray size>>>",ag_jArry.length()+"");
            for (int i = 0; i < mr_jArry.length(); i++) {
                JSONObject jo_inside = mr_jArry.getJSONObject(i);
                Log.d("Agent Terminal", jo_inside.getString("agent"));
                Log.d("ATM Terminal", jo_inside.getString("atm"));


                for (int j=0; j <jo_inside.length(); j++) {
                    agent = jo_inside.getJSONArray("agent");
                    Log.d("Agents Array size>>>", agent.length() + "");


                }

                for (int k=0; k<agent.length(); k++)
                {

                    agent_inside = agent.getJSONObject(k);

                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.bgreen);

                    AGENT = new LatLng(Double.parseDouble(agent_inside.getString("lat")),
                            Double.parseDouble(agent_inside.getString("long")));

//                    mMap.addMarker(new MarkerOptions().position(AGENT).title(agent_inside.getString("name"))
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bgreen)));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(AGENT));

                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.bgreen));
////               .icon(icon);

                    Log.d("TerminalID", agent_inside.getString("terminalId"));
                    Log.d("Name", agent_inside.getString("name"));
                    Log.d("Address", agent_inside.getString("address"));
                    Log.d("Latitude", agent_inside.getString("lat"));
                    Log.d("Longitude", agent_inside.getString("long"));

                }


                for (int j=0; j <jo_inside.length(); j++) {
                    atm = jo_inside.getJSONArray("atm");
                    Log.d("ATM Array size>>>", atm.length() + "");

                }

                for (int k=0; k<atm.length(); k++)
                {

                    JSONObject atm_inside = atm.getJSONObject(k);
                    Log.d("TerminalID", atm_inside.getString("terminalId"));
                    Log.d("Name", atm_inside.getString("name"));
                    Log.d("Address", atm_inside.getString("address"));
                    Log.d("Latitude", atm_inside.getString("lat"));
                    Log.d("Longitude", atm_inside.getString("long"));

//                    LatLng ATM = new LatLng(Double.parseDouble(atm_inside.getString("lat")),
//                            Double.parseDouble(atm_inside.getString("long")));
//
//                    mMap.addMarker(new MarkerOptions().position(ATM).title(atm_inside.getString("name")));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ATM));

                }


            }

        } catch (JSONException e) {
            e.printStackTrace();

//        mMap.setOnMarkerClickListener(this);
//        mMap.setOnMapClickListener(this);



        }









        //Reading Json OLD JSON file

//        try {
//            Log.i("onClick", "before stating array");
//            JSONObject obj = new JSONObject(loadJSONFromAsset());
//            JSONArray m_jArry = obj.getJSONArray("markers");
//
//            Log.d("Array size>>>", m_jArry.length() + "");
//            for (int i = 0; i < m_jArry.length(); i++) {
//
//                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                // LatLng JS = new LatLng(16.817082, 96.130973);
//                //LatLng TW = new LatLng(16.785182, 96.142669);
////        mMap.addMarker(new MarkerOptions().position(TW).title("Tawwin Center"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(TW));
//
//                LatLng JS = new LatLng(Double.parseDouble(jo_inside.getString("lat")),
//                        Double.parseDouble(jo_inside.getString("long")));
//
//                mMap.addMarker(new MarkerOptions().position(JS).title(jo_inside.getString("name")));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(JS));
//
//                Log.d("TerminalID", jo_inside.getString("terminalId"));
//                Log.d("Name", jo_inside.getString("name"));
//                Log.d("Address", jo_inside.getString("address"));
//                Log.d("Latitude", jo_inside.getString("lat"));
//                Log.d("Longitude", jo_inside.getString("long"));
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(16.817082, 96.130973)));


//        LatLng JS = new LatLng(16.817082, 96.130973);
//        mMap.addMarker(new MarkerOptions().position(JS).title("Junction Square Shopping Mall"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(JS));
//        LatLng TW = new LatLng(16.785182, 96.142669);
//        mMap.addMarker(new MarkerOptions().position(TW).title("Tawwin Center"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(TW));
//        LatLng MP = new LatLng(16.828173, 96.155027);
//        mMap.addMarker(new MarkerOptions().position(MP).title("Market Place"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(MP));

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        System.out.println("My Current Lat Long Value" +location.getLatitude()+ ","+ location.getLongitude());
        Toast.makeText(getApplicationContext(),
                "My Current Lat Long Value" +location.getLatitude()+ ","+
                        location.getLongitude(), Toast.LENGTH_LONG).show();


        // To Get Nearest Places


        //System.out.println("Lat Long Value: "+location.getLongitude()+", "+location.getLatitude());
//        LatLng MP = new LatLng(16.828173, 96.155027);
//        mMap.addMarker(new MarkerOptions().position(MP).title("Market Place"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(MP));

//Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title("My Current Location (" +location.getLatitude()+ " ,"+ location.getLongitude() +")" );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("AGENT_ATM_Location_Update.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    // Click on Map
    @Override
    public void onMapClick(LatLng latLng) {
        llmarkerclick.setVisibility(View.GONE);

    }



    // Click on Marker

    @Override
    public boolean onMarkerClick(Marker marker) {

        llmarkerclick.setVisibility(View.VISIBLE);

//      Toast.makeText(getApplicationContext(),"On Clicking Marker" , Toast.LENGTH_LONG).show();
//              if(marker.equals( )){
//            Log.w("Click", "test");
//            llmarkerclick.setVisibility(View.VISIBLE);
//            return true;
//        }
      return false;
    }


//    private double distance(double lat1, double lon1, double lat2, double lon2) {
//        // haversine great circle distance approximation, returns meters
//
//        lat1 =mLastLocation.getLatitude();
//        lon1 =mLastLocation.getLongitude();
//       // Log.d("Longitude", jo_inside.getString("long"));
//
//        Log.d("CurLat", "lat1");
//        Log.d("CurLon", "lon1");
//
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
//                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
//                * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60; // 60 nautical miles per degree of seperation
//        dist = dist * 1852; // 1852 meters per nautical mile
//        return (dist);
//    }
//
//    private double deg2rad(double deg) {
//        return (deg * Math.PI / 180.0);
//    }
//
//    private double rad2deg(double rad) {
//        return (rad * 180.0 / Math.PI);
//    }


}