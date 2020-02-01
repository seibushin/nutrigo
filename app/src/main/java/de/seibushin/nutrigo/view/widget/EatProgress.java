package de.seibushin.nutrigo.view.widget;

public class EatProgress {
    public String name = "";
    public double value = 0;
    // use 0 for no additive arc
    public int drawOrder = 0;
    public boolean isTotal = false;
    public int color = android.R.color.holo_green_light;

    public EatProgress(double value, int drawOrder, boolean isTotal, int color) {
        this.value = value;
        this.drawOrder = drawOrder;
        this.isTotal = isTotal;
        this.color = color;
    }
}
