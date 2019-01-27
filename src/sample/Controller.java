package sample;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class Controller implements Initializable {


    @FXML
    private Pane paneMain;


    @FXML
    private TableView<Yakit> tableMain;

    @FXML
    private TableColumn<Yakit, String> merkezColumn;

    @FXML
    private TableColumn<Yakit, String> plakaColumn;

    @FXML
    private TableColumn<Yakit, String> tarihColumn;

    @FXML
    private TableColumn<Yakit, String> yakitcinsiColumn;

    @FXML
    private TableColumn<Yakit, Double> yakitmiktariColumn;

    @FXML
    private TableColumn<Yakit, Double> tutar;


    @FXML
    private ComboBox<String> merkezCombox;

    @FXML
    private TextField lblToplamLT;

    @FXML
    private TextField lblToplamTL;

    @FXML
    private TextField txtTarihSearch;

    @FXML
    private ComboBox<String> merkezSec;

    @FXML
    private Button yukleButton;

    @FXML
    private Button kayitTemizle;

    @FXML
    private ComboBox<String> plakaCombox;

    @FXML
    private Button kaydetBtn;

    @FXML
    private TextField txtmerkezSearch;

    @FXML
    private DatePicker tarihPicker;


    @FXML
    private Button ekleBtn;


    @FXML
    private Button sllKayit;

    @FXML
    private TextField yakittipi;

    @FXML
    private TextField yakitmiktari;

    @FXML
    private TextField yakitTL;

    @FXML
    private Button yazdirBtn;

    @FXML
    private Label statusLabel;


    public LoginModel loginModel = new LoginModel();


    // Merkezler
    ObservableList<String> merkezdata = observableArrayList("Antalya", "Isparta", "Burdur", "Muğla", "Bodrum",
            "Milas", "Datça", "Marmaris", "Alanya", "Gazipaşa");
    ObservableList<String> merkezler = observableArrayList("Hepsi", "Antalya", "Isparta", "Burdur", "Muğla", "Bodrum",
            "Milas", "Datça", "Marmaris", "Alanya", "Gazipaşa");
    //plakalar
    ObservableList<String> antalyaPlakalar = observableArrayList("06 KA 2168", "06 FZ 3257", "07 AC 0135", "06 AEB 943");
    ObservableList<String> muglaPlakalar = observableArrayList("07 AC 0134");
    ObservableList<String> bodrumPlakalar = observableArrayList("07 AC 0132");
    ObservableList<String> IspartaPlakalar = observableArrayList("06 GB 3995");
    ObservableList<String> burdurPlakalar = observableArrayList("07 AC 0136");
    ObservableList<String> datcaPlakalar = observableArrayList("AKSA-JEN", "06 AVK 01", "48 R 3415");
    ObservableList<String> marmarisPlakalar = observableArrayList("06 BH 1738");
    ObservableList<String> alanyaPlakalar = observableArrayList("07 AC 0138");
    ObservableList<String> gazipasaPlakalar = observableArrayList("07 AC 0188");
    ObservableList<String> milasPlakalar = observableArrayList("06 RK 023");

    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    ObservableList<Yakit> data = observableArrayList();

    ObservableList ortakVeriDB;
    ArrayList<Yakit> dataFromDB;

    FilteredList filter = new FilteredList(data, e -> true);

    double totalLT = 0;
    double totalTL = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (loginModel.isDbConnected()) {
            statusLabel.setText("Connected to DB");
        } else {
            statusLabel.setText("Not Connected");
        }


        //tableMain.setItems(data);
        tableMain.setEditable(true);
        tableMain.setItems(data);


        updateLabels();

        //id.setCellValueFactory(new PropertyValueFactory<>("id"));
        merkezColumn.setCellValueFactory(new PropertyValueFactory<>("merkez"));
        plakaColumn.setCellValueFactory((new PropertyValueFactory<>("plaka")));
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("tarih"));
        yakitcinsiColumn.setCellValueFactory(new PropertyValueFactory<>("yakittipi"));
        yakitmiktariColumn.setCellValueFactory((new PropertyValueFactory<>("yakitmiktari")));
        tutar.setCellValueFactory(new PropertyValueFactory<>("yakitTutari"));
        merkezSec.setItems(merkezler);
        merkezSec.getSelectionModel().selectFirst();

        // TODO: TableView Veritabanından verileri yükle ve ortak değişkene ata

        merkezCombox.setItems(merkezdata);

        merkezCombox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                System.out.print("Required");
            } else {
                plakaCombox.setDisable(false);
                switch (newValue) {
                    case "Antalya":
                        plakaCombox.getItems().setAll(antalyaPlakalar);
                        break;
                    case "Isparta":
                        plakaCombox.getItems().setAll(IspartaPlakalar);
                        break;

                    case "Burdur":
                        plakaCombox.getItems().setAll(burdurPlakalar);
                        break;
                    case "Muğla":
                        plakaCombox.getItems().setAll(muglaPlakalar);
                        break;
                    case "Bodrum":
                        plakaCombox.getItems().setAll(bodrumPlakalar);
                        break;
                    case "Milas":
                        plakaCombox.getItems().setAll(milasPlakalar);
                        break;
                    case "Datça":
                        plakaCombox.getItems().setAll(datcaPlakalar);
                        break;
                    case "Marmaris":
                        plakaCombox.getItems().setAll(marmarisPlakalar);
                        break;
                    case "Alanya":
                        plakaCombox.getItems().setAll(alanyaPlakalar);
                        break;
                    case "Gazipaşa":
                        plakaCombox.getItems().setAll(gazipasaPlakalar);
                        break;

                }

            }
        });

        plakaCombox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    yakittipi.clear();
                } else if (newValue == "06 AVK 01" || newValue == "06 RK 023" || newValue == "48 R 3415") {
                    yakittipi.setText("Benzin");
                } else {
                    yakittipi.setText("Motorin");
                }
            }
        });


        merkezColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        merkezColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Yakit, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Yakit, String> t) {
                        ((Yakit) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setMerkez(t.getNewValue());
                    }
                }
        );
        // MERKEZE GÖRE FİLTRELE
        //////////////


        plakaColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        plakaColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Yakit, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Yakit, String> t) {
                        ((Yakit) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPlaka(t.getNewValue());
                    }
                }
        );

        tarihColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tarihColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Yakit, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Yakit, String> t) {
                        ((Yakit) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTarih(t.getNewValue());
                    }
                }
        );


        yakitcinsiColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        yakitcinsiColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Yakit, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Yakit, String> t) {
                        ((Yakit) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setYakittipi(t.getNewValue());
                    }
                }
        );



        yakitmiktariColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        yakitmiktariColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Yakit, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Yakit, Double> t) {
                        ((Yakit) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setYakitmiktari(t.getNewValue());
                        updateLabels();

                    }
                }
        );

        tutar.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tutar.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Yakit, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Yakit, Double> t) {
                        ((Yakit) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setYakitTutari(t.getNewValue());
                        updateLabels();

                    }
                }
        );
        ekleBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    data.add(new Yakit(merkezCombox.getValue(), plakaCombox.getValue(), df.format(tarihPicker.getValue()),
                            yakittipi.getText(), Double.parseDouble(yakitmiktari.getText()), Double.parseDouble(yakitTL.getText())));
                    updateLabels();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        yukleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String merkez = merkezSec.getValue();

                ArrayList<Yakit> yakitlar = new ArrayList<>();
                try {
                    yakitlar = loginModel.loadData(merkez);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (yakitlar.isEmpty()) {
                    return;
                }
                data.clear();
                System.out.println(yakitlar.size());
                data.setAll(yakitlar);
                updateLabels();
            }
        });

        kaydetBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Yakit> yakits = new ArrayList<>(tableMain.getItems());
                System.out.println(yakits.size());
                try {
                    loginModel.storeDB(yakits);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Kaydet Bilgi");
                    alert.setHeaderText(null);
                    alert.setContentText("Kayıt Başarılı");
                    alert.showAndWait();

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Bad request");
                }

            }
        });

        sllKayit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Yakit yakit = tableMain.getSelectionModel().getSelectedItem();
                Long id = yakit.getId();
                if (id == 0) {
                    cleanRecord();
                } else {
                    try {
                        loginModel.deleteRecord(yakit);
                        cleanRecord();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Kayıt Silme Bilgi");
                        alert.setHeaderText(null);
                        alert.setContentText("Kayıt Veri Tabanından Silindi");
                        alert.showAndWait();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                yukleButton.requestFocus();
            }
        });
    }

    @FXML
    private void updateLabels() {
        totalLT = tableMain.getItems().stream().collect(Collectors.summingDouble(Yakit::getYakitmiktari));
        totalTL = tableMain.getItems().stream().collect(Collectors.summingDouble(Yakit::getYakitTutari));
        String miktar = String.format("%.2f", totalLT) + " LT";
        String tutar = String.format("%.2f", totalTL) + " TL";
        lblToplamLT.setText(miktar);
        lblToplamTL.setText(tutar);
    }


    @FXML
    private void cleanRecord() {
        int selectedIndex = tableMain.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            data.remove(selectedIndex);
            updateLabels();
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("Seçim Yapılmadı");
            alert.showAndWait();
        }
    }

    @FXML
    public void updateTable(ObservableList data) {
        tableMain.setItems(data);

    }


    @FXML
    private void searchByMkz(KeyEvent event) {
        txtmerkezSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Yakit>) (Yakit yakit) -> {
                if (newValue.isEmpty() || newValue == null) {
                    updateLabels();
                    return true;
                }
                String lowerCased = newValue.toLowerCase();
                if (yakit.getMerkez().toLowerCase().contains(lowerCased)) {
                    updateLabels();
                    return true;
                }

                return false;
            });
        });

        SortedList sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(tableMain.comparatorProperty());
        updateTable(sortedList);
    }

    @FXML
    void SearchByTarih(KeyEvent event) {
        txtTarihSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(((Predicate<? super Yakit>) (Yakit yakit) -> {
                if (newValue.isEmpty() || newValue == null) {
                    updateLabels();
                    return true;
                }
                String lowerCased = newValue.toLowerCase();
                if (yakit.getTarih().toLowerCase().contains(lowerCased)) {
                    updateLabels();
                    return true;
                }
                return false;
            }));
        });
        SortedList sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(tableMain.comparatorProperty());
        updateTable(sortedList);
    }




    @FXML
    void YazdirFunc(ActionEvent event) throws IOException, URISyntaxException {
        updateLabels();
        FileChooser fileChooser = new FileChooser();
        Node node = (Node) event.getSource();
        fileChooser.setTitle("Dosya kaydet");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("Yakıt Bilgisi.xlsx");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Dosyası", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File savedFile = fileChooser.showSaveDialog(node.getScene().getWindow());
        if (savedFile != null) {
            saveFile(savedFile);
        } else if (savedFile == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Yazdırma İptal Edildi");
            alert.showAndWait();
        }


    }

