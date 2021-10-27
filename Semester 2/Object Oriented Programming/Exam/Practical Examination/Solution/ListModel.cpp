#include "ListModel.h"

ListModel::ListModel(Service& _service, std::string _to_search, QObject* parent) : service { _service }, to_search{ _to_search }, QAbstractListModel{ parent }
{
	this->guys = this->service.get_by_name_id(_to_search);
}

void ListModel::change_search(std::string _to_search)
{
	this->to_search = _to_search;
	emit layoutChanged();
}

int ListModel::rowCount(const QModelIndex& parent) const
{
	return guys.size();
}

QVariant ListModel::data(const QModelIndex& index, int role) const
{
	int row = index.row();
	auto stars = this->guys;
	if (role == Qt::DisplayRole) {
		return QString::fromStdString(stars[row].to_string().c_str());
	}
	return QVariant{};
}

void ListModel::propagate()
{
	this->guys = this->service.get_by_name_id(this->to_search);
	emit layoutChanged();
}
