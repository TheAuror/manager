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
import hu.unideb.inf.auror.manager.Services.StatisticsService;
import hu.unideb.inf.auror.manager.models.FinancialRecordModel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the main view of the application.
 */
public class MainController implements Initializable {
    /**
     * SLF4J Logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(MainController.class);

    /**
     * Contains the style used on the records that are an income.
     */
    private static String incomeStyle = "-fx-background-color:lightgreen";
    /**
     * Contains the style used on the records that are an expense.
     */
    private static String expenseStyle = "-fx-background-color:indianred";


    /**
     * JavaFX TableView.
     */
    public TableView tableView;
    /**
     * JavaFX DatePicker.
     */
    public DatePicker dateTimeBegin;
    /**
     * JavaFX DatePicker.
     */
    public DatePicker dateTimeEnd;
    /**
     * JavaFX Label.
     * Displays the sum of the incomes.
     */
    public Label sumOfIncomesLabel;
    /**
     * JavaFX Label.
     * Displays the sum of the expenses.
     */
    public Label sumOfExpensesLabel;
    /**
     * JavaFX Label.
     * Displays if the given time period is in surplus or deficit.
     */
    public Label surplusOrDeficitLabel;
    /**
     * JavaFX Label.
     * Displays the sum of the records.
     */
    public Label totalSumLabel;
    /**
     * JavaFX Label.
     * Displays the average of the incomes.
     */
    public Label averageOfIncomesLabel;
    /**
     * JavaFX Label.
     * Displays the average of the incomes.
     */
    public Label averageOfExpensesLabel;
    /**
     * JavaFX Label.
     * Displays the average of the records.
     */
    public Label averageLabel;

    /**
     * Data access object for the financial records.
     */
    private FinancialRecordDAO dao = FinancialRecordDAO.getInstance();
    /**
     * An ObservableList that contains the records for the TableView.
     */
    private ObservableList<FinancialRecordModel> records = FXCollections.observableArrayList(dao.GetAllRecords());
    /**
     * Timeline created for the animation.
     */
    private Timeline timeline;

    /**
     * Closes the application.
     */
    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Reloads the records list's records from the database.
     */
    @FXML
    private void refreshData() {
        records.setAll(dao.GetRecordsForUser().stream()
                .filter(e -> (e.getDateOfCreation().isAfter(getBeginDate()) ||
                        e.getDateOfCreation().isEqual(getBeginDate())) &&
                        (e.getDateOfCreation().isBefore(getEndDate()) ||
                                e.getDateOfCreation().isEqual(getEndDate())))
                .collect(Collectors.toCollection(ArrayList::new)));
        calculateStatistics();
        timeline.play();
    }

    /**
     * Sets the corresponding statistical values to its labels.
     */
    private void calculateStatistics() {
        sumOfIncomesLabel.setText(StatisticsService.getSumOfIncome(records) + " Ft");
        sumOfExpensesLabel.setText(StatisticsService.getSumOfExpenses(records) + " Ft");
        double totalSum = StatisticsService.getTotalSum(records);
        totalSumLabel.setText(totalSum + " Ft");
        if(totalSum > 0)
        {
            surplusOrDeficitLabel.setText("Többlet:");
            totalSumLabel.setTextFill(Paint.valueOf("#20ab32"));
        }
        if(totalSum < 0)
        {
            surplusOrDeficitLabel.setText("Hiány:");
            totalSumLabel.setTextFill(Paint.valueOf("#ee0000"));
        }
        surplusOrDeficitLabel.setVisible(totalSum != 0);
        totalSumLabel.setVisible(totalSum != 0);
        averageOfIncomesLabel.setText(StatisticsService.getAverageOfIncome(records)+ " Ft");
        averageOfExpensesLabel.setText(StatisticsService.getAverageOfExpenses(records) + " Ft");
        averageLabel.setText(StatisticsService.getAverage(records)+ " Ft");
    }

    /**
     * @return Returns a LocalDateTime based on the dateTimeBegin DatePicker's value.
     */
    private LocalDateTime getBeginDate() {
        return dateTimeBegin.getValue() == null ? LocalDateTime.MIN : dateTimeBegin.getValue().atStartOfDay();
    }

    /**
     * @return Returns a LocalDateTime based on the dateTimeEnd DatePicker's value.
     */
    private LocalDateTime getEndDate() {
        return dateTimeEnd.getValue() == null ? LocalDateTime.MAX : dateTimeEnd.getValue().atStartOfDay();
    }

    /**
     * Configures the TableView for the window.
     * Creates the columns for the TableView.
     * Creates the CellValueFactories for the Columns.
     * Sets the records list as the data source of the table.
     */
    private void configureTableView() {
        tableView.setEditable(false);
        tableView.setItems(records);
        TableColumn nameColumn = new TableColumn("Tétel neve");
        nameColumn.setCellValueFactory(new PropertyValueFactory<FinancialRecordModel, String>("name"));
        TableColumn amountColumn = new TableColumn("Összeg");
        amountColumn.setCellValueFactory(new PropertyValueFactory<FinancialRecordModel, String>("amount"));
        TableColumn dateColumn = new TableColumn("Dátum");
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
        tableView.setRowFactory(e -> new TableRow<FinancialRecordModel>() {
            @Override
            protected void updateItem(FinancialRecordModel record, boolean empty) {
                super.updateItem(record, empty);
                if (record == null) {
                    setStyle("-fx-background-color:white");
                    return;
                }
                if (record.getIsIncome()) {
                    setTextFill(Color.GREEN);
                    setStyle(incomeStyle);
                } else {
                    setTextFill(Color.RED);
                    setStyle(expenseStyle);
                }
            }
        });

        tableView.getColumns().addAll(nameColumn, amountColumn, dateColumn);
        createAnimation();
    }

    /**
     * Creates the timeline used in the table flip animation.
     */
    private void createAnimation() {
        timeline = new Timeline();

        for (int i = 1; i < 360; i++) {
            timeline.getKeyFrames().add(new KeyFrame(new Duration(1 / i * 600), new KeyValue(tableView.rotateProperty(), i)));
        }
    }

    /**
     * Initializes the controller.
     *
     * @param location  <code>URL</code>
     * @param resources <code>ResourceBundle</code>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTableView();
        refreshData();
    }

    /**
     * Creates a new record.
     * Opens the editor dialog so the user can fill out the properties for the new record.
     */
    public void createNewRecord() {
        logger.info("Creating new record...");
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/EditorView.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Új rekord felvétele");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(e -> {
                refreshData();
            });
            stage.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * Edits the selected record.
     * Opens the editor dialog and sets the selected record as edited record in it's controller.
     */
    public void editRecord() {
        logger.info("Editing selected record...");
        Parent root;
        FinancialRecordModel record = (FinancialRecordModel) tableView.getSelectionModel().getSelectedItem();
        if (record == null) {
            logger.debug("No record is selected!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/fxml/EditorView.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Rekord szerkesztése");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            loader.<EditorController>getController().setRecord(record);
            stage.setOnHidden(e -> refreshData());
            stage.show();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * Deletes the selected record, and refreshes the table.
     */
    public void deleteRecord() {
        logger.info("Deleting selected record...");
        FinancialRecordModel record = (FinancialRecordModel) tableView.getSelectionModel().getSelectedItem();
        if (record == null) {
            logger.debug("No record is selected!");
            return;
        }
        FinancialRecordDAO.getInstance().Delete(record);
        refreshData();
    }
}
