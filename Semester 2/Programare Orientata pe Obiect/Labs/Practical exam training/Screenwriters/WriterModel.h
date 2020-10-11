//
// Created by jack on 6/13/20.
//

#ifndef SCREENWRITERS_WRITERMODEL_H
#define SCREENWRITERS_WRITERMODEL_H


#include <QtCore/QAbstractListModel>
#include "service.h"

class WriterModel : public QAbstractListModel {
private:
    Service &service;
public:
    explicit WriterModel(Service &_service, QObject* parent = nullptr);

    [[nodiscard]] int rowCount(const QModelIndex &parent)const override;

    [[nodiscard]] QVariant data (const QModelIndex &index, int role)const override;

    void propagate();
};


#endif //SCREENWRITERS_WRITERMODEL_H
