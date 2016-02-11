package org.acm.sviet.schedulerama.hod;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.interfaces.Refresh;
import org.acm.sviet.schedulerama.models.Course;
import org.acm.sviet.schedulerama.utility.ApiUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CourseEditActivity extends AppCompatActivity {


    private EditText courseNameEditText;
    private EditText facultyNameEditText;
    private EditText facContactEditText;
    private EditText courseDescriptionEditText;
    private EditText refBookEditText;
    private EditText refLinkEditText;

    private final String TAG = "/CourseEditActivity";
    private String purpose = "";

    private Spinner typeEditSpinner;
    private Spinner deptEditSpinner;
    private Spinner semEditSpinner;
    private Spinner sectionEditSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialising all view components.
        courseNameEditText = (EditText) findViewById(R.id.courseNameEditText);
        facultyNameEditText = (EditText) findViewById(R.id.facultyNameEditText);
        facContactEditText = (EditText) findViewById(R.id.facContactEditText);
        courseDescriptionEditText = (EditText) findViewById(R.id.courseDescriptionEditText);
        refBookEditText = (EditText) findViewById(R.id.refBookEditText);
        refLinkEditText = (EditText) findViewById(R.id.refBookLinkEditText);

        typeEditSpinner = (Spinner) findViewById(R.id.typeEditSpinner);
        deptEditSpinner = (Spinner) findViewById(R.id.deptEditSpinner);
        semEditSpinner = (Spinner) findViewById(R.id.semEditSpinner);
        sectionEditSpinner = (Spinner) findViewById(R.id.sectionEditSpinner);

        String typeArray[] = {"main","temp"};
        String deptArray[] = getResources().getStringArray(R.array.dept_array);
        String semArray[] = getResources().getStringArray(R.array.sem_array);
        String sectionArray[] = getResources().getStringArray(R.array.section_array);

        List<String> typeData = new ArrayList<>(Arrays.asList(typeArray));
        List<String> deptData = new ArrayList<>(Arrays.asList(deptArray));
        List<String> semData = new ArrayList<>(Arrays.asList(semArray));
        List<String> sectionData = new ArrayList<>(Arrays.asList(sectionArray));

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.edit_spinner_layout,R.id.textViewEditScheduleListLayout,typeData);
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.edit_spinner_layout, R.id.textViewEditScheduleListLayout, deptData);
        ArrayAdapter<String> semAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.edit_spinner_layout, R.id.textViewEditScheduleListLayout, semData);
        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.edit_spinner_layout, R.id.textViewEditScheduleListLayout, sectionData);

        typeEditSpinner.setAdapter(typeAdapter);
        deptEditSpinner.setAdapter(deptAdapter);
        semEditSpinner.setAdapter(semAdapter);
        sectionEditSpinner.setAdapter(sectionAdapter);
        //end of initialisation.

        //checking whether the activity is used to update a old course or add a new course.
        final Bundle extras = getIntent().getExtras();
        if(extras!=null){
            purpose = extras.getString("purpose");
            if(purpose!=null&&purpose.equals("update")){
                String jsonCourse = extras.getString("course");
                Course course = new Gson().fromJson(jsonCourse,Course.class);

                courseNameEditText.setText(course.getName());
                courseDescriptionEditText.setText(course.getDescription());
                facultyNameEditText.setText(course.getFaculty());
                facContactEditText.setText(course.getFac_contact());
                refBookEditText.setText(course.getRefBook());
                refLinkEditText.setText(course.getRefBookLink());


                typeEditSpinner.setSelection(getSpinnerPositionByItem(course.getType(),typeData));
                deptEditSpinner.setSelection(getSpinnerPositionByItem(course.getDept(),deptData));
                semEditSpinner.setSelection(getSpinnerPositionByItem(course.getSem(),semData));
                sectionEditSpinner.setSelection(getSpinnerPositionByItem(course.getSection(),sectionData));
            }

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCourseEdit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(courseNameEditText.getText().toString().isEmpty()||
                        courseDescriptionEditText.getText().toString().isEmpty()||
                        facultyNameEditText.getText().toString().isEmpty()||
                        facContactEditText.getText().toString().isEmpty()||
                        refBookEditText.getText().toString().isEmpty()||
                        refLinkEditText.getText().toString().isEmpty()||
                        deptEditSpinner.getSelectedItemPosition()==0||
                        semEditSpinner.getSelectedItemPosition()==0||
                        sectionEditSpinner.getSelectedItemPosition()==0){
                    Snackbar.make(view, "Please fill the course details properly.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    Course course = new Course(courseNameEditText.getText().toString(),
                            typeEditSpinner.getSelectedItem().toString(),
                            courseDescriptionEditText.getText().toString(),
                            facultyNameEditText.getText().toString(),
                            facContactEditText.getText().toString(),
                            deptEditSpinner.getSelectedItem().toString(),
                            semEditSpinner.getSelectedItem().toString(),
                            sectionEditSpinner.getSelectedItem().toString(),
                            refBookEditText.getText().toString(),
                            refLinkEditText.getText().toString());
                    try {
                        uploadCourse(course);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private int getSpinnerPositionByItem(String item,List<String> list){
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals(item)){return i;}
        }
        return 0;
    }

    private void uploadCourse(Course course) throws UnsupportedEncodingException {
        String jsonBody = new Gson().toJson(course);
        //TODO : upload course json data to database using the API with help of AsyncHttpClient.

        final AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = new StringEntity(jsonBody);

       if(purpose.equals("update")){
           ApiUtil.sync(getBaseContext(),null, ApiUtil.TASK.UPDATE_COURSE, course);
       }else{
            ApiUtil.sync(getBaseContext(),null, ApiUtil.TASK.PUT_NEW_COURSE,course);
       }

        finish();


    }

}
