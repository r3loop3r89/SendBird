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
import com.sendbird.android.User;
import com.sendbird.android.UserMessage;
import com.shra1.sendbird.utils.MyProgressDialog;
import com.shra1.sendbird.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class GuestActivity extends AppCompatActivity {
    public static final String ADMIN = "admin@admin.com";
    SharedPreferencesManager s;
    Context mCtx;
    Dialog setNameDialog;
    GroupChannel myGroupChannel = null;
    PreviousMessageListQuery previousMessageListQuery = null;
    GuestMessagesRecyclerViewAdapter adapter;
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

        if (s.getGuestId().equals("GuestId")) {
            setNameDialog = new Dialog(mCtx);
            setNameDialog.setContentView(R.layout.set_name_dialog_layout);


            etSNDLEmail = (EditText) setNameDialog.findViewById(R.id.etSNDLEmail);
            etSNDLName = (EditText) setNameDialog.findViewById(R.id.etSNDLName);
            bSNDLSave = (Button) setNameDialog.findViewById(R.id.bSNDLSave);

            bSNDLSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = etSNDLEmail.getText().toString().trim();
                    String name = etSNDLName.getText().toString().trim();

                    s.setGuestId(id);
                    s.setGuestName(name);

                    MAINTASKS();
                    setNameDialog.dismiss();
                }
            });

            setNameDialog.show();
            return;
        }

        MAINTASKS();


    }

    public void MAINTASKS() {

        GuestActivity.this.setTitle("hello, " + s.getGuestName() + " <" + s.getGuestId() + ">");

        MyProgressDialog.showMyProgressDialog(mCtx, "Logging in...", false);
        SendBird.connect(s.getGuestId(), new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                MyProgressDialog.dissmisMyProgressDialog();
                if (e != null) {
                    e.printStackTrace();
                    finish();
                }


                loadUsersChats();

            }
        });

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
        users.add(s.getGuestId());

        MyProgressDialog.showMyProgressDialog(mCtx, "Creating Channel...", false);
        GroupChannel.createChannelWithUserIds(users,
                true,
                s.getGuestId(),null,null,
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

                    adapter = new GuestMessagesRecyclerViewAdapter(mCtx, messages);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCtx);
                    rvGAChat.setLayoutManager(layoutManager);
                    rvGAChat.setAdapter(adapter);

                }
            });

            setupChannelHandler();

        }
    }

    private void setupChannelHandler() {
        SendBird.addChannelHandler(s.getGuestId(), new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                if (baseMessage instanceof UserMessage){
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
