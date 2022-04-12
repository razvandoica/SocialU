package socialnetwork.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class Message implements Comparable<Message> {
    private String from;
    private String to;
    private String text;
    private LocalDateTime data;
    private int group;

    public Message(String from, String to, String text, int group) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.data = LocalDateTime.now();
        this.group = group;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    @Override
    public int compareTo(Message u) {
        if (getData() == null || u.getData() == null) {
            return 0;
        }
        return getData().compareTo(u.getData());
    }

    @Override
    public String toString() {
        return  "from='" + from +
                ", text='" + text +
                ", data=" + data;
    }
}


