package dylan.gresham;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application
{
    @FXML
    private static Scene primScene;

    @FXML
    private BorderPane mainBord;

    @FXML
    private HBox controlBar;

    @FXML
    private TextArea notes;

    @FXML
    private Button openBut, saveBut, boldBut, italicBut, underBut;

    @FXML
    private CheckBox takeBox;

    @Override
    public void start(Stage primStage) throws IOException
    {
        primStage.setMaximized(true);

        mainBord = new BorderPane();
        mainBord.setId("mainBord");
        controlBar = new HBox();
        controlBar.setId("controlBar");
        
        // TODO: Gotta change notes to be a div container to be able to be able to hold rich content.
        // See https://stackoverflow.com/questions/12831101/format-text-in-a-textarea
        notes = new TextArea();
        notes.setWrapText(true);
        notes.setPromptText("Thoughts in class...");
        notes.setId("notes");
        notes.setMaxHeight(960);
        notes.setMaxWidth(720);
        
        openBut = new Button("Open");
        openBut.setOnAction(e ->
        {
            primScene.setCursor(Cursor.WAIT);
            FileChooser file = new FileChooser();
            file.setTitle("Open File");
            File toOpenFile = file.showOpenDialog(primStage);
            if(toOpenFile != null)
            {
                if(toOpenFile.exists())
                {
                    try
                    {
                        BufferedReader br = Files.newBufferedReader(toOpenFile.toPath(), StandardCharsets.UTF_8);
                        String line;
                        while((line = br.readLine()) != null)
                        {
                            notes.appendText(line + "\n");
                        }
                        br.close();
                    } catch (Exception excp)
                    {
                        excp.printStackTrace();
                    }
                }
            }
            primScene.setCursor(Cursor.DEFAULT);
        });

        saveBut = new Button("Save");
        saveBut.setOnAction(e ->
        {
            primScene.setCursor(Cursor.WAIT);
            FileChooser file = new FileChooser();
            file.setTitle("Save File");
            File saveFile = file.showSaveDialog(primStage);
            
            if(saveFile != null)
            {
                Path saveFilePath = saveFile.toPath();
                if(Files.exists(saveFilePath))
                {
                    try
                    {
                        BufferedWriter bw = Files.newBufferedWriter(saveFilePath, StandardCharsets.UTF_8);
                        bw.write(notes.getText());
                        bw.close(); // By default, flushes before closing
                    }
                    catch (Exception ex) {ex.printStackTrace();}
                }
                else if(!Files.exists(saveFilePath))
                {
                    try
                    {
                        Files.createFile(saveFilePath.toAbsolutePath());
    
                        BufferedWriter bw = Files.newBufferedWriter(saveFilePath, StandardCharsets.UTF_8);
                        bw.write(notes.getText());
                        bw.close(); // By default, flushes before closing
                    }
                    catch (Exception exc) {exc.printStackTrace();}
                }
            }
            primScene.setCursor(Cursor.DEFAULT);
        });

        saveBut.setOnMouseEntered(e -> {primScene.setCursor(Cursor.HAND);});
        saveBut.setOnMouseExited(e -> {primScene.setCursor(Cursor.DEFAULT);});
        openBut.setOnMouseEntered(e -> {primScene.setCursor(Cursor.HAND);});
        openBut.setOnMouseExited(e -> {primScene.setCursor(Cursor.DEFAULT);});

        // Bold/Italic/Underline buttons will be implemented later on.
        boldBut = new Button("B");
        // italicBut = new Button("I");
        // underBut = new Button("U");
        takeBox = new CheckBox("Take");

        boldBut.setId("boldBut");
        // italicBut.setId("italicBut");
        // underBut.setId("underBut");
        takeBox.setId("takeBox");
        openBut.setId("openBut");
        saveBut.setId("saveBut");
        boldBut.setAlignment(Pos.CENTER);
        takeBox.setAlignment(Pos.CENTER);
        openBut.setAlignment(Pos.CENTER);
        saveBut.setAlignment(Pos.CENTER);

        boldBut.setOnAction(e -> 
        {
            // TODO: Implement
        });

        takeBox.setSelected(true);
        takeBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) ->
        {
            if(takeBox.isSelected())
            {
                takeBox.setText("Take");
                notes.setEditable(true);
            }
            else
            {
                takeBox.setText("Read");
                notes.setEditable(false);
            }
        });
        controlBar.getChildren().addAll(openBut, saveBut, takeBox);

        mainBord.setCenter(notes);
        mainBord.setTop(controlBar);

        primScene = new Scene(mainBord);
        primScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());

        primStage.setScene(primScene);
        primStage.setTitle("Nottah");
        primStage.show();
    }

    static void setRoot(String fxml) throws IOException
    {
        primScene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void main(String[] args)
    {
        launch();
    }
}
