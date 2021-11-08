import dateFormat from "dateformat";

export const baseUrl = 'localhost:3000';

export const getLogger: (tag: string) => (...args: any) => void =
    tag => (...args) => {};

const log = getLogger('api');

export interface ResponseProps<T> {
    data: T;
}

export function withLogs<T>(promise: Promise<ResponseProps<T>>, fnName: string): Promise<T> {
    log(`${fnName} - started`);
    return promise
        .then(res => {
            log(`${fnName} - succeeded`);
            return Promise.resolve(res.data);
        })
        .catch(err => {
            log(`${fnName} - failed`);
            return Promise.reject(err);
        });
}

export const config = {
    headers: {
        'Content-Type': 'application/json'
    }
};

export const authConfig = (token?: string) => ({
    headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
    }
});

export const libraryDateFormat = "yyyy-mm-dd HH:mm";
export const IonDateTimeDateFormat = "YYYY-MM-DD HH:mm";

export function dateToString(date: Date): string {
    return dateFormat(date, libraryDateFormat);
}

export function stringToDate(string: string | undefined | null): Date {
    return new Date(string || new Date());
}

