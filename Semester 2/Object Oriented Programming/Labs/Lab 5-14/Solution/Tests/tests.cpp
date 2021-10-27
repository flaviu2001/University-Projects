//
// Created by jack on 3/22/20.
//

#include "../Service/Service.h"
#include "../Utilities/Exceptions.h"
#include <cassert>
#include <iostream>


void test__service_add__2_adds__list_has_2_turrets(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    service.add("Strawberries", "cherries", 4, 8, "and an angel's kiss in spring");
    auto list = service.get_turret_list();
    assert(list.size() == 2 && list[0].get_location() == "loco" && list[1].get_location() == "Strawberries");
    remove("test_repo.txt");
}

void test__service_add__2_adds_with_identical_location__ValueError(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
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
    remove("test_repo.txt");
}

void test__service_add__invalid_mode__ModeError(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.set_mode("B");
    try{
        service.add("loco", "poco", 123, 456, "everywhere");
        assert(false);
    }catch (ModeError &ve){
        //correct
    }catch (...){
        assert(false);
    }
    remove("test_repo.txt");
}

void test__service_remove__1_remove__list_is_empty(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    service.remove("loco");
    assert(service.get_turret_list().empty());
    remove("test_repo.txt");
}

void test__service_remove__1_inexistant_remove__ValueError(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    try{
        service.remove("poco");
        assert(false);
    }catch (ValueError &ve){
        //correct
    }catch (...){
        assert(false);
    }
    remove("test_repo.txt");
}

void test__service_remove__invalid_mode__ModeError(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.set_mode("B");
    try{
        service.remove("loco");
        assert(false);
    }catch (ModeError &ve){
        //correct
    }catch (...){
        assert(false);
    }
    remove("test_repo.txt");
}

void test__service_update__1_valid_update__turret_updated(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    service.update("loco", "moco", 112, 412, "nowhere");
    Turret turret = service.get_turret_list()[0];
    assert(turret.get_size() == "moco" && turret.get_aura_level() == 112 && turret.get_separate_parts() == 412 && turret.get_vision() == "nowhere");
    remove("test_repo.txt");
}

void test__service_update__1_invalid_update__ValueError(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("loco", "poco", 123, 456, "everywhere");
    try {
        service.update("roco", "moco", 112, 412, "nowhere");
        assert(false);
    }catch (ValueError &ve){
        //correct
    }catch (...){
        assert(false);
    }
    remove("test_repo.txt");
}

void test__service_update__invalid_mode__ModeError(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.set_mode("B");
    try{
        service.update("loco", "poco", 123, 456, "everywhere");
        assert(false);
    }catch (ModeError &ve){
        //correct
    }catch (...){
        assert(false);
    }
    remove("test_repo.txt");
}

void test__turret_operator_equal__self_assign__nothing_changes(){
    Turret turret("loco", "poco", 123, 456, "everywhere");
    turret = turret;
    assert(turret.get_location() == "loco" && turret.get_size() == "poco" && turret.get_aura_level() == 123 && turret.get_separate_parts() == 456 && turret.get_vision() == "everywhere");
    remove("test_repo.txt");
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
    remove("test_repo.txt");
}

void test__next_turret__3_next_calls_on_list_of_3_turrets__next_loops_around(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("home", "big", 10, 14, "bad");
    service.add("work", "small", 22, 13, "good");
    service.add("Guatemala", "medium", 142, 2, "like kinda ok");
    service.set_mode("B");
    Turret turret[4];
    for (auto & i : turret)
        i = service.next_turret();
    assert(turret[0].get_location() == "home" || turret[1].get_location() == "work" || turret[2].get_location() == "Guatemala" || turret[3].get_location() == "home");
    remove("test_repo.txt");
}

void test__save_turret__save_2objects__2objects_added_to_mylist(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("home", "big", 10, 14, "bad");
    service.add("work", "small", 22, 13, "good");
    service.add("Guatemala", "medium", 142, 2, "like kinda ok");
    service.set_mode(B_MODE);
    service.save_turret("home");
    service.save_turret("Guatemala");
    std::vector<Turret> turrets = service.get_saved_turrets_list();
    assert(turrets[0].get_location() == "home" && turrets[1].get_location() == "Guatemala");
    remove("test_repo.txt");
}

void test__get_turret_list_by_size_by_parts__4objects__1filtered(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("100", "1X2X3", 500, 44, "zzz");
    service.add("200", "2X3X4", 500, 44, "zzz");
    service.add("300", "1X2X3", 500, 22, "zzz");
    service.add("400", "2X3X4", 500, 22, "zzz");
    service.set_mode(B_MODE);
    auto turrets = service.get_turret_list_by_size_by_parts("1X2X3", 33);
    assert(turrets.size() == 1 && turrets[0].get_location() == "100");
    remove("test_repo.txt");
}

