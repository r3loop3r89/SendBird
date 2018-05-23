package com.shra1.sendbird;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.shra1.sendbird.utils.MyProgressDialog;
import com.shra1.sendbird.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import static com.shra1.sendbird.GuestActivity.ADMIN;
import static com.shra1.sendbird.UserChatActivity.chattingWithID1;

public class AdminActivity extends AppCompatActivity {
    Context mCtx;
    SharedPreferencesManager s;
    ChatUser[] users;
    List<ChatUser> chatUserList;
    Dialog addUser;
    private ListView lvAAUsersList;
    private Button bAddUser;
    private EditText etSNDLEmail;
    private EditText etSNDLName;
    private Button bSNDLSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mCtx = this;
        s = SharedPreferencesManager.getInstance(mCtx);
        initViews();

        setTitle(ADMIN);

        chatUserList = new ArrayList<>();

        MyProgressDialog.showMyProgressDialog(mCtx, "Connecting...", false);
        SendBird.connect(ADMIN, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                MyProgressDialog.dissmisMyProgressDialog();
                if (e != null) {
                    e.printStackTrace();
                    finish();
                    return;
                }

                Toast.makeText(mCtx, "Connected...", Toast.LENGTH_SHORT).show();

                GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
                channelListQuery.setIncludeEmpty(true);
                channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
                    @Override
                    public void onResult(List<GroupChannel> list, SendBirdException e) {
                        if (e != null) {
                            // Error.
                            return;
                        }

                        for (GroupChannel g:list){
                            chatUserList.add(new ChatUser(g.getName(), g.getName()));
                        }

                        ArrayAdapter<ChatUser> adapter = new ArrayAdapter<ChatUser>(
                                mCtx,
                                android.R.layout.simple_list_item_1,
                                chatUserList
                        );
                        lvAAUsersList.setAdapter(adapter);
                    }
                });




            }
        });

        bAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser = new Dialog(mCtx);
                addUser.setContentView(R.layout.set_name_dialog_layout);

                etSNDLEmail = (EditText) addUser.findViewById(R.id.etSNDLEmail);
                etSNDLName = (EditText) addUser.findViewById(R.id.etSNDLName);
                bSNDLSave = (Button) addUser.findViewById(R.id.bSNDLSave);

                bSNDLSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String email = etSNDLEmail.getText().toString().trim();
                        String name = etSNDLName.getText().toString().trim();

                        ChatUser u = new ChatUser(email, name);

                        if (!chatUserList.contains(u)) {
                            Gson gson = new Gson();
                            chatUserList.add(u);
                            String userss = gson.toJson(chatUserList);
                            s.setUsersList(userss);
                        }

                        addUser.dismiss();
                    }
                });

                addUser.show();
            }
        });

        lvAAUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatUser selectedUser = (ChatUser) parent.getItemAtPosition(position);

                Intent intent = new Intent(mCtx, UserChatActivity.class);
                intent.putExtra(chattingWithID1, selectedUser.getEmail());
                startActivity(intent);
            }
        });

    }

    private void initViews() {
        lvAAUsersList = (ListView) findViewById(R.id.lvAAUsersList);
        bAddUser = (Button) findViewById(R.id.bAddUser);
    }

}
