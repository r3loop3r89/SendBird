package com.shra1.sendbird;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;
import com.shra1.sendbird.utils.MyProgressDialog;
import com.shra1.sendbird.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import static com.shra1.sendbird.GuestActivity.ADMIN;

public class UserChatActivity extends AppCompatActivity {
    public static final String chattingWithID1 = "chattingWithID";
    SharedPreferencesManager s;
    Context mCtx;
    Dialog setNameDialog;
    GroupChannel myGroupChannel = null;
    PreviousMessageListQuery previousMessageListQuery = null;
    AdminMessagesRecyclerViewAdapter adapter;
    String chattingWithID;
    List<UserMessage> messages;
    private RecyclerView rvGAChat;
    private EditText etGAChatText;
    private Button bGASend;
    private EditText etSNDLEmail;
    private EditText etSNDLName;
    private Button bSNDLSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        mCtx = this;
        initViews();
        s = SharedPreferencesManager.getInstance(mCtx);

        chattingWithID = getIntent().getStringExtra(chattingWithID1);

        MAINTASKS();


    }

    public void MAINTASKS() {

        UserChatActivity.this.setTitle("hello, " + s.getGuestName());

        loadUsersChats();


        bGASend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etGAChatText.getText().toString().trim();
                myGroupChannel.sendUserMessage(s, new BaseChannel.SendUserMessageHandler() {
                    @Override
                    public void onSent(UserMessage userMessage, SendBirdException e) {
                        //messages.add(userMessage);
                        adapter.addMessage(userMessage);
                        etGAChatText.setText("");
                    }
                });
            }
        });
    }

    private void loadUsersChats() {
        List<String> users = new ArrayList<>();
        users.add(ADMIN);
        users.add(chattingWithID);

        MyProgressDialog.showMyProgressDialog(mCtx, "Creating Channel...", false);
        GroupChannel.createChannelWithUserIds(users,
                true,
                chattingWithID,null,null,
                new GroupChannel.GroupChannelCreateHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        MyProgressDialog.dissmisMyProgressDialog();
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }

                        if (myGroupChannel == null) {
                            myGroupChannel = groupChannel;
                        }

                        loadHistory();

                    }
                });

    }

    private void loadHistory() {
        if (previousMessageListQuery == null) {
            previousMessageListQuery = myGroupChannel.createPreviousMessageListQuery();

            MyProgressDialog.showMyProgressDialog(mCtx, "Loading history...", false);
            previousMessageListQuery.load(30, false, new PreviousMessageListQuery.MessageListQueryResult() {
                @Override
                public void onResult(List<BaseMessage> list, SendBirdException e) {
                    MyProgressDialog.dissmisMyProgressDialog();
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }

                    messages = new ArrayList<>();
                    for (BaseMessage b : list) {
                        if (b instanceof UserMessage) {
                            UserMessage u = (UserMessage) b;
                            messages.add(u);
                        }
                    }

                    adapter = new AdminMessagesRecyclerViewAdapter(mCtx, messages);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCtx);
                    rvGAChat.setLayoutManager(layoutManager);
                    rvGAChat.setAdapter(adapter);

                }
            });
            setupChannelHandler();
        }
    }

    private void setupChannelHandler() {
        SendBird.addChannelHandler(chattingWithID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseMessage instanceof UserMessage) {
                    UserMessage ux = (UserMessage) baseMessage;
                    adapter.addMessage(ux);
                }
            }
        });
    }

    private void initViews() {
        rvGAChat = (RecyclerView) findViewById(R.id.rvGAChat);
        etGAChatText = (EditText) findViewById(R.id.etGAChatText);
        bGASend = (Button) findViewById(R.id.bGASend);
    }
}
