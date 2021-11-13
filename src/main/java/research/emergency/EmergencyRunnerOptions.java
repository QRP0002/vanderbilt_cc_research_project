package research.emergency;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface EmergencyRunnerOptions extends PipelineOptions {
    @Description("Path to the input file")
    @Default.String("./src/main/resources/Seattle_Real_Time_Fire_911_Calls.csv")
    String getInputFile();
    void setInputFile(String value);

    @Description("Path to the output file")
    @Default.String("./src/main/resources/emergency_output.txt")
    String getOutputFile();
    void setOutputFile(String value);

    @Description("Mongo Uri")
    @Default.String("mongodb://spark-main:27017")
    String getMongoUri();
    void setMongoUri(String value);

    @Description("Mongo Database")
    @Default.String("spark_medium")
    String getMongoDatabase();
    void setMongoDatabase(String value);

    @Description("Mongo Collection")
    @Default.String("emergency")
    String getMongoCollection();
    void setMongoCollection(String value);
}
