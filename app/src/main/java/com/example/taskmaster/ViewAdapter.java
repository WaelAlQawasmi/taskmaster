package com.example.taskmaster;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter  extends RecyclerView.Adapter<ViewAdapter.TaskViewHolder>{

    CustomClickListener listener;
    List<Task> dataList;

    public ViewAdapter(List<Task> dataList, CustomClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }



    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItemView = layoutInflater.inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(listItemView, listener);
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView titel;
        public TextView body;
        public   TextView state;

        public TextView getTitel() {
            return titel;
        }

        public TextView getBody() {
            return body;
        }

        public TextView getState() {
            return state;
        }

        public CustomClickListener getListener() {
            return listener;
        }


        CustomClickListener listener;
        public TaskViewHolder(@NonNull  View itemView, CustomClickListener listener) {
            super(itemView);

            this.listener = listener;

            titel = (TextView) itemView.findViewById(R.id.the_title);
            body =(TextView)  itemView.findViewById(R.id.body);
            state = (TextView) itemView.findViewById(R.id.state);
            itemView.setOnClickListener(view -> listener.onTaskClicked(getAdapterPosition()) );
     itemView.setOnClickListener(view -> listener.onTaskClicked(getAdapterPosition()));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Log.i("tilt",dataList.get(position).getBody());
         holder.titel.setText(dataList.get(position).getTitle());
        holder.body.setText(dataList.get(position).getBody());
        holder.state.setText(dataList.get(position).getState());
    }
    public interface CustomClickListener {
        void onTaskClicked(int position);
    }



}
