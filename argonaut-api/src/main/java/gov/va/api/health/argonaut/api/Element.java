package gov.va.api.health.argonaut.api;

import java.util.List;

public interface Element {
    String id();

    List<Extension> extension();
}