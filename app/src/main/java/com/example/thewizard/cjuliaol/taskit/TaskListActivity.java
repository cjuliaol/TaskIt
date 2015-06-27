package com.example.thewizard.cjuliaol.taskit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class TaskListActivity extends AppCompatActivity {

    private ListView mListView;
    private static final String TAG = "TaskListActivity";
    private static final int EDIT_TASK_REQUEST = 10;
    private int mLastPositionClicked;
    private Task[] mTasks;
    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        //TODO: create Task items

        mTasks = new Task[3];
        mTasks[0] = new Task();
        mTasks[0].setName("Task 1");
        mTasks[0].setDueDate(new Date());
        mTasks[1] = new Task();
        mTasks[1].setName("Task 2");
        mTasks[1].setDone(true);
        mTasks[2] = new Task();
        mTasks[2].setName("Task 3");

        mListView = (ListView) findViewById(R.id.task_list);
        // Not Custom Adapter
        //mListView.setAdapter(new ArrayAdapter<Task>(this,android.R.layout.simple_list_item_1,items));

        // Using a Custom Adapter
        mAdapter = new TaskAdapter(mTasks);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast popup = Toast.makeText(getApplicationContext(),"Position clicked at " +position,Toast.LENGTH_SHORT);
                //popup.show();
                mLastPositionClicked = position;
                Intent intent = new Intent(TaskListActivity.this, TaskActivity.class);
                Task task = (Task) parent.getAdapter().getItem(position);
                intent.putExtra(TaskActivity.EXTRA, task);
                startActivityForResult(intent, EDIT_TASK_REQUEST);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_TASK_REQUEST) { // Check if come back from the specific request: save button
            if (resultCode == RESULT_OK) {  // Check if the Result is OK
                Task task = (Task) data.getSerializableExtra(TaskActivity.EXTRA);
                mTasks[mLastPositionClicked] = task;
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, task.getName());

            }

        }


    }

    // Custon ArrayAdapter for ListView
    private class TaskAdapter extends ArrayAdapter<Task> {
        // Here I set the custom layout
        TaskAdapter(Task[] tasks) {
            super(TaskListActivity.this, R.layout.task_list_row, R.id.task_item_name, tasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            // Manipulating Row Elements
            TextView taskName = (TextView) convertView.findViewById(R.id.task_item_name);
            Task task = getItem(position);
            taskName.setText(task.getName());

            CheckBox doneBox = (CheckBox) convertView.findViewById(R.id.task_item_done);
            doneBox.setChecked(task.isDone());

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
