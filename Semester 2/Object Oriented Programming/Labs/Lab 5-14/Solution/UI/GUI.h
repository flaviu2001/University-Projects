//
// Created by jack on 5/8/20.
//

#ifndef IMRE5_GUI_H
#define IMRE5_GUI_H


#include <QtWidgets/QWidget>
#include <QtWidgets/QListWidget>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QTextEdit>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QComboBox>
#include <QtWidgets/QLabel>
#include "Service/Service.h"
#include "TurretsTableView.h"

#include <QtCharts/QChartView>
#include <QtCharts/QBarSeries>
#include <QtCharts/QBarSet>
#include <QtCharts/QLegend>
#include <QtCharts/QBarCategoryAxis>
#include <QtCharts/QValueAxis>
#include <QtWidgets/QTableView>

QT_CHARTS_USE_NAMESPACE

class GUI : public QWidget{
Q_OBJECT
private:
    Service &service;
    QListWidget* turrets_list;
    QListWidget* filtered_list;
    QLineEdit* location_edit;
    QLineEdit* size_edit;
    QLineEdit* aura_level_edit;
    QLineEdit* separate_parts_edit;
    QLineEdit* vision_edit;
    QLineEdit* file_location_edit;
    QLineEdit* mylist_location_edit;
    QPushButton* add_turret_button;
    QPushButton* delete_turret_button;
    QPushButton* update_turret_button;
    QPushButton* save_button;
    QPushButton* filter_button;
    QPushButton* next_button;
    QPushButton* mylist_button;
    QPushButton* mylist_mvc_button;
    QPushButton* undo_button;
    QPushButton* redo_button;
    QComboBox* mode_choice;
    QLabel* label_next;
    QChart *chart;
    QBarSeries *series;
    QValueAxis *axisY;
    QTabWidget *tab_widget;
    TurretsTableView *myview;

    void populate_turret_list();
    void populate_filtered_list(const std::string& size, int parts);
    void build_chart();
    void add_turret_button_handler();
    void delete_turret_button_handler();
    void update_turret_button_handler();
    void save_turret_button_handler();
    void filter_turret_button_handler();
    void next_button_handler();
    void undo_button_handler();
    void redo_button_handler();
    void set_mode_handler();
    int get_selected_index();
    void list_item_changed();
    void reset_everything();
    void connect_signals_and_slots();
public:
    explicit GUI(Service &_service);
};


#endif //IMRE5_GUI_H
