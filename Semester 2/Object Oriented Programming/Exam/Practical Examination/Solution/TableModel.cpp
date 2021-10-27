#include "TableModel.h"

TableModel::TableModel(Service& _service, Astronomer _astronomer, QObject* parent) : service{_service}, QAbstractTableModel{parent}, astronomer{_astronomer}
{
    checked = false;
}

int TableModel::rowCount(const QModelIndex& parent) const
{
    if (!checked)
	    return service.get_stars().size();
    std::vector<Star> v;
    for (auto x : service.get_stars())
        if (x.constellation == astronomer.constellation)
            v.push_back(x);
    return v.size();
}

int TableModel::columnCount(const QModelIndex& parent) const
{
	return 5;
}

QVariant TableModel::data(const QModelIndex& index, int role) const
{
    int row = index.row();
    int col = index.column();

    std::vector<Star> stars;
    if (!checked)
        stars = this->service.get_stars();
    else {
        for (auto x : service.get_stars())
            if (x.constellation == astronomer.constellation)
                stars.push_back(x);
    }
    Star star = stars[row];
    if (role == Qt::DisplayRole) {
        switch (col) {
        case 0: {
            return QString::fromStdString(star.name);
        }
        case 1: {
            return QString::fromStdString(star.constellation);
        }
        case 2: {
            return QString::fromStdString(std::to_string(star.ra));
        }
        case 3: {
            return QString::fromStdString(std::to_string(star.dec));
        }
        case 4: {
            return QString::fromStdString(std::to_string(star.diameter));
        }
        default:
            break;
        }
    }
    return QVariant{};
}

void TableModel::change()
{
    checked = !checked;
    propagate();
}

void TableModel::propagate()
{
    emit layoutChanged();
}
