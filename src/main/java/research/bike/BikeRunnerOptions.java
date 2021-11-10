package research.bike;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface BikeRunnerOptions extends PipelineOptions {

    @Description("Path to the input file")
    @Default.String("./src/main/resources/Fremont_Bridge_Bicycle_Counter.csv")
    String getInputFile();
    void setInputFile(String value);

    @Description("Path to the output file")
    @Default.String("./src/main/resources/bike_output.txt")
    String getOutputFile();
    void setOutputFile(String value);
}
