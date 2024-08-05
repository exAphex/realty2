package org.exaphex.realty.model.ui.cmb;

import org.exaphex.realty.model.Credit;
import org.exaphex.realty.model.DocumentType;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class DocumentTypeComboBoxModel implements ComboBoxModel<DocumentType> {
    private List<DocumentType> documentTypes;
    int index = -1;
    public DocumentTypeComboBoxModel(List<DocumentType> documentTypes) {
        this.documentTypes = new ArrayList<>(documentTypes);
    }

    public void setDocumentTypes(List<DocumentType> documentTypes) {
        this.documentTypes = new ArrayList<>(documentTypes);
    }

    public int getDocumentTypeIndexById(String id) {
        if (id == null) {
            return -1;
        }
        for (int i = 0; i < documentTypes.size(); i++) {
            if (id.equals(documentTypes.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        for(int i = 0; i < documentTypes.size(); i++) {
            if(documentTypes.get(i).equals(anItem)) {
                index = i;
                break;
            }
        }
    }

    @Override
    public Object getSelectedItem() {
        if(index >= 0){
            return documentTypes.get(index);
        } else {
            return "";
        }
    }

    @Override
    public int getSize() {
        return documentTypes.size();
    }

    @Override
    public DocumentType getElementAt(int index) {
        return documentTypes.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
