package see.fa.jmeter.parallelrequest;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

/**
 *
 * @author franz
 */
public class ParallelSampler extends AbstractSampler {

    private final ParallelSamplerGroup parallelSamplerGroup;
    private final Sampler mainSampler;

    public ParallelSampler(ParallelSamplerGroup parallelSamplerGroup, Sampler mainSampler) {
        this.parallelSamplerGroup = parallelSamplerGroup;
        this.mainSampler = mainSampler;
    }

    public SampleResult sample(Entry e) {
        return parallelSamplerGroup.sample(mainSampler);
    }

    @Override
    public boolean equals(Object o) {
        return mainSampler.equals(o);
    }

    @Override
    public int hashCode() {
        return mainSampler.hashCode();
    }

}
