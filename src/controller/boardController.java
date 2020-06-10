package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import model.Board;
import model.Iris;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

import static javafx.scene.control.SelectionMode.SINGLE;

public class boardController {
    @FXML
    ListView lvMenu;
    @FXML
    Group groupBoard;
    @FXML
    ScatterChart scatterChart;

    public boardController() {
        loadIrisData();
    }

    @FXML
    private void initialize() {
        lvMenu.getSelectionModel().setSelectionMode(SINGLE);
        loadIrisToBoard(0, 1);

        List<Board> boardList = new ArrayList<>();
        for (int i = 0; i < Iris.featureNum-1; i++){
            for (int j = i+1; j < Iris.featureNum; j++){
                boardList.add(new Board(i, j));
            }
        }

        ObservableList<Board> boards = FXCollections.observableArrayList(boardList);
        lvMenu.setItems(boards);
        lvMenu.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Board board = (Board) newValue;
                loadIrisToBoard(board.i, board.j);
            }
        });
    }

    ArrayList<Iris> irisSet = new ArrayList<>();


    private void loadIrisData() {
        File irisFile = new File("resource/iris.dat");
        if (irisFile.exists()) {
            System.out.println("Read file successfully!\n");
            Scanner sc = null;
            try {
                sc = new Scanner(irisFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().replaceAll("( +)", " ").trim().split(" ");
                if (line.length < Iris.featureNum) continue;
                Iris iris = new Iris();
                for (int j = 0; j < Iris.featureNum; j++) {
                    iris.features[j] = Double.parseDouble(line[j]);
                }
                iris.type = 4 * Integer.parseInt(line[Iris.featureNum]);
                iris.type += 2 * Integer.parseInt(line[Iris.featureNum + 1]);
                iris.type += Integer.parseInt(line[Iris.featureNum + 2]);
                irisSet.add(iris);
                System.out.println(iris.toString());
            }
        } else {
            System.out.println("Could not read file!\n");
        }
    }

    private void loadIrisToBoard(int firstF, int secondF) {
        XYChart.Series<Number, Number> iType1 = new XYChart.Series<Number, Number>();
        XYChart.Series<Number, Number> iType2 = new XYChart.Series<Number, Number>();
        XYChart.Series<Number, Number> iType3 = new XYChart.Series<Number, Number>();
        iType1.setName("Iris 1");
        iType2.setName("Iris 2");
        iType3.setName("Iris 3");

        irisSet.forEach(iris -> {
            switch (iris.type){
                case 1:
                    iType1.getData().add(new XYChart.Data<Number, Number>(iris.features[firstF],iris.features[secondF]));
                    break;
                case 2:
                    iType2.getData().add(new XYChart.Data<Number, Number>(iris.features[firstF],iris.features[secondF]));
                    break;
                case 4:
                    iType3.getData().add(new XYChart.Data<Number, Number>(iris.features[firstF],iris.features[secondF]));
                    break;
            }
        });
        if (!scatterChart.getData().isEmpty()){
            scatterChart.getData().remove(0, scatterChart.getData().size());
        }
        scatterChart.getData().addAll(iType1, iType2, iType3);

    }
}
