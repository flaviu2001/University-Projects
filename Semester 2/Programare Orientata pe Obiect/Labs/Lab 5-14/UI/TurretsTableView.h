//
// Created by jack on 5/28/20.
//

#ifndef IMRE5_TURRETSTABLEVIEW_H
#define IMRE5_TURRETSTABLEVIEW_H


#include <Service/Service.h>
#include <QtCore/QAbstractTableModel>
#include <QtWidgets/QTableView>

class TurretsTableView : public QAbstractTableModel {
private:
    Service &service;
public:
    explicit TurretsTableView(Service &_service, QObject* parent = nullptr);

    [[nodiscard]] int rowCount(const QModelIndex &parent)const override;

    [[nodiscard]] int columnCount(const QModelIndex &parent)const override;

    [[nodiscard]] QVariant data (const QModelIndex &index, int role)const override;

    void propagate();
};


#endif //IMRE5_TURRETSTABLEVIEW_H
