package research.windowing;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;

public interface WindowingOptions extends TimeOptions {
    @Description("Fixed window duration, in minutes")
    @Default.Integer(30)
    Integer getWindowSize();
    void setWindowSize(Integer value);

    @Description("Minimum randomly assigned timestamp, in milliseconds-since-epoch")
    @Default.InstanceFactory(DefaultToCurrentSystemTime.class)
    Long getMinTimestampMillis();
    void setMinTimestampMillis(Long value);

    @Description("Maximum randomly assigned timestamp, in milliseconds-since-epoch")
    @Default.InstanceFactory(DefaultToMinTimestampPlusOneHour.class)
    Long getMaxTimestampMillis();
    void setMaxTimestampMillis(Long value);

    @Description("Fixed number of shards to produce per window")
    Integer getNumShards();
    void setNumShards(Integer numShards);
}
