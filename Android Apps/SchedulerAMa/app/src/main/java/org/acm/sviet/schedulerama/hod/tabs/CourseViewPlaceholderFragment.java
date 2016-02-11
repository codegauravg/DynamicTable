package org.acm.sviet.schedulerama.hod.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.adapters.CourseAdapter;
import org.acm.sviet.schedulerama.interfaces.Refresh;
import org.acm.sviet.schedulerama.models.Course;
import org.acm.sviet.schedulerama.utility.ApiUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Anurag on 09-02-2016.
 *
 * placeholder fragment for course list view.
 */
public class CourseViewPlaceholderFragment extends Fragment{

    private RecyclerView courseRecycleView;
    private ArrayList<Course> courses;
    private CourseAdapter courseAdapter;
    private final String TAG = "/CourseViewFragment";


    public CourseViewPlaceholderFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_view,container,false);

        courseRecycleView = (RecyclerView) rootView.findViewById(R.id.courseListRecycleView);
        getCourses();
        courseRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }



    private void getCourses(){
        ApiUtil.sync(
                //context for any UI response.
                getActivity(),
                //interface the view to refresh as per needed.
                new Refresh() {
                    @Override
                    public void callback() {
                        getCourses();
                    }
                },
                //task type
                ApiUtil.TASK.GET_COURSES,
                //view to adapt
                courseRecycleView);
    }


}

