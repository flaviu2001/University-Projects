#include "ChartGUI.h"

int max(int a, int b)
{
    return (a < b ? b : a);
}

ChartGUI::ChartGUI(Service& _service) : service{ _service }
{
    auto* chart_layout = new QVBoxLayout{ this };

    this->chart = new QChart;
    auto* chartView = new QChartView(this->chart);
    chart_layout->addWidget(chartView);
    this->series = new QBarSeries();
    this->chart->addSeries(this->series);
    this->chart->setTitle("Theses chart");
    this->chart->setAnimationOptions(QChart::SeriesAnimations);
    chart->legend()->setVisible(true);
    chart->legend()->setAlignment(Qt::AlignBottom);
    QStringList categories;
    for (auto x : this->service.get_teachers())
        categories << x.name.c_str();
    categories << "No teacher";
    auto* axisX = new QBarCategoryAxis;
    axisX->append(categories);
    this->chart->addAxis(axisX, Qt::AlignBottom);
    this->series->attachAxis(axisX);
    this->axisY = new QValueAxis;
    this->axisY->setRange(0, 10);
    chart->addAxis(axisY, Qt::AlignLeft);
    this->series->attachAxis(this->axisY);
    build_chart();
}

void ChartGUI::build_chart()
{
    this->series->clear();
    int mx = 0;
    std::unordered_map<std::string, std::vector<Student>> mp;
    for (const auto& student : this->service.get_students())
        mp[student.coordinator].push_back(student);
    auto set = new QBarSet("Number of students");
    for (auto x : this->service.get_teachers()) {
        mx = max(mx, mp[x.name].size());
        *set << mp[x.name].size();
    }
    *set << mp[""].size();
    this->series->append(set);
    this->axisY->setRange(0, mx);
}