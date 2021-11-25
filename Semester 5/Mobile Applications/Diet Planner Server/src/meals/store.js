import dataStore from 'nedb-promise';

export class MealStore {
    constructor({ filename, autoload }) {
        this.store = dataStore({ filename, autoload });
    }

    async find(props) {
        return this.store.find(props);
    }

    async findOne(props) {
        return this.store.findOne(props);
    }

    async insert(meal) {
        let mealText = meal.name;
        if (!mealText) { // validation
            throw new Error('Missing text property')
        }
        return this.store.insert(meal);
    };

    async update(props, meal) {
        return this.store.update(props, meal);
    }

    async remove(props) {
        return this.store.remove(props);
    }
}

export default new MealStore({ filename: './db/meals.json', autoload: true });