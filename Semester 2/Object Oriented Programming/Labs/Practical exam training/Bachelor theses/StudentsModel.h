#pragma once
#include <QtCore/QAbstractListModel>
#include "Service.h"

class StudentsModel : public QAbstractListModel
{
private:
    Service& service;
    Teacher teacher;
public:
    explicit StudentsModel(Service& _service, Teacher _teacher, QObject* parent = nullptr);

    [[nodiscard]] int rowCount(const QModelIndex& parent)const override;

    [[nodiscard]] QVariant data(const QModelIndex& index, int role)const override;

    void propagate();
};

