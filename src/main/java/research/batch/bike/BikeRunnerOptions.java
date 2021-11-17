package research.batch.bike;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface BikeRunnerOptions extends PipelineOptions {

    @Description("Path to the input file")
    @Default.String("/home/pommerq/flink/Fremont_Bridge_Bicycle_Counter.csv")
    String getInputFile();
    void setInputFile(String value);

    //spark-main
    @Description("Mongo Uri")
    @Default.String("mongodb://flink-main:27017")
    String getMongoUri();
    void setMongoUri(String value);

    @Description("Mongo Database")
    @Default.String("spark_small")
    String getMongoDatabase();
    void setMongoDatabase(String value);

    @Description("Mongo Collection")
    @Default.String("bikes")
    String getMongoCollection();
    void setMongoCollection(String value);
}
