package gov.va.api.health.argonaut.api.resources;

import static gov.va.api.health.argonaut.api.RoundTrip.assertRoundTrip;
import gov.va.api.health.argonaut.api.bundle.AbstractBundle.BundleType;
import gov.va.api.health.argonaut.api.bundle.AbstractEntry;
import gov.va.api.health.argonaut.api.bundle.BundleLink;
import gov.va.api.health.argonaut.api.bundle.BundleLink.LinkRelation;
import gov.va.api.health.argonaut.api.samples.SampleMedicationDispenses;
import gov.va.api.health.argonaut.api.resources.MedicationDispense.Bundle;
import gov.va.api.health.argonaut.api.resources.MedicationDispense.Entry;
import java.util.Collections;
import org.junit.Test;

public class MedicationDispenseTest {
    private final SampleMedicationDispenses data = SampleMedicationDispenses.get();

    @Test
    public void bundlerCanBuildMedicationDispenseBundles() {
        Entry entry =
            Entry.builder()
                .extension(Collections.singletonList(data.extension()))
                .fullUrl("http://medicationdispense.com")
                .id("123")
                .link(
                    Collections.singletonList(
                        BundleLink.builder()
                            .relation(LinkRelation.self)
                            .url("http://medicationdispense/123")
                            .build()))
                .resource(data.medicationDispense())
                .search(data.search())
                .request(data.request())
                .response(data.response())
                .build();

        Bundle bundle =
            Bundle.builder()
                .entry(Collections.singletonList(entry))
                .link(
                    Collections.singletonList(
                        BundleLink.builder()
                            .relation(LinkRelation.self)
                            .url("https://medicationdispense?patient=456")
                            .build()))
                .type(BundleType.searchset)
                .build();

        assertRoundTrip(bundle);

        //What is the point of this
        AbstractEntry.Search.builder().build().id();
    }

    @Test
    public void medicationDispense() {
        assertRoundTrip(data.medicationDispense());
    }
}
