//
// Created by jack on 6/13/20.
//

#include <QtWidgets/QListView>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QMessageBox>
#include <qdebug.h>
#include <fstream>
#include <QtWidgets/QTextEdit>
#include "WriterGUI.h"

WriterGUI::WriterGUI(Service &_service, Screenwriter &_writer, WriterModel *_model) : service{_service}, writer{_writer}, model{_model}{
    this->setWindowTitle(writer.name.c_str());
    layout = new QVBoxLayout{this};
    auto ideas = new QListView{};
    layout->addWidget(ideas);
    ideas->setModel(this->model);
    auto le_description = new QLineEdit{"Description"};
    auto le_act = new QLineEdit{"Act"};
    layout->addWidget(le_description);
    layout->addWidget(le_act);
    auto btn_add = new QPushButton{"Add idea"};
    layout->addWidget(btn_add);
    connect(btn_add, &QPushButton::clicked, [this, le_description, le_act](){
        Idea i;
        i.description = le_description->text().toStdString();
        i.act = le_act->text().toInt();
        i.status = "proposed";
        i.creator = this->writer.name;
        try{
            this->service.add_idea(i);
            this->model->propagate();
        }catch (std::runtime_error &re){
            QMessageBox::critical(this, "Error", re.what());
        }
    });
    if (this->writer.expertise == "Senior"){
        auto btn_accept = new QPushButton{"accept highlighted idea"};
        layout->addWidget(btn_accept);
        connect(btn_accept, &QPushButton::clicked, [this, ideas](){
            auto row = ideas->currentIndex().row();
            if (row >= 0 && row < this->service.get_ideas().size())
                Service::update_idea(this->service.get_ideas()[row], "accepted");
            this->model->propagate();
        });
    }
    auto btn_plot = new QPushButton{"Save plot"};
    layout->addWidget(btn_plot);
    connect(btn_plot, &QPushButton::clicked, [this](){
        std::unordered_map<int, std::vector<Idea>> mp;
        for (const auto& x : this->service.get_ideas())
            mp[x.act].push_back(x);
        std::ofstream fout (this->writer.name);
        for (int i = 1; i <= 3; ++i){
            fout << "Act " << i << "\n";
            for (const auto& x : mp[i])
                if (x.status == "accepted")
                    fout << x.description << " (" << x.creator << ")\n";
        }
        fout.close();
    });
    bool hasAccepted = false;
    for (const auto& x : this->service.get_ideas())
        if (x.status == "accepted" && x.creator == this->writer.name){
            hasAccepted = true;
            break;
        }
    if (hasAccepted){
        auto btn_develop = new QPushButton{"Develop"};
        this->layout->addWidget(btn_develop);
        connect(btn_develop, &QPushButton::clicked, [this, btn_develop](){
            btn_develop->setEnabled(false);
            for (const auto& x : this->service.get_ideas())
                if (x.creator == this->writer.name && x.status == "accepted"){
                    auto te = new QTextEdit{x.description.c_str()};
                    auto btn_save_developed = new QPushButton{"Save"};
                    connect(btn_save_developed, &QPushButton::clicked, [this, te, x](){
                        std::ofstream fout (this->writer.name + " " + x.description);
                        fout << te->toPlainText().toStdString();
                        fout.close();
                    });
                    this->layout->addWidget(te);
                    this->layout->addWidget(btn_save_developed);
                }
        });
    }
}
