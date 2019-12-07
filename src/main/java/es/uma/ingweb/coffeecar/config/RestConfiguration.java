package es.uma.ingweb.coffeecar.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uma.ingweb.coffeecar.RestTemplateProxy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return getRestTemplate(builder);
    }

    @Bean
    public RestTemplateProxy restTemplateProxy(RestTemplateBuilder builder) {
        RestTemplate restTemplate = getRestTemplate(builder);
        return new RestTemplateProxy(restTemplate);
    }

    private RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(MediaTypes.HAL_JSON));
        converter.setObjectMapper(mapper);

        return builder.messageConverters(converter).build();
    }
}
