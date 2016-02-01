package org.acm.sviet.schedulerama;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.acm.sviet.schedulerama.models.Schedule;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView facultyTextView;
    private TextView fac_contactTextView;
    private TextView descriptionTextView;
    private TextView deptTextView;
    private TextView semTextView;
    private TextView sectionTextView;
    private TextView refBookTextView;
    private TextView refBookLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameTextView  = (TextView) findViewById(R.id.TextViewCourseName);
        facultyTextView = (TextView) findViewById(R.id.TextViewFacultyName);
        fac_contactTextView = (TextView)findViewById(R.id.TextViewFacultyContact);
        descriptionTextView = (TextView) findViewById(R.id.TextViewDescription);
        deptTextView = (TextView) findViewById(R.id.TextViewDept);
        semTextView = (TextView) findViewById(R.id.TextViewSem);
        sectionTextView = (TextView) findViewById(R.id.TextViewSection);
        refBookTextView = (TextView) findViewById(R.id.TextViewRefBook);
        refBookLinkTextView = (TextView) findViewById(R.id.TextViewRefBookLink);

        String scheduleJSON = null;
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            scheduleJSON = extras.getString("schedule");
            Schedule schedule = new Gson().fromJson(scheduleJSON,Schedule.class);

            nameTextView.setText(schedule.getCourse().getName());
            facultyTextView.setText(schedule.getCourse().getFaculty());
            fac_contactTextView.setText(schedule.getCourse().getFac_contact());
            descriptionTextView.setText(schedule.getCourse().getDescription());
            deptTextView.setText(schedule.getCourse().getDept());
            semTextView.setText(schedule.getCourse().getSem());
            sectionTextView.setText(schedule.getCourse().getSection());
            refBookTextView.setText(schedule.getCourse().getRefBook());
            refBookLinkTextView.setText(schedule.getCourse().getRefBookLink());
        }
       else{
            Toast.makeText(getApplicationContext(),"No Schedule Acquired.",Toast.LENGTH_LONG).show();
        }

    }

}
