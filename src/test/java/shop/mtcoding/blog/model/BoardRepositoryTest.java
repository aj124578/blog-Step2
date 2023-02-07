package shop.mtcoding.blog.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardResp.BoardMainRespDto;

// F - DS - C - S - R - MyBatis - DB
@MybatisTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void findAllWithUser_test() throws Exception {
        // given
        ObjectMapper om = new ObjectMapper();

        // when
        List<BoardMainRespDto> BoardMainRespDto = boardRepository.findAllWithUser();
        String responseBody = om.writeValueAsString(BoardMainRespDto); // 스프링은 기본적으로 오브젝트 매핑이 제공되어 json으로 파싱해줌
        System.out.println("테스트 : " + responseBody);

        // then
        assertThat(BoardMainRespDto.get(5).getUsername()).isEqualTo("love");
    }
}