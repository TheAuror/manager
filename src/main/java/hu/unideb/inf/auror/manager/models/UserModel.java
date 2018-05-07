package hu.unideb.inf.auror.manager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class UserModel {
    @Id
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String password;

    public UserModel() {

    }


}
