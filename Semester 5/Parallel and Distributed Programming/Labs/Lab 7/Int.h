#ifndef INT_H
#define INT_H

#include <vector>
#include <iostream>

class Int{
    private:
        std::vector<int> digits;
        void clear();
        void push_back(int);
        void pop_back();
        void resize(int, int);
        int back();
        std::vector<int>::iterator begin();
        std::vector<int>::iterator end();
        static void sum(Int &, Int, Int);
        static void sub(Int &, Int, Int);
        static void prod(Int &, Int, Int);
        friend std::istream& operator>>(std::istream &, Int&);
        friend std::ostream& operator<<(std::ostream &, Int);
    public:
        Int();
        explicit Int(int);
        explicit Int(const char*);
        ~Int();
        [[nodiscard]] int size() const;
        Int operator+(const Int&);
        Int operator-(const Int&);
        Int operator*(const Int&);
        bool operator==(const Int&) const;
        bool operator!=(const Int&) const;
        explicit operator int ();
        int& operator[] (int);
        [[nodiscard]] std::string to_string() const;
};

#endif // INT_H
