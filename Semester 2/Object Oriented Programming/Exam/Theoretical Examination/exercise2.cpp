// app.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <cassert>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

class Icecream {
public:
    virtual string getDescription() = 0;
    virtual double computePrice() = 0;
    virtual ~Icecream() {

    }
};

class SimpleIcecream : public Icecream {
private:
    string description;
    double price;
public:
    SimpleIcecream(string _description, double _price) {
        description = _description;
        price = _price;
    }
    string getDescription() override {
        return string{"simple ice cream with "} + description;
    }
    double computePrice() override {
        return price;
    }
};

class IcecreamWithTopping : public Icecream{
protected:
    Icecream* ice;
public:
    ~IcecreamWithTopping() {
        delete ice;
    }
    IcecreamWithTopping(Icecream* _ice) {
        ice = _ice;
    }
    virtual string addTopping() = 0;
    string getDescription() override {
        return ice->getDescription() + " " + addTopping();
    }
};

class IcecreamWithChocolateTopping : public IcecreamWithTopping{
public:
    IcecreamWithChocolateTopping(Icecream* _ice) : IcecreamWithTopping{ _ice } {

    }
    string addTopping() override {
        return "with chocolate topping";
    }
    double computePrice() {
        return ice->computePrice() + 3;
    }
};

class IcecreamWithCaramelTopping : public IcecreamWithTopping {
public:
    IcecreamWithCaramelTopping(Icecream* _ice) : IcecreamWithTopping{ _ice } {

    }
    string addTopping() override {
        return "with caramel topping";
    }
    double computePrice() {
        return ice->computePrice() + 2;
    }
};

class Order {
private:
    vector<Icecream*> v;
public:
    void addIcecream(Icecream* ice) {
        v.push_back(ice);
    }
    void printOrder() {
        auto w = v;
        sort(w.begin(), w.end(), [](Icecream* i1, Icecream* i2) {return i1->computePrice() > i2->computePrice(); });
        for (auto x : w)
            cout << x->getDescription() << " " << x->computePrice() << "\n";
    }
    ~Order() {
        for (auto x : v)
            delete x;
    }
};


int main()
{
    Order o;
    o.addIcecream(new SimpleIcecream{"Vanilla", 2});
    o.addIcecream(new IcecreamWithCaramelTopping{ new IcecreamWithChocolateTopping{ new SimpleIcecream{"Pistachio", 3} } });
    o.addIcecream(new IcecreamWithCaramelTopping{ new SimpleIcecream{"Chocolate", 1.5} });
    o.addIcecream(new SimpleIcecream{ "Hazelnuts", 1 }); //I hate hazelnuts
    o.printOrder();
    return 0;
}