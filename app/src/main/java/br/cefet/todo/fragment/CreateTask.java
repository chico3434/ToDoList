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

    ToDoOpenHelper toDoOpenHelper;

    SQLiteDatabase connection;

    public static boolean newTask = false;

    public static Task theTask;

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

        toDoOpenHelper = new ToDoOpenHelper(view.getContext());
        connection = toDoOpenHelper.getWritableDatabase();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d(ToDoApplication.TAG, "On Click Listener");

            String title = editTitle.getText().toString();

            String description = editDescription.getText().toString();

            Task task = new Task(title, description);

            new TaskRepository(connection).insert(task);

            MainActivity.bottomNavigationView.setSelectedItemId(R.id.menuHome);

            newTask = true;
            theTask = task;
        }
    };

}
