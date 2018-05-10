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
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.converter.DoubleStringConverter;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.time.Period;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the dialog, that allows the user to create or edit a <code>FinancialRecordModel</code>.
 */
public class EditorController implements Initializable {
    /**
     * Static string for "A year" in hungarian.
     */
    private static final String YEAR = "Egy év";
    /**
     * Static string for "A month" in hungarian.
     */
    private static final String MONTH = "Egy hónap";
    /**
     * Static string for "A week" in hungarian.
     */
    private static final String WEEK = "Egy hét";
    /**
     * Static string for "A day" in hungarian.
     */
    private static final String DAY = "Egy nap";
    /**
     * Regex for checking if a string is parsable to double based on JavaDoc.
     * https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#valueOf-java.lang.String-
     */
    private final String fpRegex =
            ("[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)" +
                    "(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit" +
                    "}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{D" +
                    "igit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(" +
                    "0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[" +
                    "pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

    /**
     * JavaFX TextField.
     * Contains the name of the record.
     */
    public TextField recordNameTextField;
    /**
     * JavaFX TextField.
     * Contains the amount of the record.
     */
    public TextField recordAmountTextField;
    /**
     * JavaFX CheckBox.
     * Checked if the record is income.
     */
    public CheckBox isIncomeCheckBox;
    /**
     * JavaFX DatePicker.
     * Contains the record's date of creation.
     */
    public DatePicker dateOfCreationPicker;
    /**
     * JavaFX ChoiceBox.
     * Contains the available Period choices.
     */
    public ChoiceBox<java.io.Serializable> intervalChoiceBox;
    /**
     * JavaFX CheckBox.
     * Checked if the record is periodical.
     */
    public CheckBox isRecurringCheckBox;
    /**
     * JavaFX Label.
     */
    public Label intervalLabel;
    /**
     * Currently edited record.
     */
    private FinancialRecordModel editedRecord;

    /**
     * @return Returns a <code>FinancialRecordModel</code> with the properties that the user gave.
     */
    private FinancialRecordModel getRecord() {
        if (editedRecord == null)
            editedRecord = new FinancialRecordModel();
        editedRecord.setName(recordNameTextField.getText());
        editedRecord.setAmount(Double.parseDouble(recordAmountTextField.getText()));
        editedRecord.setIsIncome(isIncomeCheckBox.isSelected());
        if (dateOfCreationPicker.getValue() != null)
            editedRecord.setDateOfCreation(dateOfCreationPicker.getValue().atStartOfDay());
        editedRecord.setPeriod(getPeriod());
        return editedRecord;
    }

    /**
     * Sets the dialogs TextFields and its other controls with
     * the corresponding property from the given record
     * so the user can edit it.
     *
     * @param record A <code>FinancialRecordModel</code>
     */
    void setRecord(FinancialRecordModel record) {
        recordNameTextField.setText(record.getName());
        recordAmountTextField.setText(String.valueOf(record.getAmount()));
        isIncomeCheckBox.setSelected(record.getIsIncome());
        dateOfCreationPicker.setValue(record.getDateOfCreation().toLocalDate());
        isRecurringCheckBox.setSelected(record.getIsRecurring());
        if (record.getIsRecurring()) {
            if (record.getPeriod().equals(Period.ofYears(1)))
                intervalChoiceBox.setValue(YEAR);
            if (record.getPeriod().equals(Period.ofMonths(1)))
                intervalChoiceBox.setValue(MONTH);
            if (record.getPeriod().equals(Period.ofWeeks(1)))
                intervalChoiceBox.setValue(WEEK);
            if (record.getPeriod().equals(Period.ofDays(1)))
                intervalChoiceBox.setValue(DAY);
            isRecurringChecked();
        }
        editedRecord = record;
    }

    /**
     * @return Returns Period according to the selected value.
     */
    @Nullable
    private Period getPeriod() {
        Period period = null;
        if (!isRecurringCheckBox.isSelected())
            return null;
        switch (intervalChoiceBox.getValue().toString()) {
            case YEAR:
                period = Period.ofYears(1);
                break;
            case MONTH:
                period = Period.ofMonths(1);
                break;
            case WEEK:
                period = Period.ofWeeks(1);
                break;
            case DAY:
                period = Period.ofDays(1);
                break;
        }
        return period;
    }

    /**
     * Initializes the scene.
     *
     * @param location  <code>URL</code>
     * @param resources <code>ResourceBundle</code>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBox();
        isRecurringChecked();
        initRecordAmountTextField();
    }

    /**
     * Adds the items to the intervalChoiceBox.
     */
    private void initChoiceBox() {
        intervalChoiceBox.getItems().add(YEAR);
        intervalChoiceBox.getItems().add(MONTH);
        intervalChoiceBox.getItems().add(WEEK);
        intervalChoiceBox.getItems().add(DAY);
    }

    /**
     * Checks if the recurringCheckBox is checked and sets the
     * intervalLabel's and the intervalChoiceBox's visibility based on said check.
     */
    public void isRecurringChecked() {
        if (isRecurringCheckBox.isSelected()) {
            intervalLabel.setVisible(true);
            intervalChoiceBox.setVisible(true);
        } else {
            intervalLabel.setVisible(false);
            intervalChoiceBox.setVisible(false);
        }
    }

    /**
     * Initializes the amountTextField.
     * Compiles the regex used for validation and creates a new
     * <code>TextFormatter</code> which applies it.
     */
    private void initRecordAmountTextField() {
        Pattern validDoubleText = Pattern.compile(fpRegex);
        TextFormatter<Double> textFormatter = new TextFormatter<>(new DoubleStringConverter(), 0.0,
                change -> {
                    String newText = change.getControlNewText();
                    if (validDoubleText.matcher(newText).matches()) {
                        return change;
                    } else return null;
                });
        recordAmountTextField.setTextFormatter(textFormatter);
    }

    /**
     * Hides the window.
     *
     * @param actionEvent <code>ActionEvent</code>
     */
    public void cancel(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * Checks if the gives values are valid then saves the record and hides the window.
     *
     * @param actionEvent <code>ActionEvent</code>
     */
    public void saveRecord(ActionEvent actionEvent) {
        if (recordNameTextField.getText().isEmpty()) {
            showAlertWindow("A tétel neve nem lehet üres!");
            return;
        }
        if (recordAmountTextField.getText() == null ||
                Double.parseDouble(recordAmountTextField.getText()) == 0) {
            showAlertWindow("Az összeg nem lehet üres vagy nulla!");
            return;
        }
        if (isRecurringCheckBox.isSelected() && intervalChoiceBox.getValue() == null) {
            showAlertWindow("Válasszon intervallumot!");
            return;
        }
        FinancialRecordDAO.getInstance().Save(getRecord());
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * Creates an alert window with the given error message.
     *
     * @param error Error message.
     */
    private void showAlertWindow(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hiba");
        alert.setHeaderText("Hibás vagy hiányzó adat");
        alert.setContentText(error);
        alert.showAndWait();
    }
}
