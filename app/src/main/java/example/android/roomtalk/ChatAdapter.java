package example.android.roomtalk;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import example.android.roomtalk.model.Message;
import example.android.roomtalk.model.Msg;

public class ChatAdapter extends RecyclerView.Adapter {

    public static final int CHAT_MINE = 0;
    public static final int  CHAT_PARTNER = 1;
    public static final int  USER_JOIN = 2;
    public static final int  USER_LEAVE = 3;

    List<Message> messageList;
    Context context;

    public ChatAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){

            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_msg_item_layout, parent, false);
                return new ChatMyViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_msg_layout_item, parent, false);
                return new ChatOtViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_leave_item_layout, parent, false);
                return new ChatJoinViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_leave_item_layout, parent, false);
                Log.d("TAG", "onCreateViewHolder: ");
                return new ChatLeaveViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        String userName = message.getUserName();
        String msgContent = message.getMsgContent();
        int viewType =  message.getViewType();

        switch (viewType){

            case 0:
                ChatMyViewHolder chatMyViewHolder = (ChatMyViewHolder) holder;
               chatMyViewHolder.message.setText(msgContent);
               break;
            case 1:
                ChatOtViewHolder chatOtViewHolder = (ChatOtViewHolder) holder;
                chatOtViewHolder.userName.setText(userName);
                chatOtViewHolder.message.setText(msgContent);
                break;
            case 2:
                ChatJoinViewHolder chatJoinViewHolder = (ChatJoinViewHolder) holder;
                chatJoinViewHolder.text.setText(userName);
                break;
            case 3:
                ChatLeaveViewHolder chatLeaveViewHolder = (ChatLeaveViewHolder) holder;
                chatLeaveViewHolder.text.setText(userName);
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ChatMyViewHolder extends RecyclerView.ViewHolder{

        TextView  message;

        public ChatMyViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message1);
        }
    }

    public static class ChatOtViewHolder extends RecyclerView.ViewHolder{

        TextView userName, message;

        public ChatOtViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message2);
        }
    }

    public static class ChatJoinViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public ChatJoinViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

    public static class ChatLeaveViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public ChatLeaveViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text);
        }
    }


}
