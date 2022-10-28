package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.Constants.FieldsFirebase;
import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewReceiver.ReceiverAdapater;
import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.RecyclerViewReceiver.ReceiverObject;

public class ChooseReceiverActivity extends ParentActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String Uid;
    private Bitmap bitmap;
    private FloatingActionButton mFab;
    private CheckBox mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_receiver);
        setUI();
    }

    private void setUI() {
        try {
            bitmap = BitmapFactory.decodeStream(getApplication().openFileInput("imageToSend"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            finish();
            return;
        }

        Uid = FirebaseAuth.getInstance().getUid();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ReceiverAdapater(getDataSet(),getApplication());
        mRecyclerView.setAdapter(mAdapter);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToStories();
            }
        });
    }


    private ArrayList<ReceiverObject> results = new ArrayList<>();
    private ArrayList<ReceiverObject> getDataSet() {
        listenForData();
        return results;
    }


    private void listenForData() {
        for(int i = 0; i < UserInformation.listFollowing.size();i++){
            DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child(FieldsFirebase.USERS_FIELD_FIREBASE).child(UserInformation.listFollowing.get(i));
            usersDb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String email = "";
                    String uid = dataSnapshot.getRef().getKey();
                    if(dataSnapshot.child(FieldsFirebase.EMAIL_FIELD_FIREBASE).getValue() != null){
                        email = dataSnapshot.child(FieldsFirebase.EMAIL_FIELD_FIREBASE).getValue().toString();
                    }
                    ReceiverObject obj = new ReceiverObject(email, uid, false);
                    if(!results.contains(obj)){
                        results.add(obj);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    private void saveToStories() {
        final DatabaseReference userStoryDb = FirebaseDatabase.getInstance().getReference().child(FieldsFirebase.USERS_FIELD_FIREBASE).child(Uid).child(FieldsFirebase.STORY_FIELD_FIREBASE);
        final String key = userStoryDb.push().getKey();

        StorageReference filePath = FirebaseStorage.getInstance().getReference().child(FieldsFirebase.CAPTURES_FIELD_FIREBASE).child(key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] dataToUpload = baos.toByteArray();
        UploadTask uploadTask = filePath.putBytes(dataToUpload);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());

                Uri imageUrl = urlTask.getResult();

                Long currentTimestamp = System.currentTimeMillis();
                Long endTimestamp = currentTimestamp + (24*60*60*1000);


                // receive
                mStory = findViewById(R.id.receive);
                if(mStory.isChecked()){
                    Map<String, Object> mapToUpload = new HashMap<>();
                    mapToUpload.put(FieldsFirebase.IMAGE_URL_FIELD_FIREBASE, imageUrl.toString());
                    mapToUpload.put(FieldsFirebase.TIMESTAMP_BEG_FIELD_FIREBASE, currentTimestamp);
                    mapToUpload.put(FieldsFirebase.TIMESTAMP_END_FIELD_FIREBASE, endTimestamp);
                    userStoryDb.child(key).setValue(mapToUpload);
                }
                for(int i = 0; i< results.size(); i++){
                    if(results.get(i).getReceive()){
                        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child(FieldsFirebase.USERS_FIELD_FIREBASE).child(results.get(i).getUid()).child(FieldsFirebase.RECEIVED_FIELD_FIREBASE).child(Uid);
                        Map<String, Object> mapToUpload = new HashMap<>();
                        mapToUpload.put(FieldsFirebase.IMAGE_URL_FIELD_FIREBASE, imageUrl.toString());
                        mapToUpload.put(FieldsFirebase.TIMESTAMP_BEG_FIELD_FIREBASE, currentTimestamp);
                        mapToUpload.put(FieldsFirebase.TIMESTAMP_END_FIELD_FIREBASE, endTimestamp);
                        userDb.child(key).setValue(mapToUpload);
                    }
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });



        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
                return;
            }
        });

    }


}