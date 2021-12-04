package research.entities;

import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
public class Service {
    String id;
    String date;
    long count;

    public Service() {}

    public Service(String id, String date) {
        this.id = id;
        this.date = date;
        this.count = 1;
    }

    public String getTicket() {
        return id;
    }

    public void setTicket(String ticket) {
        this.id = ticket;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}