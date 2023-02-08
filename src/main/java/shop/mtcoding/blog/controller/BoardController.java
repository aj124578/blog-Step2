package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponseDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.BoardService;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;


    // 글수정
    @PutMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> update(@PathVariable int id, @RequestBody BoardUpdateReqDto boardUpdateReqDto) {
        System.out.println(boardUpdateReqDto.getTitle());
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }

        if (boardUpdateReqDto.getTitle() == null || boardUpdateReqDto.getTitle().isEmpty()) {
            throw new CustomApiException("title을 작성해주세요");
        }

        if (boardUpdateReqDto.getContent() == null || boardUpdateReqDto.getContent().isEmpty()) {
            throw new CustomApiException("content를 작성해주세요");
        }
        boardService.게시글수정(id, boardUpdateReqDto, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "게시글수정성공", null), HttpStatus.OK);

    }

    @DeleteMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id){
        User principal = (User) session.getAttribute("principal"); // 이런거는 앞부분 필터에서 막는게 편함
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // UNAUTHORIZED : 401 임
        }
        boardService.게시글삭제(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제성공", null), HttpStatus.OK);
        

    }

    @PostMapping("/board")
    public String save(BoardSaveReqDto boardSaveReqDto){
        // 1. 권한 검사
        User principal = (User) session.getAttribute("principal"); // 이런거는 앞부분 필터에서 막는게 편함
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED); // UNAUTHORIZED : 401 임
        }
        
        // 2. 유효성 검사
        if (boardSaveReqDto.getTitle() == null || boardSaveReqDto.getTitle().isEmpty()) {
            throw new CustomException("title을 작성해주세요");
        }

        if (boardSaveReqDto.getContent() == null || boardSaveReqDto.getContent().isEmpty()) {
            throw new CustomException("content를 작성해주세요");
        }

        if (boardSaveReqDto.getTitle().length() > 100) {
            throw new CustomException("title의 길이가 100자 이하여야 합니다.");
        }

        // 3. 서비스 호출
        boardService.글쓰기(boardSaveReqDto, principal.getId());
        
        return "redirect:/";
    }
    
    @GetMapping({ "/", "/board" })
    public String main(Model model) {
        model.addAttribute("dtos", boardRepository.findAllWithUser());
        return "board/main";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("dto", boardRepository.findByIdWithUser(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, Model model) {
        // 1. 인증체크
        User principal = (User) session.getAttribute("principal"); 
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다.", HttpStatus.UNAUTHORIZED);
        }
        
        // 2. 게시글 존재 유무 확인
        Board boardPs = boardRepository.findById(id);
        if (boardPs == null) {
            throw new CustomException("없는 게시글을 수정할 수 없습니다.");
        }

        // 3. 수정 권한체크
        if (boardPs.getUserId() != principal.getId()) {
            throw new CustomException("게시글을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        model.addAttribute("board", boardPs);
        return "board/updateForm";
    }
}
