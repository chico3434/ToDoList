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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> data;

    public TaskAdapter(List<Task> tasks) {
        data = tasks;
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        ImageView buttonComplete;
        ImageView buttonUpdate;
        ImageView buttonArchive;
        ImageView buttonDelete;

        public TaskHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);

            buttonComplete = itemView.findViewById(R.id.item_button_complete);
            buttonUpdate = itemView.findViewById(R.id.item_button_update);
            buttonArchive = itemView.findViewById(R.id.item_button_archive);
            buttonDelete= itemView.findViewById(R.id.item_button_delete);
        }

    }

    ToDoOpenHelper toDoOpenHelper;

    SQLiteDatabase connection;

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

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // getting connection with db
        toDoOpenHelper = new ToDoOpenHelper(viewGroup.getContext());
        connection = toDoOpenHelper.getWritableDatabase();

        return new TaskHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final TaskHolder taskHolder, int i) {
        final Task task = data.get(i);
        taskHolder.itemTitle.setText(task.getTitle());

        final String TAG = "Click Event";

        taskHolder.buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Complete clicked");
                taskHolder.buttonComplete.setImageResource(R.drawable.ic_check_box_black_24dp);
                task.setCompleted(true);
            }
        });

        taskHolder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Update clicked");
                //updateItem("Att", "Foi att", taskHolder.getAdapterPosition());
            }
        });

        taskHolder.buttonArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Archive clicked");
                removeItem(taskHolder.getAdapterPosition());
                task.setArchived(true);
            }
        });

        taskHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Delete clicked");
                removeItem(taskHolder.getAdapterPosition());
                task.setDeleted(true);
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
}
