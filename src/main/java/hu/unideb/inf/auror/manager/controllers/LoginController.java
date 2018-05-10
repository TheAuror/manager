package hu.unideb.inf.auror.manager.controllers;

import hu.unideb.inf.auror.manager.DAOs.UserDAO;
import hu.unideb.inf.auror.manager.Services.PasswordService;
import hu.unideb.inf.auror.manager.models.UserModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the login dialog.
 */
public class LoginController implements Initializable {
    /**
     * SLF4J Logger.
     */
    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
    /**
     * JavaFX TextField.
     */
    public TextField usernameTextField;
    /**
     * JavaFX PasswordField.
     */
    public PasswordField passwordField;
    /**
     * JavaFX Label.
     */
    public Label errorLabel;

    /**
     * Checks the given username and password in the database.
     *
     * @param actionEvent <code>ActionEvent</code>
     */
    public void Login(ActionEvent actionEvent) {
        if(!CheckTextFields())
            return;
        List<UserModel> users = UserDAO.getInstance().GetUsers();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        Optional<UserModel> tmpUser = users.stream().filter(e -> e.getName().equals(username)).findFirst();
        if (!tmpUser.isPresent()) {
            logger.info("Wrong username");
            errorLabel.setText("Hibás felhasználónév!");
            errorLabel.setVisible(true);
            return;
        }
        UserModel user = tmpUser.get();
        if (PasswordService.comparePassword(user.getPassword(), password)) {
            UserDAO.getInstance().SetCurrentUser(user);
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } else {
            logger.info("Wrong password");
            errorLabel.setText("Hibás jelszó!");
            errorLabel.setVisible(true);
            return;
        }
        OpenMainView();
    }

    /**
     * Creates a new user model with the text field's texts.
     */
    public void Register() {
        UserDAO userDAO = UserDAO.getInstance();
        CheckTextFields();
        UserModel user = new UserModel();
        user.setName(usernameTextField.getText());
        user.setPassword(passwordField.getText());
        if (userDAO.GetUsers().stream().anyMatch(e -> e.getName().equals(user.getName()))) {
            logger.info("Username already taken!");
            errorLabel.setText("A felhasználónév már foglalt!");
            errorLabel.setVisible(true);
            return;
        }
        userDAO.Save(user);
        OpenMainView();
    }

    /**
     * Checks the text fields.
     *
     * @return Returns true if all the fields fulfill the requirements.
     */
    private boolean CheckTextFields() {
        if (usernameTextField.getText().isEmpty()) {
            errorLabel.setText("Adjon meg egy felhasználó nevet!");
            errorLabel.setVisible(true);
            return false;
        }
        if (passwordField.getText().isEmpty()) {
            errorLabel.setText("Adjon meg egy jelszót!");
            errorLabel.setVisible(true);
            return false;
        }
        if (passwordField.getText().length() < 4) {
            errorLabel.setText("Túl rövid jelszó!");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    /**
     * Creates the main view and opens it.
     */
    private void OpenMainView() {
        Parent root = null;
        try {
            root = FXMLLoader.load(LoginController.class.getResource("/fxml/MainView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Financial manager");
        stage.setOnCloseRequest(e ->
        {
            Platform.exit();
            System.exit(0);
        });
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the controller.
     * Sets the errorLabel's visibility to false.
     *
     * @param location  <code>URL</code>
     * @param resources <code>ResourceBundle</code>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);
    }
}
