package research.batch.service;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.mongodb.MongoDbIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.bson.Document;
import research.WriteToText;

import java.util.HashMap;
import java.util.Map;

public class ServiceRunner {

    public static void main(String[] args) {
        ServiceRunnerOptions options = PipelineOptionsFactory.fromArgs(args).withoutStrictParsing().as(ServiceRunnerOptions.class);
        runRunner(options);
    }

    static void runRunner(ServiceRunnerOptions options) {
        Pipeline pipeline = Pipeline.create(options);

        pipeline
            .apply("Read Input File", TextIO.read().from(options.getInputFile()))
            .apply("Parse Input", ParDo.of(new ParseServiceEventFn()))
            .apply("Extract Total", new ExtractAndSumService())
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
        pipeline.run().waitUntilFinish();
    }
    protected static Map<String, WriteToText.FieldFn<KV<String, Long>>> configureOutput() {
        Map<String, WriteToText.FieldFn<KV<String, Long>>> config = new HashMap<>();
        config.put("Date", (c, w) -> c.element().getKey());
        config.put("Total Tickets per Day", (c, w) -> c.element().getValue());
        return config;
    }
}
