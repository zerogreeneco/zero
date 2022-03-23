const url = 'http://localhost:8080/zerogreen';
let stompClient;
let selectedUser;
let newMessages = new Map();

function connectToChat(userId) {
    console.log("connecting to chat...")
    let socket = new SockJS(url + '/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        stompClient.subscribe("/topic/messages/" + userId, function (response) {
            let data = JSON.parse(response.body);
            if (selectedUser === data.fromLogin) {
                render(data.message, data.fromLogin);
            } else {
                newMessages.set(data.fromLogin, data.message);
                $('#userNameAppender_' + data.fromLogin).append('<span id="newMessage_' + data.fromLogin + '" style="color: yellow"> N</span>');
            }
        });
    });
}

function sendMsg(from, text) {
    stompClient.send("/app/chat/" + selectedUser, {}, JSON.stringify({
        fromLogin: from,
        message: text
    }));
}

function registration() {
    let userId = document.getElementById("userId").value;
    let myId = document.getElementById("myId").value;
    $.get(url + "/registration/" + userId, function (response) {
        connectToChat(userId);
    }).fail(function (error) {
        if (error.status === 400) {
            alert("Login is already busy!")
        }
    })
}

function selectUser(userId) {
    console.log("selecting users: " + userId);
    selectedUser = userId;
    let isNew = document.getElementById("newMessage_" + userId) !== null;
    if (isNew) {
        let element = document.getElementById("newMessage_" + userId);
        element.parentNode.removeChild(element);
        render(newMessages.get(userId), userId);
    }
    $('#selectedUserId').html('');
    $('#selectedUserId').append("<input type='hidden' id='selectId' value='" + userId + "'>");
    $('#selectedUserId').append('To.  '+userId);
}

function fetchAll() {
    $.get(url + "/fetchAllUsers", function (response) {
        let users = response;
        let nickname = document.getElementById("nickname").value;
        let usersTemplateHTML = "";
        for (let i = 0; i < users.length; i++) {
            usersTemplateHTML = usersTemplateHTML + '<a href="#" onclick="selectUser(\'' + users[i] + '\')"><li class="clearfix">\n' +
                '                <div class="about">\n' +
                '                    <div id="userNameAppender_' + users[i] + '" class="name" style="color:white;">' + users[i] + '</div>\n' +
                '                    <div class="status">\n' +
                '                    </div>\n' +
                '                </div>\n' +
                '            </li></a>';
        }
        $('#usersList').html(usersTemplateHTML);
    });
}