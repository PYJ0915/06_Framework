package edu.kh.project.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	/** 게시글 부분 작성 SQL 수행 (제목, 내용)
	 * @param inputBoard
	 * @return
	 */
	int boardInsert(Board inputBoard);

	/** 게시글 이미지 삽입 SQL 수행
	 * @param uploadList
	 * @return
	 */
	int insertUploadList(List<BoardImg> uploadList);

	/** 게시글 부분 수정 SQL 수행 (제목, 내용)
	 * @param inputBoard
	 * @return
	 */
	int boardUpdate(Board inputBoard);

	/** 게시글 이미지 삭제 SQL 수행
	 * @param map
	 * @return
	 */
	int deleteImg(Map<String, Object> map);

	/** 게시글 이미지 수정 SQL 수행
	 * @param img
	 * @return
	 */
	int updateImage(BoardImg img);

	/** 게시글 이미지 삽입 SQL 수행
	 * @param img
	 * @return
	 */
	int insertImage(BoardImg img);

	/** 게시글 이미지 삭제 SQL 수행
	 * @param board
	 * @return
	 */
	int boardDelete(Board board);

}
