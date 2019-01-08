package bean;

import java.io.Serializable;

public class MessageEvent implements Serializable {
    private String message;
    private long id;
    private int status;


    public MessageEvent( long id, int status) {
        this.id = id;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

