package com.salesmanager.test.shop.integration.customer;

import com.salesmanager.shop.application.ShopApplication;
import com.salesmanager.shop.model.customer.PersistableCustomer;
import com.salesmanager.shop.model.customer.ReadableCustomer;
import com.salesmanager.shop.store.security.AuthenticationResponse;
import com.salesmanager.test.shop.common.ServicesTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerApiTest extends ServicesTestSupport {

    @Test
    void getCustomerProfile_returnsCustomerProfile() throws IOException {
        final PersistableCustomer testCustomer = customerWithoutEmail();
        testCustomer.setEmailAddress("customer2@test.com");

        HttpEntity<PersistableCustomer> entity = new HttpEntity<>(testCustomer);

        final ResponseEntity<AuthenticationResponse> registerResponse = testRestTemplate.postForEntity("/api/v1/customer/register", entity, AuthenticationResponse.class);

        testCustomer.setId(registerResponse.getBody().getId());
        testCustomer.setPassword(null);

        HttpHeaders headers = getCustomerHeader("customer2@test.com", "Test1234");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("profile-photo", getTestFile());
        body.add("customer", getCustomerAsFile(getCustomerAsJson(testCustomer)));

        HttpEntity<MultiValueMap<String, Object>> updateCustomerEntity = new HttpEntity<>(body, headers);

        final ResponseEntity<PersistableCustomer> updateResponse = testRestTemplate.exchange("/api/v1/auth/customer/update", HttpMethod.PUT, updateCustomerEntity, PersistableCustomer.class);

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity getProfileEntity = new HttpEntity(headers);

        final ResponseEntity<ReadableCustomer> customerProfileResponse = testRestTemplate.exchange("/api/v1/auth/customer/profile/", HttpMethod.GET,
                getProfileEntity, ReadableCustomer.class);

        ReadableCustomer customerProfile = customerProfileResponse.getBody();

        assertEquals(OK, updateResponse.getStatusCode());
        assertNotNull(customerProfile);
        assertNotNull(customerProfile.getImageUrl());
        assertNotEquals("", customerProfile.getImageUrl());

    }

    @Test
    void getCustomerProfile_retruns500NotFoundUserToken() throws Exception {
        final HttpEntity entity = new HttpEntity(getDummyCustomerHeader());

        final ResponseEntity<ReadableCustomer> response = testRestTemplate.exchange("/api/v1/auth/customer/profile/", HttpMethod.GET,
                entity, ReadableCustomer.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateCustomerProfile_updatedSuccessfully() throws IOException {
        final PersistableCustomer testCustomer = customerWithoutEmail();
        testCustomer.setEmailAddress("customer3@test.com");

        final HttpEntity<PersistableCustomer> entity = new HttpEntity<>(testCustomer);

        final ResponseEntity<AuthenticationResponse> registerResponse = testRestTemplate.postForEntity("/api/v1/customer/register", entity, AuthenticationResponse.class);

        testCustomer.setId(registerResponse.getBody().getId());
        testCustomer.setPassword(null);

        HttpHeaders headers = getCustomerHeader("customer3@test.com", "Test1234");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("profile-photo", getTestFile());
        body.add("customer", getCustomerAsFile(getCustomerAsJson(testCustomer)));

        HttpEntity<MultiValueMap<String, Object>> updateCustomerEntity = new HttpEntity<>(body, headers);


        final ResponseEntity<PersistableCustomer> updateResponse = testRestTemplate.exchange("/api/v1/auth/customer/update", HttpMethod.PUT, updateCustomerEntity, PersistableCustomer.class);

        PersistableCustomer updatedCustomer = updateResponse.getBody();

        assertEquals(OK, updateResponse.getStatusCode());
        assertNotNull(updatedCustomer);
        assertNotNull(updatedCustomer.getImageUrl());
        assertNotEquals("", updatedCustomer.getImageUrl());
    }
}