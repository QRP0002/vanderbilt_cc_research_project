package research.stream.service;

import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.Sum;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import research.entities.Service;

public class ExtractAndSumService extends PTransform<PCollection<Service>, PCollection<KV<String, Long>>> {
    @Override
    public PCollection<KV<String, Long>> expand(PCollection<Service> service) {
        return service
            .apply(
                MapElements.into(
                    TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.longs()))
                    .via((Service s) -> KV.of(s.getDate(), s.getCount())))
            .apply(Sum.longsPerKey());
    }
}
