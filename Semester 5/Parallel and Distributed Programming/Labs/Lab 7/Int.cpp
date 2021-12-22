#include "Int.h"
#include <vector>
#include <algorithm>
#include <iostream>
#include <sstream>
#include <cstring>

int max(int x, int y)
{
    return x > y ? x : y;
}

std::ostream &operator<<(std::ostream &os, Int a)
{
    std::stringstream ss;
    for (int i : a)
        ss << i;
    return os << ss.str();
}

std::istream &operator>>(std::istream &is, Int &a)
{
    a.clear();
    std::string s;
    is >> s;
    for (char & it : s)
        if(it >= '0' && it <= '9')
            a.push_back(it-'0');
    return is;
}

void Int::clear()
{
    digits.clear();
}

void Int::push_back(int x)
{
    digits.push_back(x);
}

void Int::pop_back()
{
    digits.pop_back();
}

void Int::resize(int x, int y = 0)
{
    digits.resize(x, y);
}

int Int::back()
{
    return digits.back();
}

std::vector<int>::iterator Int::begin()
{
    return digits.begin();
}

std::vector<int>::iterator Int::end()
{
    return digits.end();
}

void Int::sum(Int &dest, Int x, Int y)
{
    dest.clear();
    reverse(x.begin(), x.end());
    reverse(y.begin(), y.end());
    int mn = max(x.size(), y.size()), t=0;
    for (int i = 0; i < mn; ++i){
        int X = 0, Y = 0;
        if(x.size() > i)
            X = x[i];
        if(y.size() > i)
            Y = y[i];
        dest.push_back(X+Y+t);
        t = dest[i]/10;
        dest[i] %= 10;
    }
    if(t)
        dest.push_back(t);
    reverse(dest.begin(), dest.end());
}

void Int::sub(Int &c, Int x, Int y)
{
    c.clear();
    reverse(x.begin(), x.end());
    reverse(y.begin(), y.end());
    int mn = max(x.size(), y.size()), t=0;
    for (int i = 0; i < mn; ++i){
        int X = 0, Y = 0;
        if(x.size() > i)
            X = x[i];
        if(y.size() > i)
            Y = y[i];
        c.push_back(X-Y-t);
        if(c[i] < 0){
            t = 1;
            c[i] += 10;
        }else
            t = 0;
    }
    while(c.size() > 1 && c.back() == 0)
        c.pop_back();
    reverse(c.begin(), c.end());
}

void Int::prod(Int &c, Int a, Int b)
{
    c.clear();
    reverse(a.begin(), a.end());
    reverse(b.begin(), b.end());
    c.resize(a.size() + b.size()-1, 0);
    for (int i = 0; i < a.size(); ++i)
        for (int j = 0; j < b.size(); ++j)
            c[i+j] += a[i] * b[j];
    int t = 0;
    for (int i = 0; i <= a.size() + b.size()-2; ++i){
        c[i] += t;
        t = c[i]/10;
        c[i] %= 10;
    }
    if(t)
        c.push_back(t);
    reverse(c.begin(), c.end());
}

Int::Int()
{
    digits.push_back(0);
}

Int::Int(int x)
{
    if(x == 0)
        digits.push_back(0);
    else
        while(x){
            digits.push_back(x % 10);
            x/=10;
        }
    reverse(digits.begin(), digits.end());
}

Int::~Int()
{
    digits.clear();
}

int Int::size() const
{
    return (int)digits.size();
}

Int Int::operator+(const Int& b)
{
    Int res;
    sum(res, *this, b);
    return res;
}

Int Int::operator-(const Int& b)
{
    Int res;
    sub(res, *this, b);
    return res;
}

Int Int::operator*(const Int& b)
{
    Int res;
    prod(res, *this, b);
    return res;
}

bool Int::operator==(const Int &b) const
{
    if(this->size() != b.size())
        return false;
    for (int i = 0; i < b.size(); ++i)
        if(digits[i] != b.digits[i])
            return false;
    return true;
}

bool Int::operator!=(const Int &b) const
{
    if(this->size() != b.size())
        return true;
    for (int i = 0; i < b.size(); ++i)
        if(digits[i] != b.digits[i])
            return true;
    return false;
}

int& Int::operator[] (int i)
{
    return digits[i];
}

Int::operator int()
{
    int aux = 0;
    for (int i : *this)
        aux = aux*10 + i;
    return aux;
}

Int::Int(const char *q) {
    int n = (int)strlen(q);
    for (int i = 0; i < n; ++i)
        digits.push_back(q[i] - '0');
}

std::string Int::to_string() const {
    std::stringstream ss;
    for (int i : digits)
        ss << i;
    return ss.str();
}
