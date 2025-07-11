package de.Roboter007.voxiety.utils;

public class NumCalc<N extends Number> {


    public NumCalc() {
    }

    @SuppressWarnings("unchecked")
    public N multiply(N number, int multiplier) {
        Class<? extends Number> cls = number.getClass();
        if (cls == Integer.class) {
            return (N) Integer.valueOf(number.intValue() * multiplier);
        }
        if (cls == Long.class) {
            return (N) Long.valueOf(number.longValue() * multiplier);
        }
        if(cls == Double.class) {
            return (N) Double.valueOf(number.doubleValue() * multiplier);
        }
        if(cls == Float.class) {
            return (N) Float.valueOf(number.floatValue() * multiplier);
        }
        throw new UnsupportedOperationException("unknown class: " + cls);
    }

    @SuppressWarnings("unchecked")
    public N divide(N number, N multiplier) {
        Class<? extends Number> cls = number.getClass();
        Class<? extends  Number> cls2 = multiplier.getClass();
        if (cls == Integer.class && cls2 == Integer.class) {
            return (N) Integer.valueOf(number.intValue() / multiplier.intValue());
        }
        if (cls == Long.class && cls2 == Long.class) {
            return (N) Long.valueOf(number.longValue() / multiplier.longValue());
        }
        if(cls == Double.class && cls2 == Double.class) {
            return (N) Double.valueOf(number.doubleValue() / multiplier.doubleValue());
        }
        if(cls == Float.class && cls2 == Float.class) {
            return (N) Float.valueOf(number.floatValue() / multiplier.floatValue());
        }
        throw new UnsupportedOperationException("unknown class: " + cls);
    }

    @SuppressWarnings("unchecked")
    public N multiply(N number, N multiplier) {
        Class<? extends Number> cls = number.getClass();
        Class<? extends  Number> cls2 = multiplier.getClass();
        if (cls == Integer.class && cls2 == Integer.class) {
            return (N) Integer.valueOf(number.intValue() * multiplier.intValue());
        }
        if (cls == Long.class && cls2 == Long.class) {
            return (N) Long.valueOf(number.longValue() * multiplier.longValue());
        }
        if(cls == Double.class && cls2 == Double.class) {
            return (N) Double.valueOf(number.doubleValue() * multiplier.doubleValue());
        }
        if(cls == Float.class && cls2 == Float.class) {
            return (N) Float.valueOf(number.floatValue() * multiplier.floatValue());
        }
        throw new UnsupportedOperationException("unknown class: " + cls);
    }

    @SuppressWarnings("unchecked")
    public N addition(N number, N add) {
        Class<? extends Number> cls = number.getClass();
        Class<? extends  Number> cls2 = add.getClass();
        if (cls == Integer.class && cls2 == Integer.class) {
            return (N) Integer.valueOf(number.intValue() + add.intValue());
        }
        if (cls == Long.class && cls2 == Long.class) {
            return (N) Long.valueOf(number.longValue() + add.longValue());
        }
        if(cls == Double.class && cls2 == Double.class) {
            return (N) Double.valueOf(number.doubleValue() + add.doubleValue());
        }
        if(cls == Float.class && cls2 == Float.class) {
            return (N) Float.valueOf(number.floatValue() + add.floatValue());
        }
        throw new UnsupportedOperationException("unknown class: " + cls);
    }

    @SuppressWarnings("unchecked")
    public N addition(N number, int add) {
        Class<? extends Number> cls = number.getClass();
        if (cls == Integer.class) {
            return (N) Integer.valueOf(number.intValue() + add);
        }
        if (cls == Long.class) {
            return (N) Long.valueOf(number.longValue() + add);
        }
        if(cls == Double.class) {
            return (N) Double.valueOf(number.doubleValue() + add);
        }
        if(cls == Float.class) {
            return (N) Float.valueOf(number.floatValue() + add);
        }
        throw new UnsupportedOperationException("unknown class: " + cls);
    }

    @SuppressWarnings("unchecked")
    public N addition(N number, double add) {
        Class<? extends Number> cls = number.getClass();
        if (cls == Integer.class) {
            return (N) Integer.valueOf((int) (number.intValue() + add));
        }
        if (cls == Long.class) {
            return (N) Long.valueOf((long) (number.longValue() + add));
        }
        if(cls == Double.class) {
            return (N) Double.valueOf(number.doubleValue() + add);
        }
        if(cls == Float.class) {
            return (N) Float.valueOf((float) (number.floatValue() + add));
        }
        throw new UnsupportedOperationException("unknown class: " + cls);
    }


    @SuppressWarnings("unchecked")
    public N subtract(N number, N sub) {
        Class<? extends Number> cls = number.getClass();
        Class<? extends  Number> cls2 = sub.getClass();
        if (cls == Integer.class && cls2 == Integer.class) {
            return (N) Integer.valueOf(number.intValue() - sub.intValue());
        }
        if (cls == Long.class && cls2 == Long.class) {
            return (N) Long.valueOf(number.longValue() - sub.longValue());
        }
        if(cls == Double.class && cls2 == Double.class) {
            return (N) Double.valueOf(number.doubleValue() - sub.doubleValue());
        }
        if(cls == Float.class && cls2 == Float.class) {
            return (N) Float.valueOf(number.floatValue() - sub.floatValue());
        }
        throw new UnsupportedOperationException("unknown class: " + cls);
    }

}
