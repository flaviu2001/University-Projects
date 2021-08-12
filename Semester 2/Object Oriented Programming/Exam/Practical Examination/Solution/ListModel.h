#pragma once
#include <QtCore/QAbstractListModel>
#include "Service.h"

class ListModel : public QAbstractListModel
{
private:
    Service& service;
    std::string to_search;
    std::vector<Star> guys;
public:
    ListModel(Service& _service, std::string _to_search, QObject* parent = nullptr);

    void change_search(std::string _to_search);

    [[nodiscard]] int rowCount(const QModelIndex& parent)const override;

    [[nodiscard]] QVariant data(const QModelIndex& index, int role)const override;

    void propagate();
};

