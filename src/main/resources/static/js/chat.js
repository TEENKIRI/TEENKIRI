// badWords.js 파일에서 금지된 단어들을 가져옵니다.
import { words as forbiddenWords } from './badWords.js';

let stompClient = null; // STOMP 클라이언트를 저장할 변수
let username = null; // 현재 사용자 이름을 저장할 변수
let channel = null; // 현재 채널 이름을 저장할 변수

// 특수 문자를 이스케이프 처리하는 함수
function escapeRegExp(string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'); // 특수 문자를 이스케이프 처리합니다.
}

// 메시지에서 욕설을 필터링하는 함수
function filterMessage(content) {
    forbiddenWords.forEach(word => { // 각 금지된 단어에 대해
        if (word) { // 빈 문자열이 아닌 경우에만 처리
            const regex = new RegExp(word.split('').map(escapeRegExp).join('.*?'), 'gi'); // 단어를 정규식으로 변환
            content = content.replace(regex, '*'.repeat(word.length)); // 단어를 *로 대체
        }
    });
    return content; // 필터링된 메시지 반환
}

function connect() {
    const socket = new SockJS('/ws'); // SockJS를 통해 WebSocket 연결 생성
    stompClient = Stomp.over(socket); // STOMP 클라이언트 생성

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame); // 연결 성공 시 로그 출력
        if (frame.headers.server) { // 서버 헤더가 있는 경우
            console.log('connected to server ' + frame.headers.server); // 서버 정보 출력
        }
        stompClient.subscribe(`/topic/${channel}`, function(message) { // 특정 채널 구독
            displayMessage(JSON.parse(message.body)); // 수신된 메시지를 표시
        });
    });
}

export function sendMessage() {
    let messageContent = document.getElementById("messageContent").value; // 메시지 입력 필드의 값을 가져옴
    if (messageContent.trim() !== '') {
        // 메시지를 전송하기 전에 필터링을 적용합니다.
        messageContent = filterMessage(messageContent); // 메시지 필터링
        const chatMessage = { // 채팅 메시지 객체 생성
            sender: username,
            content: messageContent,
            channel: channel,
            type: 'CHAT',
            timestamp: new Date().toISOString() // 현재 시간을 타임스탬프로 추가
        };
        console.log('Sending message: ', chatMessage); // 전송할 메시지 로그 출력
        stompClient.send(`/app/chat.sendMessage/${channel}`, {}, JSON.stringify(chatMessage)); // 메시지 전송
        document.getElementById("messageContent").value = ''; // 메시지 입력 필드 초기화
    }
}

function displayMessage(message) {
    const messageArea = document.getElementById("messageArea"); // 메시지 표시 영역 가져옴

    const messageWrapper = document.createElement('div'); // 메시지 래퍼 div 생성
    messageWrapper.classList.add('message-wrapper'); // 메시지 래퍼에 클래스 추가

    const messageElement = document.createElement('div'); // 메시지 요소 div 생성
    messageElement.classList.add('message', message.sender === username ? 'message-own' : 'message-other'); // 메시지 클래스 추가 (자신의 메시지인지 여부에 따라 다르게 설정)

    const messageSender = document.createElement('div'); // 메시지 발신자 div 생성
    messageSender.classList.add('message-sender'); // 메시지 발신자에 클래스 추가
    messageSender.innerText = message.sender; // 발신자 이름 설정

    const messageContent = document.createElement('p'); // 메시지 내용 p 태그 생성
    messageContent.innerText = message.content; // 메시지 내용 설정

    const messageTimestamp = document.createElement('div'); // 메시지 타임스탬프 div 생성
    messageTimestamp.classList.add('message-timestamp', message.sender === username ? 'timestamp-own' : 'timestamp-other'); // 타임스탬프 클래스 추가 (자신의 메시지인지 여부에 따라 다르게 설정)
    const messageTime = new Date(message.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }); // 타임스탬프 포맷 설정
    messageTimestamp.innerText = messageTime; // 타임스탬프 설정

    messageElement.appendChild(messageSender); // 메시지 요소에 발신자 추가
    messageElement.appendChild(messageContent); // 메시지 요소에 내용 추가
    messageWrapper.appendChild(messageElement); // 메시지 래퍼에 메시지 요소 추가
    messageWrapper.appendChild(messageTimestamp); // 메시지 래퍼에 타임스탬프 추가

    messageArea.appendChild(messageWrapper); // 메시지 영역에 메시지 래퍼 추가
    messageArea.scrollTop = messageArea.scrollHeight; // 메시지 영역 스크롤을 맨 아래로 이동
}

document.addEventListener('DOMContentLoaded', (event) => { // 문서가 로드되었을 때 이벤트 리스너 추가
    const url = new URL(window.location.href); // 현재 URL 객체 생성
    const pathParts = url.pathname.split("/"); // URL 경로를 '/'로 분리
    channel = pathParts[2]; // URL에서 채널 이름 추출
    username = pathParts[3]; // URL에서 사용자 이름 추출
    console.log('Username: ' + username); // 사용자 이름 로그 출력
    console.log('Channel: ' + channel); // 채널 이름 로그 출력
    connect(); // WebSocket 연결 함수 호출
});

document.getElementById("messageContent").addEventListener("keypress", function(event) { // 메시지 입력 필드에 키프레스 이벤트 리스너 추가
    if (event.key === "Enter") { // Enter 키가 눌렸을 때
        event.preventDefault(); // 기본 동작 방지
        sendMessage(); // 메시지 전송 함수 호출
    }
});

// sendMessage 함수를 전역 범위로 노출시킵니다.
window.sendMessage = sendMessage;
