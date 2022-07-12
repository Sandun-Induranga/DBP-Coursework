package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Student;
import view.tm.StudentTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainFormController {
    public TableView<StudentTM> tblStudent;
    public TextField txtId;
    public TextField txtName;
    public TextField txtEmail;
    public TextField txtContact;
    public TextField txtAddress;
    public TextField txtNic;

    public void initialize(){
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("email"));
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nic"));
        try {
            loadAllStudents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllStudents() throws SQLException, ClassNotFoundException {
        PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Student");
        ResultSet result = stm.executeQuery();

        ObservableList<StudentTM> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new StudentTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getString(5),
                            result.getString(6)
                    )
            );
        }
        tblStudent.setItems(obList);
    }
}
