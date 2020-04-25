//
// Created by jack on 3/22/20.
//

#ifndef IMRE5_EXCEPTIONS_H
#define IMRE5_EXCEPTIONS_H

#include <exception>

class ValueError : public std::exception {
private:
    const char* message;
public:
    explicit ValueError(const char* c);
    [[nodiscard]] const char * what () const noexcept override;
};

class ModeError : public std::exception {
private:
    const char* message;
public:
    explicit ModeError(const char* c);
    [[nodiscard]] const char * what () const noexcept override;
};

class RepositoryError : public std::exception {
private:
    const char* message;
public:
    explicit RepositoryError(const char* c);
    [[nodiscard]] const char * what () const noexcept override;
};

#endif //IMRE5_EXCEPTIONS_H
