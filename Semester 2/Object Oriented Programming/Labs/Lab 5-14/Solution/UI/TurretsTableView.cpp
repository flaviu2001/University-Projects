//
// Created by jack on 5/28/20.
//

#include "TurretsTableView.h"
#include "string"

TurretsTableView::TurretsTableView(Service &_service, QObject *parent) : service{_service}, QAbstractTableModel{parent}{
}

int TurretsTableView::rowCount(const QModelIndex &parent) const {
    return this->service.get_saved_turrets_list().size();
}

int TurretsTableView::columnCount(const QModelIndex &parent) const {
    return 5;
}

QVariant TurretsTableView::data(const QModelIndex &index, int role) const {
    int row = index.row();
    int col = index.column();
    auto turrets = this->service.get_saved_turrets_list();
    Turret turret = turrets[row];
    if (role == Qt::DisplayRole){
        switch (col){
            case 0:{
                return QString::fromStdString(turret.get_location());
            }
            case 1:{
                return QString::fromStdString(turret.get_size());
            }
            case 2:{
                return QString::fromStdString(std::to_string(turret.get_aura_level()));
            }
            case 3:{
                return QString::fromStdString(std::to_string(turret.get_separate_parts()));
            }
            case 4:{
                return QString::fromStdString(turret.get_vision());
            }
            default:
                break;
        }
    }
    return QVariant{};
}

void TurretsTableView::propagate() {
    emit layoutChanged();
}
