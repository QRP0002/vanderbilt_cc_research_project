package research.windowing;

import org.apache.beam.sdk.transforms.DoFn;
import org.bson.Document;
import org.joda.time.Instant;

import java.util.concurrent.ThreadLocalRandom;

public class AddTimestampFn extends DoFn<String, String> {
    private final Instant minTimestamp;
    private final Instant maxTimestamp;

    public AddTimestampFn(Instant minTimestamp, Instant maxTimestamp) {
        this.minTimestamp = minTimestamp;
        this.maxTimestamp = maxTimestamp;
    }

    @ProcessElement
    public void processElement(@Element String element, OutputReceiver<String> receiver) {
        Instant randomTimestamp =
                new Instant(
                        ThreadLocalRandom.current()
                                .nextLong(minTimestamp.getMillis(), maxTimestamp.getMillis()));
        System.out.println("WE are random" + randomTimestamp);
        receiver.outputWithTimestamp(element, randomTimestamp);
    }
}
