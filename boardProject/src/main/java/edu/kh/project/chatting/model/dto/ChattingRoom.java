package edu.kh.project.chatting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChattingRoom {

	private int chattingRoomNo; // 채팅방 번호
	private String lastMessage; // 채팅방의 마지막 메시지 
	private String sendTime; // 마지막 메시지 전송 시간
	private int targetNo; // 채팅방의 대상자 회원 번호
	private String targetNickname; // 채팅방의 대상자 닉네임
	private String targetProfile; // 채팅방의 대상자 프로필 이미지 경로
	private int notReadCount; // 채팅방의 읽지 않은 메시지 개수
	
}
