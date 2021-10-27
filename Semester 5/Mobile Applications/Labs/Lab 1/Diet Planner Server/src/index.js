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
    console.log(`${ctx.method} ${ctx.url} ${ctx.response.status} - ${ms}ms`);
});

app.use(async (ctx, next) => {
    await new Promise(resolve => setTimeout(resolve, 2000));
    await next();
});

app.use(async (ctx, next) => {
    try {
        await next();
    } catch (err) {
        ctx.response.body = {issue: [{error: err.message || 'Unexpected error'}]};
        ctx.response.status = 500;
    }
});

class Meal {
    constructor({id, name, calories, dateAdded, vegetarian}) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.dateAdded = dateAdded;
        this.vegetarian = vegetarian;
    }
}

const meals = [];
for (let i = 0; i < 3; i++) {
    meals.push(new Meal({id: `${i}`, name: `meal ${i}`, calories: i, dateAdded: new Date(Date.now() + i), vegetarian: i%2 === 0}));
}
let lastUpdated = meals[meals.length - 1].date;
let lastId = meals[meals.length - 1].id;

const broadcast = data =>
    wss.clients.forEach(client => {
        if (client.readyState === WebSocket.OPEN) {
            client.send(JSON.stringify(data));
        }
    });

const router = new Router();

router.get('/meal', ctx => {
    const ifModifiedSince = ctx.request.get('If-Modified-Since');
    if (ifModifiedSince && new Date(ifModifiedSince).getTime() >= lastUpdated.getTime() - lastUpdated.getMilliseconds()) {
        ctx.response.status = 304; // NOT MODIFIED
        return;
    }
    ctx.response.set('Last-Modified', lastUpdated.toUTCString());
    ctx.response.body = meals;
    ctx.response.status = 200;
});

router.get('/meal/:id', async (ctx) => {
    const mealId = ctx.request.params.id;
    const meal = meals.find(meal => mealId === meal.id);
    if (meal) {
        ctx.response.body = meal;
        ctx.response.status = 200; // ok
    } else {
        ctx.response.body = {issue: [{warning: `meal with id ${mealId} not found`}]};
        ctx.response.status = 404; // NOT FOUND (if you know the resource was deleted, then return 410 GONE)
    }
});

const createMeal = async (ctx) => {
    const meal = ctx.request.body;
    if (!meal.name) { // validation
        ctx.response.body = {issue: [{error: 'Text is missing'}]};
        ctx.response.status = 400; //  BAD REQUEST
        return;
    }
    meal.id = `${parseInt(lastId) + 1}`;
    lastId = meal.id;
    meals.push(meal);
    ctx.response.body = meal;
    ctx.response.status = 201; // CREATED
    broadcast({event: 'created', payload: {meal: meal}});
};

router.post('/meal', async (ctx) => {
    await createMeal(ctx);
});

router.put('/meal/:id', async (ctx) => {
    const id = ctx.params.id;
    const meal = ctx.request.body;
    const mealId = meal.id;
    if (mealId && id !== meal.id) {
        ctx.response.body = {issue: [{error: `Param id and body id should be the same`}]};
        ctx.response.status = 400; // BAD REQUEST
        return;
    }
    if (!mealId) {
        await createMeal(ctx);
        return;
    }
    const index = meals.findIndex(meal => meal.id === id);
    if (index === -1) {
        ctx.response.body = {issue: [{error: `meal with id ${id} not found`}]};
        ctx.response.status = 400; // BAD REQUEST
        return;
    }
    meals[index] = meal;
    lastUpdated = new Date();
    ctx.response.body = meal;
    ctx.response.status = 200; // OK
    broadcast({event: 'updated', payload: {meal: meal}});
});

setInterval(() => {
    lastUpdated = new Date();
    lastId = `${parseInt(lastId) + 1}`;
    const meal = new Meal({id: lastId, name: `meal ${lastId}`, dateAdded: lastUpdated, calories: lastId, vegetarian: lastId%2 === 0});
    meals.push(meal);
    console.log(`${meal.name}`);
    broadcast({event: 'created', payload: {meal: meal}});
}, 3000);

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);
