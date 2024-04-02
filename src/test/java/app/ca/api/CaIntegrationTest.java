package app.ca.api;

import app.ca.api.model.AccountDto;
import app.ca.api.model.AccountOpeningRequest;
import app.ca.api.model.CustomerCreationRequest;
import app.ca.api.model.CustomerWithAccountsDto;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import javax.money.Monetary;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CaIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomersController customersController;

    @Test
    void testContextLoads() throws Exception {
        Assertions.assertThat(customersController).isNotNull();
    }

    @Test
    @DisplayName("Trying to fetch non existing customer should return 404 (Not found)")
    void testGetUnknownCustomer() throws Exception {
        String customerId = UUID.randomUUID().toString();
        ResponseEntity<CustomerWithAccountsDto> response = restTemplate.getForEntity("/customers/" + customerId, CustomerWithAccountsDto.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
    }

    @Test
    @DisplayName("Create Customer should create customer and assign no accounts to the customer")
    void testCreateCustomer() throws Exception {
        // Creation
        CustomerCreationRequest request = CustomerCreationRequest.builder().name("name").surname("surname").build();
        ResponseEntity<CustomerWithAccountsDto> creationResponse = restTemplate.postForEntity("/customers", request, CustomerWithAccountsDto.class);
        Assertions.assertThat(creationResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        CustomerWithAccountsDto body = creationResponse.getBody();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getCustomer()).isNotNull();
        Assertions.assertThat(body.getCustomer().getCustomerId()).isNotNull();
        Assertions.assertThat(body.getAccounts()).isEmpty();
        String customerId = body.getCustomer().getCustomerId();
        // Fetch Again
        ResponseEntity<CustomerWithAccountsDto> getResponse = restTemplate.getForEntity("/customers/" + customerId, CustomerWithAccountsDto.class);
        Assertions.assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(getResponse.getBody()).isEqualTo(body);
    }

    @Test
    @DisplayName("Create Account should create account and return customer along with all accounts")
    void testCreateAccount() throws Exception {
        // Create Customer
        CustomerCreationRequest customerCreationRequest = CustomerCreationRequest.builder().name("name").surname("surname").build();
        ResponseEntity<CustomerWithAccountsDto> customerCreationResponse = restTemplate.postForEntity("/customers", customerCreationRequest, CustomerWithAccountsDto.class);
        Assertions.assertThat(customerCreationResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(customerCreationResponse.getBody()).isNotNull();
        String customerId = customerCreationResponse.getBody().getCustomer().getCustomerId();
        // Create Account
        AccountOpeningRequest accountOpeningRequest = AccountOpeningRequest.builder().customerId(customerId).initialAmount(Money.zero(Monetary.getCurrency("USD"))).build();
        ResponseEntity<AccountDto> accountCreationResponse = restTemplate.postForEntity("/accounts", accountOpeningRequest, AccountDto.class);
        Assertions.assertThat(accountCreationResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(accountCreationResponse.getBody()).isNotNull();
        AccountDto account = accountCreationResponse.getBody();
        // Fetch Customer again
        ResponseEntity<CustomerWithAccountsDto> getCustomerResponse = restTemplate.getForEntity("/customers/" + customerId, CustomerWithAccountsDto.class);
        Assertions.assertThat(getCustomerResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        CustomerWithAccountsDto body = getCustomerResponse.getBody();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getCustomer()).isNotNull();
        Assertions.assertThat(body.getCustomer().getCustomerId()).isEqualTo(customerId);
        Assertions.assertThat(body.getAccounts()).hasSize(1);
        Assertions.assertThat(body.getAccounts()).contains(account);
    }

    @Test
    @DisplayName("Get Customer should return customer along with all accounts")
    void testGetCustomerWithoutAccounts() throws Exception {
        // Create Customer
        CustomerCreationRequest customerCreationRequest = CustomerCreationRequest.builder().name("name").surname("surname").build();
        ResponseEntity<CustomerWithAccountsDto> customerCreationResponse = restTemplate.postForEntity("/customers", customerCreationRequest, CustomerWithAccountsDto.class);
        Assertions.assertThat(customerCreationResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(customerCreationResponse.getBody()).isNotNull();
        String customerId = customerCreationResponse.getBody().getCustomer().getCustomerId();
        // Create 1st Account
        AccountOpeningRequest accountOpeningRequest1 = AccountOpeningRequest.builder().customerId(customerId).initialAmount(Money.zero(Monetary.getCurrency("USD"))).build();
        ResponseEntity<AccountDto> accountCreationResponse1 = restTemplate.postForEntity("/accounts", accountOpeningRequest1, AccountDto.class);
        Assertions.assertThat(accountCreationResponse1.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(accountCreationResponse1.getBody()).isNotNull();
        AccountDto account1 = accountCreationResponse1.getBody();
        // Create 2nd Account
        AccountOpeningRequest accountOpeningRequest2 = AccountOpeningRequest.builder().customerId(customerId).initialAmount(Money.of(10, Monetary.getCurrency("USD"))).build();
        ResponseEntity<AccountDto> accountCreationResponse2 = restTemplate.postForEntity("/accounts", accountOpeningRequest2, AccountDto.class);
        Assertions.assertThat(accountCreationResponse2.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        Assertions.assertThat(accountCreationResponse2.getBody()).isNotNull();
        AccountDto account2 = accountCreationResponse2.getBody();
        // Fetch Customer again
        ResponseEntity<CustomerWithAccountsDto> getCustomerResponse = restTemplate.getForEntity("/customers/" + customerId, CustomerWithAccountsDto.class);
        Assertions.assertThat(getCustomerResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        CustomerWithAccountsDto body = getCustomerResponse.getBody();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getCustomer()).isNotNull();
        Assertions.assertThat(body.getCustomer().getCustomerId()).isEqualTo(customerId);
        Assertions.assertThat(body.getAccounts()).hasSize(2);
        Assertions.assertThat(body.getAccounts()).contains(account1, account2);
    }

}
