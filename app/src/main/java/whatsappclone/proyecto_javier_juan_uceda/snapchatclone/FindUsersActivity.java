package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.Constants.FieldsFirebase;
import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewFollow.FollowAdapater;
import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewFollow.FollowObject;

public class FindUsersActivity extends ParentActivity {


    public static final String EMAIL_FIELD = "email";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText mInput;
    private Button mSearch;

    private ArrayList<FollowObject> results = new ArrayList<>();

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
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FollowAdapater(getDataSet(),getApplication());
        mRecyclerView.setAdapter(mAdapter);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                listenForData();
            }
        });

    }

    private void listenForData() {
        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child(FieldsFirebase.USERS_FIELD_FIREBASE);
        String emailText = mInput.getText().toString();
        Query query = usersDb.orderByChild(EMAIL_FIELD).startAt(emailText).endAt(emailText + "\uf8ff");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String email = "";
                String uid = dataSnapshot.getRef().getKey();
                if(dataSnapshot.child(EMAIL_FIELD).getValue() != null){
                    email = dataSnapshot.child(EMAIL_FIELD).getValue().toString();
                }
                if(!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    FollowObject obj = new FollowObject(email, uid);
                    results.add(obj);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void clear() {
        int size = this.results.size();
        this.results.clear();
        mAdapter.notifyItemRangeChanged(0, size);
    }


    private ArrayList<FollowObject> getDataSet() {
        listenForData();
        return results;
    }
}