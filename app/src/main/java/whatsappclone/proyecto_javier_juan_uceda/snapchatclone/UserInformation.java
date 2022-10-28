package whatsappclone.proyecto_javier_juan_uceda.snapchatclone;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import whatsappclone.proyecto_javier_juan_uceda.snapchatclone.Constants.FieldsFirebase;

public class UserInformation {
   public static ArrayList<String> listFollowing = new ArrayList<>();

   public void startFetching(){
      listFollowing.clear();
      getUserFollowing();
   }

   private void getUserFollowing() {
      DatabaseReference userFollowingDB = FirebaseDatabase.getInstance().getReference().child(FieldsFirebase.USERS_FIELD_FIREBASE).child(FirebaseAuth.getInstance().getUid()).child(FieldsFirebase.FOLLOWING_FIELD_FIREBASE);
      userFollowingDB.addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()){
               String uid = dataSnapshot.getRef().getKey();
               if (uid != null && !listFollowing.contains(uid)){
                  listFollowing.add(uid);
               }
            }
         }

         @Override
         public void onChildChanged(DataSnapshot dataSnapshot, String s) {

         }

         @Override
         public void onChildRemoved(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
               String uid = dataSnapshot.getRef().getKey();
               if (uid != null){
                  listFollowing.remove(uid);
               }
            }
         }

         @Override
         public void onChildMoved(DataSnapshot dataSnapshot, String s) {

         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
      });
   }
}
