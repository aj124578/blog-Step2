package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import shop.mtcoding.blog.model.User;


/*
 * SpringBootTest는 통합테스트 (실제 환경과 동일하게 Bean이 생성됨)
 * AutoConfigureMockMvc는 Mock 환경의 Ioc 컨테이너에 MockMvc Bean이 생성됨
 */


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // MOCK 라고 하면 가짜 환경의 Ioc 컨테이너가 존재하게 되는 것
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void join_test() throws Exception {
        // given
        String requestBody = "username=cos&password=1234&email=cos@nate.com";

        // when
        ResultActions resultActions = mvc.perform(post("/join").content(requestBody)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }

    @Test
    public void login_test() throws Exception {
        // given
        String requestBody = "username=ssar&password=1234"; // 통신으로 들어가는 데이터는 문자열이기 때문에

        // when
        ResultActions resultActions = mvc.perform(post("/login").content(requestBody) // mvc.perform 하면 controller를 때릴수 있음.content하면 request의 body를 넣을 수 있음 content영역은 type 필요
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)); // 기본타입은 x-www , ResulatActions는 request, response의 정보를 다 가지고 있음

                HttpSession session = resultActions.andReturn().getRequest().getSession();
                User principal = (User) session.getAttribute("principal");
                // System.out.println(principal.getUsername());

        // then
        assertThat(principal.getUsername()).isEqualTo("ssar");
        resultActions.andExpect(status().is3xxRedirection());
    }
}