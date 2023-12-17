package com.example.pruebasjavafx.controllers;

import com.example.pruebasjavafx.HelloApplication;
import com.example.pruebasjavafx.Sesion;
import com.example.pruebasjavafx.models.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class DetallesPersonaController implements Initializable {

    @FXML
    private TextField tvNombre;
    @FXML
    private TextField tvApellido;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private Spinner spEdad;
    @FXML
    private ComboBox<String> cbProfesion;
    @FXML
    private TextArea taObservaciones;

    //Para conocer la persona actual.
    private Persona personaActual;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Obtiene la persona actual guardada en Sesión.
        personaActual = Sesion.getPersona();

        //Inicializa el spinner con los datos de la persona que hay en Sesión.
        spEdad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, personaActual.getEdad(), 1));

        ObservableList<String> profesiones = FXCollections.observableArrayList();
        profesiones.addAll("Programador", "Cocinero", "Educador");

        cbProfesion.setItems(profesiones);
        cbProfesion.setValue(personaActual.getProfesion());

        tvNombre.setText(personaActual.getNombre());

        tvApellido.setText(personaActual.getApellido());

        taObservaciones.setText(personaActual.getObservaciones());

    }

    @Deprecated
    public void modificarPersona(ActionEvent actionEvent) {
        personaActual.setNombre(tvNombre.getText());
        personaActual.setApellido(tvApellido.getText());
        //Parseo para la fecha.
        LocalDate fechaSeleccionada = dpFecha.getValue(); // Obtener la fecha seleccionada del DatePicker
        Date fechaParseada = java.sql.Date.valueOf(fechaSeleccionada);
        personaActual.setFechaNacimiento(fechaParseada);
        personaActual.setEdad((Integer) spEdad.getValue());
        personaActual.setProfesion(cbProfesion.getSelectionModel().getSelectedItem());
        personaActual.setObservaciones(taObservaciones.getText());

        //Hay que meter en Sesión la nueva persona modificada.
        Sesion.setPersona(personaActual);

        //Hay que modificar también en la Sesión la lista de Personas.
        Sesion.getPersonas().set(Sesion.getPos(), Sesion.getPersona());

        //Alerta para informar de que los cambios fueron realizados con éxito.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Cambio realizado");
        alert.showAndWait();
        Sesion.setPos(null);

        HelloApplication.cargarFXML("hello-view.fxml");

    }

    @Deprecated
    public void eliminarPersona(ActionEvent actionEvent) {
        //Se elimina a la persona de la lista que hay en Sesión.
        System.out.println(Sesion.getPersonas().remove( (int) Sesion.getPos()));
        HelloApplication.cargarFXML("hello-view.fxml");
    }
}
