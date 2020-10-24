//
// Created by jack on 5/8/20.
//

#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QFormLayout>
#include <QtWidgets/QMessageBox>
#include <QDebug>
#include <sstream>
#include <Utilities/SettingsParser.h>
#include <QtWidgets/QShortcut>
#include <QtWidgets/QHeaderView>
#include "Utilities/Utils.h"
#include "GUI.h"

GUI::GUI(Service &_service) : service{_service} {
    this->setWindowTitle("Awesome App");

    // Overall app setting
    this->tab_widget = new QTabWidget;  //tab widget which contains widgets
    auto* parent_layout = new QVBoxLayout{this};    //The big layout which contains just the tab widget
    parent_layout->addWidget(this->tab_widget);   //Add the tab widget to the parent layout

    // General layout
    auto* general_window = new QWidget;
    auto* layout = new QHBoxLayout{general_window}; //general layout
    this->tab_widget->addTab(general_window, "General");  //Add general window to tab widget

    // Left side
    auto* left_side = new QWidget;
    layout->addWidget(left_side);
    auto* leftVbox = new QVBoxLayout(left_side);

    // List
    auto* turret_list_name = new QLabel{"Turrets list"};
    leftVbox->addWidget(turret_list_name);
    this->turrets_list = new QListWidget;
    this->turrets_list->setSelectionMode(QAbstractItemView::SingleSelection);
    leftVbox->addWidget(this->turrets_list);

    // Fields
    auto* turret_data_widget = new QWidget;
    auto* fields_form = new QFormLayout{turret_data_widget};
    leftVbox->addWidget(turret_data_widget);

    this->location_edit = new QLineEdit;
    auto* location_label = new QLabel("Location:");
    fields_form->addRow(location_label, this->location_edit);

    this->size_edit = new QLineEdit;
    auto* size_label = new QLabel("Size:");
    fields_form->addRow(size_label, this->size_edit);

    this->aura_level_edit = new QLineEdit;
    auto* aura_level_label = new QLabel("Aura level:");
    fields_form->addRow(aura_level_label, this->aura_level_edit);

    this->separate_parts_edit = new QLineEdit;
    auto* separate_parts_label = new QLabel("Separate parts:");
    fields_form->addRow(separate_parts_label, this->separate_parts_edit);

    this->vision_edit = new QLineEdit;
    auto* vision_label = new QLabel("Vision:");
    fields_form->addRow(vision_label, this->vision_edit);

    // Modes
    this->mode_choice = new QComboBox;
    mode_choice->addItem("Mode A");
    mode_choice->addItem("Mode B");
    leftVbox->addWidget(mode_choice);
    this->set_mode_handler();

    // Paths
    if (this->service.is_repo_file() || this->service.is_saved_file()){
        auto* paths_widget = new QWidget;
        auto* path_form = new QFormLayout{paths_widget};
        leftVbox->addWidget(paths_widget);

        QString default_file_location = SettingsParser::get_main().c_str();
        QString default_mylist_location = SettingsParser::get_mylist().c_str();
        if (this->service.is_repo_file()){
            this->file_location_edit = new QLineEdit(default_file_location);
            auto* file_label = new QLabel("File location:");
            path_form->addRow(file_label, this->file_location_edit);
            this->service.set_file_name_main_repository(default_file_location.toStdString());
        }
        if (this->service.is_saved_file()){
            this->mylist_location_edit = new QLineEdit(default_mylist_location);
            auto* mylist_label = new QLabel("Mylist location:");
            path_form->addRow(mylist_label, this->mylist_location_edit);
            this->service.set_file_name_saved_turrets_repository(default_mylist_location.toStdString());
        }
    }

    // Right side
    auto* right_side = new QWidget;
    layout->addWidget(right_side);
    auto* rightVbox = new QVBoxLayout(right_side);

    // Filtered list
    auto* filtered_list_name = new QLabel{"Filtered turrets list"};
    rightVbox->addWidget(filtered_list_name);
    this->filtered_list = new QListWidget;
    this->filtered_list->setSelectionMode(QAbstractItemView::SingleSelection);
    rightVbox->addWidget(this->filtered_list);

    // Buttons
    if (this->service.is_saved_file()){
        this->mylist_button = new QPushButton{"Mylist external app"};
        rightVbox->addWidget(this->mylist_button);
    }else this->mylist_button = nullptr;

    auto* buttons_layout = new QHBoxLayout;
    this->add_turret_button = new QPushButton("Add turret");
    this->delete_turret_button = new QPushButton("Delete turret");
    this->update_turret_button = new QPushButton("Update turret");
    buttons_layout->addWidget(this->add_turret_button);
    buttons_layout->addWidget(this->delete_turret_button);
    buttons_layout->addWidget(this->update_turret_button);
    rightVbox->addLayout(buttons_layout);

    // Save and filter button
    auto* other_buttons_layout = new QHBoxLayout;
    this->save_button = new QPushButton("Save turret");
    this->filter_button = new QPushButton("Filter turrets");
    this->next_button = new QPushButton("Next turret");
    other_buttons_layout->addWidget(this->save_button);
    other_buttons_layout->addWidget(this->filter_button);
    other_buttons_layout->addWidget(this->next_button);
    rightVbox->addLayout(other_buttons_layout);

    // Undo and Redo buttons
    auto* last_buttons_layout = new QHBoxLayout;
    this->undo_button = new QPushButton{"Undo"};
    this->redo_button = new QPushButton{"Redo"};
    this->mylist_mvc_button = new QPushButton{"Mylist MVC"};
    last_buttons_layout->addWidget(this->undo_button);
    last_buttons_layout->addWidget(this->redo_button);
    last_buttons_layout->addWidget(this->mylist_mvc_button);
    rightVbox->addLayout(last_buttons_layout);

    // Label next
    auto* next_widget = new QWidget;
    rightVbox->addWidget(next_widget);
    auto* next_layout = new QHBoxLayout{next_widget};
    this->label_next = new QLabel{"Press next:"};
    next_layout->addWidget(this->label_next);

    // Chart layout
    auto* chart_window = new QWidget;
    auto* chart_layout = new QVBoxLayout{chart_window};
    this->tab_widget->addTab(chart_window, "Chart");

    // Chart business
    this->chart = new QChart;
    auto* chartView = new QChartView(this->chart);
    chart_layout->addWidget(chartView);
    this->series = new QBarSeries();
    this->chart->addSeries(this->series);
    this->chart->setTitle("Turrets chart");
    this->chart->setAnimationOptions(QChart::SeriesAnimations);
    chart->legend()->setVisible(true);
    chart->legend()->setAlignment(Qt::AlignBottom);
    QStringList categories;
    categories << "Aura level" << "Parts";
    auto *axisX = new QBarCategoryAxis;
    axisX->append(categories);
    this->chart->addAxis(axisX, Qt::AlignBottom);
    this->series->attachAxis(axisX);
    this->axisY = new QValueAxis;
    this->axisY->setRange(0,20);
    chart->addAxis(axisY, Qt::AlignLeft);
    this->series->attachAxis(this->axisY);
    this->axisY->setRange(0,200);

    // Rest of the stuff like connecting buttons
    this->myview = new TurretsTableView{this->service};
    this->connect_signals_and_slots();
    this->populate_turret_list();
}

