package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Student;
import util.CrudUtil;
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
    public Button btnSave;
    public TextField txtSearch;

    public void initialize() {
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("email"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("nic"));
        try {
            loadAllStudents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                txtId.setText(newValue.getId());
                txtName.setText(newValue.getName());
                txtEmail.setText(newValue.getEmail());
                txtContact.setText(newValue.getEmail());
                txtAddress.setText(newValue.getAddress());
                txtNic.setText(newValue.getNic());
                btnSave.setText("Update");
            }
        });
    }

    private void loadAllStudents() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Student");
        ResultSet result = stm.executeQuery();

        ObservableList<StudentTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
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

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (btnSave.getText().equals("Save")){
            Student s = new Student(txtId.getText(), txtName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNic.getText());
            try {
                if (CrudUtil.execute("INSERT INTO Student VALUES (?,?,?,?,?,?)", s.getId(), s.getName(), s.getEmail(), s.getContact(), s.getAddress(), s.getNic())) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }else {
            Student s = new Student(txtId.getText(), txtName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNic.getText());
            try {
                if (CrudUtil.execute("UPDATE Student SET student_name=?, email=?, contact=?, address=?, nic=? WHERE student_id = ?", s.getName(), s.getEmail(), s.getContact(), s.getAddress(), s.getNic(),s.getId())) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Updated!..").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

        try {
            loadAllStudents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try{
            if (CrudUtil.execute("DELETE FROM Student WHERE student_id=?",txtId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }

        }catch (ClassNotFoundException | SQLException e){
        }
        try {
            loadAllStudents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtSearchOnAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        if (txtSearch.getText().equals("")){
            loadAllStudents();
            return;
        }
        PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Student WHERE student_id LIKE '%"+txtSearch.getText()+"%'");
        ResultSet result = stm.executeQuery();

        ObservableList<StudentTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
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
