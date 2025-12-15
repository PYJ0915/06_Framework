package edu.kh.project.board.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.board.model.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
@RequiredArgsConstructor
public class BoardCotroller {
	
	private final BoardService service;
	
	/** 게시글 목록 조회
	 * {boardCode}
	 * - /board/xxx : /board 이하 1 레벨 자리에 어떤 주소값이 들어오든 모두 이 메서드에 매핑
	 * 
	 *	/board/ 이하 1레벨 자리에 숫자로된 요청주소가 작성 되어있을 때만 동작 => 정규표현식
	 *
	 * {boardCode:[0-9]+}
	 * - [0-9] : 한 칸에 0 ~ 9 사이 숫자 입력 가능
	 * - [0-9]+ : 모든 숫자 가능
	 * 
	 * @param boardCode : 게시판 종류 구분 번호 (1, 2, 3)
	 * @param cp : 현재 조회 요청한 페이지 번호 (없으면 1)
	 * @param paramMap (검색 시 이용) : 제출된 파라미터가 모두 저장된 Map
	 * 		  => 검색 시 key, query 담겨있음 EX) {key=t, query=폭탄}
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(@PathVariable("boardCode") int boardCode,
		@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
		@RequestParam Map<String, Object> paramMap, 
		Model model ) {
		
		// 조회 서비스 호출 후 결과 반환 받아오기
		Map<String, Object> map = null;
		
		if(paramMap.get("key") == null) {
			// 검색이 아닌 경우
			
			// 게시글 목록 조회 서비스 호출
			map = service.selectBoardList(boardCode, cp);
			
		} else {
			// 검색인 경우
			
			// 검색(내가 검색하고 싶은 게시글 목록 조회) 서비스 호출
			
		}
		
		// model에 결과값 등록
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
		return "board/boardList";
	}

}
