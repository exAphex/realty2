package org.exaphex.realty.processor.export;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.db.DatabaseConnector;
import org.exaphex.realty.db.service.*;
import org.exaphex.realty.model.transport.ExportModel;

import javax.swing.*;
import java.io.*;
import java.util.ResourceBundle;

public class ExportProcessor {
    protected static final Logger logger = LogManager.getLogger();
    private static final ResourceBundle res = ResourceBundle.getBundle("i18n");
    private static ExportModel extractData() {
        ExportModel retEM = new ExportModel();
        retEM.setVersion(""+DatabaseConnector.getDatabaseVersion());
        retEM.setBuildings(BuildingService.getBuilding());
        retEM.setCredits(CreditService.getCredit());
        retEM.setRents(RentService.getRents(null));
        retEM.setTransactions(TransactionService.getTransactions());
        retEM.setUnits(UnitService.getUnits(null));
        retEM.setValuations(ValuationService.getValuations(null));
        retEM.setExpenseCategories(ExpenseCategoryService.getCategories());
        retEM.setContacts(ContactService.getContacts());
        return retEM;
    }

    private static void saveData(ExportModel em) {
        if (!em.getVersion().equals(""+DatabaseConnector.getDatabaseVersion())) {
            JOptionPane.showMessageDialog(new JFrame(), res.getString("msgWrongImportVersion"), res.getString("msgError"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        BuildingService.addBuildings(em.getBuildings());
        CreditService.addCredits(em.getCredits());
        RentService.addRents(em.getRents());
        TransactionService.addTransactions(em.getTransactions());
        UnitService.addUnits(em.getUnits());
        ValuationService.addValuations(em.getValuations());
        ExpenseCategoryService.addExpenseCategories(em.getExpenseCategories());
        ContactService.addContacts(em.getContacts());
    }

    public static void exportToFile(File f) {
        Gson gson = new Gson();
        ExportModel em = extractData();

        try (FileWriter fw = new FileWriter(f.getAbsolutePath())){
            gson.toJson(em, fw);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public static void importFromFile(File f) {
        Gson gson = new Gson();
        try (FileReader fr = new FileReader(f.getAbsolutePath())) {
            JsonReader reader = new JsonReader(fr);
            ExportModel data = gson.fromJson(reader, ExportModel.class);
            saveData(data);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
