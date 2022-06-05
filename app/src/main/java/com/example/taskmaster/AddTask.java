package com.example.taskmaster;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AddTask extends AppCompatActivity {
    String[] states = {"new", "assigned", "in progress", "complete"};
    String[] teamsName = {"team1", "team2", "team3"};
    HashMap<String, String> teams = new HashMap<String, String>();
    public static final int REQUEST_CODE = 123;
    public static String IMGurl = null;

    private FusedLocationProviderClient fusedLocationClient;
    private int PERMISSION_ID = 44;

    private double latitude;
    private double longitude;

    private GoogleMap googleMap;

    private Button toggle;

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            LatLng coordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(coordinate)
                    .title("Marker"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12.0f));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// CALL CONFIGER CLASS
        configureAmplify();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                try {
                    handleSendImage(intent); // Handle single image being sent
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            // Handle other intents, such as being started from the home screen
        }

        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    int i = 0;
                    if (response.getData() != null) {
                        for (Team team : response.getData()) {
                            this.teams.put(team.getName(), team.getId());

                            Bundle bundle = new Bundle();
                            Message message = new Message();
                            message.setData(bundle);

                            Log.i(team.getName() + " NoTask SESS", "Query");
                        }
                    }


                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );


        setContentView(R.layout.activity_main2);
        Button myTasks = findViewById(R.id.submit);
        Button upload = findViewById(R.id.upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        Spinner spinTems = (Spinner) findViewById(R.id.teams);


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, states);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);


        ArrayAdapter teamAdpter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, teamsName);
        teamAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTems.setAdapter(teamAdpter);

        upload.setOnClickListener(view -> {
            pictureUpload();
        });


        myTasks.setOnClickListener(view -> {


            Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
            String selected = mySpinner.getSelectedItem().toString();

            Spinner mySpinnerTEAM = (Spinner) findViewById(R.id.teams);
            String selectedTeams = mySpinnerTEAM.getSelectedItem().toString();

            EditText title = findViewById(R.id.title);
            String titleName = title.getText().toString();

            EditText body = findViewById(R.id.body);
            String bodyName = body.getText().toString();

            if (IMGurl.equals(null)) {
                //AWS OBJECT
                Task item = Task.builder()
                        .title(titleName)
                        .description(bodyName)
                        .status(selected)
                        .teamTaskId(teams.get(selectedTeams))
                        .location(theLocation)
                        .build();
                // Data store save
                Amplify.DataStore.save(item,
                        success -> Log.i(TAG, "Saved item: " + success.item().getTitle()),
                        error -> Log.e(TAG, "Could not save item to DataStore", error)
                );

            } else {
                //AWS OBJECT
                Task item = Task.builder()
                        .title(titleName)
                        .description(bodyName)
                        .status(selected)
                        .teamTaskId(teams.get(selectedTeams))
                        .imageurl(IMGurl)
                        .location(theLocation)
                        .build();
                // Data store save
                Amplify.DataStore.save(item,
                        success -> Log.i(TAG, "Saved item: " + success.item().getTitle()),
                        error -> Log.e(TAG, "Could not save item to DataStore", error)
                );

            }


            // Data store query
            Amplify.DataStore.query(Task.class,
                    tasks -> {
                        while (tasks.hasNext()) {
                            Task task = tasks.next();

                            Log.i(TAG, "==== Task ====");
                            Log.i(TAG, "Name: " + task.getTitle());
                        }
                    },
                    failure -> Log.e(TAG, "Could not query DataStore", failure)
            );

            Amplify.DataStore.observe(Task.class,
                    started -> {
                        Log.i(TAG, "Observation began.");
                    },
                    change -> {
                        Log.i(TAG, change.item().toString());


                    },
                    failure -> Log.e(TAG, "Observation failed.", failure),
                    () -> Log.i(TAG, "Observation complete.")
            );


            // API save to backend
//            Amplify.API.mutate(
//                    ModelMutation.create(item),
//                    success -> Log.i(TAG, "Saved itemApi: " + success.getData().getTitle()),
//                    error -> Log.e(TAG, "Could not save item to API", error)
//            );


            //ROOM
//
//            Task task = new Task(titleName,bodyName,selected);
//            Long newStudentId = TasksDatabase.getInstance(getApplicationContext()).TasksDea().insertStudent(task);
//
            Context context = getApplicationContext();
            CharSequence text = "submit! " + titleName + " " + selected;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        });


    }


    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {

        }
    }

    void handleSendImage(Intent intent) throws IOException {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Log.e(TAG, "Upload failed " + " noooo");
            File file = BitMapHandler(imageUri, "hi.jpg");
            uploadFile(file, "hi.jpg");
        }

    }


    private void pictureUpload() {
        // Launches photo picker in single-select mode.
        // This means that the user can select one photo or video.

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_CODE);
    }

    private File BitMapHandler(Uri currentUri, String titleName) throws IOException {
        Bitmap bitmap = getBitmapFromUri(currentUri);

        File file = new File(getApplicationContext().getFilesDir(), titleName + ".jpg");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();
        return file;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EditText title = findViewById(R.id.title);
        String titleName = title.getText().toString();
        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            Log.e(TAG, "onActivityResult: Error getting image from device");
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE:
                // Get photo picker response for single select.
                Uri currentUri = data.getData();

                // Do stuff with the photo/video URI.
                Log.i(TAG, "onActivityResult: the uri is => " + currentUri);
                try {
                    File file = BitMapHandler(currentUri, titleName);
                    uploadFile(file, titleName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // upload to s3
                // uploads the file

                return;
        }
    }


    private void uploadFile(File file, String titleName) {


        Log.e(TAG, "Upload failed " + titleName);
        Amplify.Storage.uploadFile(
                titleName + ".jpg",
                file,
                result -> {
                    Log.i(TAG, "Successfully uploaded: " + result.getKey());
                    IMGurl = result.getKey();
                    changUploadBotonColor();
                },
                storageFailure -> Log.e(TAG, "Upload failed", storageFailure)

        );

    }

    public void changUploadBotonColor() {
        Button upload = findViewById(R.id.upload);
        upload.setText("uploded!");
        upload.setBackgroundColor(this.getResources().getColor(R.color.upload));
    }


    public void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }


    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then

//https://stackoverflow.com/questions/25483352/how-to-get-last-known-location-for-location-manager-in-android
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // permission was denied, show alert to explain permission
                    askPermission();
                } else {
                    //permission is granted now start a background service
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fetchLastLocation();
                    }
                }
            }
        }
    }

    private void askPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
    }

   private String theLocation;

    private String fetchLastLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions

                askPermission();

            }
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                           String X=  location.getLatitude()+","+ location.getLongitude();
                            locationhandler(X);
                         //   Log.e("LAS2T LOCATION: ", X);


                        }
                    }
                });



        return null;
    }
     private void locationhandler(String X){
        this.theLocation=X;
         Log.e("LASp8T LOCATION: ", this.theLocation);
     }


}