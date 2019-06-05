package br.cefet.todo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.cefet.todo.R;
import br.cefet.todo.domain.entity.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> data;

    public TaskAdapter(List<Task> tasks) {
        data = tasks;
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        TextView itemDescription;

        public TaskHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDescription = itemView.findViewById(R.id.item_description);
        }

    }

    private void insertItem(Task task) {
        data.add(task);
        notifyItemInserted(getItemCount());
    }

    public void updateList(Task task) {
        insertItem(task);
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TaskHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(TaskHolder taskHolder, int i) {
        taskHolder.itemTitle.setText(data.get(i).getTitle());
        taskHolder.itemDescription.setText(data.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
