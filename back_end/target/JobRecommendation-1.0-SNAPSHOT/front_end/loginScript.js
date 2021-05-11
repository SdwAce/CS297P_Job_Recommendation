(function() {
    var user_id = '';
    var first_name = '';
    var loginMess = document.querySelector('#login-message').innerHTML;
    function init() {
        document.querySelector('#login-btn').addEventListener('click', login);
    }
    /**
     *
     * @param method - GET|POST|PUT|DELETE
     * @param url - API end point
     * @param data - request payload data
     * @param successCallback - Successful callback function
     * @param errorCallback - Error callback function
     */
    function ajax(method, url, data, successCallback, errorCallback) {
        var xhr = new XMLHttpRequest();

        xhr.open(method, url, true);

        xhr.onload = function() {
            if (xhr.status === 200) {
                successCallback(xhr.responseText);
            } else {
                errorCallback(xhr.responseText);
            }
        };

        xhr.onerror = function() {
            console.error("The request couldn't be completed.");
            errorCallback();
        };

        if (data === null) {
            xhr.send();
        } else {
            xhr.setRequestHeader("Content-Type",
                "application/json;charset=utf-8");
            xhr.send(data);
        }
    }


    function login() {
        //hideElement(loginMess);
        var username = document.querySelector('#username').value;
        var password = document.querySelector('#password').value;
        console.log(username);
        console.log(password);
        var resultStatus;
        var url = './login';
        var req = JSON.stringify({
            user_id: username,
            password: password,
        });
        ajax('POST', url, req,
            // successful callback
            function (res) {
                var result = JSON.parse(res);
                // successfully logged in
                if (result.status === 'OK') {
                    user_id = result.user_id;
                    first_name = result.first_name;
                    showLoginMessage('success');
                }
            },
            function (res) {
                showLoginMessage('failed');
            });
    }

    function showLoginMessage(res) {
        if (res === 'success'){
            document.querySelector('#login-message').innerHTML = ' Welcome! '+ first_name;
        }else{
            document.querySelector('#login-message').innerHTML = 'Invalid username or password ';
        }
        //showElement(loginMess);

    }
    function showElement(element, style) {
        var displayStyle = style ? style : 'block';
        element.style.display = displayStyle;
    }




    function checkSession() {
        var url = './login';
        var req = JSON.stringify({});

        // check session is valid or not
        ajax('GET', url, req,
            function(res) {
                var result = JSON.parse(res);

                if (result.status === 'OK') {
                    onSessionValid(result);
                }
            }, function(){
                console.log('login error');
            });
    }
    init();
})()