package edu.kh.project.chatting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message {

	private int messageNo; // 메시지 번호
	private String messageContent; // 메시지 내용
	private String readFl; // 메시지 확인 여부
	private String sendTime; // 메시지 전송 시간
	private int senderNo; // 메시지 전송 회원 번호
	private int chattingRoomNo; // 채팅방 번호
	private int targetNo; // 웹소켓을 이용한 메시지 DTO 필드 세팅 시 필요
	
}
