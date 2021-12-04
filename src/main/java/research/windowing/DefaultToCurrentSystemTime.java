package research.windowing;

import org.apache.beam.sdk.options.DefaultValueFactory;
import org.apache.beam.sdk.options.PipelineOptions;
import org.joda.time.Duration;

public class DefaultToCurrentSystemTime implements DefaultValueFactory<Long> {
    @Override
    public Long create(PipelineOptions options) {
        return System.currentTimeMillis();
    }
}

