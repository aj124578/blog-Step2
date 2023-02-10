package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.dto.board.BoardResp;
import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.blog.dto.reply.ReplyResp.ReplyDetailRespDto;
import shop.mtcoding.blog.model.User;


/*
 * SpringBootTest는 통합테스트 (실제 환경과 동일하게 Bean이 생성됨)
 * AutoConfigureMockMvc는 Mock 환경의 Ioc 컨테이너에 MockMvc Bean이 생성됨
 */

@Transactional // 메서드 실행 직후 롤백!! , 기본은 runtime // 단점 : auto_increment 초기화가 안됨
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // MOCK 라고 하면 가짜 환경의 Ioc 컨테이너가 존재하게 되는 것
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ObjectMapper om;

    private MockHttpSession mockSession;
    
    @BeforeAll
    public static void 테이블차리기(){ // 컨트롤러 테스트가 실행되기 직전에 한번 실행됨
        // 테이블 만들고
        
    }

    @AfterEach
    public  void teardown() { // 메서드가 실행된 직후 매번 실행됨
        // 데이터 지우고(trumcate로 데이터만 날림), 다시 insert

    }

    @Test
    public void update_test() throws Exception {
        // given
        int id = 1;
        BoardUpdateReqDto boardUpdateReqDto = new BoardUpdateReqDto();
        boardUpdateReqDto.setTitle("제목1-수정");
        boardUpdateReqDto.setContent("내용1-수정");


        // json으로 받아온 데이터를 
        String requestBody = om.writeValueAsString(boardUpdateReqDto);
        System.out.println("테스트 : " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(
                put("/board/" + id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .session(mockSession));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));
    }


    @BeforeEach // Test 메서드 실행 직전 마다 호출됨
    public void setUp(){
        // 테이블 insert

        // 세션 주입
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@nate.com");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void detail_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(get("/board/" + id));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardDetailRespDto boardDto = (BoardDetailRespDto) map.get("boardDto");
        List<ReplyDetailRespDto> replyDtos = (List<ReplyDetailRespDto>) map.get("replyDtos");
        String boardJson = om.writeValueAsString(boardDto);
        String replyListJson = om.writeValueAsString(replyDtos);
        System.out.println("테스트 : " + boardJson);
        System.out.println("테스트 : " + replyListJson);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(boardDto.getUserId()).isEqualTo(1);
        assertThat(boardDto.getUsername()).isEqualTo("ssar");
        assertThat(boardDto.getTitle()).isEqualTo("1번째 제목");
        assertThat(replyDtos.get(1).getComment()).isEqualTo("게시글 a : 댓글1-1");
        assertThat(replyDtos.get(1).getUsername()).isEqualTo("love");

    }

    @Test
    public void main_test() throws Exception{
        // given
        ObjectMapper om = new ObjectMapper();
        
        // when
        ResultActions resultActions = mvc.perform(get("/"));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardResp.BoardMainRespDto> dtos = (List<BoardResp.BoardMainRespDto>) map.get("dtos");
        String model = om.writeValueAsString(dtos);
        System.out.println("테스트 : " + model);
    

        // then
        resultActions.andExpect(status().isOk());
        Assertions.assertThat(dtos.size()).isEqualTo(6);
        Assertions.assertThat(dtos.get(0).getUsername()).isEqualTo("ssar");
    

    }

    @Test
    public void save_test() throws Exception {
        // given
        BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();
        boardSaveReqDto.setTitle("제목");
        boardSaveReqDto.setContent("내용");


        String requestBody = om.writeValueAsString(boardSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(
            post("/board")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .session(mockSession));
                
        // then
        resultActions.andExpect(status().isCreated());
    }



    @Test
    public void delete_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(
                delete("/board/" + id).session(mockSession));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        /**
         * jsonPath
         * 최상위 : $
         * 객체탐색 : 닷(.)
         * 배열 : [0]
         */
        // then
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(status().isOk());
    }
}