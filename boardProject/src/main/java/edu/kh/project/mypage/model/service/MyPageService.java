package edu.kh.project.mypage.model.service;

import java.util.Map;

import edu.kh.project.member.model.dto.Member;

public interface MyPageService {

	int updateInfo(Member inputMember, String[] memberAddress);

	int changePw(Map<String, String> pwMap, Member loginMember);

}
