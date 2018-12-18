package com.youthink.comchatapp;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import static android.support.constraint.Constraints.TAG;


public class AllChatFragment extends Fragment {

    List<ChatRoom> chatRooms = new ArrayList<>();
    FirestoreRecyclerAdapter adapter;
    String user_addr;
    Query query;

    public AllChatFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_allchat, container, false);

        /* 사용자 주소 받아오기 */
        MainActivity main = (MainActivity)getActivity();
        user_addr = main.getUserAddr();

//        Query query = FirebaseFirestore.getInstance().collection("chatrooms");
//        Query filteredQuery = filterQuery(query);
        getListItems(); //전체 채팅방 리스트 받아오는 함수
       // Query filteredQuery = filterQuery(query);
        FirestoreRecyclerOptions<ChatRoom> options = new FirestoreRecyclerOptions.Builder<ChatRoom>().setQuery(query.orderBy("timeStamp"), ChatRoom.class).build();


        adapter = new FirestoreRecyclerAdapter<ChatRoom, ChatViewHolder>(options) {
            public void onBindViewHolder(ChatViewHolder holder, int position, ChatRoom model){
                holder.roomContent.setText(model.getContent());
                holder.roomDeadline.setText(model.getDeadline());
                holder.roomLocation.setText(model.getLocation());
                holder.roomTitle.setText(model.getTitle());
            }

            public ChatViewHolder onCreateViewHolder(ViewGroup group, int i){
                View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_chatroom, group, false);
                final ChatViewHolder viewHolder = new ChatViewHolder(view);

                view.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        Intent intent = new Intent(view.getContext(), ChatActivity.class);
                        intent.putExtra("chat_room_name", viewHolder.roomTitle.toString());
                        startActivity(intent);
                        Toast.makeText(getActivity(),viewHolder.getAdapterPosition()+"!", Toast.LENGTH_SHORT).show();
                    }
                });

                return viewHolder;
            }

        };

        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    /* sortQuery() : 채팅방 목록을 필터링하는 메소드 */
    private Query filterQuery(Query q){
        StringTokenizer st = new StringTokenizer(user_addr, " ");
        String key = st.nextToken();
        return q.whereEqualTo("location",key);
    }

    private class ChatViewHolder extends RecyclerView.ViewHolder{
        public TextView roomTitle;
        public TextView roomLocation;
        public TextView roomDeadline;
        public TextView roomContent;
        public ImageView roomHost;

        public ChatViewHolder(View itemView){
            super(itemView);
            roomHost = (ImageView)itemView.findViewById(R.id.room_host);
            roomTitle = (TextView)itemView.findViewById(R.id.room_title);
            roomLocation = (TextView)itemView.findViewById(R.id.room_location);
            roomDeadline = (TextView)itemView.findViewById(R.id.room_deadline);
            roomContent = (TextView)itemView.findViewById(R.id.room_content);
        }

    }
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }
    private void getListItems() {
       query = FirebaseFirestore.getInstance().collection("chatrooms");
            query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        }
                        else{

                            List <ChatRoom> crooms = queryDocumentSnapshots.toObjects(ChatRoom.class);
                            chatRooms.addAll(crooms);
                            Log.d(TAG, "onSuccess: " + chatRooms);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
       // Query filteredQuery = filterQuery(query);
       // FirestoreRecyclerOptions<ChatRoom> options = new FirestoreRecyclerOptions.Builder<ChatRoom>().setQuery(filteredQuery.orderBy("timeStamp"), ChatRoom.class).build();

    }



}