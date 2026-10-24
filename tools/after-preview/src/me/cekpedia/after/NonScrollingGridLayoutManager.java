package me.cekpedia.after;

/** The outer ScrollView owns scrolling, matching the application's HomeFragment. */
public class NonScrollingGridLayoutManager extends android.support.v7.widget.GridLayoutManager {
    public NonScrollingGridLayoutManager(android.content.Context context, int columns) {
        super(context, columns);
    }
    @Override public boolean canScrollVertically() { return false; }
}
