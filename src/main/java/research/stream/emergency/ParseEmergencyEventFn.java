package research.stream.emergency;

import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import research.batch.bike.ParseBikeEventFn;
import research.entities.Emergency;


public class ParseEmergencyEventFn extends DoFn<String, Emergency> {
    private static final Logger LOG = LoggerFactory.getLogger(ParseBikeEventFn.class);
    private final Counter parseErrs = Metrics.counter("main", "ParseErrors");

    @ProcessElement
    public void processElement(ProcessContext c) throws NullPointerException {
        String[] components = c.element().split(",", -1);

        try {
            String date = parseDate(components[1].trim());
            Emergency emergency = new Emergency(date);

            c.output(emergency);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e) {
            parseErrs.inc();
            LOG.info("Parse Error on " + c.element() + ", " + e.getMessage());
        }
    }

    private String parseDate(String date) throws StringIndexOutOfBoundsException {
        String[] components = date.split("=", -1);
        return components[1].substring(0, 10).trim();
    }
}
