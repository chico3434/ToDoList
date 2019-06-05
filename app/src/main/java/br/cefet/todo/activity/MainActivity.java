package br.cefet.todo.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import br.cefet.todo.R;
import br.cefet.todo.fragment.CreateTask;
import br.cefet.todo.fragment.Home;
import br.cefet.todo.fragment.Trash;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    // Fragments

    Home home = new Home();
    CreateTask createTask = new CreateTask();
    Trash trash = new Trash();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menuHome);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.menuHome:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                home.update();
                return true;

            case R.id.menuCreate:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, createTask).commit();
                return true;

            case R.id.menuTrash:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, trash).commit();
                return true;
        }

        return false;
    }
}
