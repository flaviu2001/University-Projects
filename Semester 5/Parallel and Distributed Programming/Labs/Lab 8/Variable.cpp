#include "Variable.h"
#include "utils.h"

Variable::Variable(): lock(pthread_mutex_t()) {
    pthread_mutex_init(&lock, nullptr);
    value = 0;
    subscribers = std::vector<int>();
}

Variable::~Variable() {
    pthread_mutex_destroy(&lock);
}

int Variable::get_value() const {
    int the_value = value;
    return the_value;
}

void Variable::set_value(int new_value) {
    value = new_value;
}

const std::vector<int> &Variable::get_subscribers() {
    return subscribers;
}

void Variable::add_subscriber(int id) {
    subscribers.push_back(id);
}
