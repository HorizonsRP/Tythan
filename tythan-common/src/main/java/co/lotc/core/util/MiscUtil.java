package co.lotc.core.util;

public class MiscUtil {

    public static float getYawFromFacing(String facing) {
        float output = 0;
        if (facing.equalsIgnoreCase("west")) {
            output = 90;
        } else if (facing.equalsIgnoreCase("north")) {
            output = 180;
        } else if (facing.equalsIgnoreCase("east")) {
            output = -90;
        }
        return output;
    }

    public static String getFacingFromYaw(float yaw) {
        int halfwayGap = 45;
        String output = "north";
        if (yaw < 180-halfwayGap) {
            if (yaw >= 90 - halfwayGap) {
                output = "west";
            } else if (yaw >= -halfwayGap) {
                output = "south";
            } else if (yaw >= -90 - halfwayGap) {
                output = "east";
            }
        }
        return output;
    }

}
