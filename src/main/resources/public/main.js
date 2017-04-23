(function() {
 /*   var socket = io();
    socket.on('message', function(msg) {
        appendLog(msg, 'response');
    });
    socket.on('clear', clearLog);
    socket.on('disconnect', disconnected);*/
    appendLog('Welcome to Hackaman game\n', 'response');
    var createStore = Redux.createStore

    var credential = [];
    var login = function() {
        appendLog('Please fill your username and password to login or register', 'response');
    }

    var hiddenInput = document.querySelector('#hiddenInput');
    var spanInput = document.querySelector('#input');
    var form = document.querySelector('form');
    hiddenInput.focus();
    hiddenInput.addEventListener('keydown', function(e) {
        // Prevent arrow keys.
        if ([37, 38, 39, 40].indexOf(e.keyCode) !== -1) {
            e.preventDefault();
        }
    });
    hiddenInput.addEventListener('input', function() {
        copyValue(hiddenInput, spanInput);
    });
    document.addEventListener('click', function() {
        hiddenInput.focus();
    });

    function type(element, msg) {
        if (msg.length > 0) {
            element.textContent += msg.charAt(0);
            setTimeout(function() {
                type(element, msg.substring(1));
            }, 10);
        }
    }

    function copyValue(input, span) {
        span.textContent = input.value;
    }

    function appendLog(msg, className) {
        var log = document.querySelector('#log');
        var span = document.createElement('span');
        if (className === 'response') {
            type(span, msg);
        } else {
            span.textContent = msg;
        }
        span.className = className || '';
        log.appendChild(span);
        scrollToBottom();
    }

    function clearLog() {
        document.querySelector('#log').innerHTML = '';
    }

    function disconnected() {
        appendLog('--- DISCONNECTED', 'response');

    }

    function scrollToBottom() {
        var terminal = document.querySelector('#terminal');
        terminal.scrollTop = terminal.scrollHeight - terminal.clientHeight;
    }

    function submit(text) {
        if (text.length > 0) {
            //socket.send(text);
            appendLog(text + '\n');
            input.textContent = '';
        }
    }

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        submit(hiddenInput.value);
        form.reset();
    });

})();