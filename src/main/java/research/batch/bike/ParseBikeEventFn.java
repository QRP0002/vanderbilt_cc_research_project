package research.batch.bike;

import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.bson.Document;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import research.entities.Bike;

public class ParseBikeEventFn extends DoFn<Document, Bike> {
    private static final Logger LOG = LoggerFactory.getLogger(ParseBikeEventFn.class);
    private final Counter parseErrs = Metrics.counter("main", "ParseErrors");

    @ProcessElement
    public void processElement(ProcessContext c) {
        String dateStr = "Date";
        String totalStr = "Fremont Bridge Total";

        try {
            String date = parseDate(c.element().get(dateStr).toString());
            int total = Integer.parseInt(c.element().get(totalStr).toString().trim());

            if(c.element().get(totalStr).toString() != null) {
                Bike bike = new Bike(date, total);
                c.output(bike);
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e) {
            parseErrs.inc();
            LOG.info("Parse Error on " + c.element() + ", " + e.getMessage());
        }
    }

    private String parseDate(String date) {
        return date.substring(0, 10).trim();
    }
}
