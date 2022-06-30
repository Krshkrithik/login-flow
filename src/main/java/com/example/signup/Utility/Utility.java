package com.example.signup.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Utility {

    private static List<Pattern> pathPatterns = new ArrayList<Pattern>()
    {{
        add(Pattern.compile("/configuration/ui"));
        add(Pattern.compile("/swagger-resources.*"));
        add(Pattern.compile("/configuration/security"));
        add(Pattern.compile("/swagger-ui.html"));
        add(Pattern.compile("/v2/api-docs"));
        add(Pattern.compile("/webjars/.*"));
        add(Pattern.compile("/possibleValues/.*"));

    }};

    public static boolean matchPaths(String uri){
        for(Pattern pattern:pathPatterns){
            if(pattern.matcher(uri).matches())
                return true;
        }
        return false;
    }
}
