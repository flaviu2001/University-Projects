#include "StudentsModel.h"

StudentsModel::StudentsModel(Service& _service, Teacher _teacher, QObject* parent) : service{ _service }, teacher{ _teacher }, QAbstractListModel{ parent }
{
}

int StudentsModel::rowCount(const QModelIndex& parent) const
{
    int cnt = 0;
    for (auto x : this->service.get_students())
        if (x.coordinator == teacher.name)
            ++cnt;
    return cnt;
}

QVariant StudentsModel::data(const QModelIndex& index, int role) const
{
    int row = index.row();
    auto students = this->service.get_students();
    if (role == Qt::DisplayRole) {
        int cnt = 0;
        for (auto x : students)
            if (x.coordinator == teacher.name) {
                if (cnt == row) {
                    std::string toShow = x.to_string();
                    return QString::fromStdString(toShow);
                }
                else ++cnt;
            }
    }
    return QVariant{};
}

void StudentsModel::propagate()
{
    emit layoutChanged();
}
