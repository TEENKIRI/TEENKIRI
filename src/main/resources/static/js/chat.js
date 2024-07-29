// badWords.js 파일에서 금지된 단어들을 가져옵니다.
import { words as forbiddenWords } from './badWords.js';

let stompClient = null;
let username = null;
let channel = null;

// 특수 문자를 이스케이프 처리하는 함수
function escapeRegExp(string) {
    return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'); // 특수 문자를 이스케이프 처리합니다.
}

// 메시지에서 욕설을 필터링하는 함수
function filterMessage(content) {
    forbiddenWords.forEach(word => {
        if (word) { // 빈 문자열이 아닌 경우에만 처리
            const regex = new RegExp(word.split('').map(escapeRegExp).join('.*?'), 'gi');
            content = content.replace(regex, '*'.repeat(word.length));
        }
    });
    return content;
}

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        if (frame.headers.server) {
            console.log('connected to server ' + frame.headers.server);
        }
        stompClient.subscribe(`/topic/${channel}`, function(message) {
            displayMessage(JSON.parse(message.body));
        });
    });
}

export function sendMessage() {
    let messageContent = document.getElementById("messageContent").value;
    if (messageContent.trim() !== '') {
        // 메시지를 전송하기 전에 필터링을 적용합니다.
        messageContent = filterMessage(messageContent);
        const chatMessage = {
            sender: username,
            content: messageContent,
            channel: channel,
            type: 'CHAT',
            timestamp: new Date().toISOString()
        };
        console.log('Sending message: ', chatMessage);
        stompClient.send(`/app/chat.sendMessage/${channel}`, {}, JSON.stringify(chatMessage));
        document.getElementById("messageContent").value = '';
    }
}

function displayMessage(message) {
    const messageArea = document.getElementById("messageArea");

    const messageWrapper = document.createElement('div');
    messageWrapper.classList.add('message-wrapper');

    const messageElement = document.createElement('div');
    messageElement.classList.add('message', message.sender === username ? 'message-own' : 'message-other');

    const messageSender = document.createElement('div');
    messageSender.classList.add('message-sender');
    messageSender.innerText = message.sender;

    const messageContent = document.createElement('p');
    messageContent.innerText = message.content;

    const messageTimestamp = document.createElement('div');
    messageTimestamp.classList.add('message-timestamp', message.sender === username ? 'timestamp-own' : 'timestamp-other');
    const messageTime = new Date(message.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    messageTimestamp.innerText = messageTime;

    messageElement.appendChild(messageSender);
    messageElement.appendChild(messageContent);
    messageWrapper.appendChild(messageElement);
    messageWrapper.appendChild(messageTimestamp);

    messageArea.appendChild(messageWrapper);
    messageArea.scrollTop = messageArea.scrollHeight;
}

document.addEventListener('DOMContentLoaded', (event) => {
    const url = new URL(window.location.href);
    const pathParts = url.pathname.split("/");
    channel = pathParts[2]; // URL에서 채널 이름 추출
    username = pathParts[3]; // URL에서 사용자 이름 추출
    console.log('Username: ' + username);
    console.log('Channel: ' + channel);
    connect();
});

document.getElementById("messageContent").addEventListener("keypress", function(event) {
    if (event.key === "Enter") {
        event.preventDefault();
        sendMessage();
    }
});

// sendMessage 함수를 전역 범위로 노출시킵니다.
window.sendMessage = sendMessage;
