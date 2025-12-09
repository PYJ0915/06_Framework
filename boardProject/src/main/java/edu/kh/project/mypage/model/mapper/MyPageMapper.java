package edu.kh.project.mypage.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MyPageMapper {

	int updateInfo(Member inputMember);

	String getPw(int memberNo);
	
	int changePw(Member loginMember);

}