/*
    @FXML
    void YazdirFunc(ActionEvent event) throws IOException, URISyntaxException {
        ObservableList<Yakit> yakits = observableArrayList(tableMain.getItems());
        updateLabels();
        URL resource = Main.class.getResource("yakitdetay.xlsx");
        Workbook wb = WorkbookFactory.create(new File(resource.toURI()));
        Sheet sheet = wb.getSheet("Yakıt Tüketim");
        int i, j = 0;

        for (i = 0, j = 2; i < yakits.size(); i++, j++) {
            Yakit yakit = yakits.get(i);
            System.out.println(yakit.getMerkez());
            Row row = sheet.createRow(j);
            row.createCell(0).setCellValue(yakit.getMerkez());
            row.createCell(1).setCellValue(yakit.getPlaka());
            row.createCell(2).setCellValue(yakit.getYakittipi());
            row.createCell(3).setCellValue(yakit.getTarih());
            row.createCell(4).setCellValue(yakit.getYakitmiktari());
            row.createCell(5).setCellValue(yakit.getYakitTutari());
        }
        Row row = sheet.createRow(j);
        row.createCell(3).setCellValue("TOPLAM: ");
        row.createCell(4).setCellValue(totalLT);
        row.createCell(5).setCellValue(totalTL);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);

        try(FileOutputStream out = new FileOutputStream(new File(System.getProperty("user.home")+"/Desktop/Yakıt Bilgisi.xlsx"));){
            wb.write(out);
            wb.close();
            alert.setContentText("Yakıt bilgisi masaüstüne yazdırıldı");
            alert.showAndWait();
        } catch (Exception e){
            alert.setContentText("Yazdırma Fonksiyon Hatası");
            alert.showAndWait();
        }

        //OutputStream out = Files.newOutputStream(file.toPath());

    }
*/
    @FXML
    void saveFile(File file)  throws IOException, URISyntaxException {
        ObservableList<Yakit> yakits = observableArrayList(tableMain.getItems());
        updateLabels();

        Workbook workbook= new XSSFWorkbook();


        Sheet sheet = workbook.createSheet("Yakıt Tüketim");
        Font headerFont = workbook.createFont();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLUE.getIndex());


        // Ana başlık

        CellStyle headerCellStyle = workbook.createCellStyle();
        CellStyle boldCellStyle = workbook.createCellStyle();
        boldCellStyle.setFont(boldFont);
        headerCellStyle.setFont(headerFont);
        Row headerRow1 = sheet.createRow(0);
        Cell cell = headerRow1.createCell(1);
        cell.setCellValue("Meteoroloji 4. Bölge Müdürlüğü " +
                "Akaryakıt Tüketim Değerleri");
        cell.setCellStyle(headerCellStyle);

        // Alt başlık
        Row headerRow2 = sheet.createRow(1);
        Cell merkez = headerRow2.createCell(1);
        merkez.setCellValue("MERKEZ");
        merkez.setCellStyle(boldCellStyle);



        Cell plaka = headerRow2.createCell(2);
        plaka.setCellValue("PLAKA");
        plaka.setCellStyle(boldCellStyle);

        Cell yakittipi = headerRow2.createCell(3);
        yakittipi.setCellValue("YAKIT TİPİ");
        yakittipi.setCellStyle(boldCellStyle);

        Cell tarih = headerRow2.createCell(4);
        tarih.setCellValue("TARİH");
        tarih.setCellStyle(boldCellStyle);

        Cell miktar = headerRow2.createCell(5);
        miktar.setCellValue("YAKIT (LT)");
        miktar.setCellStyle(boldCellStyle);

        Cell tutar = headerRow2.createCell(6);
        tutar.setCellValue("TUTAR (TL)");
        tutar.setCellStyle(boldCellStyle);
        int i, j = 0;

        for (i = 0, j = 2; i < yakits.size(); i++, j++) {
            Yakit yakit = yakits.get(i);
            System.out.println(yakit.getMerkez());
            Row row = sheet.createRow(j);
            row.createCell(1).setCellValue(yakit.getMerkez());
            row.createCell(2).setCellValue(yakit.getPlaka());
            row.createCell(3).setCellValue(yakit.getYakittipi());
            row.createCell(4).setCellValue(yakit.getTarih());
            row.createCell(5).setCellValue(yakit.getYakitmiktari());
            row.createCell(6).setCellValue(yakit.getYakitTutari());
        }
        Row row = sheet.createRow(j);
        row.createCell(4).setCellValue("TOPLAM: ");
        row.createCell(5).setCellValue(totalLT);
        row.createCell(6).setCellValue(totalTL);


        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(4);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);

        try {
            OutputStream out = new FileOutputStream(new File(String.valueOf(file)));
            workbook.write(out);
            workbook.close();
            out.close();
            alert.setContentText("Yakıt bilgisi yazdırıldı");
            alert.showAndWait();
        } catch (Exception e){
            alert.setContentText("yazdırma İptal Edildi");
            alert.showAndWait();
        }
    }

/*
    private void saveIt(URL url,  File saveAs) throws IOException {
        InputStream in = url.openStream();
        OutputStream out = new FileOutputStream(new File(System.getProperty("user.home")+"\\Desktop\\Yakıt Bilgisi.xlsx"));
        System.out.println(System.getProperty("user.home"));
        //FileOutputStream fos = new FileOutputStream(saveAs);
        IOUtils.copy(in, out);
        /* int length = -1;
        byte[] buffer = new byte[1024];
        while ((length = in.read(buffer)) > -1) {
            fos.write(buffer, 0, length);
        }

        out.close();
        in.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Yakıt bilgisi yazdırıldı");
        alert.showAndWait();
    }
    */
}
