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

public class TrashTaskAdapter extends RecyclerView.Adapter<TrashTaskAdapter.TrashViewHolder> {

    private List<Task> data;

    ToDoOpenHelper toDoOpenHelper;

    SQLiteDatabase connection;

    public TrashTaskAdapter(List<Task> data) {
        this.data = data;
    }

    @Override
    public TrashViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        toDoOpenHelper = new ToDoOpenHelper(viewGroup.getContext());
        connection = toDoOpenHelper.getWritableDatabase();

        return new TrashTaskAdapter.TrashViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trash_task_list, viewGroup, false));
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

    @Override
    public void onBindViewHolder(final TrashViewHolder trashViewHolder, int i) {
        final Task task = data.get(i);

        trashViewHolder.trashTaskTitle.setText(task.getTitle());

        trashViewHolder.trashRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Trash", "Restore clicked");
                task.setDeleted(false);
                new TaskRepository(connection).update(task);
                removeItem(trashViewHolder.getAdapterPosition());
            }
        });

        trashViewHolder.trashDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Trash", "Delete clicked");
                new TaskRepository(connection).delete(task.getId());
                removeItem(trashViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (data != null) ? data.size() : 0;
    }

    public class TrashViewHolder extends RecyclerView.ViewHolder {

        TextView trashTaskTitle;
        ImageView trashRestore;
        ImageView trashDelete;

        public TrashViewHolder(View itemView) {
            super(itemView);

            trashTaskTitle = itemView.findViewById(R.id.trash_task_title);
            trashRestore = itemView.findViewById(R.id.trash_restore);
            trashDelete = itemView.findViewById(R.id.trash_delete);

        }
    }


}
