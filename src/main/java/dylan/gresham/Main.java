package dylan.gresham;

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
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.GenericStyledArea;

public class Main extends Application
{
    @FXML
    private static Scene primScene;

    @FXML
    private BorderPane mainBord;

    @FXML
    private HBox controlBar;

    @FXML
    private Button openBut, saveBut, boldBut, italicBut, underBut;

    @FXML
    private CheckBox takeBox;

    @FXML
    private GenericStyledArea<PS, SEG, S> notes;

    @Override
    public void start(Stage primStage) throws IOException
    {
        primStage.setMaximized(true);

        // Changes app icon.
        primStage.getIcons().add(new Image(Main.class.getResourceAsStream("nottahRin.jpg")));

        mainBord = new BorderPane();
        mainBord.setId("mainBord");
        controlBar = new HBox();
        controlBar.setId("controlBar");

        notes = new GenericStyledArea<PS,SEG,S>(
            , null, null, null, null); // TODO: Figure this shit out. https://github.com/FXMisc/RichTextFX
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
                        StringBuilder sb = new StringBuilder();
                       
                        // sb.append("<pre>");
                        Files.lines(toOpenFile.toPath()).forEach(line -> sb.append(line).append("\n"));
                        // sb.append("</pre>");
                        
                        notes.setHtmlText(sb.toString());
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
                        Files.write(saveFilePath, removeHTML(notes.getHtmlText()).getBytes());
                        
                        // BufferedWriter bw = Files.newBufferedWriter(saveFilePath, StandardCharsets.UTF_8);
                        // bw.write(notes.getHtmlText());
                        // bw.close(); // By default, flushes before closing
                    }
                    catch (Exception ex) {ex.printStackTrace();}
                }
                else if(!Files.exists(saveFilePath))
                {
                    try
                    {
                        Files.createFile(saveFilePath.toAbsolutePath());
    
                        BufferedWriter bw = Files.newBufferedWriter(saveFilePath, StandardCharsets.UTF_8);
                        bw.write(notes.getHtmlText());
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

        takeBox = new CheckBox("Take");

        takeBox.setId("takeBox");
        openBut.setId("openBut");
        saveBut.setId("saveBut");

        takeBox.setSelected(true);
        takeBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) ->
        {
            if(takeBox.isSelected())
            {
                takeBox.setText("Take");
                // notes.setEditable(true);
            }
            else
            {
                takeBox.setText("Read");
                // notes.setEditable(false);
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

    private String removeHTML(String text)
    {
        String ret = text;

        while(ret.contains("<"))
        {
            int idx = ret.indexOf("<");
            ret = ret.substring(0, idx) + ret.substring(ret.indexOf(">") + 1);
        }

        return ret;
    }
    
    public static void main(String[] args)
    {
        launch();
    }
}
