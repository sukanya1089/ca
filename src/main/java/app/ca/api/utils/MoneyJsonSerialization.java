package app.ca.api.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Handwritten Json Serializer for Money
 */
public class MoneyJsonSerialization {

    public static final String VALUE = "value";
    public static final String CURRENCY = "currency";

    public static JsonDeserializer<Money> getDeserializer() {
        return new JsonDeserializer<>() {
            @Override
            public Money deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
                JsonNode root = jsonParser.getCodec().readTree(jsonParser);
                String currency = Optional.ofNullable(root.get(CURRENCY)).map(JsonNode::asText).filter(x -> !x.isBlank()).orElse(null);
                BigDecimal value = Optional.ofNullable(root.get(VALUE)).map(JsonNode::asText).filter(x -> !x.isBlank()).map(BigDecimal::new).orElse(null);
                if (currency == null || value == null) {
                    throw new IllegalArgumentException("Invalid Money instance either missing value or currency");
                }
                return Money.of(value, Monetary.getCurrency(currency));
            }

            @Override
            public Class<Money> handledType() {
                return Money.class;
            }
        };
    }

    public static JsonSerializer<Money> getSerializer() {
        return new JsonSerializer<>() {
            @Override
            public void serialize(Money value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField(CURRENCY, value.getCurrency().getCurrencyCode());
                jsonGenerator.writeStringField(VALUE, value.getNumberStripped().toPlainString());
                jsonGenerator.writeEndObject();
            }

            @Override
            public Class<Money> handledType() {
                return Money.class;
            }
        };
    }

}