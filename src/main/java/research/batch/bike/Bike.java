package research.batch.bike;

import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
public class Bike {
    String date;
    int total;
    int wSide;
    int eSide;

    public Bike() {}

    public Bike(String date, int total, int wSide, int eSide) {
        this.date = date;
        this.total = total;
        this.wSide = wSide;
        this.eSide = eSide;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWSide() {
        return this.wSide;
    }

    public void setWSide(int wSide) {
        this.wSide = wSide;
    }

    public int getESide() {
        return this.eSide;
    }

    public void setESide(int eSide) {
        this.eSide = eSide;
    }
}

