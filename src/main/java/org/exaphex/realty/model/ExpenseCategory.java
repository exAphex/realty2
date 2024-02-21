package org.exaphex.realty.model;

import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class ExpenseCategory {
    private static final ResourceBundle res = ResourceBundle.getBundle("i18n");
    public static final ExpenseCategoryCalculationType[] calculationTypes = {
            new ExpenseCategoryCalculationType(0,"categoryTypePerUnit", res.getString("categoryTypePerUnit")),
            new ExpenseCategoryCalculationType(1,"categoryTypePerPerson", res.getString("categoryTypePerPerson")),
            new ExpenseCategoryCalculationType(2,"categoryTypePerArea", res.getString("categoryTypePerArea"))
    };
    private final String id;
    private final String name;
    private final boolean wrapable;
    private final int calculationType;

    public ExpenseCategory(String name, boolean wrapable, int calculationType) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.wrapable = wrapable;
        this.calculationType = calculationType;
    }

    public ExpenseCategory(String id, String name, boolean wrapable, int calculationType) {
        this.id = id;
        this.name = name;
        this.wrapable = wrapable;
        this.calculationType = calculationType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isWrapable() {
        return wrapable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseCategory expenseCategory = (ExpenseCategory) o;
        return Objects.equals(id, expenseCategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getCalculationType() {
        return calculationType;
    }

    public static ExpenseCategoryCalculationType getExpenseCategoryCalulationTypeById(int id) {
        for (ExpenseCategoryCalculationType ext : calculationTypes) {
            if (id == ext.getId()) {
                return ext;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
