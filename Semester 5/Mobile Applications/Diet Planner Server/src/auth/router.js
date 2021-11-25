import Router from 'koa-router';
import userStore from './store';
import jwt from 'jsonwebtoken';
import { jwtConfig } from '../utils';

export const router = new Router();

const createToken = (user) => {
    return jwt.sign({ username: user.username, _id: user._id }, jwtConfig.secret, { expiresIn: 60 * 60 * 60 });
};

const createUser = async (user, response) => {
    try {
        await userStore.insert(user);
        response.body = { token: createToken(user) };
        response.status = 201; // created
    } catch (err) {
        response.body = { issue: [{ error: err.message }] };
        response.status = 400; // bad request
    }
};

router.post('/signup', async (ctx) => await createUser(ctx.request.body, ctx.response));

router.post('/login', async (ctx) => {
    const credentials = ctx.request.body;
    const response = ctx.response;
    const user = await userStore.findOne({ username: credentials.username });
    if (user && credentials.password === user.password) {
        response.body = { token: createToken(user) };
        response.status = 201; // created
    } else {
        response.body = { issue: [{ error: 'Invalid credentials' }] };
        response.status = 400; // bad request
    }
});
