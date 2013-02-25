package see.fa.jmeter.parallelrequest;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

import java.util.concurrent.atomic.AtomicBoolean;

public class SamplerSpy extends AbstractSampler {

    private AtomicBoolean wasSampled = new AtomicBoolean();

    public SampleResult sample(Entry e) {
        wasSampled.set(true);
        return null;
    }

    public boolean wasSampled() {
        return wasSampled.get();
    }
}
