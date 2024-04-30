/**
 * param 均为  json 类型
 */


/**
 *  localStorage 封装
 * @param method
 * @param key
 * @param value
 * @returns {string|boolean}
 */

function handleLocalStorage(method, key, value) {
    switch (method) {
        case 'get' : {
            let temp = window.localStorage.getItem(key);
            if (temp) {
                return temp
            } else {
                return false
            }
        }
        case 'set' : {
            window.localStorage.setItem(key, value);
            break
        }
        case 'remove': {
            window.localStorage.removeItem(key);
            break
        }
        default : {
            return false
        }
    }
}

/**
 *  ajax请求的封装代码不携带token
 */
function myAjaxPost(url, params, handle) {
    $.ajax({
        type: 'post',
        url: url,
        data: params,
        cache: false,
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        success: function (data) {
            handle(data)
        },
        error: function (err) {
            alert("服务器异常");
        }
    });
}

/**
 *  ajax请求的封装代码携带token
 */
function ajaxPostWithToken(url, params, handle) {
    $.ajax({
        type: 'post',
        url: url,
        data: JSON.stringify(params),
        cache: false,
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("Authorization", handleLocalStorage('get', 'Authorization'));
        },
        success: function (data) {
            handle(data)
        },
        error: function (err) {
            alert("服务器异常");
        }
    });
}

function ajaxGet(url, params, handle) {
    console.log(params)
    $.ajax({
        url: url,
        type: 'get',
        data: params,
        cache: false,
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("Authorization", handleLocalStorage('get', 'Authorization'));
        },
        success: function (data) {
            handle(data)
        },
        error: function (err) {
            alert("服务器异常");
        }
    });
}

function ajaxGet2(url, params, handle) {
    console.log(params)
    $.ajax({
        url: url,
        type: 'get',
        data: params,
        cache: false,
        async: false,
        dataType: 'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("Authorization", handleLocalStorage('get', 'Authorization'));
        },
        success: function (data) {
            handle(data)
        },
        error: function (err) {
            alert("服务器异常");
        }
    });
}

/** ajax post请求，处理文件上传 */
var ajaxPostForFile = function (url, params, handle) {
    $.ajax({
        type: 'post',
        url: url,
        data: params,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
            handle(data)
        },
        error: function (err) {
            alert("服务器异常");
        }
    });
}
