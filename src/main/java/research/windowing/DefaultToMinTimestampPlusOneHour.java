package research.windowing;

import org.apache.beam.sdk.options.DefaultValueFactory;
import org.apache.beam.sdk.options.PipelineOptions;
import org.joda.time.Duration;

/**
 * A {@link DefaultValueFactory} that returns the minimum timestamp plus one hour.
 */
public class DefaultToMinTimestampPlusOneHour implements DefaultValueFactory<Long> {
    @Override
    public Long create(PipelineOptions options) {
        return options.as(WindowingOptions.class).getMinTimestampMillis()
                + Duration.standardHours(1).getMillis();
    }
}
