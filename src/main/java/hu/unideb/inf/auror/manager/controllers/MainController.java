package hu.unideb.inf.auror.manager.controllers;

/*-
 * #%L
 * Manager
 * %%
 * Copyright (C) 2016 - 2018 Faculty of Informatics, University of Debrecen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import hu.unideb.inf.auror.manager.DAOs.FinancialRecordDAO;
import hu.unideb.inf.auror.manager.models.FinancialRecordModel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    @FXML
    private TableView tableView;
    @FXML
    private DatePicker dateTimeBegin;
    @FXML
    private DatePicker dateTimeEnd;

    private FinancialRecordDAO dao = FinancialRecordDAO.getInstance();
    private ObservableList<FinancialRecordModel> records = FXCollections.observableArrayList(dao.GetAllRecord());
    private Timeline timeline;

    public void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void refreshData() {
        records.setAll(dao.GetAllRecord().stream()
                .filter(e -> (e.getDateOfCreation().isAfter(getBeginDate()) ||
                        e.getDateOfCreation().isEqual(getBeginDate())) &&
                        (e.getDateOfCreation().isBefore(getEndDate()) ||
                                e.getDateOfCreation().isEqual(getEndDate())))
                .collect(Collectors.toCollection(ArrayList::new)));
        timeline.play();
    }

    private LocalDateTime getBeginDate() {
        return dateTimeBegin.getValue() == null ? LocalDateTime.MIN : dateTimeBegin.getValue().atStartOfDay();
    }

    private LocalDateTime getEndDate() {
        return dateTimeEnd.getValue() == null ? LocalDateTime.MAX : dateTimeEnd.getValue().atStartOfDay();
    }

    private void configureTableView() {
        tableView.setEditable(false);
        tableView.setItems(records);
        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<FinancialRecordModel, String>("id"));
        TableColumn isIncomeColumn = new TableColumn("isIncome");
        isIncomeColumn.setCellValueFactory(new PropertyValueFactory<FinancialRecordModel, Boolean>("isIncome"));
        TableColumn amountColumn = new TableColumn("amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<FinancialRecordModel, String>("amount"));
        TableColumn dateColumn = new TableColumn("date");
        dateColumn.setCellFactory(e -> {
            TableCell<FinancialRecordModel, LocalDateTime> cell = new TableCell<>() {
                private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        this.setText(item.format(formatter));
                    }
                }
            };

            return cell;
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<FinancialRecordModel, LocalDateTime>("dateOfCreation"));
        tableView.getColumns().addAll(idColumn, isIncomeColumn, amountColumn, dateColumn);
        createAnimation();
    }

    private void createAnimation() {
        timeline = new Timeline();
        for (int i = 1; i < 180; i++) {
            timeline.getKeyFrames().add(new KeyFrame(new Duration(1 / i * 400), new KeyValue(tableView.rotateProperty(), i)));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTableView();
    }
}
