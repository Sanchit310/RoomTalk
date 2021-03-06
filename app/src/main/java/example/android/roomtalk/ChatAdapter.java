package example.android.roomtalk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    public static final int  IMAGE_SENT = 4;
    public static final int  IMAGE_RECEIVED = 5;

    OnItemClickListener mListener;

    List<Message> messageList;
    Context context;

    public interface OnItemClickListener{
        public void onImageClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mListener = onItemClickListener;
    }

    public ChatAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){

            case CHAT_MINE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_msg_item_layout, parent, false);
                return new ChatMyViewHolder(view);
            case CHAT_PARTNER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_msg_layout_item, parent, false);
                return new ChatOtViewHolder(view);
            case USER_JOIN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_leave_item_layout, parent, false);
                return new ChatJoinViewHolder(view);
            case USER_LEAVE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_leave_item_layout, parent, false);
                Log.d("TAG", "onCreateViewHolder: ");
                return new ChatLeaveViewHolder(view);
            case IMAGE_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_image_layout, parent, false);
                Log.d("TAG", "onCreateViewHolder: ");
                return new ImageSendViewHolder(view);
            case IMAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recevied_image_layout, parent, false);
                Log.d("TAG", "onCreateViewHolder: ");
                return new ImageReceviedViewHolder(view, mListener);

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

            case CHAT_MINE:
                ChatMyViewHolder chatMyViewHolder = (ChatMyViewHolder) holder;
               chatMyViewHolder.message.setText(msgContent);
               break;
            case CHAT_PARTNER:
                ChatOtViewHolder chatOtViewHolder = (ChatOtViewHolder) holder;
                chatOtViewHolder.userName.setText(userName);
                chatOtViewHolder.message.setText(msgContent);
                break;
            case USER_JOIN:
                ChatJoinViewHolder chatJoinViewHolder = (ChatJoinViewHolder) holder;
                chatJoinViewHolder.text.setText(userName);
                break;
            case USER_LEAVE:
                ChatLeaveViewHolder chatLeaveViewHolder = (ChatLeaveViewHolder) holder;
                chatLeaveViewHolder.text.setText(userName);
                break;
            case IMAGE_SENT:
                ImageSendViewHolder imageSendveViewHolder = (ImageSendViewHolder) holder;
                Bitmap bitmap = getBitmapFromString(msgContent);
                imageSendveViewHolder.sentImage.setImageBitmap(bitmap);
                break;
            case IMAGE_RECEIVED:
                ImageReceviedViewHolder imageReceviedViewHolder = (ImageReceviedViewHolder) holder;
                Bitmap bitmap1 = getBitmapFromString(msgContent);
                imageReceviedViewHolder.receivedImage.setImageBitmap(bitmap1);
                imageReceviedViewHolder.userName.setText(userName);
                break;

        }
    }

    public Bitmap getBitmapFromString(String image){
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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

    public static class ImageSendViewHolder extends RecyclerView.ViewHolder{

        ImageView sentImage;

        public ImageSendViewHolder(@NonNull View itemView) {
            super(itemView);

            sentImage = itemView.findViewById(R.id.sentImage);
        }
    }

    public static class ImageReceviedViewHolder extends RecyclerView.ViewHolder{

        ImageButton downloadBtn;
        ImageView receivedImage;
        TextView userName;

        public ImageReceviedViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            receivedImage = itemView.findViewById(R.id.receviedImage);
            userName = itemView.findViewById(R.id.usernameImage);
            downloadBtn = itemView.findViewById(R.id.downloadBtn);

            downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onImageClicked(position);
                        }
                    }
                }
            });
        }
    }


}
