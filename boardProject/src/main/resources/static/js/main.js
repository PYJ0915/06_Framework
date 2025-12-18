// 쿠키에 저장된 이메일 input창에 출력

// 쿠키에서 매개변수로 전달받은 key가 일치하는 value 얻어오는 함수
const getCookie = (key) => {
 const cookies = document.cookie; // "K=V; K=V; ....."
 //console.log(cookies); // saveId=user01@kh.or.kr; testKey=testValue
  // cookies 문자열을 배열 형태로 변환
 const cookieList = cookies.split("; ") // ["K=V", "K=V"...]
 				.map( el => el.split("=") );  // ["K", "V"]..
				
//console.log(cookieList);
// ['saveId', 'user01@kh.or.kr'], 
// ['testKey', 'testValue']
	// 배열.map(함수) : 배열의 각 요소를 이용해 함수 수행 후
	//					결과 값으로 새로운 배열을 만들어서 반환
	// 배열 -> 객체로 변환 (그래야 다루기 쉽다)
	
	const obj = {}; // 비어있는 객체 선언
	
	for(let i = 0; i < cookieList.length; i++) {
		const k = cookieList[i][0]; // key 값
		const v = cookieList[i][1]; // value 값
		obj[k] = v; // 객체에 추가
		// obj["saveId"] = "user01@kh.or.kr";
		// obj["testKey"]  = "testValue";
	}
	
	//console.log(obj); // {saveId: 'user01@kh.or.kr', testKey: 'testValue'}
	
	return obj[key]; // 매개변수로 전달받은 key와
					// obj 객체에 저장된 key가 일치하는 요소의 value값 반환
	
}


// 이메일 작성 input 태그 요소
const loginEmail = document.querySelector("#loginForm input[name='memberEmail']")

if(loginEmail != null) {
  // 로그인 창의 이메일 input 태그가 화면 상에 존재할 경우 (== 로그인이 되어있지 않은 경우)

  // 쿠키 중 key 값이 "saveId"인 쿠키의 value 얻어오기
  const saveId = getCookie("saveId")  // 이메일 또는 undefined

  // saveId의 값이 있을 경우
  if(saveId != undefined) {
    // 쿠키에서 얻어온 이메일값을 input 요소의 value 세팅
    loginEmail.value = saveId;

    // 아이디 저장 체크 박스에 체크 해두기
    document.querySelector("input[name='saveId']").checked = true;
  }
  
}

const tbody = document.querySelector("#memberList")

function selectMemberList() {

	fetch("/member/selectMember")
	.then(resp => resp.json())
	.then(memberList => {

		tbody.innerHTML = "";

		for(let member of memberList) {

			const tr = document.createElement("tr");
			const arr = ['memberNo', 'memberEmail', 'memberNickname', 'memberDelFl']

			for(let key of arr) {

				const td = document.createElement("td");

				td.innerText = member[key];
				tr.append(td);

			}
			tbody.append(tr);
		}

	});

} 

document.querySelector("#selectMemberList").addEventListener("click", selectMemberList);


document.querySelector("#resetPw").addEventListener("click", () => {

	const memberNo = document.querySelector("#resetMemberNo");

	fetch("/member/resetPw", {
		method : "PUT",
		headers : {"Content-Type" : "application/json"}, 
		body : JSON.stringify(memberNo.value)
	})
	.then(resp => resp.text())
	.then(result => {

		console.log(result);

		if (result > 0) {
			alert("비밀번호가 초기화되었습니다!");
			memberNo.value = "";
		} else {
			alert("초기화 실패 ㅜㅜ");
		}

	});

});

document.querySelector("#restorationBtn").addEventListener("click", () => {

	const memberNo = document.querySelector("#restorationMemberNo");

	fetch("/member/restorationMember", {
		method : "PUT",
		headers : {"Content-Type" : "application/json"},
		body : JSON.stringify(memberNo.value)
	})
	.then(resp => resp.text())
	.then(result => {

		if (result > 0) {
			alert("회원 복구가 완료되었습니다.");
			memberNo.value = "";
			selectMemberList();
		} else {
			alert("없는 회원번호이거나 회원이 탈퇴하지 않았습니다.");
		}

	});

});

