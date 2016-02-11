package org.acm.sviet.schedulerama.utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.adapters.CourseAdapter;
import org.acm.sviet.schedulerama.adapters.ScheduleRecycleViewAdapter;
import org.acm.sviet.schedulerama.interfaces.Refresh;
import org.acm.sviet.schedulerama.models.Course;
import org.acm.sviet.schedulerama.models.Schedule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Ref;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.entity.EntityBuilder;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * Created by Anurag on 09-02-2016.
 *
 * API connection and service utility class.
 *
 * TODO : create all tasks in the API service generation. *DONE*
 */
public class ApiUtil {

    //enumeration of all tasks for this API service provider class.
    public enum TASK{
        GET_COURSES,
        GET_SCHEDULE_FOR_WEEKDAY,
        DELETE_COURSE,
        PUT_NEW_COURSE,
        UPDATE_COURSE,
        ADD_REPEATITION_TO_COURSE,
        DELETE_REPEATITION_FROM_COURSE,
        GET_SCHEDULE_COURSE_OPTIONS
    }

    private final static String baseUrl = "http://schedulerama-svietacm.rhcloud.com/API/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String TAG = "SCAMA/APIutils";

    public static void sync(Context context,Refresh refresh,TASK task,Course course) throws UnsupportedEncodingException {
        switch (task){
            case PUT_NEW_COURSE: putCourse(context,course);
                break;
            case UPDATE_COURSE: updateCourse(context,course);
                break;
            case DELETE_COURSE: deleteCourse(context,refresh,course);
            default:
        }
    }

    public static void sync(Context context,Refresh refresh,TASK task, RecyclerView view,String dept,String sem,String section,int weekDay){
        if(task.equals(TASK.GET_SCHEDULE_FOR_WEEKDAY)) {
            getSchedule(context,refresh, view, dept, sem, section, weekDay);
        }
    }

    public static void sync(Context context,TASK task,Spinner spinner,String dept,String sem,String section,int weekDay,int lectureNo){
        if(task.equals(TASK.GET_SCHEDULE_COURSE_OPTIONS)){
            getOptions(context, spinner, dept, sem, section, weekDay, lectureNo);
        }
    }

    public static void sync(Context context,Refresh refresh,TASK task,RecyclerView view){
        if(task.equals(TASK.GET_COURSES)){
            getCourses(context,refresh, view);
        }
    }

    public static void sync(Context context,Refresh refresh,TASK task, String courseName,int lectureNo,int weekDay){
        if(task.equals(TASK.ADD_REPEATITION_TO_COURSE)){
            addRepeatition(context,refresh, courseName, lectureNo, weekDay);
        }
        else if(task.equals(TASK.DELETE_REPEATITION_FROM_COURSE)){
            deleteRepeatition(context,refresh, courseName, lectureNo, weekDay);
        }
    }


