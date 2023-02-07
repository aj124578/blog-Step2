package shop.mtcoding.blog.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardMainRespDto;

@Mapper
public interface BoardRepository {

    public BoardDetailRespDto findByIdWithUser(int id);

    public List<BoardMainRespDto> findAllWithUser();

    public List<User> findAll();

    public User findById(int id);

    public User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    public int insert(@Param("title") String title, @Param("content") String content, 
            @Param("userId") int userId);

    public int updateById(@Param("id") int id, @Param("title") String title,
            @Param("content") String content);

    public int deleteById(int id);

}