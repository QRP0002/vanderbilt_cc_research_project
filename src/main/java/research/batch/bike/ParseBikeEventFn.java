package research.batch.bike;

import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ParseBikeEventFn extends DoFn<String, Bike> {
    private static final Logger LOG = LoggerFactory.getLogger(ParseBikeEventFn.class);
    private final Counter parseErrs = Metrics.counter("main", "ParseErrors");

    @ProcessElement
    public void processElement(ProcessContext c) {
        String[] components = c.element().split(",", -1);

        try {
            String date = parseDate(components[0].trim());
            int total = Integer.parseInt(components[1].trim());
            int eSide = Integer.parseInt(components[2].trim());
            int wSide = Integer.parseInt(components[3].trim());

            Bike bike = new Bike(date, total, eSide, wSide);
            c.output(bike);

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e) {
            parseErrs.inc();
            LOG.info("Parse Error on " + c.element() + ", " + e.getMessage());
        }
    }

    private String parseDate(String date) {
        return date.substring(0, 10);
    }
}
