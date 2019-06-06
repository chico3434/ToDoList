package br.cefet.todo.fragment;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.cefet.todo.R;
import br.cefet.todo.ToDoApplication;
import br.cefet.todo.activity.MainActivity;
import br.cefet.todo.adapter.HomeTaskAdapter;
import br.cefet.todo.database.ToDoOpenHelper;
import br.cefet.todo.domain.entity.Task;
import br.cefet.todo.domain.repository.TaskRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTask extends Fragment {

    private EditText editTitle;

    private EditText editDescription;

    private Button buttonCreateTask;

    private Button buttonCancel;

    ToDoOpenHelper toDoOpenHelper;

    SQLiteDatabase connection;

    public CreateTask() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_task, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTitle = view.findViewById(R.id.editTitle);

        editDescription = view.findViewById(R.id.editDescription);

        buttonCreateTask = view.findViewById(R.id.buttonCreateTask);

        buttonCreateTask.setOnClickListener(onClickListener);

        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonCancel.setVisibility(View.INVISIBLE);
        buttonCancel.setOnClickListener(onClickCancel);

        toDoOpenHelper = new ToDoOpenHelper(view.getContext());
        connection = toDoOpenHelper.getWritableDatabase();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (HomeTaskAdapter.taskToUpdate != null) {
            Log.d("TESTE", HomeTaskAdapter.taskToUpdate.getTitle());
            Log.d("TESTE", HomeTaskAdapter.taskToUpdate.getDescription());
            editTitle.setText(HomeTaskAdapter.taskToUpdate.getTitle());
            editDescription.setText(HomeTaskAdapter.taskToUpdate.getDescription());
            buttonCreateTask.setText(getString(R.string.update_task));
            buttonCancel.setVisibility(View.VISIBLE);

        }
    }

    View.OnClickListener onClickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editTitle.setText("");
            editDescription.setText("");
            buttonCreateTask.setText(getString(R.string.button_create_task));
            buttonCancel.setVisibility(View.INVISIBLE);

            HomeTaskAdapter.taskToUpdate = null;

            MainActivity.bottomNavigationView.setSelectedItemId(R.id.menuHome);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d(ToDoApplication.TAG, "Button Create clicked");

            if (HomeTaskAdapter.taskToUpdate == null) {
                String title = editTitle.getText().toString();

                String description = editDescription.getText().toString();

                Task task = new Task(title, description);

                new TaskRepository(connection).insert(task);
            } else {

                Task task = HomeTaskAdapter.taskToUpdate;
                HomeTaskAdapter.taskToUpdate = null;

                task.setTitle(editTitle.getText().toString());
                task.setDescription(editDescription.getText().toString());

                new TaskRepository(connection).update(task);
            }

            editTitle.setText("");
            editDescription.setText("");
            buttonCreateTask.setText(getString(R.string.button_create_task));
            buttonCancel.setVisibility(View.INVISIBLE);

            MainActivity.bottomNavigationView.setSelectedItemId(R.id.menuHome);
        }
    };

}
