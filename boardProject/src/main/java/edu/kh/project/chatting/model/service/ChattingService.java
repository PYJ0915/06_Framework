package edu.kh.project.chatting.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.chatting.model.dto.ChattingRoom;
import edu.kh.project.chatting.model.dto.Message;
import edu.kh.project.member.model.dto.Member;

public interface ChattingService {

	/** 채팅방 목록 조회 서비스
	 * @param memberNo
	 * @return
	 */
	List<ChattingRoom> selectRoomList(int memberNo);

	/** 채팅 상대 검색 서비스
	 * @param map
	 * @return
	 */
	List<Member> selectTarget(Map<String, Object> map);

	/** 기존 채팅방 번호 확인 서비스
	 * @param map
	 * @return
	 */
	int checkChattingRoomNo(Map<String, Integer> map);

	/** 새로운 채팅방 생성 서비스
	 * @param map
	 * @return
	 */
	int createChattingRoom(Map<String, Integer> map);

	
	/** 메시지 목록 조회 서비스
	 * @param paramMap
	 * @return
	 */
	List<Message> selectMessageList(Map<String, Object> paramMap);

	/** 채팅 읽음 표시 업데이트
	 * @param paramMap
	 * @return
	 */
	int updateReadFlag(Map<String, Object> paramMap);

	/** 채팅 입력 서비스
	 * @param msg
	 * @return
	 */
	int insertMessage(Message msg);

}
