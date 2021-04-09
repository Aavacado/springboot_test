package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    private MockMvc mockmvc;

    // after running test: test successful. after posting, deleting, then checking history, string is not found.
    @Test
    void testDeleteNotInHistory() throws Exception{
        // post the testString
        mockmvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=testString")).andReturn();
        // attempt to delete the same testString
        mockmvc.perform(MockMvcRequestBuilders.delete("/delete?post_text=testString")).andReturn();
        // access history to check if testString is there
        mockmvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(not(containsString("testString"))));
    }

    // after running test: test failed. delete is NOT case sensitive.
    @Test
    void testDeleteCaseSensitive() throws Exception{
        // post a testString
        mockmvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=testString")).andReturn();
        // attempt to delete the test string as 'teststring' NOTE NO CAPITALS VERSUS ORIGINAL 'testString'
        mockmvc.perform(MockMvcRequestBuilders.delete("/delete?post_text=teststring")).andReturn();
        // to verify, check history file
        mockmvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(not(containsString("testString"))));
    }
}