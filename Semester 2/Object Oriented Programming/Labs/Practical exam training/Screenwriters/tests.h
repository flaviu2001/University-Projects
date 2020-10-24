//
// Created by jack on 6/13/20.
//

#ifndef SCREENWRITERS_TESTS_H
#define SCREENWRITERS_TESTS_H

#include <cassert>
#include <stdexcept>
#include "service.h"

void test__service_add_idea()
{
    Service s("screenwriters.txt", "ideas.txt");
    int nr = s.get_ideas().size();
    Idea i{"a", "b", "c", 2};
    s.add_idea(i);
    try{
        s.add_idea(i);
        assert(false);
    }catch (std::runtime_error &re){
        static_assert(true);
    }
    i.act = 4;
    try{
        s.add_idea(i);
        assert(false);
    }catch (std::runtime_error &re){
        static_assert(true);
    }
    i.description.clear();
    i.act = 2;
    try{
        s.add_idea(i);
        assert(false);
    }catch (std::runtime_error &re){
        static_assert(true);
    }
    assert(s.get_ideas().size()-nr == 1);
}

void test__service_update_idea()
{
    Idea i{"a", "proposed", "c", 2};
    Service::update_idea(i, "accepted");
    assert(i.status == "accepted");
}

#endif //SCREENWRITERS_TESTS_H
