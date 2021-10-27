#include "SearchModel.h"

SearchModel::SearchModel(Service& _service, std::string _teacher, std::string _to_search, QObject* parent) : service{ _service }, to_search{ _to_search }, QAbstractListModel{ parent }, teacher{ _teacher }
{
	guys = this->service.get_by_name_id(to_search);
}

void SearchModel::change_search(std::string _to_search)
{
	this->to_search = _to_search;
	this->guys = this->service.get_by_name_id(_to_search);
	emit layoutChanged();
}

int SearchModel::rowCount(const QModelIndex& parent) const
{
	return guys.size();
}

QVariant SearchModel::data(const QModelIndex& index, int role) const
{
	int row = index.row();
	auto students = this->guys;
	if (role == Qt::DisplayRole) {
		return QString::fromStdString(students[row].to_string().c_str());
	}
	return QVariant{};
}

void SearchModel::propagate()
{
	this->guys = this->service.get_by_name_id(this->to_search);
	emit layoutChanged();
}