void test__turret_equality__one_equal_one_not__first_true_second_false(){
    assert(Turret("a", "b", 1, 2, "c") == Turret("a", "d", 4, 3, "e") && !(Turret("a", "b", 1, 2, "c") == Turret("e", "d", 4, 3, "e")));
    remove("test_repo.txt");
}

void test__service_remove_turret__remove_saved_turret__saved_turret_removed(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("home", "big", 10, 14, "bad");
    service.add("work", "small", 22, 13, "good");
    service.add("Guatemala", "medium", 142, 2, "like kinda ok");
    service.set_mode(B_MODE);
    service.save_turret("home");
    service.set_mode(A_MODE);
    service.remove("home");
    service.set_mode(B_MODE);
    assert(service.get_saved_turrets_list().empty());
    remove("test_repo.txt");
}

void test__get_turret_list_by_size_by_parts__incorrect_mode__ModeError(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("home", "big", 10, 14, "bad");
    service.add("work", "small", 22, 13, "good");
    service.add("Guatemala", "medium", 142, 2, "like kinda ok");
    try{
        service.get_turret_list_by_size_by_parts("big", 10);
        assert(false);
    }catch (ModeError &ve){
        // good
    }catch (...){
        assert(false);
    }
    remove("test_repo.txt");
}

void test__repository_update__one_turret_updated__fields_modified()
{
    RepositoryMemory repository;
    repository.add(Turret("a", "b", 2, 1, "e"));
    repository.update("a", "e", 1, 2, "f");
    assert(repository.get_turret_list()[0].get_size() == "e");
}

void test__repository_find__one_turret_to_find__turret_found()
{
    RepositoryMemory repository;
    repository.add(Turret("a", "b", 2, 1, "e"));
    assert(repository.find_turret("a").get_size() == "b");
}

void test__repository_find__no_turret_found__error_thrown()
{
    RepositoryMemory repository;
    repository.add(Turret("a", "b", 2, 1, "e"));
    try{
        repository.find_turret("e");
        assert(false);
    }catch (ValueError &ve){
        // good
    }catch (...){
        assert(false);
    }
    remove("test_repo.txt");
}

void test__next_turret__incorrect_mode__ModeError(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(A_MODE);
    service.add("home", "big", 10, 14, "bad");
    try{
        service.next_turret();
        assert(false);
    }catch (ModeError &ve){
        //correct
    }catch (...){
        assert(false);
    }
    remove("test_repo.txt");
}

void test__next_turret__empty_list__ValueError(){
    Repository *repository = new RepositoryCSV;
    Repository *repository_saved = new RepositoryMemory;
    Service service(repository, repository_saved);
    service.set_file_name_main_repository("test_repo.txt");
    service.set_mode(B_MODE);
    try{
        service.next_turret();
        assert(false);
    }catch (ValueError &ve){
        //correct
    }catch (...){
        assert(false);
    }
    remove("test_repo.txt");
}

void test__repository_SQL_add__3_turrets_save_2__list_is_size_2()
{
    Repository *repository = new RepositoryMemory;
    Service service(repository);
    service.set_file_name_saved_turrets_repository("test.sql");
    service.set_mode(A_MODE);
    service.add("ana", "are", 1, 2, "mere");
    service.add("banana", "are", 2, 1, "raci");
    service.add("hello", "greetings", 121, 213, "arbuz");
    service.set_mode(B_MODE);
    service.save_turret("ana");
    service.save_turret("hello");
    assert(service.get_saved_turrets_list().size() == 2);
    remove("test.sql");
}

void test__repository_SQL_update__3_turrets_save_1_update_1__list_is_size_2()
{
    Repository *repository = new RepositoryMemory;
    Service service(repository);
    service.set_file_name_saved_turrets_repository("test.sql");
    service.set_mode(A_MODE);
    service.add("ana", "are", 1, 2, "mere");
    service.add("banana", "are", 2, 1, "raci");
    service.add("hello", "greetings", 121, 213, "arbuz");
    service.set_mode(B_MODE);
    service.save_turret("ana");
    service.set_mode(A_MODE);
    service.update("ana", "rana", 1, 2, "hrana");
    service.set_mode(B_MODE);
    assert(service.get_saved_turrets_list()[0].get_size() == "rana");
    remove("test.sql");
}

