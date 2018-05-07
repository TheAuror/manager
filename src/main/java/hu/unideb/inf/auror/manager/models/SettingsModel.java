package hu.unideb.inf.auror.manager.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SETTINGS")
public class SettingsModel {
    @Id
    private int id;
    @OneToOne
    private UserModel user;
}
