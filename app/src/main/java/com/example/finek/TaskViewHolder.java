package com.example.finek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finek.Bean.Tache;
import com.example.finek.Service.TaskService;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;





    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView nom_tache;
        TextView type;
        TextView date;
        private Context mContext;


        public TaskViewHolder(View itemView) {
            super(itemView);
            nom_tache = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            date = itemView.findViewById(R.id.date);
            mContext = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });
        }


        public void bindTache(Tache tache) {
            System.out.println(tache);
            nom_tache.setText(tache.getNom());
            date.setText(tache.getDate().getDay() + "/" + tache.getDate().getMonth() + "/" +
                    tache.getDate().getYear() + " " + tache.getDate().getHours() + ":" + tache.getDate().getMinutes() +
                   "H");
            type.setText(tache.getType());

        }

        private TaskViewHolder.ClickListener mClickListener;

        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(TaskViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }


    }