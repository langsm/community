package com.lsm.community.community.controller;

import com.lsm.community.community.GithubProvider.GithubProvider;
import com.lsm.community.community.dto.AccessTokenDTO;
import com.lsm.community.community.dto.GithubUser;
import com.lsm.community.community.mapper.UserMapper;
import com.lsm.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Autowired
    private UserMapper userMapper;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(accessToken);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        if(user != null){
            User user2 = new User();
            user2.setAccountId(String.valueOf(user.getId()));
            user2.setGmtCreate(System.currentTimeMillis());
            user2.setGmtModified(user2.getGmtCreate());
            user2.setName(user.getName());
            user2.setToken(UUID.randomUUID().toString());
            userMapper.insert(user2);
            //user添加到session中去
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            return "redirect:/";
        }else{
            //重新跳转到登录页
            return "redirect:/";
        }

    }
}
