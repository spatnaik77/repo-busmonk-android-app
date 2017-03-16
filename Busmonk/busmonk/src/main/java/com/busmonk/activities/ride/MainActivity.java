package com.busmonk.activities.ride;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.busmonk.R;
import com.busmonk.activities.login.MobileActivity;
import com.busmonk.activities.mybus.BusStatusActivity;
import com.busmonk.activities.mybus.MyBusActivity;
import com.busmonk.activities.navdrawer.AboutusActivity;
import com.busmonk.activities.navdrawer.FragmentDrawer;
import com.busmonk.activities.profile.MyprofileActivity;
import com.busmonk.config.Config;
import com.busmonk.session.SessionManager;
import com.busmonk.session.SharedPreferenceManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.GeoApiContext;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener
{
    //private AutoCompleteTextView source;
    //private AutoCompleteTextView destination;
    //private Button pickerBtn;
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    //private PlacesAutoCompleteAdapter mPlacesAdapter;

    Place sourcePlace;
    Place destinationPlace;

    Button btnNext;

    LocationRequest locationRequest;

    GeoApiContext context;

    private CoordinatorLayout coordinatorLayout;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;


    private static final int PERMISSION_REQUEST_CODE = 200;

    private static final LatLngBounds BOUNDS_OF_BANGALORE = new LatLngBounds(
            new LatLng(12.848534, 77.334558), new LatLng(13.134597, 77.795567));//South-West & North-East

    SharedPreferenceManager preferenceManager;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        context = new GeoApiContext();
        context.setApiKey(Config.GOOGLE_API_KEY);
        //context.setQueryRateLimit(100, 0);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        //source autocomplete fragment
        PlaceAutocompleteFragment sourceAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.source_autocomplete_fragment);
        sourceAutocompleteFragment.setBoundsBias(BOUNDS_OF_BANGALORE);
        sourceAutocompleteFragment.setHint("Home");
        sourceAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place)
            {
                sourcePlace = place;
            }

            @Override
            public void onError(Status status)
            {
                // TODO: Handle the error.
                showMessageInSB(status.toString());
            }
        });

        //destination autocomplete fragment
        PlaceAutocompleteFragment destinationAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.destination_autocomplete_fragment);
        destinationAutocompleteFragment.setBoundsBias(BOUNDS_OF_BANGALORE);
        destinationAutocompleteFragment.setHint("Office");
        destinationAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place)
            {
                destinationPlace = place;
            }

            @Override
            public void onError(Status status)
            {
                // TODO: Handle the error.
                showMessageInSB(status.toString());
            }
        });

        btnNext = (Button)findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sourcePlace == null || destinationPlace == null)
                {
                    showMessageInSB("Select Home and Office");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), RoutesActivity.class);

                intent.putExtra("sourceLat", sourcePlace.getLatLng().latitude);
                intent.putExtra("sourceLongt", sourcePlace.getLatLng().longitude);
                intent.putExtra("destLat", destinationPlace.getLatLng().latitude);
                intent.putExtra("destLongt", destinationPlace.getLatLng().longitude);
                intent.putExtra("title", "Select Home To Office");
                intent.putExtra("userRouteTag", "home-office");
                intent.putExtra("action", 1);
                startActivity(intent);
            }
        });

        preferenceManager = SharedPreferenceManager.getInstance(this);
        sessionManager = SessionManager.getInstance();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        LatLng bangl = new LatLng(12.9716, 77.5946);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bangl));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        //Check user location permission
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
        else
        {
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0)
                {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(locationAccepted)
                    {
                        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        {
                            mMap.setMyLocationEnabled(true);
                        }
                    }
                }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(30000);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }


    @Override
    public void onLocationChanged(Location location) {

        if(location == null)
        {
            Toast.makeText(this, "Cannot fetch location", Toast.LENGTH_LONG);
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mMap.addMarker(markerOptions);*/

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }


    }
    @Override
    public void onDrawerItemSelected(View view, int position)
    {
        //{"Bus status", "Change bus timing", "My profile", "Logout", "About Us"};
        switch (position)
        {
            //Bus status
            case 0:
                Intent intent = new Intent(getApplicationContext(), BusStatusActivity.class);
                startActivity(intent);
                break;

            //Change bus timing
            case 1:
                intent = new Intent(getApplicationContext(), MyBusActivity.class);
                intent.putExtra("action", 1);
                startActivity(intent);
                break;

            //My profile
            case 2:
                intent = new Intent(getApplicationContext(), MyprofileActivity.class);
                startActivity(intent);
                break;

            //Logout
            case 3:
                sessionManager.removeUser();
                preferenceManager.removeAuthToken();

                //Todo the view is not refreshing - bug to // FIXME: 12/9/16
                intent = new Intent(getApplicationContext(), MobileActivity.class);
                //clear activity stack
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;

            //About us
            case 4:
                intent = new Intent(getApplicationContext(), AboutusActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void showMessageInSB(String msg)
    {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();

    }
}
