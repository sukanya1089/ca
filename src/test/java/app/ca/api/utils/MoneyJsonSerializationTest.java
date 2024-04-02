package app.ca.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyJsonSerializationTest {

    private JsonMapper jsonMapper;

    @BeforeEach
    void setupJsonMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Money.class, MoneyJsonSerialization.getSerializer());
        module.addDeserializer(Money.class, MoneyJsonSerialization.getDeserializer());
        jsonMapper = JsonMapper.builder().build();
        jsonMapper.registerModule(module);
    }

    @Test
    @DisplayName("Deserialization should extract valid json")
    void testDeserialization() throws JsonProcessingException {
        String json = "{\"value\": \"100.23\", \"currency\": \"USD\"}";
        var money = jsonMapper.readValue(json, Money.class);
        Assertions.assertThat(money).isEqualTo(Money.of(100.23, "USD"));
    }

    @Test
    @DisplayName("Deserialization should throw error for money without currency")
    void testDeserializationForMissingCurrency() throws JsonProcessingException {
        String moneyMissingCurrency = "{\"value\": \"100\"}";
        Assertions.assertThatThrownBy(() -> jsonMapper.readValue(moneyMissingCurrency, Money.class));
    }

    @Test
    @DisplayName("Deserialization should throw error for money without value")
    void testDeserializationForMissingValue() throws JsonProcessingException {
        String moneyMissingValue = "{\"currency\": \"USD\"}";
        Assertions.assertThatThrownBy(() -> jsonMapper.readValue(moneyMissingValue, Money.class));
    }

    @Test
    @DisplayName("Deserialization should throw error for money without value and currency")
    void testDeserializationForMissingValueAndCurrency() throws JsonProcessingException {
        String moneyMissingValue = "{}";
        Assertions.assertThatThrownBy(() -> jsonMapper.readValue(moneyMissingValue, Money.class));
    }

    @Test
    @DisplayName("Serialization should convert to Json properly")
    void testSerialization() throws JsonProcessingException {
        var usd100 = Money.of(100, "USD");
        var usd100Json = jsonMapper.writeValueAsString(usd100);
        Assertions.assertThat(usd100Json).isEqualTo("{\"currency\":\"USD\",\"value\":\"100\"}");

        var eur0 = Money.of(0, "EUR");
        var eur0Json = jsonMapper.writeValueAsString(eur0);
        Assertions.assertThat(eur0Json).isEqualTo("{\"currency\":\"EUR\",\"value\":\"0\"}");

        var inrPi = Money.of(3.14, "INR");
        var inrPiJson = jsonMapper.writeValueAsString(inrPi);
        Assertions.assertThat(inrPiJson).isEqualTo("{\"currency\":\"INR\",\"value\":\"3.14\"}");
    }
}