void GUI::populate_turret_list() {
    if (this->turrets_list->count() > 0)
        this->turrets_list->clear();
    for (const auto& turret : this->service.get_turret_list()){
        QString item_in_list = QString::fromStdString(turret.get_location());
        auto* item = new QListWidgetItem{item_in_list};
        this->turrets_list->addItem(item);
    }
    if (this->turrets_list->count() > 0)
        this->turrets_list->setCurrentRow(this->turrets_list->count()-1);
}

void GUI::populate_filtered_list(const std::string& size, int parts) {
    auto filtered_vector = this->service.get_turret_list_by_size_by_parts(size, parts);
    if (this->filtered_list->count() > 0)
        this->filtered_list->clear();
    for (const auto& turret : filtered_vector){
        QString item_in_list = QString::fromStdString(turret.get_location());
        auto* item = new QListWidgetItem{item_in_list};
        this->filtered_list->addItem(item);
    }
    if (this->filtered_list->count() > 0)
        this->filtered_list->setCurrentRow(0);
}

void GUI::build_chart()
{
    this->series->clear();
    int mx = 0;
    for (const auto& turret : this->service.get_turret_list()){
        mx = std::max(mx, std::max(turret.get_aura_level(), turret.get_separate_parts()));
        auto* set = new QBarSet(turret.get_location().c_str());
        *set << turret.get_aura_level() << turret.get_separate_parts();
        this->series->append(set);
    }
    this->axisY->setRange(0, mx);
}

