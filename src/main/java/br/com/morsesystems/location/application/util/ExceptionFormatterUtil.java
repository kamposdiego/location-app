package br.com.morsesystems.location.application.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionFormatterUtil {

    private ExceptionFormatterUtil() {
        //Utility class
    }

    public static String semanticExceptionFormatter(String message){

        String specificMessageError = message;

        Pattern patternSQLError = Pattern.compile("SemanticException: (.*)");
        Matcher matcher = patternSQLError.matcher(specificMessageError);

        if (matcher.find()) {
            specificMessageError = matcher.group(1);
        }

        return specificMessageError.replace("'br.com.morsesystems.location.application.repository.entity.", "").replace("Entity'","");
    }

}
