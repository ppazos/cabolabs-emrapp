package opt_repository

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class OperationalTemplateTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testSomething() {
        def optm = OperationalTemplateManager.getInstance()
        optm.loadAll("opts")
        
        def opt = optm.getTemplate("Signos")
        assertEquals opt.getTerm("openEHR-EHR-COMPOSITION.signos.v1", "at0000"), "Signos vitales"
        assertEquals opt.getTerm("openEHR-EHR-COMPOSITION.signos.v1", "at0006"), "*OBSERVATION(es)"
        
        assertEquals opt.getTerm("openEHR-EHR-OBSERVATION.blood_pressure.v1", "at0004"), "Systolic"
        assertEquals opt.getTerm("openEHR-EHR-OBSERVATION.blood_pressure.v1", "at0008"), "Position"
        assertEquals opt.getDescription("openEHR-EHR-OBSERVATION.blood_pressure.v1", "at0008"), "The position of the subject at the time of measurement."
        
        //opt.test("openEHR-EHR-OBSERVATION.blood_pressure.v1", "at0006")
        //opt.test("openEHR-EHR-OBSERVATION.blood_pressure.v1", "at0008")
    }
}
