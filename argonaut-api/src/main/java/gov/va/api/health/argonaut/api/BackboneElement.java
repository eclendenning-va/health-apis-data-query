package gov.va.api.health.argonaut.api;

import java.util.List;

public interface BackboneElement extends Element{
    List<Extension> modifierExtension();
}