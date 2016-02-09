package org.acm.sviet.schedulerama.hod;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.acm.sviet.schedulerama.CourseEditActivity;
import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.adapters.CourseAdapter;
import org.acm.sviet.schedulerama.models.Course;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CourseViewActivity extends AppCompatActivity {

    private RecyclerView courseRecycleView;
    private ArrayList<Course> courses;
    private CourseAdapter courseAdapter;
    private Context context = this;
    private final String TAG = "/CourseViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        courseRecycleView = (RecyclerView) findViewById(R.id.courseListRecycleView);
        getCourses();
        courseRecycleView.setLayoutManager(new GridLayoutManager(this, 2));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.CourseViewfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CourseViewActivity.this, CourseEditActivity.class));
            }
        });
    }


    private void getCourses(){
        courses = null;
        courseRecycleView.setAdapter(null);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://schedulerama-svietacm.rhcloud.com/API/course", null, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.v(TAG, "Successfull Response Gathering.");

                try {
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);
                    String err = obj.getString("error");
                    String status = obj.getString("status");

                    if (!status.equals("STATUS OK")) {
                        //error
                        Log.e(TAG, "Server Error : " + err);
                        Toast.makeText(getApplication(), err, Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "course gathered..");
                        JSONArray JSONcourses = obj.getJSONArray("courses");
                        courses = new ArrayList<>();
                        for(int i=0;i<JSONcourses.length();i++){
                            JSONObject course = JSONcourses.getJSONObject(i);
                            courses.add(new Course(
                                    course.getString("name"),
                                    course.getString("type"),
                                    course.getString("description"),
                                    course.getString("faculty"),
                                    course.getString("fac_contact"),
                                    course.getString("dept"),
                                    course.getString("sem"),
                                    course.getString("section"),
                                    course.getString("refBook"),
                                    course.getString("refBookLink")
                            ));
                        }
                        courseAdapter = new CourseAdapter(context,courses);
                        courseRecycleView.setAdapter(courseAdapter);


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
