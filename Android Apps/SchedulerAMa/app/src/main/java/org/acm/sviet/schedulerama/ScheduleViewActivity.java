package org.acm.sviet.schedulerama;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.acm.sviet.schedulerama.adapters.ScheduleAdapter;
import org.acm.sviet.schedulerama.customViews.NestedListView;
import org.acm.sviet.schedulerama.login.LoginActivity;
import org.acm.sviet.schedulerama.models.Course;
import org.acm.sviet.schedulerama.models.Schedule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ScheduleViewActivity extends AppCompatActivity/*
        implements NavigationView.OnNavigationItemSelectedListener*/{

    /*
    * TODO List:
    * -> convert json object into Schedule Model object.*done*
    * -> beautify the schedule list layout.
    * -> Add a Splash Screen.
    * -> fill the options menu
    * -> there is a problem in navigation drawer, giving error in my mi3, not in emulated device. find it. fix it.
    * */

    GoogleCloudMessaging gcm;
    String regId,PROJECT_NO="846898771125";

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;

    private Context context;
    private Animation fab_rot;
    private FloatingActionButton fab;
    private NestedListView schList;
    private Spinner deptSpinner, semSpinner, sectionSpinner;
    private SharedPreferences.Editor editor;

    SharedPreferences defaultSharedPreferences;

    private AppBarLayout appbarLayout;

    private String TAG = "[ScheduleViewActivity]";

    Calendar rightNow = Calendar.getInstance();
    int day = rightNow.get(Calendar.DAY_OF_WEEK)-1;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    ArrayList<Schedule> schduleData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appbarLayout = (AppBarLayout) findViewById(R.id.app_bar);


        SharedPreferences preferences;
        preferences = getSharedPreferences("dept_sem_sec_values", MODE_PRIVATE);
        editor = preferences.edit();
        schList = (NestedListView) findViewById(R.id.list_view_schedule);


        deptSpinner = (Spinner) findViewById(R.id.deptSpinner);
        semSpinner = (Spinner) findViewById(R.id.semSpinner);
        sectionSpinner = (Spinner) findViewById(R.id.sectionSpinner);

        String deptArray[] = getResources().getStringArray(R.array.dept_array);
        String semArray[] = getResources().getStringArray(R.array.sem_array);
        String sectionArray[] = getResources().getStringArray(R.array.section_array);

        List<String> deptData = new ArrayList<>(Arrays.asList(deptArray));
        List<String> semData = new ArrayList<>(Arrays.asList(semArray));
        List<String> sectionData = new ArrayList<>(Arrays.asList(sectionArray));

        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, R.id.textViewScheduleListLayout, deptData);
        ArrayAdapter<String> semAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, R.id.textViewScheduleListLayout, semData);
        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, R.id.textViewScheduleListLayout, sectionData);

        deptSpinner.setAdapter(deptAdapter);
        semSpinner.setAdapter(semAdapter);
        sectionSpinner.setAdapter(sectionAdapter);

        //dynamically setting selection value for spinner through SharedPrefrences.
        deptSpinner.setSelection(preferences.getInt(getResources().getString(R.string.dept_value_key), getResources().getInteger(R.integer.dept_value_default)));
        semSpinner.setSelection(preferences.getInt(getResources().getString(R.string.sem_value_key), getResources().getInteger(R.integer.sem_value_default)));
        sectionSpinner.setSelection(preferences.getInt(getResources().getString(R.string.section_value_key), getResources().getInteger(R.integer.section_value_default)));


        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt(getResources().getString(R.string.dept_value_key), position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing..
            }
        });

        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt(getResources().getString(R.string.sem_value_key), position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing..
            }
        });

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt(getResources().getString(R.string.section_value_key), position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing..
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Days value is : " + day);

                if(!checkNetworkConnection(view)){return;}

                if (deptSpinner.getSelectedItemPosition() == 0 || semSpinner.getSelectedItemPosition() == 0 || sectionSpinner.getSelectedItemPosition() == 0) {
                    Snackbar.make(view, "Please Select Department, Semester & Section Values.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (day == 6 || day == 0) {
                    Snackbar.make(view, "Saturdays and Sundays are vacations, No Schedule for today.", Snackbar.LENGTH_LONG).show();
                    return;

                }

                fab_rot = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate);
                fab.startAnimation(fab_rot);

                fetchSchedules((String) deptSpinner.getSelectedItem(), (String) semSpinner.getSelectedItem(), (String) sectionSpinner.getSelectedItem(), day);
            }
        });


        schList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Schedule schedule = schduleData.get(position);
                Intent intent = new Intent(ScheduleViewActivity.this, CourseDetailActivity.class);
                intent.putExtra("schedule", new Gson().toJson(schedule));
                startActivity(intent);


            }
        });
        //basic toggle setup for navigation drawer
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        //initialing the default Shared Preference gathering.
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //if DSS is already assigned, using Shared Preferences, auto click the fab button to gather schedule.
        autoFetchSchedules();

        Button loginButton = (Button) findViewById(R.id.hodLoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleViewActivity.this,LoginActivity.class));
            }
        });

    }

