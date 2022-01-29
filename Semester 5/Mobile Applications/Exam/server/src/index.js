const Koa = require('koa');
const app = new Koa();
const server = require('http').createServer(app.callback());
const WebSocket = require('ws');
const wss = new WebSocket.Server({server});
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyparser = require('koa-bodyparser');

app.use(bodyparser());
app.use(cors());

app.use(async (ctx, next) => {
  const start = new Date();
  await next();
  const ms = new Date() - start;
  console.log(`${ctx.method} ${ctx.url} - ${ms}ms`);
});

app.use(async (ctx, next) => {
  await new Promise(resolve => setTimeout(resolve, 1000));
  await next();
});

const menuItems = Array.from(Array(10).keys()).map(code => ({ code, name: `p${code}` }));

wss.on('connection', ws => {
  console.log('on connection');
  ws.send(JSON.stringify(menuItems));
});

const router = new Router();

const items = [];

router.post('/item', ctx => {
  console.log(ctx.request.body)
  const { code, quantity } = ctx.request.body;
  const index = menuItems.findIndex(it => it.code === code);
  if (typeof code !== 'number' || index === -1) {
    ctx.response.body = { text: 'Menu item code not found' };
    ctx.response.status = 200;
  } else if (typeof quantity !== 'number' || quantity < 0) {
    ctx.response.body = {
      text: 'Quantity must be a positive number'
    };
    ctx.response.status = 200;
  } else {
    const item = { text: 'OK' };
    items.push(item);
    ctx.response.body = item;
    ctx.response.status = 200;
  }
});

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);
