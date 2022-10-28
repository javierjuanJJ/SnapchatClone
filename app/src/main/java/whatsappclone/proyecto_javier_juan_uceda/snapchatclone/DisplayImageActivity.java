package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.Constants.FieldsFirebase;

public class DisplayImageActivity extends ParentActivity {


    private String userId, currentUid, chatOrStory;

    private ImageView mImage;

    private boolean started = false;
    private int imageIterator = 0;
    ArrayList<String> imageUrlList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        setUI();

    }

    private void setUI() {

        currentUid = FirebaseAuth.getInstance().getUid();

        Bundle b = getIntent().getExtras();
        userId = b.getString("userId");

        mImage = findViewById(R.id.image);
        chatOrStory = b.getString("chatOrStory");
        switch(chatOrStory){
            case "chat":
                listenForChat();
                break;
            case FieldsFirebase.STORY_FIELD_FIREBASE:
                listenForStory();
                break;
        }
    }

    private void listenForChat() {
        final DatabaseReference chatDb = FirebaseDatabase.getInstance().getReference().child(FieldsFirebase.USERS_FIELD_FIREBASE).child(currentUid).child(FieldsFirebase.RECEIVED_FIELD_FIREBASE).child(userId);
        chatDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl = "";
                for(DataSnapshot chatSnapshot : dataSnapshot.getChildren()){

                    if(chatSnapshot.child(FieldsFirebase.IMAGE_URL_FIELD_FIREBASE).getValue() != null){
                        imageUrl = chatSnapshot.child(FieldsFirebase.IMAGE_URL_FIELD_FIREBASE).getValue().toString();
                    }
                    imageUrlList.add(imageUrl);
                    if (!started){
                        started = true;
                        initializeDisplay();
                    }
                    chatDb.child(chatSnapshot.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void listenForStory() {
        DatabaseReference followingStoryDb = FirebaseDatabase.getInstance().getReference().child(FieldsFirebase.USERS_FIELD_FIREBASE).child(userId);
        followingStoryDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageUrl = "";
                long timestampBeg = 0;
                long timestampEnd = 0;
                for(DataSnapshot storySnapshot : dataSnapshot.child(FieldsFirebase.STORY_FIELD_FIREBASE).getChildren()){
                    if(storySnapshot.child(FieldsFirebase.TIMESTAMP_BEG_FIELD_FIREBASE).getValue() != null){
                        timestampBeg = Long.parseLong(storySnapshot.child(FieldsFirebase.TIMESTAMP_BEG_FIELD_FIREBASE).getValue().toString());
                    }
                    if(storySnapshot.child(FieldsFirebase.TIMESTAMP_END_FIELD_FIREBASE).getValue() != null){
                        timestampEnd = Long.parseLong(storySnapshot.child(FieldsFirebase.TIMESTAMP_END_FIELD_FIREBASE).getValue().toString());
                    }
                    if(storySnapshot.child(FieldsFirebase.IMAGE_URL_FIELD_FIREBASE).getValue() != null){
                        imageUrl = storySnapshot.child(FieldsFirebase.IMAGE_URL_FIELD_FIREBASE).getValue().toString();
                    }
                    long timestampCurrent = System.currentTimeMillis();
                    if(timestampCurrent >= timestampBeg && timestampCurrent <= timestampEnd){
                        imageUrlList.add(imageUrl);
                        if (!started){
                            started = true;
                            initializeDisplay();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void initializeDisplay() {
        Glide.with(getApplication()).load(imageUrlList.get(imageIterator)).into(mImage);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage();
            }
        });
        final Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeImage();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void changeImage() {
        if(imageIterator == imageUrlList.size() - 1){
            finish();
            return;
        }
        imageIterator++;
        Glide.with(getApplication()).load(imageUrlList.get(imageIterator)).into(mImage);
    }

}