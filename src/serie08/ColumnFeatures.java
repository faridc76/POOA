package serie08;

enum ColumnFeatures {
    SUBJECT("Matières", "", String.class),
    COEF("Coefficients", NoteTableModel.ZERO, Double.class),
    MARK("Notes / 20", NoteTableModel.ZERO, Double.class),
    POINTS("Points", null, Double.class);
    
    private String header;
    private Object value;
    private Class<?> clazz;
    
    ColumnFeatures(String n, Object v, Class<?> c) {
        header = n;
        value = v;
        clazz = c;
    }

    Object defaultValue() {
        return value;
    }
    String header() {
        return header;
    }
    Class<?> type() {
        return clazz;
    }
}
