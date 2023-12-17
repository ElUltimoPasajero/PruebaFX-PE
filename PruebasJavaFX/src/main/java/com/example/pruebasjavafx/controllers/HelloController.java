package com.example.pruebasjavafx.controllers;

import com.example.pruebasjavafx.HelloApplication;
import com.example.pruebasjavafx.Sesion;
import com.example.pruebasjavafx.models.Persona;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField tvNombre;
    @FXML
    private TextField tvApellido;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private Spinner spEdad;
    @FXML
    private ComboBox<String> cbProfesion;
    @FXML
    private TextArea taObservaciones;
    @FXML
    private Button btAnhadir;
    @FXML
    private TableView<Persona> tvPersonas;

    //Primero tiene que ir la clase a la que pertenece y luego el tipo de dato.
    @FXML
    private TableColumn<Persona, String> cNombre;
    @FXML
    private TableColumn<Persona, String> cApellidos;

    //LocalDate es la clase que usa JavaFX para manejar las fechas.
    @FXML
    private TableColumn<Persona, String> cFechaNacimiento;
    @FXML
    private TableColumn<Persona, String> cEdad;
    @FXML
    private TableColumn<Persona, String> cProfesion;
    @FXML
    private TableColumn<Persona, String> cObservaciones;

    //Se crea el observable que servirá de intermediario.
    private ObservableList<Persona> observableListPersonas = FXCollections.observableArrayList();
    @FXML
    private ImageView ivImagen;

    //Hay que añadir la dependencia en el pom y abrir probablemente el module.info
    private MediaPlayer mediaPlayer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Mapea primero las columnas de la tabla.
        cNombre.setCellValueFactory( (fila) -> {
            String nombre = fila.getValue().getNombre();
            return new SimpleStringProperty( nombre );
        });
        cApellidos.setCellValueFactory( (fila) -> {
            String apellidos = fila.getValue().getApellido();
            return new SimpleStringProperty( apellidos );
        });
        cFechaNacimiento.setCellValueFactory( (fila) -> {
            String fechaNacimiento = String.valueOf(fila.getValue().getFechaNacimiento());
            return new SimpleStringProperty( fechaNacimiento );
        });
        cEdad.setCellValueFactory( (fila) -> {
            String edad = String.valueOf(fila.getValue().getEdad());
            return new SimpleStringProperty( edad );
        });
        cProfesion.setCellValueFactory( (fila) -> {
            String profesion = fila.getValue().getProfesion();
            return new SimpleStringProperty( profesion );
        });
        cObservaciones.setCellValueFactory( (fila) -> {
            String observaciones = fila.getValue().getObservaciones();
            return new SimpleStringProperty( observaciones );
        });

        if (Sesion.getPersonas().isEmpty()){
            ArrayList<Persona> personas = new ArrayList<>();

            //Para pasar de LocalDate a Date, ya que el constructor de la clase Persona tiene un Date.
            LocalDate fechaNacimientoLocal = LocalDate.of(2000, 4, 9);
            Date fechaNacimientoDate = java.sql.Date.valueOf(fechaNacimientoLocal); // Convertir LocalDate a Date
            personas.add(new Persona("Jorge", "Alarcón", fechaNacimientoDate, 23, "Programador", "Muy bueno en su trabajo"));
            Sesion.setPersonas(personas);
        }

        //Se rellena el Observable con lo que hay almacenado en Sesión.
        //Al estar vació se rellenará con la lista personas.
        observableListPersonas.addAll( Sesion.getPersonas() );

        tvPersonas.setItems(observableListPersonas);

        //Inicializar un combo.
        ObservableList<String> profesiones = FXCollections.observableArrayList();
        profesiones.addAll("Programador", "Cocinero", "Educador");

        cbProfesion.setItems(profesiones);
        cbProfesion.getSelectionModel().selectFirst();

        //Inicializar un spinner.
        spEdad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1));

        /*
         * Establece un listener para el evento de selección de una persona en la tabla de personas.
         * Cuando se selecciona una persona, actualiza el pedido almacenado en la sesión.
         */
        tvPersonas.getSelectionModel().selectedItemProperty().addListener((observableValue, persona, t1) -> {
            Sesion.setPersona(t1);
            tvNombre.setText(t1.getNombre());
            tvApellido.setText(t1.getApellido());
            spEdad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, t1.getEdad(), 1));
            cbProfesion.setValue(t1.getProfesion());
            taObservaciones.setText(t1.getObservaciones());
        });

        /*
         * Establece un evento para manejar el doble clic del ratón en la tabla de personas.
         * Cuando se hace doble clic en una persona, actualiza la persona almacenada en la sesión y carga los detalles de la persona.
         */
        tvPersonas.setOnMouseClicked(event -> {
            //Verifica si se ha realizado un doble clic con el botón primario del ratón.
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {

                //Obtiene la persona seleccionada.
                Persona personaSeleccionada = tvPersonas.getSelectionModel().getSelectedItem();
                if (personaSeleccionada != null) {
                    //Se actualiza la persona almacenada en la sesión.
                    Sesion.setPersona(personaSeleccionada);
                    //Setea la posición en la tabla.
                    Sesion.setPos(tvPersonas.getSelectionModel().getSelectedIndex());
                    //Carga los detalles de la persona en una nueva ventana.
                    HelloApplication.cargarFXML("detallesPersona-controller.fxml");
                }
            }
        });
        /*
        //Sonido
        Media sonido = new Media(HelloApplication.class.getClassLoader().getResource("/audio/pick-92276.wav").toExternalForm());
        mediaPlayer = new MediaPlayer(sonido);
        */

        cbProfesion.valueProperty().addListener(((observableValue, s, t1) -> {
            String imagen = "programador.png";
            if (t1 == "Cocinero") imagen = "cocinero.png";
            else if (t1 == "Educador") imagen = "profesor.png";

            ivImagen.setImage(new Image("C:\\Users\\jrgal\\IdeaProjects\\UD2Ejercicio5\\PruebasJavaFX\\src\\main\\resources\\images\\" + imagen));

            /*
            mediaPlayer.seek(new Duration(0));
            mediaPlayer.play();
            */
        }));
    }

    @FXML
    public void insertarReceta(ActionEvent actionEvent) {
        //Parsear la fecha y meterla en la tabla.
        LocalDate fechaSeleccionada = dpFechaNacimiento.getValue(); // Obtener la fecha seleccionada del DatePicker
        Date fechaParseada = java.sql.Date.valueOf(fechaSeleccionada);

        if (!tvNombre.getText().isEmpty()){
            //Para esto sirve el constructor vacío.
            Persona persona = new Persona();
            persona.setNombre(tvNombre.getText());
            persona.setApellido(tvApellido.getText());
            persona.setFechaNacimiento(fechaParseada);
            persona.setEdad((Integer) spEdad.getValue());
            persona.setProfesion(cbProfesion.getSelectionModel().getSelectedItem());
            persona.setObservaciones(taObservaciones.getText());

            //Necesario para luego modificar una persona.
            /*Si no se añade la persona nueva a la lista de personas en Sesión a la hora de modificarse dará
            * un IndexOutOfBounds.*/
            Sesion.getPersonas().add(persona);
            observableListPersonas.add(persona);

        }
    }
}