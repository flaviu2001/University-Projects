package pisicutele;

import java.util.HashMap;
import java.util.Map;


public class Colors {
    private final int cntColors;
    private final String[] colors;

    public Colors(int cntColors) {
        this.cntColors = cntColors;

        colors = new String[cntColors];
    }

    public void setColorName(int colorId, String color) {
        colors[colorId] = color;
    }

    public Map<Integer, String> getNodesToColors(int[] codes) {
        Map<Integer, String> map = new HashMap<>();

        for (int index = 0; index < codes.length; index++) {
            String color = colors[codes[index]];
            map.put(index, color);
        }

        return map;
    }

    public int getCntColors() {
        return cntColors;
    }
}
