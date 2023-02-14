package shop.mtcoding.blog.service;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;
import shop.mtcoding.blog.util.PathUtil;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Transactional // insert가 동시에 안되고 메서드 자체는 동시에 가능함. 하지만 동시접근 불가능으로 생각하면
    public void 회원가입(JoinReqDto joinReqDto){
        User sameUser = userRepository.findByUsername(joinReqDto.getUsername());
        if (sameUser != null) {
            throw new CustomException("동일한 username이 존재합니다");
        }
        int result = userRepository.insert(joinReqDto.getUsername(), joinReqDto.getPassword(), joinReqDto.getEmail());
        if (result != 1) {// 이거를 controller가 받아서 처리 할 필요가 없으므로 void로 여기서 처리하고 끝냄
            throw new CustomException("회원가입실패");
        }
    }

    // 한번 봤던 데이터를 계속 보기위하여 readOnly를 걸어줌
    @Transactional(readOnly = true) // 연산되고 있을때 데이터가 변경되면 꼬일 수가 있기 때문에 트랜잭션해서 readOnly를 걸면 변경이 안되서 한번 봤던 데이터를 계속 보게 해줌 그래서 일반적으로는 select에서도 readonly를 걸어줌
    public User 로그인(LoginReqDto loginReqDto) {
        User principal = userRepository.findByUsernameAndPassword(
            loginReqDto.getUsername(), loginReqDto.getPassword()
        );

        if (principal == null) {
            throw new CustomException("유저네임 혹은 패스워드가 잘못 입력되었습니다");
        }
        
        return principal;
    }

    @Transactional
    public User 프로필사진수정(MultipartFile profile, int pricipalId) {
        // 1번 사진을 /static/image에 UUID로 변경해서 저장
        String uuidImageName = PathUtil.writeImageFile(profile);

        // 2번 저장된 파일의 경로를 DB에 저장
        User userPS = userRepository.findById(pricipalId);
        userPS.setProfile(uuidImageName);
        userRepository.updateById(userPS.getId(), userPS.getUsername(), userPS.getPassword(), userPS.getEmail(), userPS.getPassword(), 
                userPS.getCreatedAt());

        return userPS;
    }
}
