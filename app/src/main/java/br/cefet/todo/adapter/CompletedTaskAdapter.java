package br.cefet.todo.adapter;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
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

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.CompletedViewHolder> {

    private List<Task> data;

    ToDoOpenHelper toDoOpenHelper;

    SQLiteDatabase connection;

    public CompletedTaskAdapter(List<Task> data) {
        this.data = data;
    }

    @Override
    public CompletedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        toDoOpenHelper = new ToDoOpenHelper(viewGroup.getContext());
        connection = toDoOpenHelper.getWritableDatabase();

        return new CompletedTaskAdapter.CompletedViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.completed_task_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final CompletedViewHolder completedViewHolder, int i) {
        final Task task = data.get(i);

        completedViewHolder.completedTaskTitle.setText(task.getTitle());

        completedViewHolder.completedButtonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Completed", "Complete clicked");
                task.setCompleted(false);
                new TaskRepository(connection).update(task);
                removeItem(completedViewHolder.getAdapterPosition());
            }
        });

        completedViewHolder.completedButtonArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Completed", "Archive clicked");
                removeItem(completedViewHolder.getAdapterPosition());
                task.setArchived(true);
                new TaskRepository(connection).update(task);
            }
        });

        completedViewHolder.completedButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Completed", "Delete clicked");
                removeItem(completedViewHolder.getAdapterPosition());
                task.setDeleted(true);
                new TaskRepository(connection).update(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (data != null) ? data.size() : 0;
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


    public class CompletedViewHolder extends RecyclerView.ViewHolder {

        TextView completedTaskTitle;
        ImageView completedButtonComplete;
        ImageView completedButtonArchive;
        ImageView completedButtonDelete;

        public CompletedViewHolder(View itemView) {
            super(itemView);

            completedTaskTitle = itemView.findViewById(R.id.completed_task_title);
            completedButtonComplete = itemView.findViewById(R.id.completed_button_complete);
            completedButtonArchive = itemView.findViewById(R.id.completed_button_archive);
            completedButtonDelete = itemView.findViewById(R.id.completed_button_delete);

        }
    }
}
