package research.stream.service;

import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import research.entities.Service;

public class ParseServiceEventFn extends DoFn<String, Service> {
    private static final Logger LOG = LoggerFactory.getLogger(ParseServiceEventFn.class);
    private final Counter parseErrs = Metrics.counter("main", "ParseErrors");

    @ProcessElement
    public void processElement(ProcessContext c) throws NullPointerException {
        String keyStr = "Unique Key";
        String dateStr = "Created Date";

        try {
//            String id = c.element().get(keyStr).toString().trim();
//            String date = parseDate(c.element().get(dateStr).toString().trim());
            Service service = new Service(" 56", "date");
            c.output(service);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e) {
            parseErrs.inc();
            LOG.info("Parse Error on " + c.element() + ", " + e.getMessage());
        }
    }

    private String parseDate(String date) throws StringIndexOutOfBoundsException {
        return date.substring(0, 10);
    }
}

//        String[] components = c.element().split(",", -1);
//
//        try {
//            String id = components[0].trim();
//            String date = parseDate(components[1].trim());
//
//            Service service = new Service(id, date);
//            c.output(service);
//
//        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e) {
//            parseErrs.inc();
//            LOG.info("Parse Error on " + c.element() + ", " + e.getMessage());
//        }
