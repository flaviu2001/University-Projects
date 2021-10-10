export const getLogger: (tag: string) => (...args: any) => void =
    tag => (...args) => {};
