package cl.desquite.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public abstract class Util {

    public static void printError(String methodFail, Exception e) {
        log.error(methodFail);
        log.error(e.getMessage());
        log.error(e.getCause());
    }

    public static boolean isRole(Authentication auth, String role) {
        return auth.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

    public static boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public static boolean isLider(Authentication auth) {
        return auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_LIDER"));
    }

    public static String normalizeHtml(String val) {
        val = val.replace("&Aacute;", "Á");
        val = val.replace("&aacute;", "a");
        val = val.replace("&Eacute;", "É");
        val = val.replace("&eacute;", "é");
        val = val.replace("&Iacute;", "Í");
        val = val.replace("&iacute;", "í");
        val = val.replace("&Oacute;", "Ó");
        val = val.replace("&oacute;", "ó");
        val = val.replace("&Uacute;", "Ú");
        val = val.replace("&uacute;", "ú");
        return val;
    }

    public static boolean isValidName(String val) {
        Pattern pat = Pattern.compile("[a-zA-ZñÑáÁéÉíÍóÓúÚ ]{2,200}");
        Matcher mat = pat.matcher(val.trim());
        return mat.matches();
    }

    public static boolean isValidRut(String val) {
        Pattern pat = Pattern.compile("[0-9kK-]{2,50}");
        Matcher mat = pat.matcher(val.trim().replace(".", ""));
        return mat.matches();
    }

    public static boolean isValidDirection(String val) {
        Pattern pat = Pattern.compile("[a-zA-Z0-9ñÑáÁéÉíÍóÓúÚ #,-]{2,200}");
        Matcher mat = pat.matcher(val.trim());
        return mat.matches();
    }

    public static boolean isValidPhone(String val) {
        Pattern pat = Pattern.compile("[0-9+]{1,20}");
        Matcher mat = pat.matcher(val.trim());
        return mat.matches();
    }

    public static boolean isValidEmail(String val) {
        Pattern pat = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mat = pat.matcher(val.trim());
        return mat.matches();
    }

}
