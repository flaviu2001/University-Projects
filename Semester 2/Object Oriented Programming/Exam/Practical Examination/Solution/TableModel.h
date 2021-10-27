#pragma once
#include <QtCore/QAbstractTableModel>
#include "Service.h"
class TableModel : public QAbstractTableModel
{
private:
    Service& service;
    bool checked;
    Astronomer astronomer;
public:
    explicit TableModel(Service& _service, Astronomer _astronomer, QObject* parent = nullptr);

    [[nodiscard]] int rowCount(const QModelIndex& parent)const override;

    [[nodiscard]] int columnCount(const QModelIndex& parent)const override;

    [[nodiscard]] QVariant data(const QModelIndex& index, int role)const override;
    
    void change ();

    void propagate();
};

