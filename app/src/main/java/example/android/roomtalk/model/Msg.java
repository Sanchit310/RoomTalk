package example.android.roomtalk.model;

public class Msg {

    private String userId;
    private String username;
    private String text;
    private int viewType;

    public Msg(String userId, String username, String text, int viewType) {
        this.userId = userId;
        this.username = username;
        this.text = text;
        this.viewType = viewType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
