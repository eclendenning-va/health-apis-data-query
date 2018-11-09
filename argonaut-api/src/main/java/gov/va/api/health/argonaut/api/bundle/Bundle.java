package gov.va.api.health.argonaut.api.bundle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gov.va.api.health.argonaut.api.*;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Pattern;
import java.util.List;

@Value
@Builder(toBuilder = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonDeserialize(builder = Bundle.BundleBuilder.class)
public class Bundle <T> implements Resource {
    String id;
    Meta meta;
    String implicitRules;
    String language;

    String type;
    @Pattern(regexp = Fhir.CODE)
    Integer total;
    List<Link> link;
    List<Entry<T>> entry;
    //Signature

    /** Start a builder chain for a given entry list. */
    public static <R> BundleBuilder<R> forEntry(List<Entry<R>> forEntry) {
        return Bundle.<R>builder().entry(forEntry);
    }

    @Value
    @Builder(toBuilder = true)
    public static class Entry<T> implements BackboneElement {
        String id;
        List<Extension> extension;
        List<Extension> modifierExtension;
        List<Link> link;
        String fullUrl;
        T resource;
        //Search search;
        //request
        //response

    }
}

