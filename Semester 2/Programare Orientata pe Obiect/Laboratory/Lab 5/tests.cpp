//
// Created by jack on 3/22/20.
//

#include "Service.h"
#include "Exceptions.h"
#include <cassert>


void test__service_add__2_adds__list_has_2_turrets(){
    Repository repository;
    Service service(repository);
    service.set_mode(GOD_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    service.add("Strawberries", "cherries", 4, 8, "and an angel's kiss in spring");
    auto list = service.get_turret_list();
    assert(list.size() == 2 && list[0].get_location() == "loco" && list[1].get_location() == "Strawberries");
}

void test__service_add__2_adds_with_identical_location__ValueError(){
    Repository repository;
    Service service(repository);
    service.set_mode(GOD_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    try{
        service.add("loco", "cherries", 4, 8, "and an angel's kiss in spring");
        assert(false);
    }catch(ValueError &ve){
        //correct
        std::string what = ve.what();
        assert(what == "A turret already exists in the given location."); //for 100% coverage in exceptions
    }catch(...){
        assert(false);
    }
}

void test__service_add__invalid_mode__ValueError(){
    Repository repository;
    Service service(repository);
    service.set_mode("B");
    try{
        service.add("loco", "poco", 123, 456, "everywhere");
        assert(false);
    }catch (ValueError &ve){
        //correct
    }catch (...){
        assert(false);
    }
}

void test__service_remove__1_remove__list_is_empty(){
    Repository repository;
    Service service(repository);
    service.set_mode(GOD_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    service.remove("loco");
    assert(service.get_turret_list().size() == 0);
}

void test__service_remove__1_invalid_remove__ValueError(){
    Repository repository;
    Service service(repository);
    service.set_mode(GOD_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    try{
        service.remove("poco");
        assert(false);
    }catch (ValueError &ve){
        //correct
    }catch (...){
        assert(false);
    }
}

void test__service_remove__invalid_mode__ValueError(){
    Repository repository;
    Service service(repository);
    service.set_mode("B");
    try{
        service.remove("loco");
        assert(false);
    }catch (ValueError &ve){
        //correct
    }catch (...){
        assert(false);
    }
}

void test__service_update__1_valid_update__turret_updated(){
    Repository repository;
    Service service(repository);
    service.set_mode(GOD_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    service.update("loco", "moco", 112, 412, "nowhere");
    Turret turret = service.get_turret_list()[0];
    assert(turret.get_size() == "moco" && turret.get_aura_level() == 112 && turret.get_separate_parts() == 412 && turret.get_vision() == "nowhere");
}

void test__service_update__1_invalid_update__ValueError(){
    Repository repository;
    Service service(repository);
    service.set_mode(GOD_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    try {
        service.update("roco", "moco", 112, 412, "nowhere");
        assert(false);
    }catch (ValueError &ve){
        //correct
    }catch (...){
        assert(false);
    }
}

void test__service_update__invalid_mode__ValueError(){
    Repository repository;
    Service service(repository);
    service.set_mode("B");
    try{
        service.update("loco", "poco", 123, 456, "everywhere");
        assert(false);
    }catch (ValueError &ve){
        //correct
    }catch (...){
        assert(false);
    }
}

void test__turret_operator_equal(){
    Turret turret("loco", "poco", 123, 456, "everywhere");
    turret = turret;
    assert(turret.get_location() == "loco" && turret.get_size() == "poco" && turret.get_aura_level() == 123 && turret.get_separate_parts() == 456 && turret.get_vision() == "everywhere");
}

void test__dynamic_array(){
    DynamicArray<int> array, array2;
    array.add(12);
    array.add(15);
    array.add(100);
    array.remove(15);
    array.remove(102);
    array.add(102);
    array = array;
    array2.add(14);
    array2 = array;
    assert(array.size() == 3 && array[0] == 12 && array[1] == 100 && array[2] == 102 && array == array2);
}

void test__next_turret__3_objects__allgood(){
    Repository repository;
    Service service(repository);
    service.set_mode(GOD_MODE);
    service.add("home", "big", 10, 14, "bad");
    service.add("work", "small", 22, 13, "good");
    service.add("Guatemala", "medium", 142, 2, "like kinda ok");
    service.set_mode("B");
    Turret turret[4];
    for (auto & i : turret)
        i = service.next_turret();
    assert(turret[0].get_location() == "home" || turret[1].get_location() == "work" || turret[2].get_location() == "Guatemala" || turret[3].get_location() == "home");
}

void test__save_turret__2objects__allgood(){
    Repository repository;
    Service service(repository);
    service.set_mode(GOD_MODE);
    service.add("home", "big", 10, 14, "bad");
    service.add("work", "small", 22, 13, "good");
    service.add("Guatemala", "medium", 142, 2, "like kinda ok");
    service.set_mode(B_MODE);
    service.save_turret("home");
    service.save_turret("Guatemala");
    DynamicArray<Turret> turrets = service.get_saved_turrets_list();
    assert(turrets[0].get_location() == "home" && turrets[1].get_location() == "Guatemala");
}

void test__get_turret_list_by_size_by_parts__4objects__1filtered(){
    Repository repository;
    Service service(repository);
    service.set_mode(GOD_MODE);
    service.add("100", "1X2X3", 500, 44, "zzz");
    service.add("200", "2X3X4", 500, 44, "zzz");
    service.add("300", "1X2X3", 500, 22, "zzz");
    service.add("400", "2X3X4", 500, 22, "zzz");
    service.set_mode(B_MODE);
    auto turrets = service.get_turret_list_by_size_by_parts("1X2X3", 33);
    assert(turrets.size() == 1 && turrets[0].get_location() == "100");
}

void test_all(){
    test__service_add__2_adds__list_has_2_turrets();
    test__service_add__2_adds_with_identical_location__ValueError();
    test__service_add__invalid_mode__ValueError();
    test__service_remove__1_remove__list_is_empty();
    test__service_remove__1_invalid_remove__ValueError();
    test__service_remove__invalid_mode__ValueError();
    test__service_update__1_valid_update__turret_updated();
    test__service_update__1_invalid_update__ValueError();
    test__service_update__invalid_mode__ValueError();
    test__turret_operator_equal();
    test__dynamic_array();
    test__next_turret__3_objects__allgood();
    test__save_turret__2objects__allgood();
    test__get_turret_list_by_size_by_parts__4objects__1filtered();
}