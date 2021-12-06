package research.stream.emergency;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.mongodb.MongoDbIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.windowing.*;
import org.apache.beam.sdk.values.KV;
import org.bson.Document;
import org.joda.time.Duration;
import org.joda.time.Instant;
import research.WriteToText;
import research.windowing.AddTimestampFn;
import research.windowing.WindowingOptions;

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
        WindowingOptions windowOptions = PipelineOptionsFactory
                .fromArgs(args)
                .withoutStrictParsing()
                .as(WindowingOptions.class);

        System.out.println("Mongodb: " + args[0]);
        String uri = "mongodb://lessley:password@" + args[0].replace(",", "").trim() + ":30001";
        options.setMongoUri(uri);
        runRunner(options, windowOptions);
    }

    static void runRunner(EmergencyRunnerOptions options, WindowingOptions windowOptions) {
        final Instant minTimestamp = new Instant(windowOptions.getMinTimestampMillis());
        final Instant maxTimestamp = new Instant(windowOptions.getMaxTimestampMillis());

        Pipeline pipeline = Pipeline.create(options);

        pipeline
            .apply(MongoDbIO.read()
                    .withUri(options.getMongoUri())
                    .withDatabase(options.getReadMongoDatabase())
                    .withCollection(options.getMongoCollection()))
            .apply("Add Timestamp for bounded data", ParDo.of(new AddTimestampFn(minTimestamp, maxTimestamp)))
            .apply(Window.into(FixedWindows.of(Duration.standardMinutes(windowOptions.getWindowSize()))))
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
            //.apply("WriteUserScoreSums", new WriteToText<>(options.getOutput(), configureOutput(), false));
        pipeline.run().waitUntilFinish();
    }
    protected static Map<String, WriteToText.FieldFn<KV<String, Long>>> configureOutput() {
        Map<String, WriteToText.FieldFn<KV<String, Long>>> config = new HashMap<>();
        config.put("Date", (c, w) -> c.element().getKey());
        config.put("Total Calls per Day", (c, w) -> c.element().getValue());
        return config;
    }
}

