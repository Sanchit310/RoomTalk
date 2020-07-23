package example.android.roomtalk.model;

public class Message {

    private String userName;
    private String roomName;
    private String messageContent;
    private Boolean isImage;
    private int viewType;

    public Message(String userName, String roomName, String msgContent, Boolean isImage, int viewType) {
        this.userName = userName;
        this.roomName = roomName;
        this.messageContent = msgContent;
        this.isImage = isImage;
        this.viewType = viewType;
    }

    public Boolean isImage() {
        return isImage;
    }

    public void setImage(Boolean image) {
        isImage = image;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getMsgContent() {
        return messageContent;
    }

    public int getViewType() {
        return viewType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setMsgContent(String msgContent) {
        this.messageContent = msgContent;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
