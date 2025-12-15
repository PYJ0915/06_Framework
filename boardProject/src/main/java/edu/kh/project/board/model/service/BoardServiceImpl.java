package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.Pagination;
import edu.kh.project.board.model.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService{

	private final BoardMapper mapper;

	
	/** 게시판 종류 조회 서비스
	 *
	 */
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		return mapper.selectBoardTypeList();
	}


	
	/** 특정 게시판의 지정된 페이지 목록 조회 서비스
	 *
	 */
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {
		
		// 1. 지정된 게시판(boardCode)에서 삭제되지 않은 게시글 수 조회
		int listCount = mapper.getListCount(boardCode);
		
		// 2. 1번의 결과 + cp를 이용해서 Pagination 객체 생성
		// * Pagination 객체 : 게시글 목록 구성에 필요한 값을 생성한 객체
		Pagination pagination = new Pagination(cp, listCount);
		
		// 3. 특정 게시판의 지정된 페이지 목록 조회
		/* ROWBOUNDS 객체 (MyBatis 제공 객체)
		 * - 지정된 크기만큼 건너 뛰고 (offset)
		 *   제한된 크기만큼(limit)의 행을 조회하는 객체
		 * 
		 * */
		int limit = pagination.getLimit(); // 10개
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		// Mapper 메서드 호출 시 원래 전달할 수 있는 매개변수는 1개
		// but 2개 전달할 수 있는 경우 => RowBounds를 이용할 때
		// => 1번째 매개변수 : SQL에 전달할 파라미터, 2번째 매개변수 : RowBounds
		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
		// 4. Pagination 객체 + 목록 조회 결과를 Map으로 묶어 반환
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}
	
}