/*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule_view, menu);
        return true;
    }

 /*   @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            startActivity(new Intent(ScheduleViewActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_gitfork) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/codegauravg/DynamicTable/"));
            startActivity(intent);
        } else if (id == R.id.nav_api) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://schedulerama-svietacm.rhcloud.com"));
            startActivity(intent);
        } else if (id == R.id.nav_about) {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getResources().getString(R.string.web_add)));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ScheduleViewActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //function: auto fetch the Schedules from the CLOUD service.
    private void autoFetchSchedules() {
        //if DSS is not selected, then do nothing..
        if (deptSpinner.getSelectedItemPosition() == 0 || semSpinner.getSelectedItemPosition() == 0 || sectionSpinner.getSelectedItemPosition() == 0) {
            return;
        }
        //server side consider weekDay value for monday to be 1, and incremented so on.
        fab.callOnClick();
        getRegID();
        appbarLayout.setExpanded(false);

    }

    //function: fetch Schedule from the cloud and fill it in the listView.
    private void fetchSchedules(String dept, String sem, String section, int weekDay) {

        //if to enable GCM intent gathering or not.
        //TODO : find the implementations for this.
        boolean broadcast_bool = defaultSharedPreferences.getBoolean("broadcast_switch",false);


         //Connect to server, and get Schedule for today..
        final AsyncHttpClient client = new AsyncHttpClient();


        client.get("http://schedulerama-svietacm.rhcloud.com/API/schedule/" + dept + "/" + sem + "/" + section + "/" + weekDay, null, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                schduleData.clear();
                schList.setAdapter(null);

                Log.v(TAG, "Successfull Response Gathering.");

                try {
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);
                    String err = obj.getString("error");
                    String status = obj.getString("status");

                    if (status.equals("STATUS OK")) {

                        JSONArray repeatitions = obj.getJSONArray("repeatitions");

                        schduleData = bundleDataFromJSONArray(repeatitions);

                        //adding the acquired data set to the Custom Adapter and initialing the auto fill of listView.
                        ScheduleAdapter schAdapter = new ScheduleAdapter(getApplicationContext());
                        schAdapter.fillSchedules(schduleData);
                        schList.setAdapter(schAdapter);


                    } else {
                        //error
                        Log.e(TAG, "Server Error : " + err);
                        Toast.makeText(getApplication(), err, Toast.LENGTH_LONG).show();
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Unsuccessful Response Gathering.");

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    //function: helper function to bundle a JSONArray Object to an ArrayList of Schedule.
    private ArrayList<Schedule> bundleDataFromJSONArray(JSONArray repeatitions) throws JSONException, UnsupportedEncodingException {
        JSONObject repeatition;
        ArrayList<Schedule> list = new ArrayList<>();
        for (int i = 0; i < repeatitions.length(); i++) {
                repeatition = repeatitions.getJSONObject(i);
            int lectureNo = repeatition.getInt("lectureNo");
            JSONObject course = repeatition.getJSONObject("course");

            list.add(
                    new Schedule().addWeekDay(day).addLectureNo(lectureNo)
                            .addCourse(new Course()
                                            .addName(course.getString("name"))
                                            .addDept(course.getString("dept"))
                                            .addSem(course.getString("sem"))
                                            .addSection(course.getString("section"))
                                            .addType(course.getString("type"))
                                            .addDescription(course.getString("description"))
                                            .addFac_contact(course.getString("fac_contact"))
                                            .addFaculty(course.getString("faculty"))
                                            .addRefBook(course.getString("refBook"))
                                            .addRefBookLink(course.getString("refBookLink"))
                            )
            );
        }
        return list;
    }

    /**
     * Check whether the device is connected, and if so, whether the connection
     * is wifi or mobile (it could be something else).
     */
    private boolean checkNetworkConnection(View view) {
        // BEGIN_INCLUDE(connect)
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if(wifiConnected) {
                Log.i(TAG, getString(R.string.wifi_connection));
            } else if (mobileConnected){
                Log.i(TAG, getString(R.string.mobile_connection));
            }
            return true;
        } else {
            Log.i(TAG, getString(R.string.no_wifi_or_mobile));
            Snackbar.make(view,getString(R.string.no_wifi_or_mobile),Snackbar.LENGTH_LONG).show();
            return false;
        }
        // END_INCLUDE(connect)
    }

    //function: to gather the regId of  google play service for devices.
    private void getRegID(){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... params) {
                try{
                    if(gcm==null){
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcm.register(PROJECT_NO);
                    Log.e(TAG,regId);
                    return regId;
                }catch(IOException ioe){
                   Log.e(TAG,"error message : "+ioe.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                if(s==null){return;}
                SharedPreferences IDpreferences = getSharedPreferences("device_id_pref",Context.MODE_PRIVATE);
                boolean device_state = IDpreferences.getBoolean("device_state",false);
                if(!device_state){ uploadID(s);
                }

            }
        }.execute(null, null, null);
    }

    //function: add the device ID to the server.
    private void uploadID(String ID){
        if (deptSpinner.getSelectedItemPosition() == 0 || semSpinner.getSelectedItemPosition() == 0 || sectionSpinner.getSelectedItemPosition() == 0) {
            return;
        }

        String dept = (String) deptSpinner.getSelectedItem();
        String sem = (String) semSpinner.getSelectedItem();
        String section=(String) sectionSpinner.getSelectedItem();
        final AsyncHttpClient client = new AsyncHttpClient();

        client.post("http://schedulerama-svietacm.rhcloud.com/API/device/" +
                dept + "/" + sem + "/" + section + "/" + ID, null, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.v(TAG, "Successfull Response Gathering.");

                try {
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);
                    String err = obj.getString("error");
                    String status = obj.getString("status");

                    if (!status.equals("ADD OK")) {
                        //error
                        Log.e(TAG, "Server Error : " + err);
                        Toast.makeText(getApplication(), err, Toast.LENGTH_LONG).show();
                    } else {
                        SharedPreferences IDpreferences = getSharedPreferences("device_id_pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = IDpreferences.edit();
                        editor.putBoolean("device_state", true).apply();
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Unsuccessful Response Gathering.");

                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}