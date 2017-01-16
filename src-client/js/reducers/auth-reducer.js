const initialState = {
    token: null
};

export const Type = {
    LOGIN: "login",
    LOGOUT: "logout"
};

export default function authReducer(state = initialState, action) {
    switch (action.type) {
        case Type.LOGIN:
            return Object.assign(state, {token: "tmp"});

        case Type.LOGOUT:
            return Object.assign(state, {token: null});

        default:
            return state
    }
}