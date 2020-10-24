package com.salesmanager.shop.admin.controller.categories;

import com.salesmanager.shop.application.ShopApplication;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mostafa Saied <mostafa.saied.fci@gmail.com>
 */

@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Ignore
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
    }

    @Test
    public void displayCategoryEdit() {
//        mockMvc.perform(get("/admin/categories/editCategory.html")
//                .contentType("application/json")
//                .param("sendWelcomeMail", "true")
//                .content(objectMapper.writeValueAsString(user)))
//                .andExpect(status().isOk());
    }

    @Test
    public void displayCategoryCreate() {
    }
}