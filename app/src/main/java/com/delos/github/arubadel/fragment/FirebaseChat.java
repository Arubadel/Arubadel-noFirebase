package com.delos.github.arubadel.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.delos.github.arubadel.R;
import com.delos.github.arubadel.util.ChatMessage;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sumit on 14/12/16.
 */

public class FirebaseChat extends Fragment {
    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listOfMessages;
    private InputMethodManager imm;
    private DatabaseReference getChats = FirebaseDatabase.getInstance().getReference().child("Chats");
    private String input,Name,ClickedMessage;
    private TextView messageText,messageUser,messageTime;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_firebase_chat, container, false);
        listOfMessages = (ListView)rootView.findViewById(R.id.list_of_messages);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        Name= PreferenceManager.getDefaultSharedPreferences(getContext()).getString("user_nick_name", "null");
        FloatingActionButton sendButton =
                (FloatingActionButton)rootView.findViewById(R.id.fab);
        if (getChats == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            getChats = database.getReference().child("Chats");
        }
        getChats.keepSynced(true);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText message = (EditText) rootView.findViewById(R.id.input);
                input = message.getText().toString();
                /* Read the input field and push a new instance
                  of ChatMessage to the Firebase database */
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(getContext().getApplicationContext(), "Enter message", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Chats")
                            .push()
                            .setValue(new ChatMessage(input,Name));

                /* Clear the input */
                    message.setText("");
                /*hide text*/
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
            }

        });

        displayChatMessages();
        return rootView;

    }
    private void displayChatMessages(){
        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,R.layout.fragment_firebase_chat_textview, FirebaseDatabase.getInstance().getReference().child("Chats")) {
            @Override
            protected void populateView(View v, final ChatMessage model, int position) {
                // Get references to the views of message.xml

                messageText = (TextView)v.findViewById(R.id.message_text);
                messageUser = (TextView)v.findViewById(R.id.message_user);
                messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
                listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
                        ClickedMessage =model.getMessageText().toString();
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Message", ClickedMessage);
                        clipboard.setPrimaryClip(clip);

                    }
                });
            }
        };

    listOfMessages.setAdapter(adapter);

    }

}