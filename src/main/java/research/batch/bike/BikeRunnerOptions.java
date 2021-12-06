package research.batch.bike;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface BikeRunnerOptions extends PipelineOptions {

    @Description("Path to the input file")
    @Default.String("/home/ubuntu/bikes.csv")
    String getInputFile();
    void setInputFile(String value);

    @Description("Path of the file to write to.")
    @Default.String("./src/main/resources/output-bike.txt")
    String getOutput();
    void setOutput(String value);

    @Description("Mongo Uri")
    String getMongoUri();
    void setMongoUri(String value);

    @Description("Mongo Database")
    @Default.String("batch_small")
    String getMongoDatabase();
    void setMongoDatabase(String value);

    @Description("Mongo Database")
    @Default.String("seattle_bike")
    String getReadMongoDatabase();
    void setReadMongoDatabase(String value);

    @Description("Mongo Collection")
    @Default.String("bikes")
    String getMongoCollection();
    void setMongoCollection(String value);
}
