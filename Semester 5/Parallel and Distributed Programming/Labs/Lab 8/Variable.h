#ifndef LAB_8_VARIABLE_H
#define LAB_8_VARIABLE_H


#include <vector>
#include <pthread.h>

class Variable {
private:
    int value;
    std::vector<int> subscribers;
public:
    pthread_mutex_t lock;

    Variable();
    ~Variable();

    [[nodiscard]] int get_value() const;
    void set_value(int new_value);

    const std::vector<int>& get_subscribers();
    void add_subscriber(int id);
};


#endif //LAB_8_VARIABLE_H
