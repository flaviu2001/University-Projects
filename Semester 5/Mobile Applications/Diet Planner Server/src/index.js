import {jwtConfig} from "./utils";

const Koa = require('koa');
const app = new Koa();
const server = require('http').createServer(app.callback());
const WebSocket = require('ws');
const wss = new WebSocket.Server({server});
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyparser = require('koa-bodyparser');
const {timingLogger, exceptionHandler, initWss} = require("./utils");
import { router as mealRouter } from './meals';
import { router as authRouter } from './auth';
import jwt from 'koa-jwt';

initWss(wss);

app.use(bodyparser());
app.use(cors());
app.use(timingLogger);
app.use(exceptionHandler);

const prefix = '/api';

// public
const publicApiRouter = new Router({ prefix });
publicApiRouter
    .use('/auth', authRouter.routes());
app
    .use(publicApiRouter.routes())
    .use(publicApiRouter.allowedMethods());

app.use(jwt(jwtConfig));

// protected
const protectedApiRouter = new Router({ prefix });
protectedApiRouter
    .use('/meal', mealRouter.routes());
app
    .use(protectedApiRouter.routes())
    .use(protectedApiRouter.allowedMethods());

server.listen(3000);
console.log('started on port 3000');
