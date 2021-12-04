package research.stream.emergency;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface EmergencyRunnerOptions extends PipelineOptions {
    @Description("Path to the input file")
    @Default.String("/home/pommerq/flink/Seattle_Real_Time_Fire_911_Calls.csv")
    String getInputFile();
    void setInputFile(String value);

    @Description("Path of the file to write to.")
    @Default.String("./src/main/resources/output-emergency.txt")
    String getOutput();
    void setOutput(String value);

    @Description("Mongo Uri")
    String getMongoUri();
    void setMongoUri(String value);

    @Description("Mongo Database")
    @Default.String("stream_medium")
    String getMongoDatabase();
    void setMongoDatabase(String value);

    @Description("Mongo Database")
    @Default.String("seattle_emergency")
    String getReadMongoDatabase();
    void setReadMongoDatabase(String value);

    @Description("Mongo Collection")
    @Default.String("emergency")
    String getMongoCollection();
    void setMongoCollection(String value);
}