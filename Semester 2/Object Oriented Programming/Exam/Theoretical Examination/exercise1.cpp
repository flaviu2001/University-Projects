#include <iostream>
#include <cassert>
#include <string>
#include <vector>

using namespace std;

class HTMLElement {
private:
    string text;
public:
    HTMLElement(string _text) : text{ _text } {

    }

    /**
        This function returns the html code as a string that the element has, so a paragraph would return <p>Some text<\p>
    */
    string getHTMLString() {
        return text;
    }
};

class HTMLParagraph : public HTMLElement {
private:

public:
    HTMLParagraph(string s) : HTMLElement{ string{"<p>"} + s + "</p>" } {

    }
};

class HTMLImage : public HTMLElement {
public:
    HTMLImage(string s) : HTMLElement{ string{"<img>"} + s + "</img>" } {

    }
};

template<typename T>
class HTMLBuilder {
private:
    vector<HTMLElement*> v;
public:
    HTMLBuilder& operator+=(HTMLElement* e) {
        if (e == nullptr)
            throw runtime_error("Cannot add a null element!");
        v.push_back(e);
        return *this;
    }
    friend ostream& operator<< (ostream& os, HTMLBuilder &b) {
        os << "<html><body>";
        for (auto x : b.v)
            os << x->getHTMLString();
        os << "</html></body>";
        return os;
    }
};

void fct1() {
    HTMLElement* p1 = new HTMLParagraph{ "Examination" };
    assert(p1->getHTMLString() == "<p>Examination</p>");
    HTMLElement* p2 = nullptr;
    HTMLElement* i1 = new HTMLImage{ "a.jpg" };
    HTMLElement* i2 = new HTMLImage{ "b.jpg" };
    assert(i2->getHTMLString() == "<img>b.jpg</img>");
    HTMLBuilder<HTMLElement*> html{};
    try {
        html += p2;
        assert(false);
    }
    catch (runtime_error& e) {
        assert(strcmp(e.what(), "Cannot add a null element!") == 0);
    }

    ((html += p1) += i1) += i2;
    cout << html; // prints: <html><body><p>Examination</p><img>a.jpg</img><img>b.jpg</img></html></body>
    delete p1; delete p2;
    delete i1; delete i2;
}

int main()
{
    fct1();
    return 0;
}