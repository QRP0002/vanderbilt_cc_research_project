package research.batch.service;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface ServiceRunnerOptions extends PipelineOptions {

    @Description("Path to the input file")
    @Default.String("/home/pommerq/flink/311_Service_Requests_from_2010_to_Present.csv")
    String getInputFile();
    void setInputFile(String value);

    @Description("Path to the output file")
    @Default.String("./src/main/resources/service_output.txt")
    String getOutputFile();
    void setOutputFile(String value);

    @Description("Mongo Uri")
    String getMongoUri();
    void setMongoUri(String value);

    @Description("Mongo Database")
    @Default.String("batch_large")
    String getMongoDatabase();
    void setMongoDatabase(String value);

    @Description("Mongo Database")
    @Default.String("new_york_311")
    String getReadMongoDatabase();
    void setReadMongoDatabase(String value);

    @Description("Mongo Collection")
    @Default.String("service")
    String getMongoCollection();
    void setMongoCollection(String value);
}