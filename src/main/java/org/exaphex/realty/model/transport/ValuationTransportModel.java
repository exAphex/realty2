package org.exaphex.realty.model.transport;

import com.opencsv.bean.CsvBindByName;
import org.exaphex.realty.model.Valuation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ValuationTransportModel {

    @CsvBindByName(column = "date")
    private String date;

    @CsvBindByName(column = "value")
    private String value;

    public String getDate() {
        return date;
    }

    public String getValue() {
        return value;
    }

    public Valuation getValuation(String id) throws ParseException {
        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yyyy");
        sourceFormat.parse(this.date);
        float f = Float.parseFloat(value);
        return new Valuation(id, this.date, f);
    }
}
