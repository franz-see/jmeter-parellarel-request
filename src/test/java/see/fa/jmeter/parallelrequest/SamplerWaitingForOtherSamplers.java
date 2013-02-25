package see.fa.jmeter.parallelrequest;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

import java.util.concurrent.atomic.AtomicInteger;

public class SamplerWaitingForOtherSamplers extends AbstractSampler {

    private final AtomicInteger groupCount;
    private final int expectedNumberOfSamples;

    public SamplerWaitingForOtherSamplers(AtomicInteger groupCount, int expectedNumberOfSamples) {
        this.groupCount = groupCount;
        this.expectedNumberOfSamples = expectedNumberOfSamples;
    }

    public SampleResult sample(Entry e) {
        groupCount.incrementAndGet();
        while(groupCount.get() != expectedNumberOfSamples) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignore) {
                // sleeping for 10ms is just a nice-to-have. if thread can't sleep, then continue looping.
            }
        }
        return null;
    }
}
