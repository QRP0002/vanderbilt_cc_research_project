package research.windowing;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface TimeOptions extends PipelineOptions {
    @Description("Whether to keep jobs running after local process exit")
    @Default.Boolean(false)
    boolean getKeepJobsRunning();
    void setKeepJobsRunning(boolean keepJobsRunning);

    @Description("Number of workers to use when executing the injector pipeline")
    @Default.Integer(1)
    int getInjectorNumWorkers();
    void setInjectorNumWorkers(int numWorkers);
}
