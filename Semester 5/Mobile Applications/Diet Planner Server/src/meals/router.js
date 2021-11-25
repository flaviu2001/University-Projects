import Router from 'koa-router';
import { broadcast } from "../utils";
import mealStore from './store';
import fs from "fs";

export const router = new Router();

router.get('/filter/:name?', async (ctx) => {
    const response = ctx.response;
    const userId = ctx.state.user._id;
    let name = ctx.params.name;
    if (!name)
        name = ''
    let arr = []
    let all = await mealStore.find({ userId })
    for (let meal of all)
        if (meal.name.toLowerCase().includes(name.toLowerCase()))
            arr = arr.concat(meal)
    response.body = arr;
    response.status = 200; // ok
});

router.get('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const meal = await mealStore.findOne({ _id: ctx.params.id });
    const response = ctx.response;
    if (meal) {
        if (meal.userId === userId) {
            response.body = meal;
            response.status = 200; // ok
        } else {
            response.status = 403; // forbidden
        }
    } else {
        response.status = 404; // not found
    }
});

const createMeal = async (ctx, meal, response) => {
    try {
        const userId = ctx.state.user._id;
        meal.userId = userId;
        response.body = await mealStore.insert(meal);
        response.status = 201; // created
        broadcast(userId, { type: 'created', payload: response.body });
    } catch (err) {
        response.body = { message: err.message };
        response.status = 400; // bad request
    }
};

router.post('/', async ctx => await createMeal(ctx, ctx.request.body, ctx.response));

router.post('/photo', async ctx => {
    // noinspection JSUnresolvedVariable
    const photo = ctx.request.body.data;
    let buff = Buffer.from(photo, 'base64');
    fs.writeFile('my-file.png', buff, (err) => {
        if (err) throw err;
        console.log('The binary data has been decoded and saved to my-file.png');
    });
    ctx.response.status = 200
})

router.put('/:id', async (ctx) => {
    const meal = ctx.request.body;
    const id = ctx.params.id;
    const mealId = meal._id;
    const response = ctx.response;
    if (mealId && mealId !== id) {
        response.body = { message: 'Param id and body _id should be the same' };
        response.status = 400; // bad request
        return;
    }
    if (!mealId) {
        await createMeal(ctx, meal, response);
    } else {
        const userId = ctx.state.user._id;
        meal.userId = userId;
        const updatedCount = await mealStore.update({ _id: id }, meal);
        if (updatedCount === 1) {
            response.body = meal;
            response.status = 200; // ok
            broadcast(userId, { type: 'updated', payload: meal });
        } else {
            response.body = { message: 'Resource no longer exists' };
            response.status = 405; // method not allowed
        }
    }
});

router.del('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const meal = await mealStore.findOne({ _id: ctx.params.id });
    if (meal && userId !== meal.userId) {
        ctx.response.status = 403; // forbidden
    } else {
        await mealStore.remove({ _id: ctx.params.id });
        ctx.response.status = 204; // no content
    }
});
