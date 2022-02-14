package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerChats;
    private EditText edit_text_msg;
    private FloatingActionButton sendButton;

    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";

    private ArrayList<ChatModal> chatModalArrayList;
    private ChatRVAdapter chatRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerChats = findViewById(R.id.recyclerChats);
        edit_text_msg = findViewById(R.id.edit_text_msg);
        sendButton = findViewById(R.id.sendButton);

        chatModalArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatModalArrayList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerChats.setLayoutManager(manager);
        recyclerChats.setAdapter(chatRVAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_text_msg.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a message",Toast.LENGTH_LONG).show();
                    return;
                }

                getResponse(edit_text_msg.getText().toString());
                edit_text_msg.setText("");
            }
        });
    }

    private void getResponse(String message){

        chatModalArrayList.add(new ChatModal(message, USER_KEY));  // add new message to ChatsModal
        chatRVAdapter.notifyDataSetChanged();                      // notify the Adapter that new msg is added to ChatsModal

        String url = "http://api.brainshop.ai/get?bid=163242&key=MgUXg5S6fb5h3qVE&uid=[uid]&msg=" + message;
        String BASE_URL = "http://api.brainshop.ai/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModal> call = retrofitAPI.getMessage(url);

        call.enqueue(new Callback<MsgModal>() {    // call to get the data from API
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {

                if(response.isSuccessful()){
                    // if response is successful we will pas response in MsgModal class
                    MsgModal modal = response.body();

                    // now pass this to arraylist
                    chatModalArrayList.add(new ChatModal(modal.getCnt(), BOT_KEY));

                    // notify the adapter that data has been updated in array list
                    chatRVAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                chatModalArrayList.add(new ChatModal("Please revert your question", BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });

    }
}