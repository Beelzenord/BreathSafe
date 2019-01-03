package com.breathsafe.kth.breathsafe.Maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.breathsafe.kth.breathsafe.Constants;
import com.breathsafe.kth.breathsafe.Model.DisplayOnMapList;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapsThingFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapsThingFragment";

    private MapView mMapView;
    private DisplayOnMapList displayOnMapList;
    private boolean clearMarkers;
    private TextView locationNameTextView;
    private TextView airQualityTextView;
    private Location currentLocation;
    private MenuItem favoriteMenu;


    public static MapsThingFragment newInstance() {
        return new MapsThingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_fragment, container, false);
        mMapView = view.findViewById(R.id.test_user_list_map);
        displayOnMapList = DisplayOnMapList.getInstance();
        displayOnMapList.setOnMapFalse();
        clearMarkers = false;
        setHasOptionsMenu(true);
        initGoogleMap(savedInstanceState);
        ConstraintLayout cl = view.findViewById(R.id.lul);
        cl.setBackgroundColor(getResources().getColor(R.color.colorGreyish));
        locationNameTextView = view.findViewById(R.id.location_name_view);
        airQualityTextView = view.findViewById(R.id.air_quality_view);
        ((MapActivity)getActivity()).checkForAirPollution();
        Button button = view.findViewById(R.id.go_to_comments_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: go to comment activity
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.maps_menu, menu);
        favoriteMenu = menu.findItem(R.id.menu_maps_favorite);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handles a click on the menu.
     * @param item The menu name to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_maps_clear :
                displayOnMapList.clearList();
                clearMarkers = true;
                mMapView.getMapAsync(this);
                break;
            case R.id.menu_maps_search :
                ((MapActivity)getActivity()).startSearchActivity();
                break;
            case R.id.menu_maps_favorite :
                if (currentLocation != null)
                    ((MapActivity)getActivity()).addToFavorites(currentLocation);
                break;
            case R.id.menu_maps_settings :

                break;
        }
        return true;
    }

    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(Constants.MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(Constants.MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    public void refreshMap() {
        mMapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onMapReady: failed");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (clearMarkers) {
            map.clear();
            doClearMarkers();
        }

        map.setMyLocationEnabled(true);
        List<Location> displayList = displayOnMapList.getList();
        for (Location l : displayList) {
            if (!l.isOnMap()) {
                l.setOnMap(true);
                String title = l.getName();
                Marker m = map.addMarker(new MarkerOptions().position(new LatLng(l.getLatitude(), l.getLongitude())).title(title));
                m.setTag(l);
            }
        }

        if (displayList.size() > 0) {
            Location latest = displayList.get(displayList.size() - 1);
            LatLng latLng = new LatLng(latest.getLatitude(), latest.getLongitude());
//            locationNameTextView.setText(l.getName());
            updateTextViews(latest);
            currentLocation = latest;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            map.animateCamera(CameraUpdateFactory.zoomIn());
            map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(latLng)      // Sets the center of the map to Mountain View
//                    .zoom(14)                   // Sets the zoom
//                    .bearing(0)                // Sets the orientation of the camera to east
//                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
//                    .build();                   // Creates a CameraPosition from the builder
//            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else {
            doClearMarkers();
            //TODO: Zoom to phones location
        }


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTag() != null) {
                    Location l = (Location) marker.getTag();
                    Log.i(TAG, "onMarkerClick: " + l.getName());
                    Log.i(TAG, "onMarkerClick: " + l.getAverageAQI());
                    updateTextViews(l);
                    currentLocation = l;
                    /*if (l.isFavorite())
                        favoriteMenu.setTitle(getResources().getString(R.string.menu_maps_title_remove_favorite));
                    else
                        favoriteMenu.setTitle(getResources().getString(R.string.menu_maps_title_add_favorite));*/
                }
                    marker.showInfoWindow();
                    return true;
            }
        });
        // if the map doesn't load..
        if (!mMapView.hasWindowFocus()) {
            Log.i(TAG, "onMapReady: NOT FOCUS");
            mMapView.getMapAsync(this);
        }
        else {
            Log.i(TAG, "onMapReady: HAS LOADED");
            Log.i(TAG, "onMapReady: Time: " + (System.currentTimeMillis() - Constants.getStart()));
        }
    }

    private void doClearMarkers() {
        clearMarkers = false;
        displayOnMapList.clearList();
        locationNameTextView.setText(getResources().getString(R.string.no_location_selected));
        airQualityTextView.setText(getResources().getString(R.string.no_location_selected));
        airQualityTextView.setTextColor(Color.BLACK);
    }

    private void updateTextViews(Location location) {
        locationNameTextView.setText(location.getName());
        setAirQuialityString(location.getAverageAQI());
    }

    private void setAirQuialityString(double averageAQI) {
//        Random r = new Random();
//        averageAQI = r.nextInt(250);
//        averageAQI = 80.0;
        if (averageAQI <= 0) {
            airQualityTextView.setText(getResources().getString(R.string.air_quality_string_not_found));
            airQualityTextView.setTextColor(Color.BLACK);
        }
        else if (averageAQI < 51) {
            airQualityTextView.setText(getResources().getString(R.string.air_quality_string_good));
            airQualityTextView.setTextColor(getResources().getColor(R.color.colorGreen, null));
        }
        else if (averageAQI < 101) {
            airQualityTextView.setText(getResources().getString(R.string.air_quality_string_moderate));
            airQualityTextView.setTextColor(getResources().getColor(R.color.colorYellow, null));
        }
        else if (averageAQI < 151) {
            airQualityTextView.setText(getResources().getString(R.string.air_quality_string_unhealthy_sensitive));
            airQualityTextView.setTextColor(getResources().getColor(R.color.colorOrange, null));
        }
        else if (averageAQI < 201) {
            airQualityTextView.setText(getResources().getString(R.string.air_quality_string_unhealthy));
            airQualityTextView.setTextColor(getResources().getColor(R.color.colorRed, null));
        }
        else if (averageAQI < 301) {
            airQualityTextView.setText(getResources().getString(R.string.air_quality_string_very_unhealthy));
            airQualityTextView.setTextColor(getResources().getColor(R.color.colorPurple, null));
        }
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}



















