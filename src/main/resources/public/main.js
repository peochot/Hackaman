(function () {

    var createStore = Redux.createStore
    var initState = { auth: false, username: "" }
    var reducer = function (state, action) {
        switch (action.type) {
            case "@username/set":
                return Object.assign(state, { username: action.username })
            case "@logged/in":
                return Object.assign(state, { auth: true })
            default:
                return initState
        }
    }

    var Store = createStore(reducer)

    var unsubscribe = Store.subscribe(function () {
        console.log(Store.getState())
    }
    )


    var loginCheck = function () {
        var state = Store.getState();
        if (!state.auth && !state.username) {
            appendLog('Please fill your username and password to login or register\n', 'response');
            appendLog('Enter username: ', 'response');
        } else if (!state.auth) {
            appendLog('Enter password: ', 'response');
        }
    }

    var $ = function (el) {
        return document.querySelector(el);
    }

    var Terminal = function () {
        this.element = $('#terminal');
        this.hiddenInput = $('#hiddenInput');
        this.spanInput = $('#input');
        this.form = $('form');

        this.hiddenInput.focus();
        this.hiddenInput.addEventListener('keydown', function (e) {
            // Prevent arrow keys.
            if ([37, 38, 39, 40].indexOf(e.keyCode) !== -1) {
                e.preventDefault();
            }
        });

        var $this = this;
        this.hiddenInput.addEventListener('input', function () {
            $this.copyValue();
        });

        this.form.addEventListener('submit', function (e) {
            e.preventDefault();
            submit($this.hiddenInput.value);
            $this.form.reset();
        });
    }

    Terminal.prototype.scrollToBottom = function () {
        this.element.scrollTop = this.element.scrollHeight - this.element.clientHeight;
    }

    Terminal.prototype.copyValue = function () {
        this.spanInput.textContent = this.hiddenInput.value;
    }

    var terminal = new Terminal();

    document.addEventListener('click', function () {
        terminal.hiddenInput.focus();
    });

    var type = function (element, msg) {
        if (msg.length > 0) {
            element.innerHTML += msg.charAt(0);
            setTimeout(function () {
                type(element, msg.substring(1));
            }, 10);
        }
    }

    var appendLog = function (msg, className) {
        var log = $('#log');
        var span = document.createElement('span');
        if (className === 'response') {
            type(span, msg);
        } else {
            span.innerHTML = msg;
        }
        span.className = className || '';
        log.appendChild(span);
        terminal.scrollToBottom();
    }

    var clearLog = function () {
        $('#log').innerHTML = '';
    }

    var disconnected = function () {
        appendLog('--- DISCONNECTED', 'response');

    }

    var request = function (method, url, data) {
        return new Promise(function (resolve, reject) {
            var xhr = new XMLHttpRequest();
            xhr.open(method, url, true);
            if (localStorage.getItem("secret")) {
                xhr.setRequestHeader("secret", localStorage.getItem("secret"));
            }
            xhr.setRequestHeader("Content-type", "application/json");
            xhr.setRequestHeader('Access-Control-Expose-Headers', 'Set-Cookie');
            xhr.timeout = 5000;
            xhr.withCredentials = true;
            xhr.onreadystatechange = function () {
                if (xhr.readyState !== XMLHttpRequest.DONE) {
                    return;
                }

                if (xhr.status === 200) {
                    if (xhr.responseText && xhr.responseText.length > 0) {
                        var json = JSON.parse(xhr.responseText);
                        resolve(json);
                    } else {
                        resolve();
                    }
                } else {
                    reject({ failure: true });
                }
            };
            xhr.onerror = function (e) {
                reject({ error: true, cause: e });
            };
            xhr.ontimeout = function (e) {
                reject({ timeout: true, cause: e });
            };

            if (data) {
                xhr.send(JSON.stringify(data));
            } else {
                xhr.send();
            }
        });
    };

    var submit = function (text) {
        if (text.length > 0) {
            var state = Store.getState();
            if (!state.auth && !state.username) {
                Store.dispatch({ type: "@username/set", username: hiddenInput.value })
                appendLog(text + '\n');
                loginCheck();
            } else if (!state.auth) {
                var password = hiddenInput.value
                appendLog(text + '\n');
                request("post", "/api/login", { username: state.username, password: password })
                    .then(function (response) {
                        // Set user secret into localStorage !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        localStorage.setItem("secret", response.secret);
                        clearLog();
                        Store.dispatch({ type: "@logged/in" });
                        appendLog(response.message)
                        sendText("-start");
                    }).catch(function (e) {
                        Store.dispatch({ type: "@username/clear" });
                        loginCheck();
                        console.error(e);
                    });
            } else {
                sendText(text);
            }
            input.textContent = '';
        }
    }

    var logOut = function () {
        Store.dispatch({ type: '@username/clear' })
        /// Clear user Secret if user wants to log out !!!!!
        localStorage.clear();
        clearLog();
        appendLog('You are logged out ! ');
        appendLog('Press F5 to restart !');        
    }

    var sendText = function (text) {
        if (text === '-logout') {
            logOut();
            return;
        }

        request("post", "/api/game", { command: text })
            .then(function (response) {
                if (response.clear)
                    clearLog();
                appendLog(text + '\n');
                console.log(response)
                appendLog(response.message);
            }).catch(function (e) {
                Store.dispatch({ type: "@username/clear" });
                loginCheck();
                console.error(e);
            });
    }

    appendLog('Welcome to Hackaman game\n', 'response');
    if (localStorage.getItem("secret")) {
        var username = atob(localStorage.getItem("secret"))
        Store.dispatch({ type: "@username/set", username: hiddenInput.value })
        Store.dispatch({ type: "@logged/in" });

        appendLog("Welcome " + username)
        appendLog("\nAvailable Command: \n-start to start game\n-reset to reset your score\n-leaderboard to show scoreboard\n-logout to log out \n Have fun \n");
        sendText("-start")
    }
    else {
        loginCheck();
    }
})();