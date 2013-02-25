package see.fa.jmeter.parallelrequest;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.samplers.Sampler;

/**
 *
 * @author franz
 */
public class ParallelRequestsController extends GenericController {

    private transient boolean hasAlreadyParallelizedAllSamplers = false;

    private Iterator<ParallelSampler> parallelSamplersIterator;

    @Override
    public Sampler next() {
        parallelizedAllSamplers();
        return parallelSamplersIterator.hasNext() ? parallelSamplersIterator.next() : null;
    }

    private void parallelizedAllSamplers() {
        if (hasAlreadyParallelizedAllSamplers) {
            return;
        }
        hasAlreadyParallelizedAllSamplers = true;
        List<Sampler> samplers = getAllSamplers();
        ParallelSamplerGroup parallelSamplerGroup = new ParallelSamplerGroup(samplers);
        List<ParallelSampler> parallelSamplers = new LinkedList<ParallelSampler>();
        for (Sampler sampler : samplers) {
            parallelSamplers.add(new ParallelSampler(parallelSamplerGroup, sampler));
        }
        parallelSamplersIterator = parallelSamplers.iterator();
    }

    private List<Sampler> getAllSamplers() {
        Sampler sampler;
        List<Sampler> samplers = new LinkedList<Sampler>();
        do {
            sampler = super.next();
            if (sampler != null) {
                samplers.add(sampler);
            }
        } while (sampler != null);
        return samplers;
    }


}
