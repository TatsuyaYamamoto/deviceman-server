const TOKEN_KEY = "torica-token";
const tokenTemplate = {
    header: {
        "alg": ""
    },
    payload: {
        "user_id": "",
        "iss": "",
        "admin": null,
        "iat": null,
        "jti": ""
    }
};

export default class TokenService {
    static get = () => {
        return localStorage.getItem(TOKEN_KEY);
    };

    static set = (encodedToken) => {
        localStorage.setItem(TOKEN_KEY, encodedToken);
    };

    static remove = () => {
        localStorage.removeItem(TOKEN_KEY);
    };

    static decode = (encoded) => {
        const header = encoded.split(".")[0];
        const payload = encoded.split(".")[1];
        const decoded = Object.assign(tokenTemplate, {
            header: JSON.parse(window.atob(header)),
            payload: JSON.parse(window.atob(payload))
        });
        console.log(decoded);
        return decoded;
    }
}
