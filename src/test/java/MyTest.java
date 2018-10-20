import org.camunda.bpm.dmn.engine.CoverageDmnEngineRule;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class MyTest {

    @Rule
    public CoverageDmnEngineRule dmnEngineRule = new CoverageDmnEngineRule();

    @Test
    public void test() {
        DmnEngine dmnEngine = dmnEngineRule.getDmnEngine();
        InputStream inputStream = MyTest.class.getResourceAsStream("Example.dmn");

        VariableMap variables = Variables.createVariables();
        variables.put("status", "bronze");
        variables.put("sum", 0);

        DmnDecision decision = dmnEngine.parseDecision("orderDecision", inputStream);
        DmnDecisionResult result = dmnEngine.evaluateDecision(decision, variables);

        assertEquals(0.25, dmnEngineRule.getCoverage(), 0.0);
    }

    @Test
    public void test1() {
        DmnEngine dmnEngine = dmnEngineRule.getDmnEngine();
        InputStream inputStream = MyTest.class.getResourceAsStream("Example.dmn");

        VariableMap variables = Variables.createVariables();
        variables.put("status", "bronze");
        variables.put("sum", 0);

        DmnDecision decision = dmnEngine.parseDecision("orderDecision", inputStream);
        DmnDecisionResult result = dmnEngine.evaluateDecision(decision, variables);

        // assert the result
        // ...
    }
}
