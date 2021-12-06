package research.batch.emergency;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.bson.Document;
import org.apache.beam.sdk.io.mongodb.MongoDbIO;
import research.WriteToText;

import java.util.HashMap;
import java.util.Map;

public class EmergencyRunner {

    public static void main(String[] args) {
        if(args.length <= 0) {
            System.out.println("Please provide IP for database: spark-main or flink-main");
            return;
        }

        EmergencyRunnerOptions options = PipelineOptionsFactory
            .fromArgs(args)
            .withoutStrictParsing()
            .as(EmergencyRunnerOptions.class);

        System.out.println("Mongodb: " + args[0]);
        String uri = "mongodb://lessley:password@" + args[0].replace(",", "").trim() + ":30001";
        options.setMongoUri(uri);
        runRunner(options);
    }

    static void runRunner(EmergencyRunnerOptions options) {
        Pipeline pipeline = Pipeline.create(options);

        pipeline
            .apply(MongoDbIO.read()
                .withUri(options.getMongoUri())
                .withDatabase(options.getReadMongoDatabase())
                .withCollection(options.getMongoCollection()))
            .apply("Parse Input", ParDo.of(new ParseEmergencyEventFn()))
            .apply("Extract Total", new ExtractAndSumEmergency())
            .apply("Convert to Json", MapElements.via(new SimpleFunction<KV<String, Long>, Document>() {
                public Document apply(KV<String, Long> input) {
                    return Document.parse("{\"" + input.getKey() + "\": " + input.getValue() + "}");
                }
            }))
            .apply("Write To Mongo",
                MongoDbIO.write()
                    .withUri(options.getMongoUri())
                    .withDatabase(options.getMongoDatabase())
                    .withCollection(options.getMongoCollection())
            );
            //.apply("WriteUserScoreSums", new WriteToText<>(options.getOutputFile(), configureOutput(), false));
        pipeline.run().waitUntilFinish();
    }
    protected static Map<String, WriteToText.FieldFn<KV<String, Long>>> configureOutput() {
        Map<String, WriteToText.FieldFn<KV<String, Long>>> config = new HashMap<>();
        config.put("Date", (c, w) -> c.element().getKey());
        config.put("Total Calls per Day", (c, w) -> c.element().getValue());
        return config;
    }
}

