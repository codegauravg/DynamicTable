package org.acm.sviet.schedulerama.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.acm.sviet.schedulerama.CourseDetailActivity;
import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.interfaces.Refresh;
import org.acm.sviet.schedulerama.models.Schedule;
import org.acm.sviet.schedulerama.utility.ApiUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anurag on 10-02-2016.
 *
 * Schedule Adapter for RecyclerView.
 */
public class ScheduleRecycleViewAdapter extends RecyclerView.Adapter<ScheduleRecycleViewAdapter.ViewHolder> {
    private static String TAG = "/ScheduleRecyclerAdapter";
    protected final ArrayList<Schedule> scheduleArrayList;
    private final Context context;
    private final int weekDay;
    private final String dept,sem,section;

    //the refresh callback interface to hand over to the APIUtil back;
    private final Refresh refresh;

    //int for diff viewType values.
    private final int EMPTY_VIEW = 0,SCHEDULE_VIEW = 1;

    //total number of lectures available in a day...
    private final int lectureCount = 7;

    //to use as a iterative schedule list counter.
    private int scheduleCounter = 0;

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final int scount;
        private TextView lectureNoView;
        private TextView courseNameView;
        private TextView facultyNameView;
        private Button deleteRepButton;

        //emptyView resources
        private Button addRepeatitionButton;
        private TextView contentEmptyTextView;
        private Spinner optionsSpinner;
        private Button confirmAddRepeatitionButton;
        //boolean to check if repetition has to be added here.
        private boolean addRep = true;

        public ViewHolder(View v,int viewType, final int scount){
            super(v);
            this.scount = scount;
            if(viewType == EMPTY_VIEW) {
                contentEmptyTextView = (TextView) v.findViewById(R.id.contentEmptyTextView);
                optionsSpinner = (Spinner) v.findViewById(R.id.optionsSpinner);

                final String optionCourses[] = {"SE","SMTP"};
                //TODO : task the API options service here.
                List<String> optionCoursesData = new ArrayList<>(Arrays.asList(optionCourses));
                ArrayAdapter<String> optionsAdapter = new ArrayAdapter<String>(context,R.layout.edit_spinner_layout, R.id.textViewEditScheduleListLayout, optionCoursesData);
                optionsSpinner.setAdapter(optionsAdapter);

                lectureNoView = (TextView) v.findViewById(R.id.emptyLectureNoTextView);
                addRepeatitionButton = (Button) v.findViewById(R.id.addRepeatitionButton);
                addRepeatitionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(addRep){
                            optionsSpinner.setVisibility(View.VISIBLE);
                            confirmAddRepeatitionButton.setVisibility(View.VISIBLE);
                            contentEmptyTextView.setVisibility(View.GONE);
                            lectureNoView.setVisibility(View.GONE);

                            addRepeatitionButton.setText("X");
                            ApiUtil.sync(context, ApiUtil.TASK.GET_SCHEDULE_COURSE_OPTIONS,optionsSpinner,dept,sem,section,weekDay,getAdapterPosition()+1);
                            addRep = false;
                        }
                        else{
                            optionsSpinner.setVisibility(View.GONE);
                            confirmAddRepeatitionButton.setVisibility(View.GONE);
                            lectureNoView.setVisibility(View.VISIBLE);
                            contentEmptyTextView.setVisibility(View.VISIBLE);

                            addRepeatitionButton.setText("+");
                            addRep = true;
                        }
                    }
                });

                confirmAddRepeatitionButton =(Button) v.findViewById(R.id.confirmAddRepeatitionButton);
                confirmAddRepeatitionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v,(String)optionsSpinner.getSelectedItem()+" Repeatition Added at "+(getAdapterPosition()+1)+".",Snackbar.LENGTH_LONG).show();
                        ApiUtil.sync(context,refresh, ApiUtil.TASK.ADD_REPEATITION_TO_COURSE,
                                (String) optionsSpinner.getSelectedItem(), getAdapterPosition() + 1, weekDay);

                        //TODO : task the API add repetition Service here.
                    }
                });


            } else {

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Schedule schedule = scheduleArrayList.get(scount);
                        Intent intent = new Intent(context, CourseDetailActivity.class);
                        intent.putExtra("schedule", new Gson().toJson(schedule));
                        context.startActivity(intent);
                    }
                });
                lectureNoView = (TextView) v.findViewById(R.id.textViewLectureNo);
                courseNameView = (TextView) v.findViewById(R.id.textViewCourseName);
                facultyNameView = (TextView) v.findViewById(R.id.textViewFacultyName);
                deleteRepButton = (Button) v.findViewById(R.id.deleteRepeatitionButton);
                deleteRepButton.setVisibility(View.VISIBLE);
                deleteRepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApiUtil.sync(context,refresh, ApiUtil.TASK.DELETE_REPEATITION_FROM_COURSE,
                                scheduleArrayList.get(scount).getCourse().getName(),getAdapterPosition()+1,weekDay);


                    }
                });

            }
        }

        //getters

        public Spinner getOptionsSpinner(){
            return optionsSpinner;
        }
        public TextView getContentEmptyTextView(){
            return contentEmptyTextView;
        }

        public Button getAddRepeatitionButton(){
            return addRepeatitionButton;
        }
        public TextView getLectureNoView() {
            return lectureNoView;
        }

        public TextView getCourseNameView() {
            return courseNameView;
        }

        public TextView getFacultyNameView() {
            return facultyNameView;
        }
    }


    @Override
    public int getItemViewType(int position) {

        for(Schedule sc : scheduleArrayList){
            if(position+1 == sc.getLectureNo()) {
                return SCHEDULE_VIEW;
            }
        }
        return EMPTY_VIEW;
    }

    public ScheduleRecycleViewAdapter(Context context,Refresh refresh, ArrayList<Schedule> scheduleArrayList,String dept,String sem,String section,int weekDay){
        this.context = context;
        this.scheduleArrayList = scheduleArrayList;
        this.weekDay = weekDay;
        this.dept = dept;
        this.sem = sem;
        this.section = section;
        this.refresh = refresh;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == SCHEDULE_VIEW){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_list_view_layout, parent, false);

            //incrementing scheduleCounter every time a new scheduleViewType is found.
            return new ViewHolder(v,viewType,scheduleCounter++);}
        else {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.empty_schedule_layout, parent, false);
            return new ViewHolder(v,viewType,0);
        }
    }


    @Override
    public int getItemCount() {
        return lectureCount;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        boolean scheduleFlag = false;
        for(Schedule schedule : scheduleArrayList){
            if(position+1 == schedule.getLectureNo()){
                holder.getCourseNameView().setText(scheduleArrayList.get(holder.scount).getCourse().getName());
                holder.getLectureNoView().setText(String.valueOf(scheduleArrayList.get(holder.scount).getLectureNo()));
                holder.getFacultyNameView().setText(scheduleArrayList.get(holder.scount).getCourse().getFaculty());

                scheduleFlag = true;

            }
        }

        if(!scheduleFlag) {
            //setting lectureNo textView for empty schedule position..
            holder.getLectureNoView().setText(String.valueOf(position + 1));
        }

    }


}
