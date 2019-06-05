package br.cefet.todo.adapter;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.cefet.todo.R;
import br.cefet.todo.database.ToDoOpenHelper;
import br.cefet.todo.domain.entity.Task;
import br.cefet.todo.domain.repository.TaskRepository;

public class HomeTaskAdapter extends RecyclerView.Adapter<HomeTaskAdapter.HomeTaskHolder> {

    private List<Task> data;

    public HomeTaskAdapter(List<Task> tasks) {
        data = tasks;
    }

    ToDoOpenHelper toDoOpenHelper;

    SQLiteDatabase connection;


    @Override
    public HomeTaskHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // getting connection with db
        toDoOpenHelper = new ToDoOpenHelper(viewGroup.getContext());
        connection = toDoOpenHelper.getWritableDatabase();

        return new HomeTaskHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_task_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final HomeTaskHolder homeTaskHolder, int i) {
        final Task task = data.get(i);
        homeTaskHolder.itemTitle.setText(task.getTitle());

        if (task.isCompleted()) {
            homeTaskHolder.buttonComplete.setImageResource(R.drawable.ic_check_box_black_24dp);
        }

        final String TAG = "Click Event";

        homeTaskHolder.buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Complete clicked");
                if (task.isCompleted())
                    homeTaskHolder.buttonComplete.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                else
                    homeTaskHolder.buttonComplete.setImageResource(R.drawable.ic_check_box_black_24dp);

                task.setCompleted(!task.isCompleted());
                new TaskRepository(connection).update(task);
            }
        });

        homeTaskHolder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Update clicked");
                //updateItem("Att", "Foi att", homeTaskHolder.getAdapterPosition());
            }
        });

        homeTaskHolder.buttonArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Archive clicked");
                removeItem(homeTaskHolder.getAdapterPosition());
                task.setArchived(true);
                new TaskRepository(connection).update(task);
            }
        });

        homeTaskHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Delete clicked");
                removeItem(homeTaskHolder.getAdapterPosition());
                task.setDeleted(true);
                new TaskRepository(connection).update(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    private void insertItem(Task task) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(task);
        notifyItemInserted(getItemCount());
    }

    private void removeItem(int position) {
        new TaskRepository(connection).update(data.get(position));
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    public void updateList(Task task) {
        insertItem(task);
    }

//    private void updateItem(String title, String description, int position) {
//        Task task = data.get(position);
//        task.setTitle(title);
//        task.setDescription(description);
//        notifyItemChanged(position);
//    }


    public class HomeTaskHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        ImageView buttonComplete;
        ImageView buttonUpdate;
        ImageView buttonArchive;
        ImageView buttonDelete;

        public HomeTaskHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);

            buttonComplete = itemView.findViewById(R.id.item_button_complete);
            buttonUpdate = itemView.findViewById(R.id.item_button_update);
            buttonArchive = itemView.findViewById(R.id.item_button_archive);
            buttonDelete= itemView.findViewById(R.id.item_button_delete);
        }

    }
}
