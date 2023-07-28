package com.peco.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.peco.service.MemberService;
import com.peco.vo.MemberVo;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/peco/*")
@Log4j
public class LoginController {
	
	@Autowired
	MemberService memberService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/main")
	public String main() {
		return "main";
	}
	
	@PostMapping("/loginAction")
	public @ResponseBody Map<String, Object> loginAction(@RequestBody MemberVo member, Model model, HttpSession session) {
	
	member = memberService.login(member);
	if(member != null) {
		session.setAttribute("member", member);
		session.setAttribute("userId", member.getId());
		session.setAttribute("nickName", member.getNickname());
		Map<String, Object> map= responseMap("success", "로그인 되엇습니다.");
		map.put("url","/peco/main");	

		return map;
	}else{
		
		return responseMap("fail", "아이디와 비밀번호를 확인해주세요");
	}
	
	
	}
	
	@PostMapping("/register")
	public @ResponseBody Map<String, Object> register(@RequestBody MemberVo member){
		
		try {
			
			int res = memberService.insert(member);
			return responseWriteMap(res);
			
		} catch (Exception e) {
			e.printStackTrace();
			return responseMap("fail", "등록중 예외사항이 발생 하였습니다.");
		}
	}
	
	public Map<String, Object> responseMap(int res, String msg){
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(res > 0) {
			map.put("result", "success");
			map.put("msg", msg+" 되었습니다.");
		}else {
			map.put("result", "fail");
			map.put("msg", msg+"중 예외가 발생하였습니다");

		}
		
		return map;
	}
	
	public Map<String, Object> responseWriteMap(int res){
		return responseMap(res,"등록");
	}
	
	public Map<String, Object> responseMap(String result, String msg){
		Map<String, Object> map = new HashMap<String, Object>();
		
		
			map.put("result", result);
			map.put("msg", msg);

		
		return map;
	}
	
}