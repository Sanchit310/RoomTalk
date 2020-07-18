package example.android.roomtalk.model;

public class InitialData {

    private String userName;
    private String roomName;

    public InitialData(String username, String roomname) {
        this.userName = username;
        this.roomName = roomname;
    }

    public String getUsername() {
        return userName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
