package org.acm.sviet.schedulerama.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.acm.sviet.schedulerama.CourseEditActivity;
import org.acm.sviet.schedulerama.R;
import org.acm.sviet.schedulerama.hod.CourseViewActivity;
import org.acm.sviet.schedulerama.models.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anurag on 08-02-2016.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{
    private static String TAG = "/CourseAdapter";
    protected ArrayList<Course> courseList;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseListNameTextView;
        private final TextView courseListDeptTextView;
        private final TextView courseListSemTextView;
        private final TextView courseListSectionTextView;


        public ViewHolder(View v){
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,CourseEditActivity.class);
                    intent.putExtra("purpose","update");
                    intent.putExtra("course",new Gson().toJson(courseList.get(getAdapterPosition())));
                    context.startActivity(intent);
                }
            });
            courseListNameTextView = (TextView) v.findViewById(R.id.courseListNameTextView);
            courseListDeptTextView = (TextView) v.findViewById(R.id.courseListDeptTextView);
            courseListSemTextView = (TextView) v.findViewById(R.id.courseListSemTextView);
            courseListSectionTextView = (TextView) v.findViewById(R.id.courseListSectionTextView);
        }
        //view getters.
        public TextView getCourseListNameTextView() {
            return courseListNameTextView;
        }

        public TextView getCourseListSectionTextView() {
            return courseListSectionTextView;
        }

        public TextView getCourseListSemTextView() {
            return courseListSemTextView;
        }

        public TextView getCourseListDeptTextView() {
            return courseListDeptTextView;
        }


    }

    public CourseAdapter(Context context,ArrayList<Course> courses){
        this.courseList = courses;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_list_view_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.getCourseListNameTextView().setText(courseList.get(position).getName());
        holder.getCourseListDeptTextView().setText(courseList.get(position).getDept());
        holder.getCourseListSemTextView().setText(courseList.get(position).getSem());
        holder.getCourseListSectionTextView().setText(courseList.get(position).getSection());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
