package example.android.roomtalk.model;

public class SendMessage {

    private String userName;
    private String roomName;
    private String messageContent;

    public SendMessage(String userName, String roomName, String msgContent) {
        this.userName = userName;
        this.roomName = roomName;
        this.messageContent = msgContent;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
