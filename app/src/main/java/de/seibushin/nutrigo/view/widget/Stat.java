package de.seibushin.nutrigo.view.widget;

import de.seibushin.nutrigo.R;

enum Stat {
    KCAL("kcal", R.color.colorAccent),
    FAT("fat", R.color.fat),
    CARBS("carbs", R.color.carbs),
    SUGAR("sugar", R.color.sugar),
    PROTEIN("protein", R.color.protein);

    public String statname;
    public int color;

    Stat(String statname, int color) {
        this.statname = statname;
        this.color = color;
    }
}