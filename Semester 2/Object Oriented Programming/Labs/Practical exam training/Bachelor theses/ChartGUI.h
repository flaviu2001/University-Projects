#pragma once
#include <QtWidgets/QWidget>
#include <QtWidgets/QWidget>
#include <unordered_map>
#include <QtWidgets/QListView>
#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QLabel>
#include <QtWidgets/QPushButton>
#include <QtCharts/QChartView>
#include <QtCharts/QBarSeries>
#include <QtCharts/QBarSet>
#include <QtCharts/QLegend>
#include <QtCharts/QBarCategoryAxis>
#include <QtCharts/QValueAxis>
#include <algorithm>
#include <utility>

QT_CHARTS_USE_NAMESPACE
#include "Service.h"

class ChartGUI : public QWidget
{
	Q_OBJECT
private:
	Service& service;
	QChart* chart;
	QBarSeries* series;
	QValueAxis* axisY;
public:
	ChartGUI(Service& _service);
	void build_chart();
};

