package br.cefet.todo.fragment;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.cefet.todo.R;
import br.cefet.todo.adapter.CompletedTaskAdapter;
import br.cefet.todo.database.ToDoOpenHelper;
import br.cefet.todo.domain.entity.Task;
import br.cefet.todo.domain.repository.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class Completed extends Fragment {

    RecyclerView recyclerView;

    CompletedTaskAdapter completedTaskAdapter;

    ToDoOpenHelper toDoOpenHelper;

    SQLiteDatabase connection;


    public Completed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        toDoOpenHelper = new ToDoOpenHelper(container.getContext());
        connection = toDoOpenHelper.getWritableDatabase();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.completed_recycler_view_tasks);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<Task> tasks =new TaskRepository(connection).selectCompleted();

        completedTaskAdapter = new CompletedTaskAdapter(tasks);
        recyclerView.setAdapter(completedTaskAdapter);
    }

}
