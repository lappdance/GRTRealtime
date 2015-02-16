package com.lappdance.grtrealtime.model;

public class Route implements Comparable<Route> {
    private int mId;
    private String mName;
    private int mColor;

    public Route() {
    }

    public Route(int id, String name, int color) {
        setId(id);
        setName(name);
        setColor(color);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = 0xff000000 | color;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public int compareTo(Route other) {
        return mId - other.mId;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", mId, mName);
    }

    /**
     * Selects a colour for a route.
     * The server may provide colouring information for routes, but it is not always
     * available.
     * @param id The route id.
     * @return An ARGB colour value.
     */
    public static int getColor(int id) {
        if(id == 200) { //original iXpress
            return 0xff0066ff;
        } else if(id > 200 && id < 250) { //other iXpress
            return 0xff00ff66;
        } else if(id > 9000) { //special / industrial routes
            return 0xff009ce0;
        }

        switch(id) {
            case 1:
            case 54:
            case 73:
                return 0xff0099cc;
            case 2:
                return 0xffffcc33;
            case 3:
                return 0xff9900cc;
            case 4:
                return 0xff666633;
            case 5:
                return 0xff0099ff;
            case 6:
            case 76:
                return 0xff000066;
            case 7:
            case 51:
                return 0xffcc0000;
            case 8:
            case 53:
                return 0xff009933;
            case 9:
                return 0xffff9933;
            case 10:
            case 110:
                return 0xff0066cc;
            case 11:
                return 0xff3366ff;
            case 12:
                return 0xffcc3399;
            case 13:
            case 63:
                return 0xffffcc00;
            case 14:
                return 0xff003333;
            case 15:
            case 55:
                return 0xff993399;
            case 16:
            case 116:
                return 0xff669933;
            case 17:
            case 62:
                return 0xff666666;
            case 19:
                return 0xffcc6600;
            case 20:
                return 0xffcc99cc;
            case 21:
                return 0xff99cc00;
            case 22:
                return 0xff666699;
            case 23:
                return 0xffff99cc;
            case 24:
                return 0xff333333;
            case 25:
                return 0xff3399cc;
            case 27:
                return 0xffff9966;
            case 29:
                return 0xff993366;
            case 31:
                return 0xff999966;
            case 33:
                return 0xff089018;
            case 52:
                return 0xff000099;
            case 56:
                return 0xffd0ba00;
            case 57:
                return 0xffff9900;
            case 58:
                return 0xff333300;
            case 59:
                return 0xffcc0099;
            case 60:
                return 0xff333366;
            case 61:
                return 0xff009999;
            case 64:
                return 0xff990033;
            case 67:
                return 0xffe09400;
            case 72:
                return 0xff996666;
            case 75:
                return 0xffb72700;
            case 92:
                return 0xff003986;
            case 111:
                return 0xffff3366;
            case 888:
                return 0xffff3366;
            default:
                return 0;
        }
    }
}
