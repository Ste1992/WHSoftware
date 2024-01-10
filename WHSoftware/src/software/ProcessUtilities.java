package software;

import warehouse_management.Bin;
import warehouse_management.Item;
import warehouse_management.Tote;

public class ProcessUtilities {

    private final Item item;
    private final Tote tote;
    private final Bin bin;
    private Bin randomBin;
    private String scannedTote;
    private String quantityInput;
    private int aux = 0;
    private int totalUnitsInTote = 0;

    public Tote getTote() {
        return tote;
    }

    public Bin getBin() {
        return bin;
    }

    public Item getItem() {
        return item;
    }

    public Bin getRandomBin() {
        return randomBin;
    }

    public String getScannedTote() {
        return scannedTote;
    }

    public String getQuantityInput() {
        return quantityInput;
    }

    public int getAux() {
        return aux;
    }

    public int getTotalUnitsInTote() {
        return totalUnitsInTote;
    }

    public void setRandomBin(Bin randomBin) {
        this.randomBin = randomBin;
    }

    public void setScannedTote(String scannedTote) {
        this.scannedTote = scannedTote;
    }

    public void setQuantityInput(String quantityInput) {
        this.quantityInput = quantityInput;
    }

    public void setAux(int aux) {
        this.aux = aux;
    }

    public void setTotalUnitsInTote(int totalUnitsInTote) {
        this.totalUnitsInTote = totalUnitsInTote;
    }

    public ProcessUtilities(Tote tote, Bin bin, Item item) {
        this.tote = tote;
        this.bin = bin;
        this.item = item;
    }
}
