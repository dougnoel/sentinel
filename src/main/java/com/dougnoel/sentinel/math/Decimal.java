package com.dougnoel.sentinel.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Decimal {
    private Decimal(){
        //private constructor since this is a static class
    }

    /**
     * Takes an unformatted value, and formats it to the given decimal places with rounding or truncation
     * using BigDecimal.
     *
     * @param unformattedValue Int the value to format
     * @param decimalPlaces Integer the number of decimal places to round/truncate/pad to
     * @param isRounded Boolean whether we are rounding (true) or truncating (false)
     * @return String a formatted string of the decimal with the given decimal places and rounding/truncation
     */
    public static String formatDecimal(int unformattedValue, int decimalPlaces, boolean isRounded) {
        return formatDecimal(BigDecimal.valueOf(unformattedValue), decimalPlaces, isRounded);
    }

    /**
     * Takes an unformatted value, and formats it to the given decimal places with rounding or truncation
     * using BigDecimal.
     *
     * @param unformattedValue Double the value to format
     * @param decimalPlaces Integer the number of decimal places to round/truncate/pad to
     * @param isRounded Boolean whether we are rounding (true) or truncating (false)
     * @return String a formatted string of the decimal with the given decimal places and rounding/truncation
     */
    public static String formatDecimal(double unformattedValue, int decimalPlaces, boolean isRounded) {
        return formatDecimal(BigDecimal.valueOf(unformattedValue), decimalPlaces, isRounded);
    }

    /**
     * Takes an unformatted value, and formats it to the given decimal places with rounding or truncation
     * using BigDecimal.
     *
     * @param unformattedValue Long the value to format
     * @param decimalPlaces Integer the number of decimal places to round/truncate/pad to
     * @param isRounded Boolean whether we are rounding (true) or truncating (false)
     * @return String a formatted string of the decimal with the given decimal places and rounding/truncation
     */
    public static String formatDecimal(long unformattedValue, int decimalPlaces, boolean isRounded) {
        return formatDecimal(BigDecimal.valueOf(unformattedValue), decimalPlaces, isRounded);
    }

    /**
     * Takes an unformatted value, and formats it to the given decimal places with rounding or truncation.
     *
     * @param unformattedValue BigDecimal the unformatted decimal to format
     * @param decimalPlaces Integer the number of decimal places to round/truncate/pad to
     * @param isRounded Boolean whether we are rounding (true) or truncating (false)
     * @return String a formatted string of the decimal with the given decimal places and rounding/truncation
     */
    public static String formatDecimal(BigDecimal unformattedValue, int decimalPlaces, boolean isRounded){
        String leadingFormat = "#";
        if(decimalPlaces > 0)
            leadingFormat+=".";
        String decimalPlacesLength = "0".repeat(decimalPlaces);

        DecimalFormat formatOutput = new DecimalFormat(leadingFormat + decimalPlacesLength);

        if(isRounded)
            formatOutput.setRoundingMode(RoundingMode.HALF_UP);
        else
            formatOutput.setRoundingMode(RoundingMode.DOWN);

        return formatOutput.format(unformattedValue);
    }
}