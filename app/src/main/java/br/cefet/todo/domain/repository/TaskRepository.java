package br.cefet.todo.domain.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.cefet.todo.domain.entity.Task;

public class TaskRepository {

    private SQLiteDatabase connection;

    public TaskRepository(SQLiteDatabase connection) {
        this.connection = connection;
    }

    public void insert(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("description", task.getDescription());
        contentValues.put("completed", task.isCompleted());
        contentValues.put("archived", task.isArchived());
        contentValues.put("deleted", task.isDeleted());

        connection.insertOrThrow("Task", null, contentValues);
    }

    public void delete(int id) {
        String[] params = new String[1];
        params[0] = String.valueOf(id);

        connection.delete("Task", "id = ?", params);
    }

    public void update(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task.getTitle());
        contentValues.put("description", task.getDescription());
        contentValues.put("completed", task.isCompleted());
        contentValues.put("archived", task.isArchived());
        contentValues.put("deleted", task.isDeleted());

        String[] params = new String[1];
        params[0] = String.valueOf(task.getId());

        connection.update("Task", contentValues, "id = ?", params);
    }

    public Task select(int id) {
        return null;
    }

    public List<Task> selectAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Task";

        Cursor result = connection.rawQuery(sql, null);

        if (result.getCount() > 0) {
            result.moveToFirst();

            do {

                String title = result.getString(result.getColumnIndexOrThrow("title"));
                String description = result.getString(result.getColumnIndexOrThrow("description"));

                Task task = new Task(title, description);

                int id = result.getInt(result.getColumnIndexOrThrow("id"));

                task.setId(id);

                boolean completed = (result.getInt(result.getColumnIndexOrThrow("completed")) == 0) ? false : true;

                boolean archived = (result.getInt(result.getColumnIndexOrThrow("archived")) == 0) ? false : true;

                boolean deleted = (result.getInt(result.getColumnIndexOrThrow("deleted")) == 0) ? false : true;

                task.setCompleted(completed);
                task.setArchived(archived);
                task.setDeleted(deleted);

                tasks.add(task);

            } while (result.moveToNext());

        } else {
            return null;
        }
        Log.d("Tasks count: ", String.valueOf(tasks.size()));
        return tasks;
    }

    public List<Task> selectToHome() {
        List<Task> tasks = selectAll();
        List<Task> homeTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (!task.isArchived() && !task.isDeleted()) {
                homeTasks.add(task);
            }
        }
        return homeTasks;
    }

    public List<Task> selectCompleted() {
        List<Task> completedTasks = new ArrayList<>();
        List<Task> tasks = selectAll();
        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }

    public List<Task> selectToArchived() {
        List<Task> archivedTasks = new ArrayList<>();
        List<Task> tasks = selectAll();
        for (Task task : tasks) {
            if (task.isArchived()) {
                archivedTasks.add(task);
            }
        }
        return archivedTasks;
    }

    public List<Task> selectToTrash() {
        List<Task> trashTasks = new ArrayList<>();
        List<Task> tasks = selectAll();
        for (Task task : tasks) {
            if (task.isDeleted()) {
                trashTasks.add(task);
            }
        }
        return trashTasks;
    }

}