    //function: API Utility function to delete course from service database.
    private static void deleteCourse(final Context context, final Refresh refresh,Course course){
        client.delete(baseUrl + "course/" + course.getName(), null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v(TAG, "Successfull Response Gathering.");

                try {
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);
                    String err = obj.getString("error");
                    String status = obj.getString("status");

                    if (!status.equals("DELETE OK")) {
                        //error
                        Log.e(TAG, "Server Error : " + err);
                        Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG,"course deleted.");
                        //callback to the course list view page to update the page again.
                        refresh.callback();
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failedResponseReport(context,statusCode);
            }
        });
    }

    //function: API Utility function to put a new course in service database.
    private static void putCourse(final Context context,Course course) throws UnsupportedEncodingException {
        StringEntity entity = new StringEntity(new Gson().toJson(course));
        client.put(context, baseUrl + "course", entity, "application/json", new AsyncHttpResponseHandler() {
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
                        Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG,"course added.");
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failedResponseReport(context,statusCode);
            }
        });
    }

    //function: API utility function to update course present in service database.
    private static void updateCourse(final Context context,Course course) throws UnsupportedEncodingException {
        StringEntity entity = new StringEntity(new Gson().toJson(course));

        client.post(context, baseUrl + "course/update", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v(TAG, "Successfull Response Gathering.");

                try {
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);
                    String err = obj.getString("error");
                    String status = obj.getString("status");

                    if (!status.equals("UPDATE OK")) {
                        //error
                        Log.e(TAG, "Server Error : " + err);
                        Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG,"course updated.");
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failedResponseReport(context,statusCode);
            }
        });


    }

    //function: API Utility function to gather optional courses available for a specific lectureNo.
    private static void getOptions(final Context context,final Spinner spinner,String dept,String sem,String section,int weekDay,int lectureNo){
        client.get(baseUrl+"options/"+dept+"/"+sem+"/"+section+"/"+weekDay+"/"+lectureNo
                , null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                spinner.setAdapter(null);
                ArrayList<String> optionsData = new ArrayList<>();

                Log.v(TAG, "Successfull Response Gathering.");

                try{
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);
                    String err = obj.getString("error");
                    String status = obj.getString("status");

                    if (status.equals("STATUS OK")) {

                        JSONArray courses = obj.getJSONArray("courses");

                        for(int i=0;i<courses.length();i++){
                            JSONObject course = courses.getJSONObject(i);
                            optionsData.add(course.getString("name"));
                        }

                        //adding the acquired data set to the Custom Adapter and initialing the auto fill of listView.
                        ArrayAdapter<String> optionsAdapter = new ArrayAdapter<>(context, R.layout.edit_spinner_layout,R.id.textViewEditScheduleListLayout,optionsData);
                        spinner.setAdapter(optionsAdapter);


                    } else {
                        //error
                        Log.e(TAG, "Server Error : " + err);
                        Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failedResponseReport(context,statusCode);
            }
        });
    }

    //function: utility function to add repeatition to specific schedule.
    private static void deleteRepeatition(final Context context, final Refresh refresh,String courseName,int lectureNo,int weekDay){
        client.delete("http://schedulerama-svietacm.rhcloud.com/API/repeatition/" + courseName + "/" + weekDay + "/" + lectureNo
                , null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v(TAG, "Successfull Response Gathering.");

                try {
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);
                    String err = obj.getString("error");
                    String status = obj.getString("status");

                    if (status.equals("DELETE OK")) {
                        refresh.callback();
                    } else {
                        //error
                        Log.e(TAG, "Server Error : " + err);
                        Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failedResponseReport(context, statusCode);
            }
        });
    }

    //function: utility function to add repeatition to specific schedule.
    private static void addRepeatition(final Context context, final Refresh refresh,String courseName,int lectureNo,int weekDay){
        client.post("http://schedulerama-svietacm.rhcloud.com/API/repeatition/"+courseName+"/"+weekDay+"/"+lectureNo
                , null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.v(TAG, "Successfull Response Gathering.");

                try{
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);
                    String err = obj.getString("error");
                    String status = obj.getString("status");

                    if (status.equals("UPDATE OK")) {
                        refresh.callback();
                    } else {
                        //error
                        Log.e(TAG, "Server Error : " + err);
                        Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failedResponseReport(context,statusCode);
            }
        });
    }

    //function: utility function to gather all courses data.
    private static void getCourses(final Context context, final Refresh refresh,final RecyclerView view){
        client.get("http://schedulerama-svietacm.rhcloud.com/API/course", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                view.setAdapter(null);
                ArrayList<Course> courseData = new ArrayList<>();

                Log.v(TAG, "Successfull Response Gathering.");

                try{
                String response = new String(responseBody, "UTF-8");

                JSONObject obj = new JSONObject(response);
                String err = obj.getString("error");
                String status = obj.getString("status");

                if (status.equals("STATUS OK")) {

                    JSONArray courses = obj.getJSONArray("courses");

                    courseData = bundleCourseDataFromJSONArray(courses);

                    //adding the acquired data set to the Custom Adapter and initialing the auto fill of listView.
                    CourseAdapter adapter = new CourseAdapter(context,refresh,courseData);
                    view.setAdapter(adapter);


                } else {
                    //error
                    Log.e(TAG, "Server Error : " + err);
                    Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                }

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failedResponseReport(context,statusCode);
            }
        });
    }

    //private utility functions: get Schedule from API for a weekDay and specific DSS value.
    private static void getSchedule(final Context context, final Refresh refresh, final RecyclerView view,final String dept,final String sem,final String section, final int weekDay){

        client.get("http://schedulerama-svietacm.rhcloud.com/API/schedule/"+dept+"/"+sem+"/"+section+"/"+weekDay
                , null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                view.setAdapter(null);
                ArrayList<Schedule> scheduleData = new ArrayList<>();

                Log.v(TAG, "Successfull Response Gathering.");

                try {
                    String response = new String(responseBody, "UTF-8");

                    JSONObject obj = new JSONObject(response);
                    String err = obj.getString("error");
                    String status = obj.getString("status");

                    if (status.equals("STATUS OK")) {

                        JSONArray repeatitions = obj.getJSONArray("repeatitions");

                        scheduleData = bundleScheduleDataFromJSONArray(repeatitions);

                        //adding the acquired data set to the Custom Adapter and initialing the auto fill of listView.
                        ScheduleRecycleViewAdapter adapter = new ScheduleRecycleViewAdapter(context,refresh,scheduleData,dept,sem,section,weekDay);
                        view.setAdapter(adapter);


                    } else {
                        //error
                        Log.e(TAG, "Server Error : " + err);
                        Toast.makeText(context, err, Toast.LENGTH_LONG).show();

                        view.setAdapter(new ScheduleRecycleViewAdapter(context,refresh,scheduleData,dept,sem,section,weekDay));
                    }

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                failedResponseReport(context,statusCode);
            }
        });

    }

    //general function to response on the failed response gathering scenario.
    private static void failedResponseReport(Context context, int statusCode){
        Log.v(TAG, "Unsuccessful Response Gathering.");

        // When Http response code is '404'
        if (statusCode == 404) {
            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
        }
        // When Http response code is '500'
        else if (statusCode == 500) {
            Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
        }
        // When Http response code other than 404, 500
        else {
            Toast.makeText(context, "Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
        }

    }

    //function: helper function to bundle a JSONArray Object to an ArrayList of Schedule.
    private static ArrayList<Schedule> bundleScheduleDataFromJSONArray(JSONArray repeatitions) throws JSONException, UnsupportedEncodingException {
        JSONObject repeatition;
        ArrayList<Schedule> list = new ArrayList<>();
        for (int i = 0; i < repeatitions.length(); i++) {
            repeatition = repeatitions.getJSONObject(i);
            int lectureNo = repeatition.getInt("lectureNo");
            int weekDay = repeatition.getInt("weekDay");
            JSONObject course = repeatition.getJSONObject("course");

            list.add(
                    new Schedule().addWeekDay(weekDay).addLectureNo(lectureNo)
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

    //function: helper function to bundle a JSONArray Object to an ArrayList of Course.
    private static ArrayList<Course> bundleCourseDataFromJSONArray(JSONArray courses) throws JSONException, UnsupportedEncodingException {
        JSONObject course;
        ArrayList<Course> list = new ArrayList<>();
        for (int i = 0; i < courses.length(); i++) {
            course = courses.getJSONObject(i);

            list.add(
                    new Course()
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

            );
        }
        return list;
    }


}
