package hu.unideb.inf.auror.manager;

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
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Main class that contains the main method.
 */
public class Main extends Application {
    /**
     * SLF4J Logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Main method of the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates the login view.
     *
     * @param primaryStage Primary stage of the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        FinancialRecordDAO dao = FinancialRecordDAO.getInstance();
        logger.info("Main App start...");
        Parent root;
        try {
            root = FXMLLoader.load(Main.class.getResource("/fxml/LoginView.fxml"));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return;
        }
        primaryStage.setTitle((dao.IsInitialized() ? "Login" : "[Database Error]"));
        primaryStage.setOnCloseRequest(e ->
        {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setResizable(false);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
        logger.info("Showing stage...");
    }
}
