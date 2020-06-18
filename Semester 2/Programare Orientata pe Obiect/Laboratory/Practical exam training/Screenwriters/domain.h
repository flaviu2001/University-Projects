//
// Created by jack on 6/13/20.
//

#ifndef SCREENWRITERS_DOMAIN_H
#define SCREENWRITERS_DOMAIN_H


#include <string>

class Screenwriter {
public:
    std::string name;
    std::string expertise;

    friend std::istream& operator>>(std::istream& reader, Screenwriter &screenwriter);
    friend std::ostream& operator<<(std::ostream& writer, const Screenwriter &screenwriter);
};

class Idea {
public:
    std::string description;
    std::string status;
    std::string creator;
    int act;

    friend std::istream& operator>>(std::istream& reader, Idea &idea);
    friend std::ostream& operator<<(std::ostream& writer, const Idea &idea);
};




#endif //SCREENWRITERS_DOMAIN_H
