function wishlist_admi() {
    // url가져오기
    const url = window.location.href;
    const noticeId = url.match(/\/read\/(\d+)$/)?.[1];
  
    // json형식으로 저장
    const data = {
        notino: noticeId
    };
    
    // ajax 기본 형식
    fetch('/noticegood/toggle', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);

        if (data.url) { 
            alert("로그인 후 추천 가능합니다.");
            window.location.href = data.url;  // 해당 URL로 페이지 이동
        } else {
            // data.state가 0이면 버튼 텍스트 변경
            const notiBtn = document.getElementById("notibtn");
            const notiImg = document.getElementById("notiImage");
  
            if (data.state === 0) {
                notimg.src = "/noticegood/images/empstar.png";  // 이미지 변경
            } else if (data.state === 1) {
                notimg.src = "/noticegood/images/star.png";  // 이미지 변경
            }
        }
    })
    .catch(error => {
        console.error('추가 실패 : ', error);
    });
}
