package de.seibushin.nutrigo.view.widget;

public class ChartData {
    public long timestamp;
    public float kcal;
    public float fat;
    public float carbs;
    public float protein;

    public float getData(Stat stat) {
        switch (stat) {
            case KCAL:
                return kcal;
            case FAT:
                return fat;
            case CARBS:
                return carbs;
            case SUGAR:
                return 0;
            case PROTEIN:
                return protein;
        }
        return 0;
    }
}
