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

public class ArchivedTaskAdapter extends RecyclerView.Adapter<ArchivedTaskAdapter.ArchivedViewHolder> {

    private List<Task> data;

    ToDoOpenHelper toDoOpenHelper;

    SQLiteDatabase connection;

    public ArchivedTaskAdapter(List<Task> data) {
        this.data = data;
    }

    @Override
    public ArchivedTaskAdapter.ArchivedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        toDoOpenHelper = new ToDoOpenHelper(viewGroup.getContext());
        connection = toDoOpenHelper.getWritableDatabase();

        return new ArchivedTaskAdapter.ArchivedViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.archived_task_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final ArchivedViewHolder viewHolder, int i) {
        final Task task = data.get(i);

        viewHolder.archivedTaskTitle.setText(task.getTitle());

        if (task.isCompleted()) {
            viewHolder.archivedImageCompleted.setImageResource(R.drawable.ic_check_box_black_24dp);
        } else {
            viewHolder.archivedImageCompleted.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }

        viewHolder.archivedButtonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Archived", "Archive clicked");
                removeItem(viewHolder.getAdapterPosition());
                task.setArchived(false);
                new TaskRepository(connection).update(task);
            }
        });

        viewHolder.archivedButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Archived", "Delete clicked");
                removeItem(viewHolder.getAdapterPosition());
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

    public class ArchivedViewHolder extends RecyclerView.ViewHolder {

        TextView archivedTaskTitle;
        ImageView archivedImageCompleted;
        ImageView archivedButtonRestore;
        ImageView archivedButtonDelete;

        public ArchivedViewHolder(View itemView) {
            super(itemView);

            archivedTaskTitle = itemView.findViewById(R.id.archived_task_title);
            archivedImageCompleted = itemView.findViewById(R.id.archived_image_complete);
            archivedButtonRestore = itemView.findViewById(R.id.archived_button_restore);
            archivedButtonDelete = itemView.findViewById(R.id.archive_button_delete);
        }
    }
}
