package edu.kh.project.board.model.service;

import java.util.List;
import java.util.Map;

public interface BoardService {

	/** 게시판 종류 조회 서비스
	 * @return
	 */
	List<Map<String, Object>> selectBoardTypeList();

	/** 특정 게시판의 지정된 페이지 목록 조회
	 * @param boardCode (특정 게시판)
	 * @param cp (지정된 페이지)
	 * @return
	 */
	Map<String, Object> selectBoardList(int boardCode, int cp);

}