void GUI::connect_signals_and_slots() {
    connect(this->add_turret_button, &QPushButton::clicked, this, &GUI::add_turret_button_handler);
    connect(this->delete_turret_button, &QPushButton::clicked, this, &GUI::delete_turret_button_handler);
    connect(this->update_turret_button, &QPushButton::clicked, this, &GUI::update_turret_button_handler);
    connect(this->save_button, &QPushButton::clicked, this, &GUI::save_turret_button_handler);
    connect(this->filter_button, &QPushButton::clicked, this, &GUI::filter_turret_button_handler);
    connect(this->next_button, &QPushButton::clicked, this, &GUI::next_button_handler);
    if (this->mylist_button != nullptr) {
        connect(this->mylist_button, &QPushButton::clicked, [this]() {
            std::string file = this->service.get_file_name_saved_turrets_repository();
            std::string command_to_execute;
            if (termination(file) == "html") {
                command_to_execute = "firefox ";
                command_to_execute += file;
                system(command_to_execute.c_str());
            } else if (termination(file) == "csv") {
                command_to_execute = "libreoffice --calc ";
                command_to_execute += file;
                system(command_to_execute.c_str());
            } else if (termination(file) == "sql") {
                command_to_execute = "sqlitebrowser ";
                command_to_execute += file;
                system(command_to_execute.c_str());
            } else {
                command_to_execute = "subl ";
                command_to_execute += file;
                system(command_to_execute.c_str());
            }
        });
    }
    connect(this->mylist_mvc_button, &QPushButton::clicked, [this](){
        auto *window = new QWidget;
        auto* tableView = new QTableView;
        tableView->setModel(this->myview);
        tableView->horizontalHeader()->setSectionResizeMode(QHeaderView::Stretch);
        window->setMinimumWidth(tableView->width());
        auto* Vbox = new QVBoxLayout{window};
        Vbox->addWidget(tableView);
        window->show();
    });
    connect(this->turrets_list, &QListWidget::itemSelectionChanged, [this]() {this->list_item_changed();});
    connect(this->mode_choice, static_cast<void(QComboBox::*)(int)>(&QComboBox::currentIndexChanged), this, &GUI::set_mode_handler);
    if (this->service.is_repo_file())
        connect(this->file_location_edit, &QLineEdit::returnPressed, [this](){
            this->service.set_file_name_main_repository(this->file_location_edit->text().toStdString());
            SettingsParser::set_settings(this->service);
            this->reset_everything();
        });
    if (this->service.is_saved_file())
        connect(this->mylist_location_edit, &QLineEdit::returnPressed, [this](){
            this->service.set_file_name_saved_turrets_repository(this->mylist_location_edit->text().toStdString());
            SettingsParser::set_settings(this->service);
            this->reset_everything();
            this->myview->propagate();
        });
    connect(this->tab_widget, &QTabWidget::currentChanged, [this](){this->build_chart();});
    connect(this->undo_button, &QPushButton::clicked, [this](){
        this->undo_button_handler();
    });
    connect(this->redo_button, &QPushButton::clicked, [this](){
        this->redo_button_handler();
    });
    auto undo_sc = new QShortcut(QKeySequence(Qt::CTRL + Qt::Key_Z), this);
    connect(undo_sc, &QShortcut::activated, [this](){
        this->undo_button_handler();
    });
    auto redo_sc = new QShortcut(QKeySequence(Qt::CTRL + Qt::Key_Y), this);
    connect(redo_sc, &QShortcut::activated, [this](){
        this->redo_button_handler();
    });
}

void GUI::undo_button_handler() {
    try{
        this->service.undo();
        this->populate_turret_list();
    } catch (std::exception &e){
        QMessageBox::critical(this, "Error", e.what());
    }
    this->myview->propagate();
}

void GUI::redo_button_handler(){
    try{
        this->service.redo();
        this->populate_turret_list();
    } catch (std::exception &e){
        QMessageBox::critical(this, "Error", e.what());
    }
    this->myview->propagate();
}

