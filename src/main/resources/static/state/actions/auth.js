import axios from "axios";
import { API_URL } from "../const.js";

export function login(email, password) {
    return dispatch => {
        dispatch(requestLogin());
        axios.get(API_URL + "/login.php?email=" + email + "&password=" + password)
            .then(response => {
                console.log(response.data);
                if (response.data.status == "OK") {
                    dispatch(receiveLoginSuccess(response.data));
                } else {
                    dispatch(receiveLoginFailed());
                }
            })
            .catch(e => {
                dispatch(receiveLoginFailed());
            });
    };
}

export function logout() {
    return { type: "LOGOUT" };
}

// 以下はプライベート関数
function requestLogin() {
    return { type: "LOGIN_REQUEST" };
}

function receiveLoginSuccess(data) {
    return {
        type: "LOGIN_RECEIVE_SUCCESS",
        data: data
    };
}

function receiveLoginFailed() {
    return {
        type: "LOGIN_RECEIVE_FAILED"
    };
}