package gov.va.api.health.argonaut.service.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import gov.va.api.health.argonaut.api.elements.Reference;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This mapper provides additional configuration that treats Argonaut Reference objects special. It
 * will fully qualify relative reference links.
 */
@Slf4j
@Configuration
public class ArgonautJacksonMapper {

  /**
   * The published URL for argonaut, which is likely not the hostname of the machine running this
   * application.
   */
  private final String baseUrl;

  /** These base path for resources, e.g. api */
  private String basePath;

  @Autowired
  public ArgonautJacksonMapper(
      @Value("${argonaut.url}") String baseUrl, @Value("${argonaut.base-path}") String basePath) {
    this.baseUrl = baseUrl;
    this.basePath = basePath;
  }

  /**
   * Return a ready to use mapper that will work with classes adhering to the conventions described
   * in the class-level documentation.
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = JacksonConfig.createMapper();
    mapper.registerModule(new QualifiedReferenceModule());
    return mapper;
  }

  private class QualifiedReferenceModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
      super.setupModule(context);
      context.addBeanSerializerModifier(
          new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(
                SerializationConfig config,
                BeanDescription beanDesc,
                List<BeanPropertyWriter> beanProperties) {
              if (beanDesc.getBeanClass() == Reference.class) {
                for (int i = 0; i < beanProperties.size(); i++) {
                  BeanPropertyWriter beanPropertyWriter = beanProperties.get(i);
                  if ("reference".equals(beanPropertyWriter.getName())) {
                    beanProperties.set(i, new QualifiedReferenceWriter(beanPropertyWriter));
                  }
                }
              }
              return super.changeProperties(config, beanDesc, beanProperties);
            }
          });
    }
  }

  private class QualifiedReferenceWriter extends BeanPropertyWriter {

    private QualifiedReferenceWriter(BeanPropertyWriter base) {
      super(base);
    }

    private String qualify(String reference) {
      if (StringUtils.isBlank(reference)) {
        return null;
      }
      if (reference.startsWith("http")) {
        return reference;
      }
      if (reference.startsWith("/")) {
        return baseUrl + "/" + basePath + reference;
      }
      return baseUrl + "/" + basePath + "/" + reference;
    }

    @Override
    public void serializeAsField(
        Object shouldBeReference, JsonGenerator gen, SerializerProvider prov) throws Exception {
      if (!(shouldBeReference instanceof Reference)) {
        throw new IllegalArgumentException(
            "Qualified reference writer cannot serialize: " + shouldBeReference);
      }
      Reference reference = (Reference) shouldBeReference;
      String qualifiedReference = qualify(reference.reference());
      if (qualifiedReference != null) {
        gen.writeStringField(getName(), qualifiedReference);
      }
    }
  }
}
