package com.susan.eventbooking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class EventBookingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @Test
    @Order(1)
    void registerUser() throws Exception {

        String request = """
        {
            "name": "Test User",
            "email": "test_user@test.com",
            "password": "password123",
            "role": "USER"
        }
        """;

        System.out.println("===== REGISTER USER =====");
        System.out.println(request);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void loginUser() throws Exception {

        String request = """
        {
            "email": "test_user@test.com",
            "password": "password123"
        }
        """;

        System.out.println("===== LOGIN USER =====");
        System.out.println(request);

        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("===== LOGIN RESPONSE =====");
        System.out.println(response);

        JsonNode jsonNode = objectMapper.readTree(response);
        token = jsonNode.get("token").asText();

        System.out.println("===== TOKEN =====");
        System.out.println(token);
    }

    @Test
    @Order(3)
    void createEvent() throws Exception {

        String request = """
        {
            "title": "Integration Test Event",
            "description": "Testing event flow",
            "location": "Tokyo",
            "eventDate": "2026-12-01T10:00:00",
            "createdBy": {
                "id": 1
            }
        }
        """;

        System.out.println("===== CREATE EVENT =====");
        System.out.println("TOKEN: " + token);
        System.out.println(request);

        mockMvc.perform(post("/api/events")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(4)
    void getEvents() throws Exception {

        System.out.println("===== GET EVENTS =====");
        System.out.println("TOKEN: " + token);

        mockMvc.perform(get("/api/events")
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }
}