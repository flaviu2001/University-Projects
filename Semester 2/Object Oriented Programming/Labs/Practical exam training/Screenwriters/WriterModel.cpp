//
// Created by jack on 6/13/20.
//

#include <sstream>
#include "WriterModel.h"

WriterModel::WriterModel(Service &_service, QObject *parent) : service{_service}, QAbstractListModel{parent} {
}

int WriterModel::rowCount(const QModelIndex &parent) const {
    return this->service.get_ideas().size();
}

QVariant WriterModel::data(const QModelIndex &index, int role) const {
    int row = index.row();
    auto v = this->service.get_ideas();
    if (role == Qt::DisplayRole) {
        std::stringstream ss;
        ss << v[row];
        return QString::fromStdString(ss.str());
    }
    return QVariant{};
}

void WriterModel::propagate() {
    emit layoutChanged();
}
