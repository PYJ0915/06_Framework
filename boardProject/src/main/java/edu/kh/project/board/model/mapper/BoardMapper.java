package edu.kh.project.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.kh.project.board.model.dto.Board;

@Mapper
public interface BoardMapper {

	/** 게시판 종류 조회 SQL 수행
	 * @return
	 */
	List<Map<String, Object>> selectBoardTypeList();

	/** 게시글 수 조회 SQL 수행
	 * @param boardCode
	 * @return
	 */
	int getListCount(int boardCode);

	/** 특정 게시판의 지정된 페이지 목록 조회 SQL 수행
	 * @param boardCode
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardList(int boardCode, RowBounds rowBounds);

	/** 검색 조건이 맞는 게시글 수 조회 SQL 수행
	 * @param paramMap
	 * @return
	 */
	int getSearchCount(Map<String, Object> paramMap);

	/** 검색 결과 목록 조회 SQL 수행
	 * @param paramMap
	 * @param rowBounds
	 * @return
	 */
	List<Board> searchBoardList(Map<String, Object> paramMap, RowBounds rowBounds);

	/** 게시글 상세 조회 SQL 수행(BOARD / BOARD_IMG / COMMENT)
	 * @param map
	 * @return
	 */
	Board selectOne(Map<String, Integer> map);

}
