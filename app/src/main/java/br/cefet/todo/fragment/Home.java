package br.cefet.todo.fragment;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.cefet.todo.R;
import br.cefet.todo.adapter.TaskAdapter;
import br.cefet.todo.database.ToDoOpenHelper;
import br.cefet.todo.domain.entity.Task;
import br.cefet.todo.domain.repository.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {


    RecyclerView recyclerView;

    TaskAdapter taskAdapter;

    ToDoOpenHelper toDoOpenHelper;

    SQLiteDatabase connection;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        toDoOpenHelper = new ToDoOpenHelper(container.getContext());
        connection = toDoOpenHelper.getWritableDatabase();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_tasks);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        taskAdapter = new TaskAdapter(new TaskRepository(connection).selectAll());
        recyclerView.setAdapter(taskAdapter);
    }


    public void update() {
        if (CreateTask.newTask) {
            taskAdapter.updateList(CreateTask.theTask);
            CreateTask.newTask = false;
        }
    }
}
