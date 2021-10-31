export const timingLogger = async (ctx, next) => {
    const start = new Date();
    await next();
    const ms = new Date() - start;
    console.log(`${ctx.method} ${ctx.url} ${ctx.response.status} - ${ms}ms`);
}

export const exceptionHandler = async (ctx, next) => {
    try {
        return await next();
    } catch (err) {
        console.log(err)
        ctx.response.body = {issue: [{error: err.message || 'Unexpected error'}]};
        ctx.response.status = 500;
    }
}
