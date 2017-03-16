package com.busmonk.activities.mapview;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.busmonk.R;
import com.busmonk.config.Config;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.busmonk.R.id.map;

public class MapviewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar mToolbar;

    private GoogleMap gMap;
    GeoApiContext context;

    private static final int PERMISSION_REQUEST_CODE = 200;


    //Input data for this activity
    private String routeId;
    private String pickupStopName;
    private double pickupLat;
    private double pickupLong;
    private String dropStopName;
    private double dropLat;
    private double dropLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        context = new GeoApiContext();
        context.setApiKey(Config.GOOGLE_API_KEY);

        //Read the input
        routeId = getIntent().getStringExtra("routeId");

        pickupStopName = getIntent().getStringExtra("pickupStopName");
        pickupLat  = getIntent().getDoubleExtra("pickupLat", -1);
        pickupLong = getIntent().getDoubleExtra("pickupLong", -1);

        dropStopName = getIntent().getStringExtra("dropStopName");
        dropLat  = getIntent().getDoubleExtra("dropLat", -1);
        dropLong = getIntent().getDoubleExtra("dropLong", -1);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Move the camera
        //LatLngBounds b = new LatLngBounds(new LatLng(12.960642, 77.641725), new LatLng(12.927810, 77.680985));
        //LatLng bangl = new LatLng(12.9716, 77.5946);
        //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(b.getCenter(), 0));
        //gMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        //Check user location permission
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
        else
        {
            gMap.setMyLocationEnabled(true);
        }

        showRouteOnMap();

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
                            gMap.setMyLocationEnabled(true);
                        }
                    }
                }
        }
    }

    private void showRouteOnMap()
    {
        try {

            DirectionsResult result = DirectionsApi.getDirections(context, pickupLat + "," + pickupLong,
                                        dropLat + "," + dropLong).await();
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.geodesic(true);
            lineOptions.width(14);
            lineOptions.color(Color.CYAN);
            DirectionsRoute[] routes = result.routes;
            DirectionsRoute route = routes[0];//the first route is selected
            for (DirectionsLeg leg : route.legs)
            {
                for (DirectionsStep step : leg.steps)
                {
                    List<com.google.maps.model.LatLng> latLngList = step.polyline.decodePath();
                    for (com.google.maps.model.LatLng ll : latLngList)
                    {
                        lineOptions.add(new LatLng(ll.lat, ll.lng));
                    }
                }
            }
            if (lineOptions != null) {

                gMap.addPolyline(lineOptions);

            }
            //Add marker for pickup & drop
            MarkerOptions markerOptions_pickup = new MarkerOptions();
            markerOptions_pickup.position(new LatLng(pickupLat, pickupLong));
            markerOptions_pickup.title(pickupStopName);
            IconGenerator icnGenerator = new IconGenerator(this);
            icnGenerator.setContentPadding(2, 0, 2, 0);
            icnGenerator.setColor(Color.WHITE);
            icnGenerator.setContentRotation(90);
            Bitmap iconBitmap = icnGenerator.makeIcon(pickupStopName);
            //markerOptions_pickup.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_green", 100, 100)));
            markerOptions_pickup.icon(BitmapDescriptorFactory.fromBitmap(iconBitmap));
            gMap.addMarker(markerOptions_pickup);

            MarkerOptions markerOptions_drop = new MarkerOptions();
            markerOptions_drop.position(new LatLng(dropLat, dropLong));
            markerOptions_drop.title(dropStopName);
            IconGenerator icnGenerator1 = new IconGenerator(this);
            icnGenerator1.setContentPadding(10, 10, 10, 10);
            icnGenerator1.setColor(Color.WHITE);
            icnGenerator1.setContentRotation(90);
            Bitmap iconBitmap1 = icnGenerator1.makeIcon(dropStopName);
            //markerOptions_drop.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_red", 100, 100)));  //    .fromBitmap(iconBitmap1));
            markerOptions_drop.icon(BitmapDescriptorFactory.fromBitmap(iconBitmap1));
            gMap.addMarker(markerOptions_drop);


            //Moving the camera to show the route path
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            /*for(Stop s : stopList)
            {
                builder.include(new LatLng(s.getLattitude(), s.getLongitude()));
            }*/
            builder.include(new LatLng(pickupLat, pickupLong));
            builder.include(new LatLng(dropLat, dropLong));

            LatLngBounds bounds = builder.build();
            int width   = getResources().getDisplayMetrics().widthPixels;
            int height  = new Double(getResources().getDisplayMetrics().heightPixels * .8).intValue();
            int padding = (int) (width * 0.20); // offset from edges of the map 12% of screen
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            //gMap.animateCamera(cu);
            gMap.moveCamera(cu);


            //

            /*Circle circle = gMap.addCircle(new CircleOptions()
                    .center(new LatLng(pickupLat, pickupLong))
                    .radius(75)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.GREEN));

            circle = gMap.addCircle(new CircleOptions()
                    .center(new LatLng(dropLat, dropLong))
                    .radius(75)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.RED));*/

        }catch(Exception e)
        {
            Log.e("MAP ", e.getMessage());
        }

    }


    private Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showMessageInSB(String msg)
    {
        //Snackbar snackbar = Snackbar.make(containerLayout, msg, Snackbar.LENGTH_LONG);
        //snackbar.show();

    }


}