void test__repository_SQL_delete__3_turrets_save_2_remove_1__list_is_size_1()
{
    Repository *repository = new RepositoryMemory;
    Service service(repository);
    service.set_file_name_saved_turrets_repository("test.sql");
    service.set_mode(A_MODE);
    service.add("ana", "are", 1, 2, "mere");
    service.add("banana", "are", 2, 1, "raci");
    service.add("hello", "greetings", 121, 213, "arbuz");
    service.set_mode(B_MODE);
    service.save_turret("ana");
    service.save_turret("hello");
    service.set_mode(A_MODE);
    service.remove("ana");
    service.set_mode(B_MODE);
    assert(service.get_saved_turrets_list().size() == 1);
    remove("test.sql");
}

void test__repository_HTML_add__3_turrets_save_2__list_is_size_2()
{
    Repository *repository = new RepositoryMemory;
    Service service(repository);
    service.set_file_name_saved_turrets_repository("test.html");
    service.set_mode(A_MODE);
    service.add("ana", "are", 1, 2, "mere");
    service.add("banana", "are", 2, 1, "raci");
    service.add("hello", "greetings", 121, 213, "arbuz");
    service.set_mode(B_MODE);
    service.save_turret("ana");
    service.save_turret("hello");
    assert(service.get_saved_turrets_list().size() == 2);
    remove("test.html");
}

void test__repository_HTML_update__3_turrets_save_1_update_1__list_is_size_2()
{
    Repository *repository = new RepositoryMemory;
    Service service(repository);
    service.set_file_name_saved_turrets_repository("test.html");
    service.set_mode(A_MODE);
    service.add("ana", "are", 1, 2, "mere");
    service.add("banana", "are", 2, 1, "raci");
    service.add("hello", "greetings", 121, 213, "arbuz");
    service.set_mode(B_MODE);
    service.save_turret("ana");
    service.set_mode(A_MODE);
    service.update("ana", "rana", 1, 2, "hrana");
    service.set_mode(B_MODE);
    assert(service.get_saved_turrets_list()[0].get_size() == "rana");
    remove("test.html");
}

void test__repository_HTML_delete__3_turrets_save_2_remove_1__list_is_size_1()
{
    Repository *repository = new RepositoryMemory;
    Service service(repository);
    service.set_file_name_saved_turrets_repository("test.html");
    service.set_mode(A_MODE);
    service.add("ana", "are", 1, 2, "mere");
    service.add("banana", "are", 2, 1, "raci");
    service.add("hello", "greetings", 121, 213, "arbuz");
    service.set_mode(B_MODE);
    service.save_turret("ana");
    service.save_turret("hello");
    service.set_mode(A_MODE);
    service.remove("ana");
    service.set_mode(B_MODE);
    assert(service.get_saved_turrets_list().size() == 1);
    remove("test.html");
}


void test_all(){
    remove("test_repo.txt");
    remove("test.sql");
    remove("test.html");
    test__service_add__2_adds__list_has_2_turrets();
    test__service_add__2_adds_with_identical_location__ValueError();
    test__service_add__invalid_mode__ModeError();
    test__service_remove__1_remove__list_is_empty();
    test__service_remove__1_inexistant_remove__ValueError();
    test__service_remove__invalid_mode__ModeError();
    test__service_update__1_valid_update__turret_updated();
    test__service_update__1_invalid_update__ValueError();
    test__service_update__invalid_mode__ModeError();
    test__turret_operator_equal__self_assign__nothing_changes();
    test__dynamic_array();
    test__next_turret__3_next_calls_on_list_of_3_turrets__next_loops_around();
    test__save_turret__save_2objects__2objects_added_to_mylist();
    test__get_turret_list_by_size_by_parts__4objects__1filtered();
    test__turret_equality__one_equal_one_not__first_true_second_false();
    test__service_remove_turret__remove_saved_turret__saved_turret_removed();
    test__get_turret_list_by_size_by_parts__incorrect_mode__ModeError();
    test__repository_update__one_turret_updated__fields_modified();
    test__repository_find__one_turret_to_find__turret_found();
    test__repository_find__no_turret_found__error_thrown();
    test__repository_SQL_add__3_turrets_save_2__list_is_size_2();
    test__repository_SQL_update__3_turrets_save_1_update_1__list_is_size_2();
    test__repository_SQL_delete__3_turrets_save_2_remove_1__list_is_size_1();
    test__repository_HTML_add__3_turrets_save_2__list_is_size_2();
    test__repository_HTML_update__3_turrets_save_1_update_1__list_is_size_2();
    test__repository_HTML_delete__3_turrets_save_2_remove_1__list_is_size_1();
    test__next_turret__incorrect_mode__ModeError();
    test__next_turret__empty_list__ValueError();
}