void GUI::set_mode_handler() {
    if (this->mode_choice->currentText() == "Mode A")
        this->service.set_mode("A");
    else if (this->mode_choice->currentText() == "Mode B")
        this->service.set_mode("B");
}

void GUI::add_turret_button_handler() {
    QString location = this->location_edit->text();
    QString size = this->size_edit->text();
    QString aura_level = this->aura_level_edit->text();
    QString separate_parts = this->separate_parts_edit->text();
    QString vision = this->vision_edit->text();
    for (auto character : aura_level.toStdString())
        if (character > '9' || character < '0'){
            QMessageBox::critical(this, "Error", "Aura level is not a number!");
            return;
        }
    for (auto character : separate_parts.toStdString())
        if (character > '9' || character < '0'){
            QMessageBox::critical(this, "Error", "Separate parts is not a number!");
            return;
        }
    try{
        this->service.add(location.toStdString(),
                          size.toStdString(),
                          string_to_int(aura_level.toStdString()),
                          string_to_int(separate_parts.toStdString()),
                          vision.toStdString());
        this->populate_turret_list();
    }catch (std::exception &e){
        QMessageBox::critical(this, "Error", e.what());
    }
}

void GUI::delete_turret_button_handler() {
    QString location = this->location_edit->text();
    try{
        this->service.remove(location.toStdString());
        this->populate_turret_list();
    }catch (std::exception &e){
        QMessageBox::critical(this, "Error", e.what());
    }
    this->myview->propagate();
}

void GUI::update_turret_button_handler() {
    QString location = this->location_edit->text();
    QString size = this->size_edit->text();
    QString aura_level = this->aura_level_edit->text();
    QString separate_parts = this->separate_parts_edit->text();
    QString vision = this->vision_edit->text();
    try{
        this->service.update(location.toStdString(),
                          size.toStdString(),
                          string_to_int(aura_level.toStdString()),
                          string_to_int(separate_parts.toStdString()),
                          vision.toStdString());
        this->populate_turret_list();
    }catch (std::exception &e){
        QMessageBox::critical(this, "Error", e.what());
    }
    this->myview->propagate();
}

void GUI::filter_turret_button_handler() {
    QString size = this->size_edit->text();
    QString separate_parts = this->separate_parts_edit->text();
    for (auto character : separate_parts.toStdString())
        if (character > '9' || character < '0'){
            QMessageBox::critical(this, "Error", "Separate parts is not a number!");
            return;
        }
    try{
        this->populate_filtered_list(size.toStdString(), string_to_int(separate_parts.toStdString()));
    }catch (std::exception &e){
        QMessageBox::critical(this, "Error", e.what());
    }
}

void GUI::save_turret_button_handler() {
    QString location = this->location_edit->text();
    try{
        this->service.save_turret(location.toStdString());
    }catch (std::exception &e){
        QMessageBox::critical(this, "Error", e.what());
    }
    this->myview->propagate();
}

void GUI::next_button_handler() {
    try{
        std::stringstream ss;
        Turret turret = this->service.next_turret();
        ss << turret;
        this->label_next->setText(QString::fromStdString(ss.str()));
    }catch (std::exception &e){
        this->label_next->setText(e.what());
    }
}

int GUI::get_selected_index()
{
    if (this->turrets_list->count() == 0)
        return -1;

    // get selected index
    QModelIndexList index = this->turrets_list->selectionModel()->selectedIndexes();
    if (index.empty())
    {
        this->location_edit->clear();
        this->size_edit->clear();
        this->aura_level_edit->clear();
        this->separate_parts_edit->clear();
        this->vision_edit->clear();
        return -1;
    }

    int idx = index.at(0).row();
    return idx;
}

void GUI::list_item_changed()
{
    int idx = this->get_selected_index();
    if (idx == -1)
        return;

    Turret turret = this->service.get_turret_list()[idx];

    this->location_edit->setText(QString::fromStdString(turret.get_location()));
    this->size_edit->setText(QString::fromStdString(turret.get_size()));
    this->aura_level_edit->setText(QString::fromStdString(std::to_string(turret.get_aura_level())));
    this->separate_parts_edit->setText(QString::fromStdString(std::to_string(turret.get_separate_parts())));
    this->vision_edit->setText(QString::fromStdString(turret.get_vision()));
}

void GUI::reset_everything() {
    this->populate_turret_list();
    this->filtered_list->clear();
    this->label_next->clear();
}
