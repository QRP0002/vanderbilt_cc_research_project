package research.windowing;

import org.apache.beam.sdk.transforms.DoFn;
import org.bson.Document;
import org.joda.time.Instant;

import java.util.concurrent.ThreadLocalRandom;

public class AddTimestampFn extends DoFn<Document, String> {
    private final Instant minTimestamp;
    private final Instant maxTimestamp;

    public AddTimestampFn(Instant minTimestamp, Instant maxTimestamp) {
        this.minTimestamp = minTimestamp;
        this.maxTimestamp = maxTimestamp;
    }

    @ProcessElement
    public void processElement(@Element Document element, OutputReceiver<String> receiver) {
        Instant randomTimestamp =
            new Instant(
                ThreadLocalRandom.current()
                    .nextLong(minTimestamp.getMillis(), maxTimestamp.getMillis()));
        receiver.outputWithTimestamp(element.toString(), randomTimestamp);
    }
}
