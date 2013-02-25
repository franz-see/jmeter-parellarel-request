package see.fa.jmeter.parallelrequest;

import org.apache.jmeter.control.gui.AbstractControllerGui;
import org.apache.jmeter.testelement.TestElement;

/**
 *
 * @author franz
 */
public class ParallelRequestsControllerGUI extends AbstractControllerGui {

    public static final String STATIC_LABEL = "Parallel Requests Controller";

    public String getLabelResource() {
        return getClass().getName();
    }

    public TestElement createTestElement() {
        ParallelRequestsController tc = new ParallelRequestsController();
        modifyTestElement(tc);
        tc.setComment("contact franz *dot* see *at* gmail *dot* com");
        return tc;
    }

    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
    }

    @Override
    public String getStaticLabel() {
        return STATIC_LABEL;
    }
}
