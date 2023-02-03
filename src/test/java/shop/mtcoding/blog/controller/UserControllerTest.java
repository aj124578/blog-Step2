package shop.mtcoding.blog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import shop.mtcoding.blog.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    
    @Test
    public void join_test() throws Exception{
        // given
        String requestBody = "username=ssar&password=1234&email=ssar@nate.com";
        

        // // when
        // ResultActions resultActions = mvc.perform(post("/join").content(requestBody)
        //     .contentType(MediaType.APPLICATION_FORM_URLECODED_VALUE));
        
        // // then
        // resultActions.andExpect(null);
    }
}
