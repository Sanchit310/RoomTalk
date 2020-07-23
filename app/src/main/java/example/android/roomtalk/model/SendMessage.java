package example.android.roomtalk.model;

public class SendMessage {

    private String userName;
    private String roomName;
    private String messageContent;
    private Boolean isImage;

    public SendMessage(String userName, String roomName, Boolean isImage, String msgContent) {
        this.userName = userName;
        this.roomName = roomName;
        this.isImage = isImage;
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
