package research.emergency;

import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import research.bike.Bike;
import research.bike.ParseBikeEventFn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseEmergencyEventFn extends DoFn<String, Emergency> {
    private static final Logger LOG = LoggerFactory.getLogger(ParseBikeEventFn.class);
    private final Counter parseErrs = Metrics.counter("main", "ParseErrors");

    @ProcessElement
    public void processElement(ProcessContext c) throws NullPointerException {
        String[] components = cleanUpData(c.element()).split(",", -1);

        try {
            String address = components[0].trim();
            String type = components[1].trim();
            String date = parseDate(components[2].trim());

            Emergency emergency = new Emergency(address, type, date);
            c.output(emergency);

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException | StringIndexOutOfBoundsException e) {
            parseErrs.inc();
            LOG.info("Parse Error on " + c.element() + ", " + e.getMessage());
        }
    }

    private String parseDate(String date) throws StringIndexOutOfBoundsException {
        return date.substring(0, 10);
    }

    private String cleanUpData(String check) {
        return check
                .replace("Hang-Up,", "Hang Up")
                .replace("Mutual Aid,", "Mutual Aid")
                .replace("Assault w/Weapons,", "Assault w/Weapons")
                .replace("Natur Gas Outside,", "Natur Gas Outside")
                .replace("Rescue,", "Rescue");
    }
}
