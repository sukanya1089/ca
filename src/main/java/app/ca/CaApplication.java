package app.ca;

import app.ca.api.utils.MoneyJsonSerialization;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializers(MoneyJsonSerialization.getSerializer())
                .deserializers(MoneyJsonSerialization.getDeserializer());
    }
}
