#pragma once
#include <QtCore/QAbstractListModel>
#include "Service.h"

class SearchModel : public QAbstractListModel
{
private:
    Service& service;
    std::string to_search;
public:
    std::vector<Student> guys;
    std::string teacher;
    SearchModel(Service& _service, std::string _teacher, std::string _to_search, QObject* parent = nullptr);

    void change_search(std::string _to_search);

    [[nodiscard]] int rowCount(const QModelIndex& parent)const override;

    [[nodiscard]] QVariant data(const QModelIndex& index, int role)const override;

    void propagate();
};

