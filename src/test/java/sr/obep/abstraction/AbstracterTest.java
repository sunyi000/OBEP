package sr.obep.abstraction;

import junit.framework.TestCase;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.N3DocumentFormat;
import org.semanticweb.owlapi.model.*;
import sr.obep.SemanticEvent;
import sr.obep.abstration.Abstracter;
import sr.obep.abstration.AbstracterImpl;
import sr.obep.querying.OBEPQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AbstracterTest extends TestCase {

    private OBEPTestEngine testEngine;
    private Abstracter abstracter;
    private OWLOntology o;

    public static SemanticEvent createTempEvent() {
        String ONT_IRI = "http://IBCNServices.github.io/Accio-Ontology/SSNiot#";
        String ONT_DUL_IRI = "http://IBCNServices.github.io/Accio-Ontology/ontologies/DUL.owl#";
        String ONT_SSN_IRI = "http://IBCNServices.github.io/Accio-Ontology/ontologies/ssn#";
        OWLDataFactory dfactory = OWLManager.getOWLDataFactory();
        /**
         * Construct OWL Individuals
         */
        double value = 90;
        String location = "testLocation";
        long counterTemp = 1;
        OWLClass tempObservationCls = dfactory.getOWLClass(ONT_IRI + "TemperatureObservation");
        OWLClass tempSensingDeviceCls = dfactory.getOWLClass(ONT_IRI + "TemperatureSensor");
        OWLClass tempSensorOutputCls = dfactory.getOWLClass(ONT_IRI + "TemperatureSensorOutput");
        OWLClass tempObservationValCls = dfactory
                .getOWLClass(ONT_IRI + "TemperatureObservationValue");
        OWLClass tempPropCls = dfactory.getOWLClass(ONT_IRI + "Temperature");
        OWLClass eventClass = dfactory.getOWLClass(ONT_IRI + "Event");
        OWLClass placeClass = dfactory.getOWLClass(ONT_DUL_IRI + "Place");

        // Create the individuals
        OWLNamedIndividual tempObservation = dfactory
                .getOWLNamedIndividual(ONT_IRI + "temperatureObservation_" + counterTemp);
        OWLNamedIndividual tempSensingDevice = dfactory
                .getOWLNamedIndividual(ONT_IRI + "temperatureSensor_" + counterTemp);
        OWLNamedIndividual tempSensorOutput = dfactory
                .getOWLNamedIndividual(ONT_IRI + "temperatureSensorOutput_" + counterTemp);
        OWLNamedIndividual tempObservationVal = dfactory
                .getOWLNamedIndividual(ONT_IRI + "temperatureObservationValue_" + counterTemp);
        OWLNamedIndividual tempProperty = dfactory
                .getOWLNamedIndividual(ONT_IRI + "temperatureProperty_" + counterTemp);
        OWLNamedIndividual locationInd = dfactory
                .getOWLNamedIndividual(ONT_IRI + "Place" + location);

        OWLNamedIndividual eventInd = dfactory
                .getOWLNamedIndividual(ONT_IRI + "Event_" + counterTemp);
        // Create the data properties
        OWLDataProperty hasDataValueProp = dfactory
                .getOWLDataProperty(ONT_DUL_IRI + "hasDataValue");
        // Create the object properties
        OWLObjectProperty hasContextProp = dfactory.getOWLObjectProperty(ONT_IRI + "hasContext");
        OWLObjectProperty hasValueProp = dfactory.getOWLObjectProperty(ONT_SSN_IRI + "hasValue");
        OWLObjectProperty observedBy = dfactory.getOWLObjectProperty(ONT_SSN_IRI + "observedBy");
        OWLObjectProperty observationResult = dfactory
                .getOWLObjectProperty(ONT_SSN_IRI + "observationResult");
        OWLObjectProperty observedProperty = dfactory
                .getOWLObjectProperty(ONT_SSN_IRI + "observedProperty");
        OWLObjectProperty hasLocationProp = dfactory
                .getOWLObjectProperty(ONT_DUL_IRI + "hasLocation");

        // Create the message
        SemanticEvent message = new SemanticEvent(eventInd, "1", System.currentTimeMillis(), "TempStream");
        // add the axioms
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(eventClass, eventInd));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempObservationCls, tempObservation));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempSensorOutputCls, tempSensorOutput));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempObservationValCls, tempObservationVal));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempSensingDeviceCls, tempSensingDevice));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(placeClass, locationInd));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempPropCls, tempProperty));

        message.addAxiom(dfactory.getOWLDataPropertyAssertionAxiom(hasDataValueProp, tempObservationVal,
                (double) value));
        message.addAxiom(dfactory.getOWLObjectPropertyAssertionAxiom(hasValueProp, tempSensorOutput,
                tempObservationVal));
        message.addAxiom(dfactory.getOWLObjectPropertyAssertionAxiom(observationResult, tempObservation,
                tempSensorOutput));
        message.addAxiom(
                dfactory.getOWLObjectPropertyAssertionAxiom(observedBy, tempObservation, tempSensingDevice));
        message.addAxiom(
                dfactory.getOWLObjectPropertyAssertionAxiom(observedProperty, tempObservation, tempProperty));
        message.addAxiom(
                dfactory.getOWLObjectPropertyAssertionAxiom(hasLocationProp, tempSensingDevice, locationInd));
        message.addAxiom(dfactory.getOWLDataPropertyAssertionAxiom(hasDataValueProp, locationInd, location));

        message.addAxiom(
                dfactory.getOWLObjectPropertyAssertionAxiom(hasContextProp, eventInd, tempObservation));

        return message;
    }

    public static SemanticEvent createSmokeEvent() {
        String ONT_IRI = "http://IBCNServices.github.io/Accio-Ontology/SSNiot#";
        String ONT_DUL_IRI = "http://IBCNServices.github.io/Accio-Ontology/ontologies/DUL.owl#";
        String ONT_SSN_IRI = "http://IBCNServices.github.io/Accio-Ontology/ontologies/ssn#";
        OWLDataFactory dfactory = OWLManager.getOWLDataFactory();
        /**
         * Construct OWL Individuals
         */
        double value = 99;
        String location = "testLocation";
        long counterTemp = 1;
        OWLClass tempObservationCls = dfactory.getOWLClass(ONT_IRI + "SmokeObservation");
        OWLClass tempSensingDeviceCls = dfactory.getOWLClass(ONT_IRI + "SmokeSensor");
        OWLClass tempSensorOutputCls = dfactory.getOWLClass(ONT_IRI + "SmokeSensorOutput");
        OWLClass tempObservationValCls = dfactory
                .getOWLClass(ONT_IRI + "SmokeObservationValue");
        OWLClass tempPropCls = dfactory.getOWLClass(ONT_IRI + "Smoke");
        OWLClass eventClass = dfactory.getOWLClass(ONT_IRI + "Event");
        OWLClass placeClass = dfactory.getOWLClass(ONT_DUL_IRI + "Place");

        // Create the individuals
        OWLNamedIndividual tempObservation = dfactory
                .getOWLNamedIndividual(ONT_IRI + "smokeObservation_" + counterTemp);
        OWLNamedIndividual tempSensingDevice = dfactory
                .getOWLNamedIndividual(ONT_IRI + "smokeSensor_" + counterTemp);
        OWLNamedIndividual tempSensorOutput = dfactory
                .getOWLNamedIndividual(ONT_IRI + "smokeSensorOutput_" + counterTemp);
        OWLNamedIndividual tempObservationVal = dfactory
                .getOWLNamedIndividual(ONT_IRI + "smokeObservationValue_" + counterTemp);
        OWLNamedIndividual tempProperty = dfactory
                .getOWLNamedIndividual(ONT_IRI + "smokeProperty_" + counterTemp);
        OWLNamedIndividual locationInd = dfactory
                .getOWLNamedIndividual(ONT_IRI + "Place" + location);

        OWLNamedIndividual eventInd = dfactory
                .getOWLNamedIndividual(ONT_IRI + "Event_" + counterTemp);
        // Create the data properties
        OWLDataProperty hasDataValueProp = dfactory
                .getOWLDataProperty(ONT_DUL_IRI + "hasDataValue");
        // Create the object properties
        OWLObjectProperty hasContextProp = dfactory.getOWLObjectProperty(ONT_IRI + "hasContext");
        OWLObjectProperty hasValueProp = dfactory.getOWLObjectProperty(ONT_SSN_IRI + "hasValue");
        OWLObjectProperty observedBy = dfactory.getOWLObjectProperty(ONT_SSN_IRI + "observedBy");
        OWLObjectProperty observationResult = dfactory
                .getOWLObjectProperty(ONT_SSN_IRI + "observationResult");
        OWLObjectProperty observedProperty = dfactory
                .getOWLObjectProperty(ONT_SSN_IRI + "observedProperty");
        OWLObjectProperty hasLocationProp = dfactory
                .getOWLObjectProperty(ONT_DUL_IRI + "hasLocation");

        // Create the message
        SemanticEvent message = new SemanticEvent(eventInd, "2", System.currentTimeMillis(), "smokeStream");
        // add the axioms
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(eventClass, eventInd));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempObservationCls, tempObservation));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempSensorOutputCls, tempSensorOutput));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempObservationValCls, tempObservationVal));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempSensingDeviceCls, tempSensingDevice));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(placeClass, locationInd));
        message.addAxiom(dfactory.getOWLClassAssertionAxiom(tempPropCls, tempProperty));

        message.addAxiom(dfactory.getOWLDataPropertyAssertionAxiom(hasDataValueProp, tempObservationVal,
                (double) value));
        message.addAxiom(dfactory.getOWLObjectPropertyAssertionAxiom(hasValueProp, tempSensorOutput,
                tempObservationVal));
        message.addAxiom(dfactory.getOWLObjectPropertyAssertionAxiom(observationResult, tempObservation,
                tempSensorOutput));
        message.addAxiom(
                dfactory.getOWLObjectPropertyAssertionAxiom(observedBy, tempObservation, tempSensingDevice));
        message.addAxiom(
                dfactory.getOWLObjectPropertyAssertionAxiom(observedProperty, tempObservation, tempProperty));
        message.addAxiom(
                dfactory.getOWLObjectPropertyAssertionAxiom(hasLocationProp, tempSensingDevice, locationInd));
        message.addAxiom(dfactory.getOWLDataPropertyAssertionAxiom(hasDataValueProp, locationInd, location));

        message.addAxiom(
                dfactory.getOWLObjectPropertyAssertionAxiom(hasContextProp, eventInd, tempObservation));

        return message;
    }

    public void setUp() {
        abstracter = new AbstracterImpl();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        try {
            o = manager.loadOntology(IRI.create("http://IBCNServices.github.io/Accio-Ontology/SSNiot.owl#"));
            abstracter.setOntology(o);
        } catch (OWLOntologyCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        testEngine = new OBEPTestEngine();
        abstracter.init(testEngine);

    }

    public void testAddQuery() {
        OBEPQuery query = new OBEPQueryAbstracterTest();
        Set<String> registeredQueries = new HashSet<String>();
        for (OWLEquivalentClassesAxiom eventDef : query.getEventDefinitions()) {
            eventDef.namedClasses().forEach(def -> registeredQueries.add(def.getIRI().toString()));
        }
        int ontSize = o.getAxiomCount();
        abstracter.registerQuery(query);
        assertNotSame(ontSize, o.getAxiomCount());
        abstracter.sendEvent(createTempEvent());

        assertEquals(1, testEngine.getReceivedEvent().getTriggeredFilterIRIs().size());
        assertEquals("[http://IBCNServices.github.io/Accio-Ontology/SSNiot#TemperatureFilter]", testEngine.getReceivedEvent().getTriggeredFilterIRIs().toString());
    }

    private void saveOntology(OWLOntology ontology, OWLOntologyManager manager, String suffix) {
        String location = "/tmp/massif/";
        try {
            File file = new File(location + "savedontology" + suffix + ".owl");
            if (!file.canExecute()) {
                File mkdir = new File(location);
                mkdir.mkdirs();
            }
            file.createNewFile();
            manager.saveOntology(ontology, new N3DocumentFormat(), new FileOutputStream(file));
        } catch (OWLOntologyStorageException | IOException e) {
            e.printStackTrace();
        }

    }
}
