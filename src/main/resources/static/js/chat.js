let stompClient = null; // STOMP 클라이언트를 저장할 변수를 선언합니다.
let username = null; // 사용자 이름을 저장할 변수를 선언합니다.

// WebSocket 연결을 설정하는 함수입니다.
function connect() {
    // SockJS를 사용하여 WebSocket 연결을 생성합니다.
    const socket = new SockJS('/ws');
    // STOMP 프로토콜을 사용하여 SockJS를 감쌉니다.
    stompClient = Stomp.over(socket);

    // STOMP 클라이언트를 통해 서버에 연결을 시도합니다.
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame); // 연결이 성공하면 콘솔에 메시지를 출력합니다.
        // 서버에서 /topic/public 경로로 전송된 메시지를 구독합니다.
        stompClient.subscribe('/topic/public', function(message) {
            // 수신한 메시지를 JSON으로 파싱하여 화면에 표시하는 함수를 호출합니다.
            displayMessage(JSON.parse(message.body));
        });
    });
}

// 메시지를 서버로 전송하는 함수입니다.
function sendMessage() {
    // 입력 필드에서 메시지 내용을 가져옵니다.
    const messageContent = document.getElementById("messageContent").value;
    if (messageContent.trim() !== '') { // 메시지가 공백이 아닌 경우에만 전송합니다.
        const chatMessage = {
            sender: username, // URL에서 추출한 사용자 이름을 설정합니다.
            content: messageContent, // 입력한 메시지 내용을 설정합니다.
            type: 'CHAT', // 메시지 유형을 설정합니다.
            timestamp: new Date().toISOString() // 현재 시간을 ISO 8601 형식으로 설정합니다.
        };
        // STOMP 클라이언트를 통해 서버로 메시지를 전송합니다.
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        // 메시지를 전송한 후 입력 필드를 비웁니다.
        document.getElementById("messageContent").value = '';
    }
}

// 메시지 입력란에서 엔터 키를 눌렀을 때 sendMessage 함수를 호출하도록 이벤트 리스너를 추가합니다.
document.getElementById("messageContent").addEventListener("keypress", function(event) {
    if (event.key === "Enter") {
        event.preventDefault(); // 기본 엔터 키 동작을 방지합니다.
        sendMessage(); // 메시지 전송 함수 호출
    }
});

// 메시지를 채팅 UI에 표시하는 함수
function displayMessage(message) {
    // DOM에서 메시지 영역 요소를 가져옴
    const messageArea = document.getElementById("messageArea");

    // 메시지의 래퍼 요소를 생성
    const messageWrapper = document.createElement('div');
    messageWrapper.classList.add('message-wrapper');

    // 메시지 요소를 생성하고 적절한 클래스를 추가
    const messageElement = document.createElement('div');
    messageElement.classList.add('message', message.sender === username ? 'message-own' : 'message-other');

    // 발신자의 이름을 표시할 요소를 생성
    const messageSender = document.createElement('div');
    messageSender.classList.add('message-sender');
    messageSender.innerText = message.sender;

    // 메시지 내용을 표시할 요소를 생성
    const messageContent = document.createElement('p');
    messageContent.innerText = message.content;

    // 메시지의 타임스탬프를 표시할 요소를 생성
    const messageTimestamp = document.createElement('div');
    messageTimestamp.classList.add('message-timestamp', message.sender === username ? 'timestamp-own' : 'timestamp-other');
    const messageTime = new Date(message.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    messageTimestamp.innerText = messageTime;

    // 메시지 요소에 발신자 이름과 메시지 내용을 추가
    messageElement.appendChild(messageSender);
    messageElement.appendChild(messageContent);

    // 메시지 래퍼 요소에 메시지 요소와 타임스탬프를 추가
    messageWrapper.appendChild(messageElement);
    messageWrapper.appendChild(messageTimestamp);

    // 메시지 영역에 메시지 래퍼 요소를 추가
    messageArea.appendChild(messageWrapper);

    // 스크롤을 최신 메시지로 이동
    messageArea.scrollTop = messageArea.scrollHeight;
}


// 페이지가 로드될 때 connect 함수를 호출합니다.
document.addEventListener('DOMContentLoaded', (event) => {
    // URL에서 username을 추출합니다.
    const url = new URL(window.location.href);
    username = url.pathname.split("/").pop();
    console.log('Username: ' + username); // 콘솔에 추출한 사용자 이름을 출력합니다.
    connect(); // WebSocket 연결을 설정합니다.
});
