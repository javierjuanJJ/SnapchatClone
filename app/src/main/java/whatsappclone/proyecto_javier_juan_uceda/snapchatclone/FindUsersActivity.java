package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewFollow.RCAdapater;
import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewFollow.UsersObject;

public class FindUsersActivity extends ParentActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText mInput;
    private Button mSearch;

    private ArrayList<UsersObject> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        setUI();
    }

    private void setUI() {
        mInput = findViewById(R.id.input);
        mSearch = findViewById(R.id.search);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RCAdapater(getDataSet(),getApplication());
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<UsersObject> getDataSet() {
        return results;
    }
}