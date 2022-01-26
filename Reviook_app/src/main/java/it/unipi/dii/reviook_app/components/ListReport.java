package it.unipi.dii.reviook_app.components;

import it.unipi.dii.reviook_app.entity.Report;
import it.unipi.dii.reviook_app.entity.Review;
import javafx.scene.control.ListCell;

public class ListReport extends ListCell<Report> {
    @Override
    public void updateItem(Report report, boolean empty) {
        super.updateItem(report, empty);
        if (report != null) {
            DataReportCell dataReportCell = new DataReportCell();
            dataReportCell.setInfo(report);
            setGraphic(dataReportCell.getPane());
        }
    }
}
