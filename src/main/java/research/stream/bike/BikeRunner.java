package research.stream.bike;

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

public class BikeRunner {
    public static void main(String[] args) {
        if(args.length <= 0) {
            System.out.println("Please provide IP for database: spark-main or flink-main");
            return;
        }

        BikeRunnerOptions options = PipelineOptionsFactory
            .fromArgs(args)
            .withoutStrictParsing()
            .as(BikeRunnerOptions.class);
        WindowingOptions windowOptions = PipelineOptionsFactory
            .fromArgs(args)
            .withoutStrictParsing()
            .as(WindowingOptions.class);

        System.out.println("Mongodb: " + args[0]);
        String uri = "mongodb://" + args[0].replace(",", "").trim() + ":27017";
        options.setMongoUri(uri);
        runRunner(options, windowOptions);
    }

    static void runRunner(BikeRunnerOptions bikeOptions, WindowingOptions windowOptions) {
        final Instant minTimestamp = new Instant(windowOptions.getMinTimestampMillis());
        final Instant maxTimestamp = new Instant(windowOptions.getMaxTimestampMillis());

        Pipeline pipeline = Pipeline.create(bikeOptions);
        try {
            pipeline
                    .apply("Read Input DB", TextIO.read().from(bikeOptions.getInputFile()))
//                    .apply(MongoDbIO.read()
//                            .withUri(bikeOptions.getMongoUri())
//                            .withDatabase(bikeOptions.getReadMongoDatabase())
//                            .withCollection(bikeOptions.getMongoCollection()))
//                    .apply("Add Timestamp for bounded data", ParDo.of(new AddTimestampFn(minTimestamp, maxTimestamp)))
                    .apply("Window", Window.<String>into(new GlobalWindows())
                        .triggering(Repeatedly
                            .forever(AfterProcessingTime
                                .pastFirstElementInPane()
                                .plusDelayOf(Duration.standardMinutes(60))
                            )
                        )
                        .withAllowedLateness(Duration.ZERO).discardingFiredPanes())
                    .apply("Parse Input", ParDo.of(new ParseBikeEventFn()))
                    .apply("Extract Total Crossing", new ExtractAndSumBike())
                        .apply("Convert to Json", MapElements.via(new SimpleFunction<KV<String, Integer>, Document>() {
                            public Document apply(KV<String, Integer> input) {
                                return Document.parse("{\"" + input.getKey() + "\": " + input.getValue() + "}");
                            }
                        }))
                        .apply("Write To Mongo",
                                MongoDbIO.write()
                                        .withUri(bikeOptions.getMongoUri())
                                        .withDatabase(bikeOptions.getMongoDatabase())
                                        .withCollection(bikeOptions.getMongoCollection())
                        );
                    //.apply("WriteUserScoreSums", new WriteToText<>(bikeOptions.getOutput(), configureOutput(), false));
        } catch (Exception e) {
            System.out.println("LOG: This is an issue " + e);
        }
        System.out.println(bikeOptions.getMongoUri());
        pipeline.run().waitUntilFinish();
    }

    protected static Map<String, WriteToText.FieldFn<KV<String, Integer>>> configureOutput() {
        Map<String, WriteToText.FieldFn<KV<String, Integer>>> config = new HashMap<>();
        config.put("date", (c, w) -> c.element().getKey());
        config.put("total", (c, w) -> c.element().getValue());
        return config;
    }
}