package edu.kh.project.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;
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
			// => paramMap에 key라는 k에 접근하면 매핑된 value 반환 
			// ex) {key=w, query=짱구} => w 반환됨
			
			// boardCode를 paramMap에 추가
			paramMap.put("boardCode", boardCode);
			// => paramMap == {key=w, query=짱구, boardCode=1}
			
			// 검색(내가 검색하고 싶은 게시글 목록 조회) 서비스 호출
			map = service.searchBoardList(paramMap, cp);
			
		}
		
		// model에 결과값 등록
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
		return "board/boardList";
	}
	
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode,
							@PathVariable("boardNo") int boardNo,
							@SessionAttribute(value = "loginMember", required = false) Member loginMember,
							Model model, RedirectAttributes ra ) {
		
		// 게시글 상세 조회 서비스 호출
		// 1) Map으로 전달할 파리미터(boardCode, boardNo) 묶기
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		// 로그인 상태인 경우에만 memberNo map 추가
		// LIKE_CHECK 시 이용 (로그인한 사람이 좋아요 누른 게시글인지 체크하기 위함)
		if(loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo());
		}
		
		// 2) 서비스 호출
		Board board = service.selectOne(map);
		
		log.debug("board : " + board);
		
		String path = null;
		
		// 조회 결과가 없는 경우
		if(board == null) {
			path = "redirect:/board/" + boardCode; 
			// 내가 현재 보고있는 게시판 목록으로 재요청
			
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다");
		} else {
			// 조회 결과가 있는 경우
			path = "board/boardDetail";
			
			// board - 게시글 일반 내용 + imageList + commentList
			model.addAttribute("board", board);
			
			// 조회된 이미지 목록(imageList)이 있을 경우
			if(!board.getImageList().isEmpty()) {
				
				BoardImg thumbnail = null;
				
				// imgList의 0번 인덱스 == IMG_ORDER가 가장 빠른 순서
				// 만약 이미지 목록의 0번째 요소의 IMG_ORDER가 0이면 썸네일
				if(board.getImageList().get(0).getImgOrder() == 0) {
					thumbnail = board.getImageList().get(0);
				}
				// thumnail 변수에는
				// - 이미지 목록의 0번째 요소가 썸네일이면 썸네일 이미지의 BoardImg 객체, 아니라면 null
				
				// start라는 key에 thumnail이 null이 아닐 때, 1 저장 null이면 0 저장
				model.addAttribute("thumbnail", thumbnail);
				model.addAttribute("start", thumbnail != null ? 1 : 0);
				// 썸네일 있을 때 : start=1
				// 썸네일 없을 때 (일반 이미지만 있거나, 등록된 이미지가 아예 없을 때) : start=0
			}
			
		}
		
		return path;
	}

}
