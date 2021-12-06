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
        String[] components = c.element().split(",", -1);
        System.out.println(" COMPONENTS " +  components[1] +  "     " + components[2]);
        try {
            String id = parseId(components[1].trim());
            String date = parseDate(components[2].trim());

            Service service = new Service(id, date);
            c.output(service);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e) {
            parseErrs.inc();
            LOG.info("Parse Error on " + c.element() + ", " + e.getMessage());
        }
    }

    private String parseDate(String date) throws StringIndexOutOfBoundsException {
        String[] components = date.split("=", -1);
        return components[1].replace("}}", "").substring(0, 10).trim();
    }

    private String parseId(String id) {
        String[] components = id.split("=", -1);
        return components[1].trim();
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
