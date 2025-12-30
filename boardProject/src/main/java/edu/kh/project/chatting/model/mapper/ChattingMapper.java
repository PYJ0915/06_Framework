package edu.kh.project.chatting.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.chatting.model.dto.ChattingRoom;
import edu.kh.project.chatting.model.dto.Message;
import edu.kh.project.member.model.dto.Member;

@Mapper
public interface ChattingMapper {

	/** 채팅방 목록 조회 SQL 수행
	 * @param memberNo
	 * @return
	 */
	List<ChattingRoom> selectRoomList(int memberNo);

	/** 채팅 상대 검색 SQL 수행
	 * @param map
	 * @return
	 */
	List<Member> selectTarget(Map<String, Object> map);

	/** 기존 채팅방 확인 SQL 수행
	 * @param map
	 * @return 기존 채팅방 번호
	 */
	int checkChattingRoomNo(Map<String, Integer> map);

	/** 새로운 채팅방 생성 SQL 수행
	 * @param map
	 * @return 새로 생성된 채팅방 번호
	 */
	int createChattingRoom(Map<String, Integer> map);

	/** 메시지 목록 조회 SQL 수행
	 * @param integer
	 * @return
	 */
	List<Message> selectMessageList(Object chattingRoomNo);

	/** 채팅 메시지 읽음 처리 SQL 수행
	 * @param paramMap
	 */
	int updateReadFlag(Map<String, Object> paramMap);

	/** 채팅 입력 SQL 수행
	 * @param msg
	 * @return
	 */
	int insertMessage(Message msg);

}
