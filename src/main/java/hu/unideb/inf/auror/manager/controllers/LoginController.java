package hu.unideb.inf.auror.manager.controllers;

import hu.unideb.inf.auror.manager.DAOs.UserDAO;
import hu.unideb.inf.auror.manager.models.UserModel;
import hu.unideb.inf.auror.manager.utilities.PasswordUtil;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
    public TextField usernameTextField;
    public PasswordField passwordField;
    public Label errorLabel;

    public void Login(ActionEvent actionEvent) {
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
        if (PasswordUtil.comparePassword(user.getPassword(), password)) {
            UserDAO.getInstance().SetCurrentUser(user);
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } else {
            logger.info("Wrong password");
            errorLabel.setText("Hibás felhasználónév!");
            errorLabel.setVisible(true);
        }
    }

    public void Register(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);
    }
}
