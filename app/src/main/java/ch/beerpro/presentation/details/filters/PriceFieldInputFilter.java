package ch.beerpro.presentation.details.filters;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceFieldInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = Pattern.compile("^(?:[1-9]\\d*|0)?(?:\\.\\d+)?$").matcher(dest);
        if (!matcher.matches()) {
            return "";
        }

        return null;
    }

}
