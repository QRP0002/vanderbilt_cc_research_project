package research.stream.bike;

import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.vendor.grpc.v1p36p0.com.google.gson.Gson;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import research.entities.Bike;

public class ParseBikeEventFn extends DoFn<String, Bike> {
    private static final Logger LOG = LoggerFactory.getLogger(ParseBikeEventFn.class);
    private final Counter parseErrs = Metrics.counter("main", "ParseErrors");

    @ProcessElement
    public void processElement(ProcessContext c) {
        String[] components = c.element().split(",", -1);
        try {
            String date = parseDate(components[1].trim());
            int total = parseTotal(components[2].trim());

            Bike bike = new Bike(date, total);
            c.output(bike);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e) {
            parseErrs.inc();
            LOG.info("Parse Error on " + c.element() + ", " + e.getMessage());
        }
    }

    private String parseDate(String date) {
        String[] components = date.split("=", -1);
        return components[1].substring(0, 10);
    }

    private int parseTotal(String total) {
        String[] components = total.split("=", -1);
        return Integer.parseInt(components[1].replace("}}","").trim());
    }
}


//    String[] components = c.element().split(",", -1);
//
//        try {
//                String date = parseDate(components[0].trim());
//                int total = Integer.parseInt(components[1].trim());
//
//                Bike service = new Bike(date, total);
//                c.output(service);
//
//                } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e) {
//                parseErrs.inc();
//                LOG.info("Parse Error on " + c.element() + ", " + e.getMessage());
//                }